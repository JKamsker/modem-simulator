package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.state.CallMode;
import com.jkamsker.modemsim.state.CallRuntime;
import com.jkamsker.modemsim.state.FreezeMode;
import com.jkamsker.modemsim.state.ModemLifecycle;
import com.jkamsker.modemsim.state.ModemLines;
import com.jkamsker.modemsim.state.ModemRuntimeInfo;
import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.state.NetworkDelay;
import com.jkamsker.modemsim.state.NetworkRuntime;
import com.jkamsker.modemsim.state.OperatorInfo;
import com.jkamsker.modemsim.state.SessionSettings;
import com.jkamsker.modemsim.state.SignalRuntime;
import com.jkamsker.modemsim.state.SimRuntime;
import com.jkamsker.modemsim.state.SimState;
import com.jkamsker.modemsim.state.SmsRateLimit;
import com.jkamsker.modemsim.state.SmsRuntime;
import com.jkamsker.modemsim.state.SmsStorage;
import com.jkamsker.modemsim.validation.Dom;
import com.jkamsker.modemsim.validation.SchemaLocator;
import com.jkamsker.modemsim.validation.ValidationReport;
import com.jkamsker.modemsim.validation.XmlSecurity;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ProfileXmlLoader {
    private final ProfileSemanticValidator semanticValidator = new ProfileSemanticValidator();
    private final ProfileXmlSemanticValidator xmlSemanticValidator = new ProfileXmlSemanticValidator();

    public Profile load(Path xmlPath) {
        ValidationReport report = validate(xmlPath);
        report.throwIfInvalid();
        Element root = XmlSecurity.parse(xmlPath).getDocumentElement();
        return new ProfileXmlInheritanceResolver(this, root).resolve(ProfileXmlSelector.single(root));
    }

    public Profile load(Path xmlPath, String profileId) {
        ValidationReport report = validate(xmlPath);
        report.throwIfInvalid();
        Element root = XmlSecurity.parse(xmlPath).getDocumentElement();
        return new ProfileXmlInheritanceResolver(this, root).resolve(ProfileXmlSelector.byId(root, profileId));
    }

    public ValidationReport validate(Path xmlPath) {
        ValidationReport report = ValidationReport.ok();
        try {
            XmlSecurity.validate(xmlPath, SchemaLocator.schemaPath("modem-profile.schema.xsd"));
            Element root = XmlSecurity.parse(xmlPath).getDocumentElement();
            Set<String> profileIds = xmlSemanticValidator.profileIds(root);
            ProfileXmlInheritanceResolver resolver = new ProfileXmlInheritanceResolver(this, root);
            xmlSemanticValidator.validateRoot(root, report);
            for (Element profile : Dom.children(root, "profile")) {
                xmlSemanticValidator.validate(profile, profileIds, report);
                report.merge(semanticValidator.validate(resolver.resolve(profile)));
            }
        } catch (Exception e) {
            report.error(e.getMessage());
        }
        return report;
    }
    Profile parseProfile(Element profile) {
        Dialect dialect = parseDialect(Dom.child(profile, "dialect"));
        ModemState state = parseState(profile, dialect);
        ProfileXmlMetadata metadata = new ProfileXmlMetadataParser().parse(profile);
        return new Profile(
                profile.getAttribute("id"),
                ProfileXmlSupport.parents(profile.getAttribute("extends")),
                profile.getAttribute("vendor"),
                profile.getAttribute("status"),
                Dom.attr(profile, "profileKind", "cellular"),
                Dom.attr(profile, "modelFamily", null),
                Dom.attr(profile, "manualVersion", null),
                Dom.attr(profile, "manualDate", null),
                dialect,
                parseIdentity(Dom.child(profile, "identity")),
                state, metadata.commands(), metadata.registers(), metadata.coverage(), metadata.deviations());
    }

    private Dialect parseDialect(Element dialect) {
        if (dialect == null) {
            return Dialect.v250();
        }
        return new Dialect(
                Dom.boolAttr(dialect, "defaultEcho", false),
                Dom.boolAttr(dialect, "defaultQuiet", false),
                Dom.boolAttr(dialect, "defaultVerbose", true),
                terminator(Dom.attr(dialect, "commandTerminator", "CR"), true),
                terminator(Dom.attr(dialect, "responseTerminator", "CRLF"), false),
                resetPolicy(Dom.attr(dialect, "resetPolicy", "nvram-on-atz")),
                lineModel(Dom.attr(dialect, "lineModel", "minimal-v250")),
                unknownPolicy(Dom.attr(dialect, "unknownAtCommand", "ERROR")),
                com.jkamsker.modemsim.parser.RawBytes.hex(Dom.attr(dialect, "smsPromptBytes", "0D0A3E20")));
    }

    private Identity parseIdentity(Element identity) {
        if (identity == null) {
            return new Identity("unknown", "unknown", null, null);
        }
        return new Identity(
                identity.getAttribute("manufacturer"),
                identity.getAttribute("model"),
                Dom.attr(identity, "revision", null),
                Dom.attr(identity, "imei", null));
    }

    private ModemState parseState(Element profile, Dialect dialect) {
        Element initial = Dom.child(profile, "initial-state");
        String kind = Dom.attr(profile, "profileKind", "cellular");
        ModemState base = Set.of("pstn", "isdn", "base").contains(kind)
                ? ModemState.pstnReady()
                : ModemState.cellularReady();
        if (initial == null) {
            return applyDialect(base, dialect);
        }
        return applyDialect(new ModemState(
                parseSim(Dom.child(initial, "sim"), base.sim()),
                parseNetwork(Dom.child(initial, "network")),
                parseSignal(Dom.child(initial, "signal"), base.signal()),
                parseSms(Dom.child(initial, "sms"), base.sms()),
                parseCall(Dom.child(initial, "call"), base.call()),
                parseModem(Dom.child(initial, "modem"), base.modem()),
                parseLines(Dom.child(initial, "modem-lines"), base.lines()),
                base.settings(),
                0), dialect);
    }

    ModemState applyDialect(ModemState state, Dialect dialect) {
        SessionSettings settings = state.settings()
                .withEcho(dialect.defaultEcho())
                .withQuiet(dialect.defaultQuiet())
                .withVerbose(dialect.defaultVerbose())
                .withRegister(3, dialect.commandTerminator())
                .withRegister(4, dialect.responseTerminator());
        return state.withSettings(settings);
    }

    private SimRuntime parseSim(Element sim, SimRuntime fallback) {
        if (sim == null) {
            return fallback;
        }
        boolean pinQueryEnabled = Dom.boolAttr(sim, "pinQueryEnabled", false);
        return new SimRuntime(
                SimState.valueOf(Dom.attr(sim, "state", pinQueryEnabled ? "SIM_PIN_REQUIRED" : "READY")),
                pinQueryEnabled,
                Dom.attr(sim, "pinRef", null),
                Dom.attr(sim, "pin", null),
                Dom.intAttr(sim, "pinRetries", 3),
                Dom.intAttr(sim, "pukRetries", 10),
                Dom.attr(sim, "imsi", null),
                Dom.attr(sim, "iccid", null));
    }

    private NetworkRuntime parseNetwork(Element network) {
        if (network == null) {
            return null;
        }
        Element operator = Dom.child(network, "operator");
        return new NetworkRuntime(
                Dom.intAttr(network, "cregN", 2),
                Dom.intAttr(network, "stat", 0),
                Dom.attr(network, "lac", null),
                Dom.attr(network, "ci", null),
                intObject(network, "act"),
                intObject(network, "rejectCauseType"),
                intObject(network, "rejectCause"),
                parseOperator(operator),
                parseRateLimit(Dom.child(network, "sms-rate-limit")),
                parseDelays(Dom.child(network, "delays")));
    }

    private OperatorInfo parseOperator(Element operator) {
        if (operator == null) {
            return OperatorInfo.telekom();
        }
        return new OperatorInfo(
                operator.getAttribute("selectionMode"),
                operator.getAttribute("format"),
                Dom.attr(operator, "longName", ""),
                Dom.attr(operator, "shortName", ""),
                operator.getAttribute("numeric"),
                operator.getAttribute("mcc"),
                operator.getAttribute("mnc"));
    }

    private SmsRateLimit parseRateLimit(Element rate) {
        if (rate == null) {
            return SmsRateLimit.none();
        }
        return new SmsRateLimit(
                Dom.intAttr(rate, "maxMessages", 0),
                Dom.intAttr(rate, "windowSeconds", 0),
                rate.getAttribute("scope"),
                Dom.intAttr(rate, "rejectCmsError", 500));
    }

    private Map<String, NetworkDelay> parseDelays(Element delays) {
        Map<String, NetworkDelay> result = new LinkedHashMap<>();
        if (delays == null) {
            return result;
        }
        for (Element delay : Dom.children(delays, "delay")) {
            String operation = delay.getAttribute("operation");
            result.put(operation, new NetworkDelay(
                    operation,
                    Dom.intAttr(delay, "minMs", 0),
                    Dom.intAttr(delay, "maxMs", 0)));
        }
        return result;
    }

    private SignalRuntime parseSignal(Element signal, SignalRuntime fallback) {
        return signal == null ? fallback : new SignalRuntime(
                Dom.intAttr(signal, "rssi", 99), Dom.intAttr(signal, "ber", 99));
    }

    private SmsRuntime parseSms(Element sms, SmsRuntime fallback) {
        return sms == null ? fallback : new SmsRuntime(
                Dom.boolAttr(sms, "textMode", true),
                Dom.attr(sms, "smsc", fallback.smsc()),
                fallback.cnmi(),
                SmsStorage.valueOf(Dom.attr(sms, "storage", fallback.storage().name())),
                fallback.nextMessageReference(),
                fallback.messages());
    }

    private CallRuntime parseCall(Element call, CallRuntime fallback) {
        return call == null ? fallback : new CallRuntime(
                callMode(Dom.attr(call, "mode", "command")),
                Dom.boolAttr(call, "carrier", false),
                Dom.attr(call, "dialedNumber", null));
    }

    private ModemRuntimeInfo parseModem(Element modem, ModemRuntimeInfo fallback) {
        return modem == null ? fallback : new ModemRuntimeInfo(
                ModemLifecycle.valueOf(Dom.attr(modem, "lifecycle", "READY")),
                FreezeMode.valueOf(Dom.attr(modem, "freezeMode", "NONE")),
                Dom.intAttr(modem, "bootDelayMs", 0));
    }

    private ModemLines parseLines(Element lines, ModemLines fallback) {
        return lines == null ? fallback : new ModemLines(
                Dom.boolAttr(lines, "dtr", true),
                Dom.boolAttr(lines, "dsr", true),
                Dom.boolAttr(lines, "dcd", false),
                Dom.boolAttr(lines, "ri", false),
                Dom.boolAttr(lines, "rts", true),
                Dom.boolAttr(lines, "cts", true));
    }

    private Integer intObject(Element element, String name) {
        String value = Dom.attr(element, name, null);
        return value == null ? null : Integer.valueOf(value);
    }

    private ResetPolicy resetPolicy(String value) {
        return switch (value) {
            case "factory-on-atz" -> ResetPolicy.FACTORY_ON_ATZ;
            case "profile-default-on-atz" -> ResetPolicy.PROFILE_DEFAULT_ON_ATZ;
            default -> ResetPolicy.NVRAM_ON_ATZ;
        };
    }

    private LineModel lineModel(String value) {
        return switch (value) {
            case "byte-only" -> LineModel.BYTE_ONLY;
            case "profile-specific" -> LineModel.PROFILE_SPECIFIC;
            default -> LineModel.MINIMAL_V250;
        };
    }

    private int terminator(String value, boolean commandTerminator) {
        return switch (value) {
            case "LF" -> 10;
            case "CRLF" -> commandTerminator ? 13 : 10;
            default -> 13;
        };
    }

    private UnknownAtCommandPolicy unknownPolicy(String value) {
        return value.equals("restart") ? UnknownAtCommandPolicy.RESTART
                : UnknownAtCommandPolicy.valueOf(value.toUpperCase());
    }

    private CallMode callMode(String value) {
        return switch (value) {
            case "online-data" -> CallMode.ONLINE_DATA;
            case "online-command" -> CallMode.ONLINE_COMMAND;
            case "dialing" -> CallMode.DIALING;
            default -> CallMode.COMMAND;
        };
    }
}

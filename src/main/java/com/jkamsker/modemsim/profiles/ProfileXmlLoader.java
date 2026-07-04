package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.state.ModemState;
import com.jkamsker.modemsim.validation.Dom;
import com.jkamsker.modemsim.validation.SchemaLocator;
import com.jkamsker.modemsim.validation.ValidationReport;
import com.jkamsker.modemsim.validation.XmlSecurity;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

public final class ProfileXmlLoader {
    private final ProfileSemanticValidator semanticValidator = new ProfileSemanticValidator();
    private final ProfileXmlSemanticValidator xmlSemanticValidator = new ProfileXmlSemanticValidator();
    private final ProfileXmlStateParser stateParser = new ProfileXmlStateParser();

    public Profile load(Path xmlPath) {
        ValidatedRoot validated = validateRoot(xmlPath);
        validated.report().throwIfInvalid();
        Element root = validated.root();
        return new ProfileXmlInheritanceResolver(this, root).resolve(ProfileXmlSelector.single(root));
    }

    public Profile load(Path xmlPath, String profileId) {
        ValidatedRoot validated = validateRoot(xmlPath);
        validated.report().throwIfInvalid();
        Element root = validated.root();
        return new ProfileXmlInheritanceResolver(this, root).resolve(ProfileXmlSelector.byId(root, profileId));
    }

    public Profile loadResource(String resource, String profileId) {
        return ProfileXmlResource.load(this, resource, profileId);
    }

    public ValidationReport validate(Path xmlPath) {
        return validateRoot(xmlPath).report();
    }

    private ValidatedRoot validateRoot(Path xmlPath) {
        ValidationReport report = ValidationReport.ok();
        Element root = null;
        try {
            XmlSecurity.validate(xmlPath, SchemaLocator.schemaPath("modem-profile.schema.xsd"));
            root = XmlSecurity.parse(xmlPath).getDocumentElement();
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
        return new ValidatedRoot(root, report);
    }

    private record ValidatedRoot(Element root, ValidationReport report) {
    }

    Profile parseProfile(Element profile) {
        Dialect dialect = parseDialect(Dom.child(profile, "dialect"));
        ProfileXmlMetadata metadata = new ProfileXmlMetadataParser().parse(profile);
        ModemState state = applyRegistersAndDialect(stateParser.parse(profile, dialect), dialect, metadata.registers());
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
                parseErrorPolicy(Dom.child(profile, "error-policy")),
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
                com.jkamsker.modemsim.parser.RawBytes.hex(Dom.attr(dialect, "smsPromptBytes", "0D0A3E20")),
                prefixes(Dom.attr(dialect, "extendedPrefixes", "+ % # !")));
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

    private ErrorPolicy parseErrorPolicy(Element policy) {
        if (policy == null) {
            return ErrorPolicy.defaults();
        }
        return new ErrorPolicy(
                Dom.attr(policy, "invalidParameter", "CME_OR_ERROR"),
                Dom.attr(policy, "stateFailure", "CME"),
                Dom.attr(policy, "smsFailure", "CMS"),
                Dom.attr(policy, "timeout", "NO_RESPONSE"));
    }

    ModemState applyDialect(ModemState state, Dialect dialect) {
        return stateParser.applyDialect(state, dialect);
    }

    ModemState applyRegistersAndDialect(ModemState state, Dialect dialect, java.util.List<ProfileRegister> registers) {
        return applyDialect(state.withSettings(
                ProfileRegisterCatalog.applyDefaults(state.settings(), registers)), dialect);
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

    private java.util.List<String> prefixes(String value) {
        return Arrays.stream(value.trim().split("\\s+")).filter(item -> !item.isBlank()).toList();
    }

}

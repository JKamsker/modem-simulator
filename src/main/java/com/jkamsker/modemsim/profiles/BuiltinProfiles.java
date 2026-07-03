package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.state.ModemState;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BuiltinProfiles {
    private BuiltinProfiles() {
    }

    public static Profile byId(String id) {
        return switch (id) {
            case "generic-hayes-v250" -> genericHayes();
            case "3gpp-27007-r18" -> normativeCellular(id, "TS 27.007 Release 18");
            case "3gpp-27005-r16" -> normativeCellular(id, "TS 27.005 Release 16");
            case "sierra-common" -> sierraCommon();
            case "sierra-hl6-hl8-v20" -> acceptanceSierra();
            case "westermo-common" -> westermoCommon();
            case "westermo-td22-6177-2203" -> westermoTd22();
            case "westermo-td36-6618-2202" -> westermoPstn(id, "TD-36", "6618-2202");
            case "westermo-gd01-6196-2220" -> westermoCellular(id, "GD-01", "6196-2220");
            case "westermo-gdw11-6615-2220" -> westermoCellular(id, "GDW-11", "6615-2220");
            default -> throw new IllegalArgumentException("Unknown built-in profile: " + id);
        };
    }

    public static List<String> ids() {
        return List.of(
                "generic-hayes-v250",
                "3gpp-27007-r18",
                "3gpp-27005-r16",
                "sierra-common",
                "sierra-hl6-hl8-v20",
                "westermo-common",
                "westermo-td22-6177-2203",
                "westermo-td36-6618-2202",
                "westermo-gd01-6196-2220",
                "westermo-gdw11-6615-2220");
    }

    public static Profile acceptanceSierra() {
        return profile(
                "sierra-hl6-hl8-v20",
                List.of("sierra-common"),
                "Sierra Wireless / Semtech",
                "device-family",
                "cellular",
                Dialect.v250().withDefaultEcho(true),
                Identity.sierra(),
                ModemState.cellularReady());
    }

    public static Profile westermoTd22() {
        return profile(
                "westermo-td22-6177-2203",
                List.of("westermo-common"),
                "Westermo",
                "device-target",
                "pstn",
                Dialect.v250().withDefaultEcho(true),
                Identity.westermo("TD-22", "6177-2203"),
                ModemState.pstnReady());
    }

    private static Profile genericHayes() {
        return profile(
                "generic-hayes-v250",
                List.of(),
                "ITU-T",
                "normative-base",
                "base",
                Dialect.v250(),
                new Identity("Generic", "Hayes V.250", null, null),
                ModemState.pstnReady());
    }

    private static Profile normativeCellular(String id, String model) {
        return profile(
                id,
                List.of("generic-hayes-v250"),
                "3GPP",
                "normative-base",
                "base",
                Dialect.v250(),
                new Identity("3GPP", model, null, "359762080000001"),
                ModemState.cellularReady());
    }

    private static Profile sierraCommon() {
        return profile(
                "sierra-common",
                List.of("generic-hayes-v250", "3gpp-27007-r18", "3gpp-27005-r16"),
                "Sierra Wireless / Semtech",
                "manufacturer-base",
                "cellular",
                Dialect.v250().withDefaultEcho(true),
                new Identity("Sierra Wireless", "Common", null, "359762080000001"),
                ModemState.cellularReady());
    }

    private static Profile westermoPstn(String id, String model, String revision) {
        return profile(
                id,
                List.of("westermo-common"),
                "Westermo",
                "device-target",
                "pstn",
                Dialect.v250().withDefaultEcho(true),
                Identity.westermo(model, revision),
                ModemState.pstnReady());
    }

    private static Profile westermoCommon() {
        return profile(
                "westermo-common",
                List.of("generic-hayes-v250"),
                "Westermo",
                "manufacturer-base",
                "base",
                Dialect.v250().withDefaultEcho(true),
                Identity.westermo("Common", "common"),
                ModemState.pstnReady());
    }

    private static Profile westermoCellular(String id, String model, String revision) {
        return profile(
                id,
                List.of("westermo-common", "3gpp-27007-r18", "3gpp-27005-r16"),
                "Westermo",
                "device-target",
                "hybrid",
                Dialect.v250().withDefaultEcho(true),
                new Identity("Westermo", model, revision, "359762080000001"),
                ModemState.cellularReady());
    }

    private static Profile profile(
            String id, List<String> parents, String vendor, String status, String profileKind,
            Dialect dialect, Identity identity, ModemState state) {
        ProfileCoverage coverage = BuiltinProfileMetadata.coverage(id);
        return new Profile(id, parents, vendor, status, profileKind,
                identity.model(), id, manualDate(id), dialect, identity, state,
                commands(id, parents), BuiltinProfileMetadata.registers(), coverage, List.of());
    }

    private static List<ProfileCommand> commands(String id, List<String> parents) {
        Map<String, ProfileCommand> merged = new LinkedHashMap<>();
        for (String parent : parents) {
            byId(parent).commands().forEach(command -> merged.put(command.name(), command));
        }
        BuiltinProfileMetadata.commands(id).forEach(command -> merged.put(command.name(), command));
        return List.copyOf(merged.values());
    }

    private static String manualDate(String id) {
        return id.startsWith("3gpp") ? "2024-01-01"
                : id.startsWith("westermo") ? "2023-01-01" : "2024-02-01";
    }
}

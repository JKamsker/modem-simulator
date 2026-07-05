package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.state.FreezeMode;

public record FaultAction(Type type, Integer durationMs, Integer stat, Integer rssi, Integer ber, FreezeMode freezeMode) {
    public FaultAction(String type, Integer durationMs, Integer stat, Integer rssi, Integer ber, FreezeMode freezeMode) {
        this(Type.from(type), durationMs, stat, rssi, ber, freezeMode);
    }

    public String typeName() {
        return type.configName();
    }

    public enum Type {
        NETWORK_OUTAGE("network-outage"),
        NETWORK_RESTORE("network-restore"),
        MODEM_REBOOT("modem-reboot"),
        MODEM_FREEZE("modem-freeze"),
        MODEM_UNFREEZE("modem-unfreeze");

        private final String configName;

        Type(String configName) {
            this.configName = configName;
        }

        public String configName() {
            return configName;
        }

        static Type from(String value) {
            String normalized = value == null ? "" : value.trim();
            if (normalized.equals("reboot")) {
                normalized = "modem-reboot";
            }
            for (Type type : values()) {
                if (type.configName.equals(normalized)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown fault type: " + value);
        }
    }
}

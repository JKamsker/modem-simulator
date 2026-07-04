package com.jkamsker.modemsim.gui;

final class GuiHelpText {
    private GuiHelpText() {
    }

    static String forLabel(String label) {
        return switch (label) {
            case "Profile" -> "Built-in profile id or path to an XML modem profile.";
            case "Main port" -> "Serial port for the simulated modem. Use headless for in-memory runs.";
            case "Sniffer port" -> "Optional read-only sidecar port for observing traffic.";
            case "Manual DCE port" -> "Optional sidecar port whose bytes are forwarded as modem output.";
            case "Scenario" -> "Optional XML scenario applied to the profile initial state.";
            case "Seed" -> "Session seed used for deterministic delays and replay behavior.";
            case "Baud" -> "Shared serial speed for all enabled ports.";
            case "Format" -> "Data bits, parity, and stop bits in compact form such as 8N1 or 7E2.";
            case "Flow" -> "Hardware or software flow control shared by enabled ports.";
            case "Network stat" -> "3GPP registration status value and friendly meaning.";
            case "Signal" -> "CSQ pair rssi,ber. RSSI 99 means unknown or not detectable.";
            case "RSSI" -> "Received signal strength value used by AT+CSQ.";
            case "Raw DTE" -> "Inject raw bytes as if the device under test sent them to the modem.";
            case "Raw DCE" -> "Send raw modem bytes to the attached device after explicit safety confirmation.";
            case "Parsed command" -> "Execute one AT command through parser and handlers without publishing raw RX bytes.";
            case "URC" -> "Send an unsolicited modem result code such as +CREG: 4 to the device.";
            case "State patch" -> "Apply a single state path assignment, for example network.stat=4.";
            case "Replay mode" -> "Validate, drive captured DTE input, or play captured DCE output.";
            case "Replay log" -> "JSONL, YAML, or TRM transcript used by the selected replay mode.";
            default -> "This setting changes how the simulator session behaves.";
        };
    }
}

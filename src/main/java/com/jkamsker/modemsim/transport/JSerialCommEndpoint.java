package com.jkamsker.modemsim.transport;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemLines;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public final class JSerialCommEndpoint implements SerialEndpoint {
    private final String portName;
    private SerialPort port;

    public JSerialCommEndpoint(String portName) {
        this.portName = portName;
    }

    @Override
    public void open(SerialConfig config) throws SerialException {
        port = SerialPort.getCommPort(portName);
        port.setComPortParameters(config.baudRate(), config.dataBits(), config.stopBits(), parity(config.parity()));
        port.setFlowControl(flow(config.flowControl()));
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 100);
        if (!port.openPort()) {
            throw new SerialException("Cannot open serial port: " + portName);
        }
    }

    @Override
    public SerialRead read() throws IOException {
        byte[] buffer = new byte[1024];
        int count = port.readBytes(buffer, buffer.length);
        if (count < 0) {
            throw new IOException("Serial read failed on " + portName);
        }
        long now = System.nanoTime();
        return new SerialRead(RawBytes.copyOf(java.util.Arrays.copyOf(buffer, count)), now, now);
    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        int written = port.writeBytes(buffer, length, offset);
        if (written != length) {
            throw new IOException("Serial write incomplete on " + portName);
        }
    }

    @Override
    public ModemLines readLines() {
        return new ModemLines(port.getDTR(), port.getDSR(), port.getDCD(), false, port.getRTS(), port.getCTS());
    }

    @Override
    public void writeLines(ModemLines lines) {
        if (lines.dtr()) {
            port.setDTR();
        } else {
            port.clearDTR();
        }
        if (lines.rts()) {
            port.setRTS();
        } else {
            port.clearRTS();
        }
    }

    @Override
    public void close() {
        if (port != null) {
            port.closePort();
        }
    }

    private int parity(Parity parity) {
        return switch (parity) {
            case EVEN -> SerialPort.EVEN_PARITY;
            case ODD -> SerialPort.ODD_PARITY;
            case MARK -> SerialPort.MARK_PARITY;
            case SPACE -> SerialPort.SPACE_PARITY;
            default -> SerialPort.NO_PARITY;
        };
    }

    private int flow(FlowControl flowControl) {
        return switch (flowControl) {
            case RTS_CTS -> SerialPort.FLOW_CONTROL_RTS_ENABLED | SerialPort.FLOW_CONTROL_CTS_ENABLED;
            case XON_XOFF -> SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED | SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED;
            default -> SerialPort.FLOW_CONTROL_DISABLED;
        };
    }
}

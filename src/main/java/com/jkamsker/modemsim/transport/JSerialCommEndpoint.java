package com.jkamsker.modemsim.transport;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.state.ModemLines;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class JSerialCommEndpoint implements SerialEndpoint {
    private final String portName;
    private SerialPort port;
    private final AtomicBoolean disconnected = new AtomicBoolean();
    private final AtomicBoolean rxOverflow = new AtomicBoolean();

    public JSerialCommEndpoint(String portName) {
        this.portName = portName;
    }

    @Override
    public void open(SerialConfig config) throws SerialException {
        port = SerialPort.getCommPort(portName);
        if (!port.setComPortParameters(config.baudRate(), config.dataBits(), config.stopBits(), parity(config.parity()))) {
            throw new SerialException("Unsupported serial parameters for " + portName);
        }
        if (!port.setFlowControl(flow(config.flowControl()))) {
            throw new SerialException("Unsupported flow control for " + portName);
        }
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 100);
        if (!port.openPort()) {
            throw new SerialException("Cannot open serial port: " + portName);
        }
        port.addDataListener(disconnectListener());
    }

    @Override
    public SerialRead read() throws IOException {
        if (disconnected.get() || port == null || !port.isOpen()) {
            throw new IOException("Serial port lost: " + portName);
        }
        if (rxOverflow.getAndSet(false)) {
            throw new SerialOverflowException("RX overflow on " + portName);
        }
        byte[] buffer = new byte[1];
        long first = System.nanoTime();
        int count = port.readBytes(buffer, buffer.length);
        long last = System.nanoTime();
        if (count < 0) {
            throw new IOException("Serial read failed on " + portName);
        }
        if (count == 0 && !portPresent()) {
            throw new IOException("Serial port disappeared: " + portName);
        }
        return new SerialRead(RawBytes.copyOf(java.util.Arrays.copyOf(buffer, count)), first, last);
    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        int written = port.writeBytes(buffer, length, offset);
        if (written != length) {
            throw new SerialOverflowException("TX overflow on " + portName + ": wrote " + written + " of " + length);
        }
    }

    @Override
    public ModemLines readLines() {
        return new ModemLines(port.getDTR(), port.getDSR(), port.getDCD(), false, port.getRTS(), port.getCTS());
    }

    @Override
    public void writeLines(ModemLines lines) {
        // jSerialComm exposes DTR/RTS setters only; map DCE output signals onto those controls.
        if (lines.dtr() || lines.dsr() || lines.dcd() || lines.ri()) {
            port.setDTR();
        } else {
            port.clearDTR();
        }
        if (lines.rts() || lines.cts()) {
            port.setRTS();
        } else {
            port.clearRTS();
        }
    }

    @Override
    public void close() {
        if (port != null) {
            port.removeDataListener();
            port.closePort();
        }
    }

    private SerialPortDataListener disconnectListener() {
        return new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_PORT_DISCONNECTED
                        | SerialPort.LISTENING_EVENT_FIRMWARE_OVERRUN_ERROR
                        | SerialPort.LISTENING_EVENT_SOFTWARE_OVERRUN_ERROR;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
                    disconnected.set(true);
                } else {
                    rxOverflow.set(true);
                }
            }
        };
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

    private boolean portPresent() {
        String name = port.getSystemPortName();
        String path = port.getSystemPortPath();
        for (SerialPort candidate : SerialPort.getCommPorts()) {
            if (matches(portName, candidate.getSystemPortName()) || matches(portName, candidate.getSystemPortPath())
                    || matches(name, candidate.getSystemPortName()) || matches(path, candidate.getSystemPortPath())) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(String expected, String actual) {
        return expected != null && expected.equals(actual);
    }
}

package com.jkamsker.modemsim.transport;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;
import java.util.List;

public final class PortDiscovery {
    public List<String> listSystemPorts() {
        return Arrays.stream(SerialPort.getCommPorts())
                .map(SerialPort::getSystemPortName)
                .sorted()
                .toList();
    }
}

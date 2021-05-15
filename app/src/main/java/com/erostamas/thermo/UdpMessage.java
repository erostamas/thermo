package com.erostamas.thermo;

import java.net.InetAddress;

public class UdpMessage {

    public UdpMessage(String address, int port, String message) {
        _address = address;
        _port = port;
        _message = message;
    }

    String getAddress() { return _address; }
    InetAddress getInetAddress() {
        try {
            return InetAddress.getByName(_address);
        } catch (Exception e) {

        }
        return null;
    }
    int getPort() {return _port;}
    String getMessage() {return _message;}

    private int _port;
    private String _address;
    private String _message;
}

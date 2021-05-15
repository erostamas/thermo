package com.erostamas.thermo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpRequestResponse {

    private ResponseHandler _responseHandler;

    public interface ResponseHandler {
        void handleResponse(String response);
    };

    public UdpRequestResponse(ResponseHandler responseHandler) {
        this._responseHandler = responseHandler;
    }

    public void execute(UdpMessage messageToSend) {
        new Thread(() -> {
            try {
                byte[] buffer = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                int server_port = messageToSend.getPort();
                DatagramSocket s = new DatagramSocket();
                s.setBroadcast(true);
                int msg_length = messageToSend.getMessage().length();
                byte[] message = messageToSend.getMessage().getBytes();
                DatagramPacket p = new DatagramPacket(message, msg_length, messageToSend.getInetAddress(), server_port);
                s.send(p);
                Log.i("UDP", "UDP packet sent");
                s.setSoTimeout(2000);
                s.receive(packet);
                String lText = new String(buffer, 0, packet.getLength());
                //Log.i("UDP", "UDP packet received: " + lText + "from: " + packet.getAddress().getHostAddress());
                new Handler(Looper.getMainLooper()).post(() -> {
                    _responseHandler.handleResponse(lText);
                    Log.e("thermo", "Response: " + lText);
                });
            } catch (Exception e) {
                Log.e("brewer", "Exception during UDP send: " + e.getMessage());
            }

        }).start();
    }
}

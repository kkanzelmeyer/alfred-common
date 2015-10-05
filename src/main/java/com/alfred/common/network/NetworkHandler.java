package com.alfred.common.network;

import java.net.Socket;

import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage;

public interface NetworkHandler {
    
    /**
     * Called when a new connection is established
     * @param connection
     */
    public void onConnect(Socket connection);
    
    /**
     * Called when a message is received
     * @param msg
     */
    public void onMessageReceived(StateDeviceMessage msg);
}

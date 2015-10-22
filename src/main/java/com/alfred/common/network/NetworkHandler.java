package com.alfred.common.network;

import java.net.Socket;

import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage;

/**
 * 
 * This interface should be implemented by a class that needs to be notified when 
 * a new connection is established and when a message is received on a connection. 
 * 
 * Note that this interface needs to be implemented by a class on almost every type
 * of plugin. See the plugins on Alfred Server and Alfred Cliet for examples
 * 
 * @author Kevin Kanzelmeyer
 *
 */
public interface NetworkHandler {
    
    /**
     * Called when a new connection is established
     * 
     * @param connection A reference to the new socket connection
     */
    public void onConnect(Socket connection);
    
    /**
     * Called when a message is received. Note that it is the handler's responsibility
     * to determine if it is concerned with the incoming message. For example, if you 
     * are a network handler in a Doorbell plugin you probably wouldn't be concerned
     * about a Garage Door state change message.
     * 
     * @param msg An instance of the received message
     */
    public void onMessageReceived(StateDeviceMessage msg);
}

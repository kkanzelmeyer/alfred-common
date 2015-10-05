package com.alfred.common.datamodel;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfred.common.handlers.StateDeviceHandler;
import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage.State;

/**
 * State Device Manager
 * 
 * Used to manage the state devices connected to Alfred
 * @author Kevin Kanzelmeyer
 *
 */
public class StateDeviceManager {
    
    // List of devices to maintain in memory
    private static HashMap<String, StateDevice> deviceList = new HashMap<String, StateDevice>();
    // List of handlers to maintain in memory
    private static HashMap<String, StateDeviceHandler> deviceHandlers = new HashMap<String, StateDeviceHandler>();
    // Logger
    final private static Logger log = LoggerFactory.getLogger(StateDeviceManager.class);
    
    
    /**
     * Method to retrieve a clone of a device. Returns null if the 
     * device doesn't exist
     * @param id
     * @return
     */
    public static StateDevice getDevice(String id) {
        if(deviceList.containsKey(id)) {
        	StateDevice clone = new StateDevice(deviceList.get(id));
            return clone;
        } else return null;
    }
    
    /**
     * Method to retrieve all devices
     * @return
     */
    public static HashMap<String, StateDevice> getAllDevices() {
        return deviceList;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // Add, Remove, Update methods for DeviceList
    // - - - - - - - - - - - - - - - - - - - - - - - - - - 
    
    /**
     * Call this method to add a device to the device manager
     * @param device
     */
    public static void addStateDevice(StateDevice device) {
        deviceList.put(device.getId(), device);
        notifyAddListeners(device.getId(), device);
    }
    
    /**
     * Call this method to remove a device from the device manager
     * @param device
     */
    public static void removeStateDevice(StateDevice device) {
        deviceList.remove(device.getId());
        notifyRemoveListeners(device.getId(), device);
    }
    
    /**
     * If the device doesn't currently exist
     * it will be added. Note that the state device will only be updated
     * if the device's state is different
     * @param id
     * @param state
     */
    public static void updateStateDevice(StateDevice device) {
        if(deviceList.containsKey(device.getId())) {
            StateDevice clone = getDevice(device.getId());
            if(clone.getState() != device.getState()){
                clone.setState(device.getState());
                deviceList.put(device.getId(), clone);
                notifyUpdateListeners(device.getId(), clone);
            } else {
                log.info("No state change, ignoring update");
            }
        } else {
        	addStateDevice(device);
        }
    }
    
    /**
     * 
     * @param id
     * @param state
     */
    public static void updateStateDevice(String id, State state) {
    	if(deviceList.containsKey(id)) {
            StateDevice newDevice = getDevice(id);
            if(newDevice.getState() != state){
                newDevice.setState(state);
                deviceList.put(id, newDevice);
                notifyUpdateListeners(id, newDevice);
            } else {
                log.info("No state change, ignoring update");
            }
        }
    }

    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // Add, Remove methods for DeviceHandlers
    // - - - - - - - - - - - - - - - - - - - - - - - - - - 
    /**
     * Call this method to add a handler for a given device
     * @param id
     * @param handler
     */
    public static void addDeviceHandler(String id, StateDeviceHandler handler) {
        if(!deviceHandlers.containsKey(id)) {
            deviceHandlers.put(id, handler);
        }
    }
    
    /**
     * Call this method to remove a handler for a given device
     * @param id
     */
    public static void removeDeviceHandler(String id) {
        if(deviceHandlers.containsKey(id)) {
            deviceHandlers.remove(id);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // Notification methods for adding, removing, and updating
    // a device
    // - - - - - - - - - - - - - - - - - - - - - - - - - - 
    /**
     * This method is called internally when a new device is added
     * to the device manager
     * @param id
     * @param device
     */
    private static void notifyAddListeners(String id, StateDevice device) {
        if(deviceHandlers.containsKey(id)) {
            StateDeviceHandler handler = deviceHandlers.get(id);
            handler.onAddDevice(device);
        }
    }
    
    private static void notifyRemoveListeners(String id, StateDevice device) {
        if(deviceHandlers.containsKey(id)) {
            StateDeviceHandler handler = deviceHandlers.get(id);
            handler.onRemoveDevice(device);
            
        }
    }
    
    /**
     * This method notifies the handlers with a copy of the updated object
     * @param id
     * @param device
     */
    private static void notifyUpdateListeners(String id, StateDevice device) {
        if(deviceHandlers.containsKey(id)) {
            StateDeviceHandler handler = deviceHandlers.get(id);
            handler.onUpdateDevice(device);
        } else {
            log.debug("No listener found for device " + device.toString());
        }
    }
}

package com.alfred.common.datamodel;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfred.common.handlers.StateDeviceHandler;

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
     * Method to retrieve a given device. Returns null if the 
     * device doesn't exist
     * @param id
     * @return
     */
    public static StateDevice getDevice(String id) {
        if(deviceList.containsKey(id)) {
            return deviceList.get(id);
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
    private static void addStateDevice(StateDevice device) {
        log.info("Adding new device to Device Manager");
        log.info(device.toString());
        deviceList.put(device.getId(), device);
        notifyAddListeners(device.getId(), device);
    }
    
    /**
     * Call this method to remove a device from the device manager
     * @param device
     */
    public static void removeStateDevice(StateDevice device) {
        log.info("Removing device from Device Manager");
        log.info(device.toString());
        deviceList.remove(device.getId());
        notifyRemoveListeners(device.getId(), device);
    }
    
    /**
     * Call this method to update the state of a device managed
     * by the device manager. If the device doesn't currently exist
     * it will be added
     * @param id
     * @param state
     */
    public static void updateStateDevice(StateDevice device) {
        if(deviceList.containsKey(device.getId())) {
            log.info("Updating device on Device Manager");
            log.info(device.toString());
            deviceList.replace(device.getId(), device);
        } else {
            addStateDevice(device);
        }
        notifyUpdateListeners(device.getId(), device);
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
            log.info("Adding Handler for device " + id);
            deviceHandlers.put(id, handler);
        }
    }
    
    /**
     * Call this method to remove a handler for a given device
     * @param id
     */
    public static void removeDeviceHandler(String id) {
        if(deviceHandlers.containsKey(id)) {
            log.info("Removing Handler for device " + id);
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
    
    private static void notifyUpdateListeners(String id, StateDevice device) {
        if(deviceHandlers.containsKey(id)) {
            StateDeviceHandler handler = deviceHandlers.get(id);
            handler.onUpdateDevice(device);
            
        }
    }
}

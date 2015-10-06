package com.alfred.common.datamodel;

import java.util.ArrayList;
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
    private static ArrayList<StateDeviceHandler> deviceHandlers = new ArrayList<StateDeviceHandler>();
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
     * Method to see if the device manager contains a specified device
     * @param id
     * @return
     */
    public static  boolean contains(String id) {
        return deviceList.containsKey(id);
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
        for(StateDeviceHandler handler : deviceHandlers) {
            handler.onAddDevice(device);
        }
    }
    
    /**
     * Call this method to remove a device from the device manager
     * @param device
     */
    public static void removeStateDevice(StateDevice device) {
        deviceList.remove(device.getId());
        for(StateDeviceHandler handler : deviceHandlers) {
            handler.onRemoveDevice(device);
        }
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
            StateDevice updateDevice = getDevice(device.getId());
            if(updateDevice.getState() != device.getState()){
                updateDevice.setState(device.getState());
                deviceList.put(device.getId(), updateDevice);
                // notify handlers
                for(StateDeviceHandler handler : deviceHandlers) {
                    handler.onUpdateDevice(updateDevice);
                }
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
            StateDevice updateDevice = getDevice(id);
            if(updateDevice.getState() != state){
                updateDevice.setState(state);
                deviceList.put(id, updateDevice);
             // notify handlers
                for(StateDeviceHandler handler : deviceHandlers) {
                    handler.onUpdateDevice(updateDevice);
                }
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
    public static void addDeviceHandler(StateDeviceHandler handler) {
        if(!deviceHandlers.contains(handler)) {
            deviceHandlers.add(handler);
        }
    }
    
    /**
     * Call this method to remove a handler for a given device
     * @param id
     */
    public static void removeDeviceHandler(StateDeviceHandler handler) {
        if(deviceHandlers.contains(handler)) {
            deviceHandlers.remove(handler);
        }
    }
}

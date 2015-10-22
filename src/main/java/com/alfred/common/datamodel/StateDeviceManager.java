package com.alfred.common.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfred.common.handlers.StateDeviceHandler;
import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage.State;

/**
 * This class is the backbone of the Alfred API. It manages the list of devices
 * managed by the API client application (like the <a
 * href="https://github.com/kkanzelmeyer/alfred-server">Alfred Server</a>
 * project for Raspberry Pi or the <a
 * href="https://github.com/kkanzelmeyer/alfred-client">Alfred Client</a>
 * project for Android) This class also manages the list of Device Handlers
 * registered with the API.
 * <p>
 * The core functionality is simple: a device is created and added to this
 * StateDeviceManager class using the <code>addStateDevice</code> method. The
 * device can then be accessed at any point in the future by called
 * <code>getDevice</code> and supplying the device id.
 * <p>
 * Handlers are treated identically to devices - they can be added with the
 * <code>addDeviceHandler</code> method. When a state device is added, removed,
 * or updated the registered handlers are notified with a reference to the
 * device.
 * 
 * @author Kevin Kanzelmeyer
 *
 */
public class StateDeviceManager {
    
    // List of devices to manage
    private static HashMap<String, StateDevice> deviceList = new HashMap<String, StateDevice>();
    
    // List of handlers to manage
    private static ArrayList<StateDeviceHandler> deviceHandlers = new ArrayList<StateDeviceHandler>();
    
    // Logger
    final private static Logger log = LoggerFactory.getLogger(StateDeviceManager.class);
    
    
    /**
     * Method to retrieve a clone of a device. Returns null if the device
     * doesn't exist
     * 
     * @param id
     *            The ID of the desired device
     * @return an instance of the desired state device. Note that this method
     *         returns "null" if the desired object doesn't exist
     */
    public static StateDevice getDevice(String id) {
        if(deviceList.containsKey(id)) {
        	StateDevice clone = new StateDevice(deviceList.get(id));
            return clone;
        } else return null;
    }
    
    /**
     * Method to see if the device manager contains a specified device
     * 
     * @param id
     *          The ID of the desired device
     * @return True if the device exists in the Device Manager, False otherwise
     */
    public static  boolean contains(String id) {
        return deviceList.containsKey(id);
    }
    
    /**
     * Method to retrieve all devices
     * 
     * @return A Hashmap of all registered devices in the Device Manager
     */
    public static HashMap<String, StateDevice> getAllDevices() {
        return deviceList;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // Add, Remove, Update methods for DeviceList
    // - - - - - - - - - - - - - - - - - - - - - - - - - - 
    
    /**
     * This method adds the input device to be managed by the Device Manager. It will
     * notify all registered StateDeviceHandlers that a new device has been added
     * 
     * @param device A StateDevice
     * 
     */
    public static void addStateDevice(StateDevice device) {
        deviceList.put(device.getId(), device);
        for(StateDeviceHandler handler : deviceHandlers) {
            handler.onAddDevice(device);
        }
    }
    
    /**
     * This method removes a device from the device manager
     * 
     * @param device A StateDevice
     */
    public static void removeStateDevice(StateDevice device) {
        deviceList.remove(device.getId());
        for(StateDeviceHandler handler : deviceHandlers) {
            handler.onRemoveDevice(device);
        }
    }
    
    /**
     * Method to update a given state device. Note that if the device doesn't
     * currently exist it will be added. Also note that the state device will
     * only be updated if the input device state is different
     * 
     * @param device
     *            an instance of the device to update
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
     * Method to update a given state device. Note that if the device doesn't
     * currently exist it will be added. Also note that the state device will
     * only be updated if the input device state is different
     * 
     * @param id The ID of the device to update
     * @param state The new state
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
     * Method to add a StateDeviceHandler to the device manager
     * 
     * @param handler An instance of the handler
     */
    public static void addDeviceHandler(StateDeviceHandler handler) {
        if(!deviceHandlers.contains(handler)) {
            deviceHandlers.add(handler);
        }
    }
    
    /**
     * Method to add a StateDeviceHandler to the device manager
     * 
     * @param handler An instance of the handler
     */
    public static void removeDeviceHandler(StateDeviceHandler handler) {
        if(deviceHandlers.contains(handler)) {
            deviceHandlers.remove(handler);
        }
    }
}

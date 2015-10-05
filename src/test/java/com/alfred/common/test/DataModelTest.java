package com.alfred.common.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.alfred.common.datamodel.StateDevice;
import com.alfred.common.datamodel.StateDeviceManager;
import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage.State;
import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage.Type;

public class DataModelTest {

    /**
     * Test to add a new device
     */
    @Test
    public void testAddNewDevice() {
        String id = "device0001";
        String name = "State Device 1";
        StateDevice device = new StateDevice.Builder()
                            .setId(id)
                            .setType(Type.DOORBELL)
                            .setName(name)
                            .setState(State.INACTIVE)
                            .build();
        
        StateDeviceManager.updateStateDevice(device);
        
        StateDevice deviceCopy = StateDeviceManager.getDevice(id);
        if(deviceCopy != null) {
            assertEquals("Name should be equal", name, deviceCopy.getName());
        }
    }

    
    /**
     * Test to update an existing device
     */
    @Test
    public void testUpdateDevice() {
        String id = "device0002";
        String name = "State Device 1";
        StateDevice device = new StateDevice.Builder()
                            .setId(id)
                            .setName(name)
                            .setType(Type.CEILINGFAN)
                            .setState(State.INACTIVE)
                            .build();

        // Add the new device
        StateDeviceManager.updateStateDevice(device);

        // Update the device
        StateDevice updatedDevice = new StateDevice.Builder()
                .setId(id)
                .setName(name)
                .setType(Type.CEILINGFAN)
                .setState(State.ACTIVE)
                .build();
        StateDeviceManager.updateStateDevice(updatedDevice);

        StateDevice deviceCopy = StateDeviceManager.getDevice(id);
        if(deviceCopy != null) {
            assertEquals("State should be active", State.ACTIVE, deviceCopy.getState());
        }
    }
//
//    /**
//     * Test to verify handler functionality
//     */
//    @Test
//    public void testDeviceHandler() {
//        // Create test device 1
//        String id = "device0001";
//        String name = "State Device 1";
//        StateDevice device = new StateDevice.Builder()
//                            .setId(id)
//                            .setName(name)
//                            .setState(State.INACTIVE)
//                            .build();
//
//
//        // create test device two
//        String id2 = "device0002";
//        String name2 = "State Device 2";
//        StateDevice device2 = new StateDevice.Builder()
//                            .setId(id)
//                            .setName(name)
//                            .setState(State.CLOSED)
//                            .build();
//
//
//        // Add a handler
//        StateDeviceManager.addDeviceHandler(id, new DoorbellStateHandler());
//        StateDeviceManager.addDeviceHandler(id2, new DoorbellStateHandler());
//
//        // Add the new devices
//        StateDeviceManager.addStateDevice(device);
//        StateDeviceManager.addStateDevice(device2);
//
//        // Update the device
//        String newName = "TheVeldt";
//        StateDeviceManager.updateStateDevice(id, newName);
//        StateDeviceManager.updateStateDevice(id2, "^^ Noob");
//        
//        StateDevice deviceCopy = StateDeviceManager.getDevice(id);
//        if(deviceCopy != null) {
//            assertEquals("Name should be equal", newName, deviceCopy.getName());
//        }
//    }
    
}
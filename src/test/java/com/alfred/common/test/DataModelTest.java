package com.alfred.common.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.alfred.common.datamodel.StateDevice;
import com.alfred.common.datamodel.StateDeviceManager;
import com.alfred.common.handlers.StateDeviceHandler;
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
        StateDeviceManager.updateStateDevice(id, State.ACTIVE);

        StateDevice deviceCopy = StateDeviceManager.getDevice(id);
        if(deviceCopy != null) {
            assertEquals("State should be active", State.ACTIVE, deviceCopy.getState());
        }
    }

    /**
     * Test to verify handler functionality
     */
    @Test
    public void testDeviceHandler() {

        // Add a handler
        StateDeviceManager.addDeviceHandler(new DoorbellStateHandler());
        
        // Create test device 1
        String id1 = "doorbell1";
        StateDevice device = new StateDevice.Builder()
                            .setId(id1)
                            .setType(Type.DOORBELL)
                            .setName("Front Door")
                            .setState(State.INACTIVE)
                            .build();


        // create test device two
        String id2 = "doorbell2";
        StateDevice device2 = new StateDevice.Builder()
                            .setId(id2)
                            .setType(Type.DOORBELL)
                            .setName("Side Door")
                            .setState(State.INACTIVE)
                            .build();


        // Add the new devices
        StateDeviceManager.addStateDevice(device);
        StateDeviceManager.addStateDevice(device2);

        // Update the device
        State newState = State.ACTIVE;
        StateDeviceManager.updateStateDevice(id1, newState);
        
        // Test case
        StateDevice clone = StateDeviceManager.getDevice(id1);
        assertEquals(newState, clone.getState());
    }
    
    
    private class DoorbellStateHandler implements StateDeviceHandler {

        public void onAddDevice(StateDevice device) {
            if(device.getType() == Type.DOORBELL) {
                System.out.println("\nDevice added:" + device.toString());
            }
        }

        public void onUpdateDevice(StateDevice device) {
            if(device.getType() == Type.DOORBELL) {
                System.out.println("\nDevice updated:" + device.toString());
            }
        }

        public void onRemoveDevice(StateDevice device) {
            // TODO Auto-generated method stub
            
        }
        
    }
}
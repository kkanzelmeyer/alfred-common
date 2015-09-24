package com.alfred.common.datamodel;

import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage;
import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage.State;
import com.alfred.common.messages.StateDeviceProtos.StateDeviceMessage.Type;

/**
 * State Device
 * 
 * This object is used to represent a device connected to Alfred who's state is
 * of interest to the user; i.e. a light, ceiling fan, electrical outlet
 * (on/off), a garage door (open/closed), a doorbell (active, inactive), a
 * window sensor, etc.
 * 
 * @author Kevin Kanzelmeyer
 *
 */
public class StateDevice {

    private String _id;
    private String _name;
    private State  _state;
    private Type   _type;

    /**
     * Use the builder to create a new state device
     * @param builder
     */
    public StateDevice(Builder builder) {
        _id    = builder.getId();
        _name  = builder.getName();
        _state = builder.getState();
        _type  = builder.getType();
        System.out.println("Creating new State Device : " + getName());
    }
    
    /**
     * This constructor takes a state device protobuf message
     * @param msg
     */
    public StateDevice(StateDeviceMessage msg) {
        _id = msg.getId();
        _name = msg.getName();
        _state = msg.getState();
        _type = msg.getType();
    }
    
    public String getId() {
        return _id;
    }

    
    public String getName() {
        return _name;
    }

    public State getState() {
        return _state;
    }

    public void setState(State state) {
        this._state = state;
    }
    
    public Type getType() {
        return this._type;
    }
    
    public String toString() {
        return    "Device ID: " + _id
                + "\nDevice Name: " + _name
                + "\nDevice Type: " + _type
                + "\nDevice State: " + _state;
        
    }
    
    public StateDeviceMessage toMessage() {
        return StateDeviceMessage.newBuilder()
                .setId(_id)
                .setName(_name)
                .setState(_state)
                .setType(_type)
                .build();
    }

    public static class Builder {
        
        private String id;
        private String name;
        private State state;
        private Type type;
        
        public String getId() {
            return id;
        }
        
        public Builder setId(String id) {
            this.id = id;
            return this;
        }
        public String getName() {
            return name;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public State getState() {
            return state;
        }
        
        public Builder setState(State state) {
            this.state = state;
            return this;
        }
        
        public Type getType() {
            return type;
        }
        
        public Builder setType(Type type) {
            this.type = type;
            return this;
        }
        
        public StateDevice build() {
            return new StateDevice(this);
        }
    }
    
}

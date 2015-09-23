package com.alfred.common.handlers;

import com.alfred.common.datamodel.StateDevice;

public interface StateDeviceHandler {
    
    public void onAddDevice(StateDevice device);
    
    public void onUpdateDevice(StateDevice device);
    
    public void onRemoveDevice(StateDevice device);

}

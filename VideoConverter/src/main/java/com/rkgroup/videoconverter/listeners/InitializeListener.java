package com.rkgroup.videoconverter.listeners;


/**
 * Called when Init the Video converter
 *
 * @author Rufen Khokhar
 */
public interface InitializeListener {
    /**
     * @param status status of initialization
     */
    void initialized(boolean status);
}

package com.rkgroup.videoconverter.listeners;

import com.rkgroup.videoconverter.MediaInfo;

/**
 * Adapter class that implements {@link EncoderProgressListener},
 * Customized the implementation of override methods
 *
 * @author Rufen Khokhar
 */
public class EncoderProgress implements EncoderProgressListener {
    /**
     * This method is called before the encoding process starts, reporting
     * information about the source stream that will be decoded and re-encoded.
     *
     * @param info Information about the source multimedia stream.
     */
    @Override
    public void onStartEncoding(MediaInfo info) {

    }

    /**
     * This method is called to notify a progress in the encoding process.
     *
     * @param progress A progress value representing the encoding process progress.
     */

    @Override
    public void onUpdateProgress(int progress) {

    }

    /**
     * This method is called every time the encoder need to send a message
     * (usually, a warning).
     *
     * @param message The message sent by the encoder.
     */

    @Override
    public void onSendMassage(String message) {

    }

    /**
     * calls on encoding complete,abort,error
     *
     * @param completionCode (0 success,1 abort,-1 error)
     */

    @Override
    public void onCompleteEncoding(int completionCode) {

    }

    /**
     * call when the encoder error accord
     *
     * @param e {@link Exception} will be send
     */

    @Override
    public void onReceivedError(Exception e) {

    }

}

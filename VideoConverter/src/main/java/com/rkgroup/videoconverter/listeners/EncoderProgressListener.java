package com.rkgroup.videoconverter.listeners;

import com.rkgroup.videoconverter.MediaInfo;

/**
 * Encoding progress listener interface. Instances of implementing classes could
 * be used to listen an encoding process.
 *
 * @author Rufen Khokhar
 */
public interface EncoderProgressListener {

    int STATUS_COMPLETED = 0;
    int STATUS_ABORT = 1;
    int STATUS_ERROR = -1;

    /**
     * This method is called before the encoding process starts, reporting
     * information about the source stream that will be decoded and re-encoded.
     *
     * @param info Information about the source multimedia stream.
     */
    void onStartEncoding(MediaInfo info);

    /**
     * This method is called to notify a progress in the encoding process.
     *
     * @param progress A progress value representing the encoding process progress.
     */
    void onUpdateProgress(int progress);

    /**
     * This method is called every time the encoder need to send a message
     * (usually, a warning).
     *
     * @param message The message sent by the encoder.
     */
    void onSendMassage(String message);

    /**
     * calls on encoding complete,abort,error
     *
     * @param completionCode (0 success,1 abort,-1 error)
     */
    void onCompleteEncoding(int completionCode);

    /**
     * call when the encoder error accord
     *
     * @param e {@link Exception} will be send
     */
    void onReceivedError(Exception e);
}

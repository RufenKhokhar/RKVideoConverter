package com.rkgroup.videoconverter.exceptions;


/**
 * Use to send Encoder Error Messages to user
 *
 * @author Rufen Khokhar
 */
public class EncoderError extends Exception {

    private static final long serialVersionUID = 1L;

    public EncoderError(String message) {
        super(message);
    }

    EncoderError(int step, int lineNumber, String message) {
        super("In step: " + step + " Error in line " + lineNumber + " : <" + message + ">");
    }

    EncoderError(Throwable cause) {
        super(cause);
    }

    EncoderError(String message, Throwable cause) {
        super(message, cause);
    }

    public EncoderError(Exception e) {
        super(e);
    }

    public EncoderError() {
        super();
    }
}

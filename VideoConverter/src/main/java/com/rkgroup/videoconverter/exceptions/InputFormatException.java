package com.rkgroup.videoconverter.exceptions;


/**
 * Use to send InputFormatException Error Messages to user
 *
 * @author Rufen Khokhar
 */
public class InputFormatException extends EncoderError {
    private static final long serialVersionUID = 1L;

    InputFormatException() {
        super();
    }

    public InputFormatException(String message) {
        super(message);
    }
}

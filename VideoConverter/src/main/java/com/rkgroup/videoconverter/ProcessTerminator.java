package com.rkgroup.videoconverter;


/**
 * A package-private utility to add a shutdown hook to kill ongoing encoding
 * processes at the jvm shutdown.
 *
 * @author Rufen Khokhar
 */
class ProcessTerminator extends Thread {

    /**
     * The process to terminate.
     */
    private final Process process;

    /**
     * Builds the terminator.
     *
     * @param process The process to terminate.
     */
    public ProcessTerminator(Process process) {
        this.process = process;
    }

    /**
     * It terminate the supplied process.
     */
    @Override
    public void run() {
        process.destroy();
    }

}

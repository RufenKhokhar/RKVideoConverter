package com.rkgroup.videoconverter;

import android.util.Log;

import com.rkgroup.videoconverter.exceptions.EncoderError;
import com.rkgroup.videoconverter.listeners.EncoderProgressListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rufen Khokhar
 */
class ConversionAnalyzer {
    private static final String TAG = "ConversionAnalyze";

    /**
     * This regexp is used to parse the ffmpeg output about the ongoing encoding
     * process.
     */
    private static final Pattern PROGRESS_INFO_PATTERN = Pattern.compile(
            "\\s*(\\w+)\\s*=\\s*(\\S+)\\s*", Pattern.CASE_INSENSITIVE);

    private final EncoderProgressListener listener;

    private final long duration;
    private final List<String> unhandledMessages = new LinkedList<>();
    // Step 0 = Before input stuff
    // Step 1 = Input stuff
    // Step 2 = Stream Mapping
    // Step 3 = Output
    // Step 4 = frame=...
    private int step = 0;
    private int lineNR = 0;
    private String lastWarning = null;

    public ConversionAnalyzer(long duration, EncoderProgressListener listener) {
        this.duration = duration;
        this.listener = listener;
    }

    public void analyzeNewLine(String line) throws EncoderError {
        lineNR++;
        Log.d(TAG, String.format("Input Line (%d): <%s>", lineNR, line));
        if (line.startsWith("WARNING: ")) {
            if (listener != null) {
                listener.onSendMassage(line);
            }
        }
        if (line.startsWith("Press [q]")) {
            // Abort messages
        } else {
            switch (step) {
                case 0: {
                    if (line.startsWith("Input #0")) {
                        step = 1;
                    } else {
                        // wait for Stream mapping:
                    }
                }
                break;
                case 1: {
                    if (line.startsWith("Stream mapping:")) {
                        // streamMappingFound
                        step = 2;
                    } else if (line.startsWith("Output #0")) {
                        // outputFound
                        step = 2;
                    } else if (!line.startsWith("  ")) {
                        Log.i(TAG, String.format("Unhandled message in step: %d Line: %d message: <%s>", step, lineNR, line));
                        unhandledMessages.add(line);
                    } else {
                        // wait for Stream mapping:
                    }
                }
                break;
                case 2: {
                    if (line.startsWith("Output #0")) {
                        // outputFound
                        step = 3;
                    } else if (line.startsWith("Stream mapping:")) {
                        // streamMappingFound
                        step = 3;
                    } else if (!line.startsWith("  ")) {
                        Log.i(TAG, String.format("Unhandled message in step: %d Line: %d message: <%s>", step, lineNR, line));
                        unhandledMessages.add(line);
                    } else {
                        // wait for Stream mapping:
                    }
                }
                break;
                case 3: {
                    if (line.startsWith("  ")) {
                        // output details
                    } else if (line.startsWith("video:")) {
                        step = 4;
                    } else if (line.startsWith("frame=")) {
                        // Progress notification video
                    } else if (line.startsWith("size=")) {
                        // Progress notification audio
                    } else if (line.endsWith("Queue input is backward in time")
                            || line.contains("Application provided invalid, non monotonically increasing dts to muxer in stream")) {
                        // Ignore these non-fatal errors, if they are fatal, the next line(s)
                        // will trow the full error
                        if (listener != null) {
                            listener.onSendMassage(line);
                        }
                    } else {
                        Log.i(TAG, String.format("Unhandled message in step: %d Line: %d message: <%s>", step, lineNR, line));
                        unhandledMessages.add(line);
                    }
                }
            }
            if (line.startsWith("frame=") || line.startsWith("size=")) {
                try {
                    line = line.trim();
                    if (line.length() > 0) {
                        HashMap<String, String> table = parseProgressInfoLine(line);
                        if (listener != null) {
                            String time = table.get("time");
                            if (time != null) {
                                String[] dParts = time.split(":");
                                // HH:MM:SS.xx

                                double seconds = Double.parseDouble(dParts[dParts.length - 1]);
                                if (dParts.length > 1) {
                                    seconds += Double.parseDouble(dParts[dParts.length - 2]) * 60;
                                    if (dParts.length > 2) {
                                        seconds += Double.parseDouble(dParts[dParts.length - 3]) * 60 * 60;
                                    }
                                }
                                int progress = (int) Math.round((seconds * 1000L * 1000L) / (double) duration) / 10;
                                //int progress = (int) Math.round((seconds * 1000L) / (double) duration);
                                listener.onUpdateProgress(Math.min(progress, 100));
                            }
                        }
                        lastWarning = null;
                    }
                } catch (Exception ex) {
                    Log.w(TAG, String.format("Error in progress parsing for line: %s", line), ex);
                }
            }
        }
    }

    public String getLastWarning() {
        return lastWarning;
    }

    /**
     * Private utility. Parse a line and try to match its contents against the
     * {@see PROGRESS_INFO_PATTERN} pattern. It the line can be parsed,
     * it returns a hashtable with progress informations, otherwise it returns
     * null.
     *
     * @param line The line from the ffmpeg output.
     * @return A hashtable with the value reported in the line, or null if the
     * given line can not be parsed.
     */
    private HashMap<String, String> parseProgressInfoLine(String line) {
        HashMap<String, String> table = new HashMap<>();
        Matcher m = PROGRESS_INFO_PATTERN.matcher(line);
        while (m.find()) {
            String key = m.group(1);
            String value = m.group(2);
            table.put(key, value);
        }
        return table;
    }

    /**
     * @return the unhandledMessages
     */
    public List<String> getUnhandledMessages() {
        return unhandledMessages;
    }

}

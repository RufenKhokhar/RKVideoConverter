package com.rkgroup.videoconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;


/**
 * A package-private utility extending java.io.BufferedReader. If a line read
 * with {@link RKBufferedReader#readLine()} is not useful for the calling code,
 * it can be re-inserted in the stream. The same line will be returned again at
 * the next readLine() call.
 *
 * @author Rufen Khokhar
 */

class RKBufferedReader extends BufferedReader {

    /**
     * Re-inserted lines buffer.
     */
    private final ArrayList<String> lines = new ArrayList<>();

    /**
     * It builds the reader.
     *
     * @param in The underlying reader.
     */
    public RKBufferedReader(Reader in) {
        super(in);
    }

    /**
     * It returns the next line in the stream.
     */
    @Override
    public String readLine() throws IOException {
        if (lines.size() > 0) {
            return lines.remove(0);
        } else {
            return super.readLine();
        }
    }

    /**
     * Reinserts a line in the stream. The line will be returned at the next
     * {@link RKBufferedReader#readLine()} call.
     *
     * @param line The line.
     */
    public void reinsertLine(String line) {
        lines.add(0, line);
    }
}

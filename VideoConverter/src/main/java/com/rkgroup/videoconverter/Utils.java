package com.rkgroup.videoconverter;

import java.text.DecimalFormat;

/**
 * @author Rufen Khokhar
 */
public class Utils {

    /***
     * https://www.ffmpeg.org/ffmpeg-utils.html#time-duration-syntax
     *
     * Build a time/duration string based on the milisenconds passed in milis
     * [-][HH:]MM:SS[.m...] or [-]S+[.m...]
     *
     * @param milis number of miliseconds, can be negative too
     * @return String to be used for specifying positions in the video/audio file
     */
    public static String buildTimeDuration(long milis) {
        DecimalFormat df2 = new DecimalFormat("00");
        DecimalFormat df3 = new DecimalFormat("000");
        long milisPart = Math.abs(milis) % 1000;
        long seconds = Math.abs(milis) / 1000;
        long secondsPart = seconds % 60;
        long minutes = seconds / 60;
        long minutesPart = minutes % 60;
        long hours = minutes / 60;
        StringBuilder retVal = new StringBuilder();
        if (milis < 0) {
            retVal.append("-");
        }
        if (hours != 0) {
            retVal.append(df2.format(hours)).append(":");
        }
        if (minutesPart != 0 || hours != 0) {
            retVal.append(df2.format(minutesPart)).append(":");
        }
        retVal.append(df2.format(secondsPart));
        if (milisPart != 0) {
            retVal.append(".").append(df3.format(milisPart));
        }
        return retVal.toString();
    }

    /**
     * Escape all special characters []=;, to be safe to use in command line
     *
     * @param argumentIn input argument to escape
     * @return escaped string
     */
    public static String escapeArgument(String argumentIn) {
        String retVal = argumentIn.replace("[", "\\[");
        retVal = retVal.replace("]", "\\]");
        retVal = retVal.replace("=", "\\=");
        retVal = retVal.replace(":", "\\:");
        retVal = retVal.replace(",", "\\,");
        return retVal;
    }
}

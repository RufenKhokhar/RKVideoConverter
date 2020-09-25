package com.rkgroup.videoconverter;

/**
 * @author Rufen khokhar
 */
public class Color {
    private String color;
    private String alpha = "ff";

    /**
     * @param color color in RRGGBB synatx, like in html/css, but without the leading # character
     */
    public Color(String color) {
        this.color = color;
    }

    /**
     * @param color color in #RRGGBB synatx, like in html/css, but without the leading # character
     * @param alpha Alpha channel, Values from 00 up to FF, 00 means not transparent, FF means fully transparent
     */
    public Color(String color, String alpha) {
        this.color = color;
        this.alpha = alpha;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     * @return this instance
     */
    public Color setColor(String color) {
        this.color = color;
        return this;
    }

    /**
     * @return the alpha
     */
    public String getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     * @return this instance
     */
    public Color setAlpha(String alpha) {
        this.alpha = alpha;
        return this;
    }

    /**
     * @return color in command line format
     */
    public String getFfmpegColor() {
        return "0x" + color + alpha;
    }
}
package com.rkgroup.videoconverter.videofilters;

import androidx.annotation.NonNull;


public class VideoFilter {

    private String expression;

    public VideoFilter() {
        this.expression = "";
    }

    public VideoFilter(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return this.expression;
    }

    public VideoFilter setExpression(String expression) {
        this.expression = expression;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return this.expression;
    }
}

package com.rkgroup.videoconverter;

import androidx.annotation.NonNull;

/**
 * Properties used by {@link Encoder}to encode and decode the streams
 *
 * @author Rufen Khokhar
 */

class EncoderProperties {
    private static final long serialVersionUID = 1L;

    /**
     * The format name for the encoded target multimedia file. Be sure this
     * format is supported (see {@link com.rkgroup.videoconverter.Encoder#getSupportedEncodingFormats()}.
     */
    private String format = null;

    /**
     * The start offset time (seconds). If null or not specified no start offset
     * will be applied.
     */
    private Float offset = null;

    /**
     * The duration (seconds) of the re-encoded stream. If null or not specified
     * the source stream, starting from the offset, will be completely
     * re-encoded in the target stream.
     */
    private Float duration = null;

    /**
     * The attributes for the encoding of the audio stream in the target
     * multimedia file. If null of not specified no audio stream will be
     * encoded. It cannot be null if also the video field is null.
     */
    private AudioProperties audioProperties = null;

    /**
     * The attributes for the encoding of the video stream in the target
     * multimedia file. If null of not specified no video stream will be
     * encoded. It cannot be null if also the audio field is null.
     */
    private VideoProperties videoProperties = null;

    /**
     * Should we try to copy over the meta data?
     */
    private boolean mapMetaData = false;

    /**
     * Maximum number of cores/cpus to use for conversion
     * -1 means use default of ffmpeg
     */
    private int filterThreads = -1;
    /**
     * Number of threads to use for decoding (if supported by codec)
     */
    private int decodingThreads = -1;
    /**
     * Number of threads to use for encoding (if supported by codec)
     */
    private int encodingThreads = -1;


    /**
     * Returns the format name for the encoded target multimedia file.
     *
     * @return The format name for the encoded target multimedia file.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format name for the encoded target multimedia file. Be sure this
     * format is supported (see {@link com.rkgroup.videoconverter.Encoder#getSupportedEncodingFormats()}.
     *
     * @param format The format name for the encoded target multimedia file.
     * @return this instance
     */
    public EncoderProperties setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Returns the start offset time (seconds).
     *
     * @return The start offset time (seconds).
     */
    public Float getOffset() {
        return offset;
    }

    /**
     * Sets the start offset time (seconds). If null or not specified no start
     * offset will be applied.
     *
     * @param offset The start offset time (seconds).
     * @return this instance
     */
    public EncoderProperties setOffset(Float offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Returns the duration (seconds) of the re-encoded stream.
     *
     * @return The duration (seconds) of the re-encoded stream.
     */
    public Float getDuration() {
        return duration;
    }

    /**
     * Sets the duration (seconds) of the re-encoded stream. If null or not
     * specified the source stream, starting from the offset, will be completely
     * re-encoded in the target stream.
     *
     * @param duration The duration (seconds) of the re-encoded stream.
     * @return this instance
     */
    public EncoderProperties setDuration(Float duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Returns the attributes for the encoding of the audio stream in the target
     * multimedia file.
     *
     * @return The attributes for the encoding of the audio stream in the target
     * multimedia file.
     */
    public AudioProperties getAudioProperties() {
        return audioProperties;
    }

    /**
     * Sets the attributes for the encoding of the audio stream in the target
     * multimedia file. If null of not specified no audio stream will be
     * encoded. It cannot be null if also the video field is null.
     *
     * @param audioProperties The attributes for the encoding of the audio
     *                        stream in the target multimedia file.
     * @return this instance
     */
    public EncoderProperties setAudioProperties(AudioProperties audioProperties) {
        this.audioProperties = audioProperties;
        return this;
    }

    /**
     * Returns the attributes for the encoding of the video stream in the target
     * multimedia file.
     *
     * @return The attributes for the encoding of the video stream in the target
     * multimedia file.
     */
    public VideoProperties getVideoProperties() {
        return videoProperties;
    }

    /**
     * Sets the attributes for the encoding of the video stream in the target
     * multimedia file. If null of not specified no video stream will be
     * encoded. It cannot be null if also the audio field is null.
     *
     * @param videoProperties The attributes for the encoding of the video
     *                        stream in the target multimedia file.
     * @return this instance
     */
    public EncoderProperties setVideoProperties(VideoProperties videoProperties) {
        this.videoProperties = videoProperties;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName() + "(format=" + format + ", offset="
                + offset + ", duration=" + duration + ", audioProperties="
                + audioProperties + ", videoProperties=" + videoProperties
                + ")";
    }

    /**
     * @return the mapMetaData
     */
    public boolean isMapMetaData() {
        return mapMetaData;
    }

    /**
     * Copy over meta data from original file to new output if possible
     *
     * @param mapMetaData the mapMetaData to set
     * @return this instance
     */
    public EncoderProperties setMapMetaData(boolean mapMetaData) {
        this.mapMetaData = mapMetaData;
        return this;
    }

    /**
     * @return Maximum number of cores/cpus to use for filtering
     * -1 means use default of ffmpeg
     */
    public int getFilterThreads() {
        return filterThreads;
    }

    /**
     * ffmpeg uses multiple cores for filtering
     *
     * @param filterThreads Maximum number of cores/cpus to use
     *                      -1 means use default of ffmpeg
     * @return this instance
     */
    public EncoderProperties setFilterThreads(int filterThreads) {
        this.filterThreads = filterThreads;
        return this;
    }

    /**
     * Number of threads to use for decoding (if supported by codec)
     * -1 means use default of ffmpeg
     *
     * @return the decodingThreads
     */
    public int getDecodingThreads() {
        return decodingThreads;
    }

    /**
     * Number of threads to use for decoding (if supported by codec)
     * -1 means use default of ffmpeg
     *
     * @param decodingThreads the decodingThreads to set
     * @return this instance
     */
    public EncoderProperties setDecodingThreads(int decodingThreads) {
        this.decodingThreads = decodingThreads;
        return this;
    }

    /**
     * Number of threads to use for encoding (if supported by codec)
     * -1 means use default of ffmpeg
     *
     * @return the encodingThreads
     */
    public int getEncodingThreads() {
        return encodingThreads;
    }

    /**
     * Number of threads to use for encoding (if supported by codec)
     * -1 means use default of ffmpeg
     *
     * @param encodingThreads the encodingThreads to set
     * @return this instance
     */
    public EncoderProperties setEncodingThreads(int encodingThreads) {
        this.encodingThreads = encodingThreads;
        return this;
    }
}

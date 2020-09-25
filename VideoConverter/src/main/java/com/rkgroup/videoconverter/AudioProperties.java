package com.rkgroup.videoconverter;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * This class is used  to customize the AudioProperties
 *
 * @author Rufen Khokhar
 */

public class AudioProperties implements Serializable {

    /**
     * This value can be setted in the codec field to perform a direct stream
     * copy, without re-encoding of the audio stream.
     */
    public static final String COPY_ORIGINAL_STREAM = "copy";
    public static final int mono = 1;
    public static final int stereo = 2;
    public static final int quad = 4;
    private static final long serialVersionUID = 2L;
    /**
     * The codec name for the encoding process. If null or not specified the
     * encoder will perform a direct stream copy.
     */
    private String codec = null;

    /**
     * The bitrate value for the encoding process. If null or not specified a
     * default value will be picked.
     */
    private Integer bitRate = null;

    /**
     * The samplingRate value for the encoding process. If null or not specified
     * a default value will be picked.
     */
    private Integer samplingRate = null;

    /**
     * The channels value (1=mono, 2=stereo) for the encoding process. If null
     * or not specified a default value will be picked.
     */
    private Integer channels = null;

    /**
     * The volume value for the encoding process. If null or not specified a
     * default value will be picked. If 256 no volume change will be performed.
     */
    private Integer volume = null;

    /**
     * The audio quality value for the encoding process. If null or not specified
     * the ffmpeg default will be used
     */
    private Integer quality = null;

    /**
     * Returns the codec name for the encoding process.
     *
     * @return The codec name for the encoding process.
     */
    String getCodec() {
        return codec;
    }

    /**
     * Sets the codec name for the encoding process.If null or not specified
     * the encoder will perform a direct stream copy. Be sure the supplied codec name is in the list returned by
     * {@link Encoder#getAudioEncoders()}.
     * <p>
     * A special value can be picked from
     * {@link AudioProperties#COPY_ORIGINAL_STREAM}.
     *
     * @param codec The codec name for the encoding process.
     * @return this instance
     */
    public AudioProperties setCodec(String codec) {
        this.codec = codec;
        return this;
    }

    /**
     * Returns the bitrate value for the encoding process.
     *
     * @return The bitrate value for the encoding process.
     */
    Integer getBitRate() {
        return bitRate;
    }

    /**
     * Sets the bitrate value for the encoding process. If null or not specified
     * a default value will be picked.
     *
     * @param bitRate The bitrate value for the encoding process.
     * @return this instance
     */
    public AudioProperties setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    /**
     * Returns the samplingRate value for the encoding process.
     *
     * @return the samplingRate The samplingRate value for the encoding process.
     */
    Integer getSamplingRate() {
        return samplingRate;
    }

    /**
     * Sets the samplingRate value for the encoding process. If null or not
     * specified a default value will be picked.
     *
     * @param samplingRate The samplingRate value for the encoding process.
     * @return this instance
     */
    public AudioProperties setSamplingRate(Integer samplingRate) {
        this.samplingRate = samplingRate;
        return this;
    }

    /**
     * Returns the channels value (1=mono, 2=stereo, 4=quad) for the encoding process.
     *
     * @return The channels value (1=mono, 2=stereo, 4=quad) for the encoding process.
     */
    Integer getChannels() {
        return channels;
    }

    /**
     * Sets the channels value (1=mono, 2=stereo, 4=quad) for the encoding process. If
     * null or not specified a default value will be picked.
     *
     * @param channels The channels value (1=mono, 2=stereo, 4=quad) for the encoding
     *                 process.
     * @return this instance
     */
    public AudioProperties setChannels(Integer channels) {
        this.channels = channels;
        return this;
    }

    /**
     * Returns the volume value for the encoding process.
     *
     * @return The volume value for the encoding process.
     */
    Integer getVolume() {
        return volume;
    }

    /**
     * Sets the volume value for the encoding process. If null or not specified
     * a default value will be picked. If 256 no volume change will be
     * performed.
     * <p>
     * volume is the "amplitude ratio" or "sound pressure level" ratio
     * 2560 is volume=20dB
     * The formula is dBnumber=20*lg(amplitude ratio)
     * 128 means reducing by 50%
     * 512 means doubling the volume
     *
     * @param volume The volume value for the encoding process.
     * @return this instance
     */
    public AudioProperties setVolume(Integer volume) {
        this.volume = volume;
        return this;
    }

    /**
     * @return the audio conversion quality
     */
    public Integer getQuality() {
        return quality;
    }

    /**
     * The audio quality value for the encoding process. If null or not specified
     * the ffmpeg default will be used
     * <p>
     * The value depends on the choosen codec
     * <p>
     * For mp3 you can see here:
     * https://trac.ffmpeg.org/wiki/Encode/MP3
     * <p>
     * Or more general
     * https://ffmpeg.org/ffmpeg-codecs.html
     *
     * @param quality the audio conversion quality to set
     * @return this instance
     */
    public AudioProperties setQuality(Integer quality) {
        this.quality = quality;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName() + "(codec=" + codec + ", bitRate="
                + bitRate + ", samplingRate=" + samplingRate + ", channels="
                + channels + ", volume=" + volume + ", quality=" + quality + ")";
    }
}

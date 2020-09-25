package com.rkgroup.videoconverter;

import android.content.Context;
import android.util.Log;

import com.rkgroup.videoconverter.exceptions.EncoderError;
import com.rkgroup.videoconverter.listeners.EncoderProgressListener;
import com.rkgroup.videoconverter.videofilters.VideoFilter;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class of the package. Instances can encode audio and video streams.
 *
 * @author Rufen Khokhar
 */
class Encoder {

    private static final String TAG = "Encoder";

    /**
     * This regexp is used to parse the ffmpeg output about the supported
     * formats.
     */
    private static final Pattern FORMAT_PATTERN = Pattern
            .compile("^\\s*([D ])([E ])\\s+([\\w,]+)\\s+.+$");

    /**
     * This regexp is used to parse the ffmpeg output about the included
     * encoders/decoders.
     */
    private static final Pattern ENCODER_DECODER_PATTERN = Pattern.compile(
            "^\\s*([AVS]).{5}\\s(\\S+).(.+)$", Pattern.CASE_INSENSITIVE);

    /**
     * This regexp is used to parse the ffmpeg output about the success of an
     * encoding operation.
     */
    private static final Pattern SUCCESS_PATTERN = Pattern.compile(
            "^\\s*video\\:\\S+\\s+audio\\:\\S+\\s+subtitle\\:\\S+\\s+global headers\\:\\S+.*$",
            Pattern.CASE_INSENSITIVE);

    /**
     * The locator of the ffmpeg executable used by this encoder.
     */
    private final FFMPEGLocator locator;

    /**
     * The executor used to do the conversion
     * Is saved here, so we can abort the conversion process
     */
    private FFMPEGExecutor ffmpegExecutor;

    /**
     * List of unhandled messages from ffmpeng run
     */
    private List<String> unhandledMessages = null;

    /**
     * It builds an encoder using a {@link LocalFFMPEGLocator} instance to
     * locate the ffmpeg executable to use.
     */
    public Encoder(Context mContext) {
        this.locator = new LocalFFMPEGLocator(mContext);
    }

    /**
     * It builds an encoder with a custom {@link FFMPEGLocator}.
     *
     * @param locator The locator picking up the ffmpeg executable used by the
     *                encoder.
     */
    public Encoder(FFMPEGLocator locator) {
        this.locator = locator;
    }

    /**
     * Returns a list with the names of all the audio decoders bundled with the
     * ffmpeg distribution in use. An audio stream can be decoded only if a
     * decoder for its format is available.
     *
     * @return A list with the names of all the included audio decoders.
     * @throws EncoderError If a problem occurs calling the underlying
     *                      ffmpeg executable.
     */
    public String[] getAudioDecoders() throws EncoderError {
        return getCoders(false, true);
    }

    /**
     * Returns a list with the names of all the audio encoders bundled with the
     * ffmpeg distribution in use. An audio stream can be encoded using one of
     * these encoders.
     *
     * @return A list with the names of all the included audio encoders.
     * @throws EncoderError If a problem occurs calling the underlying
     *                      ffmpeg executable.
     */
    public String[] getAudioEncoders() throws EncoderError {
        return getCoders(true, true);
    }

    /**
     * Returns a list with the names of all the coders bundled with the ffmpeg
     * distribution in use.
     *
     * @param encoder Do search encoders, else decoders
     * @param audio   Do search for audio encodes, else video
     * @return A list with the names of all the included encoders
     */
    protected String[] getCoders(boolean encoder, boolean audio) throws EncoderError {
        ArrayList<String> res = new ArrayList<>();
        FFMPEGExecutor localFFMPEG = locator.createExecutor();
        localFFMPEG.addArgument(encoder ? "-encoders" : "-decoders");
        try {
            localFFMPEG.execute();
            RKBufferedReader reader =
                    new RKBufferedReader(new InputStreamReader(localFFMPEG
                            .getInputStream()));
            String line;
            String format = audio ? "A" : "V";
            boolean headerFound = false;
            boolean evaluateLine = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }
                if (headerFound) {
                    if (evaluateLine) {
                        Matcher matcher = ENCODER_DECODER_PATTERN.matcher(line);
                        if (matcher.matches()) {
                            //String encoderFlag = matcher.group(2);
                            String audioVideoFlag = matcher.group(1);
                            if (format.equals(audioVideoFlag)) {
                                String name = matcher.group(2);
                                res.add(name);
                            }
                        } else {
                            break;
                        }
                    } else {
                        evaluateLine = line.trim().equals("------");
                    }
                } else if (line.trim().equals(encoder ? "Encoders:" : "Decoders:")) {
                    headerFound = true;
                }
            }
        } catch (IOException e) {
            throw new EncoderError(e);
        } finally {
            localFFMPEG.destroy();
        }
        int size = res.size();
        String[] ret = new String[size];
        for (int i = 0; i < size; i++) {
            ret[i] = res.get(i);
        }
        return ret;
    }

    /**
     * Returns a list with the names of all the video decoders bundled with the
     * ffmpeg distribution in use. A video stream can be decoded only if a
     * decoder for its format is available.
     *
     * @return A list with the names of all the included video decoders.
     * @throws EncoderError If a problem occurs calling the underlying
     *                      ffmpeg executable.
     */
    public String[] getVideoDecoders() throws EncoderError {
        return getCoders(false, false);
    }

    /**
     * Returns a list with the names of all the video encoders bundled with the
     * ffmpeg distribution in use. A video stream can be encoded using one of
     * these encoders.
     *
     * @return A list with the names of all the included video encoders.
     * @throws EncoderError If a problem occurs calling the underlying
     *                      ffmpeg executable.
     */
    public String[] getVideoEncoders() throws EncoderError {
        return getCoders(true, false);
    }

    /**
     * Returns a list with the names of all the file formats supported at
     * encoding time by the underlying ffmpeg distribution. A multimedia file
     * could be encoded and generated only if the specified format is in this
     * list.
     *
     * @return A list with the names of all the supported file formats at
     * encoding time.
     * @throws EncoderError If a problem occurs calling the underlying
     *                      ffmpeg executable.
     */
    public String[] getSupportedEncodingFormats() throws EncoderError {
        return getSupportedCodingFormats(true);
    }

    /**
     * Returns a list with the names of all the file formats supported at
     * en/de-coding time by the underlying ffmpeg distribution.A multimedia file
     * could be encoded and generated only if the specified format is in this
     * list.
     *
     * @param encoding True for encoding job, false to decode a file
     * @return A list with the names of all the supported file formats at
     * encoding time.
     * @throws EncoderError If a problem occurs calling the underlying
     *                      ffmpeg executable.
     */
    protected String[] getSupportedCodingFormats(boolean encoding) throws EncoderError {
        ArrayList<String> res = new ArrayList<>();
        FFMPEGExecutor localFFMPEG = locator.createExecutor();
        localFFMPEG.addArgument("-formats");
        try {
            localFFMPEG.execute();
            RKBufferedReader reader =
                    new RKBufferedReader(new InputStreamReader(localFFMPEG
                            .getInputStream()));
            String line;
            String ed = encoding ? "E" : "D";
            boolean headerFound = false;
            boolean evaluateLine = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }
                if (headerFound) {
                    if (evaluateLine) {
                        Matcher matcher = FORMAT_PATTERN.matcher(line);
                        if (matcher.matches()) {
                            String encoderFlag = matcher.group(encoding ? 2 : 1);
                            if (ed.equals(encoderFlag)) {
                                String aux = matcher.group(3);
                                StringTokenizer st = new StringTokenizer(aux, ",");
                                while (st.hasMoreTokens()) {
                                    String token = st.nextToken().trim();
                                    if (!res.contains(token)) {
                                        res.add(token);
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    } else {
                        evaluateLine = line.trim().equals("--");
                    }
                } else if (line.trim().equals("File formats:")) {
                    headerFound = true;
                }
            }
        } catch (IOException e) {
            throw new EncoderError(e);
        } finally {
            localFFMPEG.destroy();
        }
        int size = res.size();
        String[] ret = new String[size];
        for (int i = 0; i < size; i++) {
            ret[i] = res.get(i);
        }
        return ret;
    }

    /**
     * Returns a list with the names of all the file formats supported at
     * decoding time by the underlying ffmpeg distribution. A multimedia file
     * could be open and decoded only if its format is in this list.
     *
     * @return A list with the names of all the supported file formats at
     * decoding time.
     * @throws EncoderError If a problem occurs calling the underlying
     *                      ffmpeg executable.
     */
    public String[] getSupportedDecodingFormats() throws EncoderError {
        return getSupportedCodingFormats(false);
    }


    /**
     * Re-encode a multimedia file.
     * <p>
     * This method is not reentrant, instead create multiple object instances
     *
     * @param multimediaObject The source multimedia file. It cannot be null. Be
     *                         sure this file can be decoded (see null null null null     {@link Encoder#getSupportedDecodingFormats()},
     *                         {@link Encoder#getAudioDecoders()} and
     *                         {@link Encoder#getVideoDecoders()}).
     * @param target           The target multimedia re-encoded file. It cannot be null.
     *                         If this file already exists, it will be overwrited.
     * @param properties       A set of properties for the encoding process.
     * @param listener         An optional progress listener for the encoding process.
     *                         It can be null.
     */
    public void encode(FFMPEGMediaObject multimediaObject, File target, EncoderProperties properties,
                       EncoderProgressListener listener) {
        List<FFMPEGMediaObject> src = new ArrayList<>();
        src.add(multimediaObject);
        encode(src, target, properties, listener);
    }


    /**
     * Re-encode a multimedia file(s).
     * <p>
     * This method is not reentrant, instead create multiple object instances
     *
     * @param multimediaObjects The source multimedia files. It cannot be null. Be
     *                          sure this file can be decoded (see null null null null     {@link Encoder#getSupportedDecodingFormats()},
     *                          {@link Encoder#getAudioDecoders()} and* {@link Encoder#getVideoDecoders()})
     *                          When passing multiple sources, make sure that they are compatible in the
     *                          way that ffmpeg can concat them. We don't use the complex filter at the moment
     *                          Perhaps you will need to first transcode/resize them
     *                          https://trac.ffmpeg.org/wiki/Concatenate @see "Concat protocol"
     * @param target            The target multimedia re-encoded file. It cannot be null.
     *                          If this file already exists, it will be overwrited.
     * @param properties        A set of properties for the encoding process.
     * @param listener          An optional progress listener for the encoding process.
     *                          It can be null.
     * @throws IllegalArgumentException If both audio and video parameters are
     *                                  null.
     */
    public void encode(List<FFMPEGMediaObject> multimediaObjects, File target, EncoderProperties properties,
                       EncoderProgressListener listener) {

        String formatAttribute = properties.getFormat();
        Float offsetAttribute = properties.getOffset();
        Float durationAttribute = properties.getDuration();
        AudioProperties audioProperties = properties.getAudioProperties();
        VideoProperties videoProperties = properties.getVideoProperties();
        if (audioProperties == null && videoProperties == null) {
            listener.onReceivedError(new IllegalArgumentException(
                    "Both audio and video properties are null"));
            return;
        }
        target = target.getAbsoluteFile();
        target.getParentFile().mkdirs();
        ffmpegExecutor = locator.createExecutor();
        // Set global options
        if (properties.getFilterThreads() != -1) {
            ffmpegExecutor.addArgument("--filter_thread");
            ffmpegExecutor.addArgument(Integer.toString(properties.getFilterThreads()));
        }
        if (offsetAttribute != null) {
            ffmpegExecutor.addArgument("-ss");
            ffmpegExecutor.addArgument(String.valueOf(offsetAttribute.floatValue()));
        }
        // Set input options, must be before -i argument
        if (properties.getDecodingThreads() != -1) {
            ffmpegExecutor.addArgument("-threads");
            ffmpegExecutor.addArgument(Integer.toString(properties.getDecodingThreads()));
        }
        ffmpegExecutor.addArgument("-i");
        if (multimediaObjects.size() == 1) {
            // Simple case with one input source
            if (multimediaObjects.get(0).isURL()) {
                ffmpegExecutor.addArgument(multimediaObjects.get(0).getURL().toString());
            } else {
                ffmpegExecutor.addArgument(multimediaObjects.get(0).getFile().getAbsolutePath());
            }
        } else {
            StringBuilder inFiles = new StringBuilder();
            inFiles.append("concat:");
            boolean isFirst = true;
            for (FFMPEGMediaObject in : multimediaObjects) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    inFiles.append("|");
                }
                if (in.isURL()) {
                    inFiles.append(in.getURL().toString());
                } else {
                    inFiles.append(in.getFile().getAbsolutePath());
                }
            }
            ffmpegExecutor.addArgument(inFiles.toString());
        }
        if (durationAttribute != null) {
            ffmpegExecutor.addArgument("-t");
            ffmpegExecutor.addArgument(String.valueOf(durationAttribute.floatValue()));
        }
        if (videoProperties == null) {
            ffmpegExecutor.addArgument("-vn");
        } else {
            String codec = videoProperties.getCodec();
            if (codec != null) {
                ffmpegExecutor.addArgument("-vcodec");
                ffmpegExecutor.addArgument(codec);
            }
            String tag = videoProperties.getTag();
            if (tag != null) {
                ffmpegExecutor.addArgument("-vtag");
                ffmpegExecutor.addArgument(tag);
            }
            Integer bitRate = videoProperties.getBitRate();
            if (bitRate != null) {
                ffmpegExecutor.addArgument("-vb");
                ffmpegExecutor.addArgument(String.valueOf(bitRate.intValue()));
            }
            Integer frameRate = videoProperties.getFrameRate();
            if (frameRate != null) {
                ffmpegExecutor.addArgument("-r");
                ffmpegExecutor.addArgument(String.valueOf(frameRate.intValue()));
            }
            VideoSize size = videoProperties.getSize();
            if (size != null) {
                ffmpegExecutor.addArgument("-s");
                ffmpegExecutor.addArgument(size.getWidth() + "x"
                        + size.getHeight());
            }

            if (videoProperties.isFaststart()) {
                ffmpegExecutor.addArgument("-movflags");
                ffmpegExecutor.addArgument("faststart");
            }

            if (videoProperties.getX264Profile() != null) {
                ffmpegExecutor.addArgument("-profile:v");
                ffmpegExecutor.addArgument(videoProperties.getX264Profile().getModeName());
            }

            if (videoProperties.getVideoFilters().size() > 0) {
                for (VideoFilter videoFilter : videoProperties.getVideoFilters()) {
                    ffmpegExecutor.addArgument("-vf");
                    ffmpegExecutor.addArgument(videoFilter.getExpression());
                }
            }

            Integer quality = videoProperties.getQuality();
            if (quality != null) {
                ffmpegExecutor.addArgument("-qscale:v");
                ffmpegExecutor.addArgument(String.valueOf(quality.intValue()));
            }
        }
        if (audioProperties == null) {
            ffmpegExecutor.addArgument("-an");
        } else {
            String codec = audioProperties.getCodec();
            if (codec != null) {
                ffmpegExecutor.addArgument("-acodec");
                ffmpegExecutor.addArgument(codec);
            }
            Integer bitRate = audioProperties.getBitRate();
            if (bitRate != null) {
                ffmpegExecutor.addArgument("-ab");
                ffmpegExecutor.addArgument(String.valueOf(bitRate.intValue()));
            }
            Integer channels = audioProperties.getChannels();
            if (channels != null) {
                ffmpegExecutor.addArgument("-ac");
                ffmpegExecutor.addArgument(String.valueOf(channels.intValue()));
            }
            Integer samplingRate = audioProperties.getSamplingRate();
            if (samplingRate != null) {
                ffmpegExecutor.addArgument("-ar");
                ffmpegExecutor.addArgument(String.valueOf(samplingRate.intValue()));
            }
            Integer volume = audioProperties.getVolume();
            if (volume != null) {
                ffmpegExecutor.addArgument("-vol");
                ffmpegExecutor.addArgument(String.valueOf(volume.intValue()));
            }
            Integer quality = audioProperties.getQuality();
            if (quality != null) {
                ffmpegExecutor.addArgument("-qscale:a");
                ffmpegExecutor.addArgument(String.valueOf(quality.intValue()));
            }
        }
        if (formatAttribute != null) {
            ffmpegExecutor.addArgument("-f");
            ffmpegExecutor.addArgument(formatAttribute);
        }
        // Set output options
        if (properties.getEncodingThreads() != -1) {
            ffmpegExecutor.addArgument("-threads");
            ffmpegExecutor.addArgument(Integer.toString(properties.getEncodingThreads()));
        }

        ffmpegExecutor.addArgument("-y");
        ffmpegExecutor.addArgument(target.getAbsolutePath());

        if (properties.isMapMetaData()) {   // Copy over meta data if possible
            ffmpegExecutor.addArgument("-map_metadata");
            ffmpegExecutor.addArgument("0");
        }

//        ffmpegExecutor.addArgument("-loglevel");
//        ffmpegExecutor.addArgument("warning"); // Only report errors

        try {
            ffmpegExecutor.execute();
        } catch (IOException e) {
            listener.onReceivedError(new EncoderError(e));
            return;
        }
        try {
            String lastWarning = null;
            long duration = 0;
            RKBufferedReader reader = new RKBufferedReader(
                    new InputStreamReader(ffmpegExecutor.getErrorStream()));
            MediaInfo info = null;
            if (multimediaObjects.size() == 1 && (!multimediaObjects.get(0).isURL() || !multimediaObjects.get(0).isReadURLOnce())) {
                info = multimediaObjects.get(0).getInfo();
            }
            if (durationAttribute != null) {
                duration = Math
                        .round((durationAttribute * 1000L));
            } else {
                if (info != null) {
                    duration = info.getDuration();
                    if (offsetAttribute != null) {
                        duration -= Math
                                .round((offsetAttribute * 1000L));
                    }
                }
            }
            if (listener != null) {
                listener.onStartEncoding(info);
            }
            String line;
            ConversionAnalyzer outputAnalyzer = new ConversionAnalyzer(duration, listener);
            while ((line = reader.readLine()) != null) {
                outputAnalyzer.analyzeNewLine(line);
            }
            if (outputAnalyzer.getLastWarning() != null) {
                if (!SUCCESS_PATTERN.matcher(lastWarning).matches()) {
                    if (listener != null) {
                        listener.onReceivedError(new EncoderError("No match for: " + SUCCESS_PATTERN + " in " + lastWarning));
                    }
                    return;
                }
            }
            unhandledMessages = outputAnalyzer.getUnhandledMessages();
            int exitCode = ffmpegExecutor.getProcessExitCode();
            if (exitCode != 0) {
                Log.e(TAG, String.format("Process exit code: %d  to %s", exitCode, target.getName()), null);
                if (listener != null) {
                    listener.onReceivedError(new EncoderError("Exit code of ffmpeg encoding run is " + exitCode));
                }
                return;
            } else {
                // if exitCode is 0 means success and progress is full
                if (listener != null) {
                    listener.onUpdateProgress(100);
                }
            }
            if (listener != null) {
                listener.onCompleteEncoding(exitCode);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onReceivedError(e);
            }
        } finally {
            ffmpegExecutor.destroy();
            ffmpegExecutor = null;
        }
    }

    /**
     * Return the list of unhandled output messages of the ffmpeng encoder run
     *
     * @return the unhandledMessages list of unhandled messages, can be null or empty
     */
    public List<String> getUnhandledMessages() {
        return unhandledMessages;
    }

    /**
     * Force the encoding process to stop
     */
    public void abortEncoding() {
        if (ffmpegExecutor != null) {
            ffmpegExecutor.destroy();
            ffmpegExecutor = null;
        }
    }

}

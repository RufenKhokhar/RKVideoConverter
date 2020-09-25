package com.rkgroup.videoconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.rkgroup.videoconverter.listeners.EncoderProgressListener;
import com.rkgroup.videoconverter.listeners.InitializeListener;
import com.rkgroup.videoconverter.videofilters.VideoFilter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.rkgroup.videoconverter.CpuArch.NONE;

/**
 * Based on Builder pattern used for Quick Start
 *
 * @author Rufen Khokhar
 */

public class VideoConverter {
    private static final String TAG = "VideoConverter";
    private static final int VERSION = 17;
    private ExecutorService executorService;
    // handler will push listener call from background thread to Main thread
    private Handler handler;
    private Encoder encoder;
    private LocalFFMPEGLocator ffmpegLocator;
    private EncoderProperties properties;

    private VideoConverter(Context mContext, EncoderProperties properties) {
        ffmpegLocator = new LocalFFMPEGLocator(mContext);
        this.properties = properties;
        encoder = new Encoder(mContext);
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler();
    }

    public static void initialize(@NonNull Context mContext, @NonNull InitializeListener listener) {
        listener.initialized(init(mContext));
    }

    private static boolean init(Context mContext) {
        // check if arch is supported
        CpuArch cpuArch = CpuArchHelper.getCpuArch();
        if (cpuArch == NONE) {
            Log.e(TAG, "arch not supported");
            return false;
        }

        // get fFmpegFile file
        File fFmpegFile = FileManager.getFFmpeg(mContext);

        SharedPreferences settings = mContext.getSharedPreferences(Constants.FFMPEG_PREFS, Context.MODE_PRIVATE);
        int version = settings.getInt(Constants.KEY_PREF_VERSION, 0);

        // check if fFmpegFile file exists
        if (!fFmpegFile.exists() || version < VERSION) {
            try {
                InputStream inputStream = mContext.getAssets().open(FileManager.FFMPEG_BINARY_FILE);
                if (!FileManager.inputStreamToFile(inputStream, fFmpegFile)) {
                    return false;
                }
                Log.d(TAG, "successfully wrote fFmpegFile file!");
                settings.edit().putInt(Constants.KEY_PREF_VERSION, VERSION).apply();
            } catch (IOException e) {
                Log.e(TAG, "error while opening assets", e);
                return false;
            }
        }

        // check if fFmpegFile can be executed
        if (!fFmpegFile.canExecute()) {
            // try to make executable
            try {
                try {
                    Runtime.getRuntime().exec("chmod -R 777 " + fFmpegFile.getAbsolutePath()).waitFor();
                } catch (InterruptedException e) {
                    Log.e(TAG, "interrupted exception", e);
                    return false;
                } catch (IOException e) {
                    Log.e(TAG, "io exception", e);
                    return false;
                }

                if (!fFmpegFile.canExecute()) {
                    // our last hope!
                    if (!fFmpegFile.setExecutable(true)) {
                        Log.e(TAG, "unable to make executable");
                        return false;
                    }
                }
            } catch (SecurityException e) {
                Log.e(TAG, "security exception", e);
                e.printStackTrace();
                return false;
            }
        }

        Log.d(TAG, "fFmpegFile is ready!");

        return true;
    }

    public void convertVideo(File source, File target, EncoderProgressListener progressListener) {
        executorService.submit(() -> {
            FFMPEGMediaObject multimediaObject = new FFMPEGMediaObject(source, ffmpegLocator);
            encoder.encode(multimediaObject, target, properties, new EncoderProgressListener() {
                @Override
                public void onStartEncoding(MediaInfo info) {
                    handler.post(() -> progressListener.onStartEncoding(info));
                }

                @Override
                public void onUpdateProgress(int progress) {
                    handler.post(() -> progressListener.onUpdateProgress(progress));
                }

                @Override
                public void onSendMassage(String message) {
                    handler.post(() -> progressListener.onSendMassage(message));
                }

                @Override
                public void onCompleteEncoding(int completionCode) {
                    handler.post(() -> progressListener.onCompleteEncoding(completionCode));
                }

                @Override
                public void onReceivedError(Exception e) {
                    handler.post(() -> progressListener.onReceivedError(e));
                }
            });

        });
        executorService.shutdown();
    }

    public void cancelConversion() {
        if (encoder != null) {
            encoder.abortEncoding();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "VideoConverter{" +
                "executorService=" + executorService +
                ", handler=" + handler +
                ", encoder=" + encoder +
                ", ffmpegLocator=" + ffmpegLocator +
                ", properties=" + properties +
                '}';
    }

    public static class Builder {
        private Context mContext;
        private AudioProperties audioProperties;
        private VideoProperties videoProperties;
        private EncoderProperties properties;

        public Builder(Context mContext) {
            this.mContext = mContext;
            audioProperties = new AudioProperties();
            videoProperties = new VideoProperties();
            properties = new EncoderProperties();
        }

        /**
         * Sets the channels value (1=mono, 2=stereo, 4=quad) for the encoding process. If
         * null or not specified a default value will be picked.
         *
         * @param channels The channels value ({@link AudioProperties#mono},
         *                 {@link AudioProperties#stereo},
         *                 {@link AudioProperties#quad})
         *                 for the encoding
         *                 process.
         * @return this instance
         */
        public Builder setAudioChannels(Integer channels) {
            audioProperties.setChannels(channels);
            return this;
        }

        /**
         * Sets the samplingRate value for the encoding process. If null or not
         * specified a default value will be picked.
         *
         * @param samplingRate The samplingRate value for the encoding process.
         * @return this instance
         */
        public Builder setAudioSamplingRate(Integer samplingRate) {
            audioProperties.setSamplingRate(samplingRate);
            return this;
        }

        /**
         * Sets the bitrate value for the encoding process. If null or not specified
         * a default value will be picked.
         *
         * @param bitRate The bitrate value for the encoding process.
         * @return this instance
         */
        public Builder setAudioBitRate(Integer bitRate) {
            audioProperties.setBitRate(bitRate);
            return this;
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
        public Builder setAudioCodec(String codec) {
            audioProperties.setCodec(codec);
            return this;
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
        public Builder setAudioVolume(Integer volume) {
            audioProperties.setVolume(volume);
            return this;
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
        public Builder setAudioQuality(Integer quality) {
            audioProperties.setQuality(quality);
            return this;
        }

        /**
         * The video quality value for the encoding process. If null or not specified
         * the ffmpeg default will be used
         *
         * @param quality the quality to set
         * @return this instance
         */
        public Builder setVideoQuality(Integer quality) {
            videoProperties.setQuality(quality);
            return this;
        }

        /**
         * @param x264Profile the x264Profile to set
         * @return this instance
         */
        public Builder setVideoX264Profile(@NonNull VideoProperties.X264_PROFILE x264Profile) {
            videoProperties.setX264Profile(x264Profile);
            return this;
        }

        /**
         * Sets the video size for the encoding process. If null or not specified
         * the source video size will not be modified.
         *
         * @param size he video size for the encoding process.
         * @return this instance
         */
        public Builder setVideoSize(@NonNull VideoSize size) {
            videoProperties.setSize(size);
            return this;
        }

        /**
         * Sets the frame rate value for the encoding process. If null or not
         * specified a default value will be picked.
         *
         * @param frameRate The frame rate value for the encoding process.
         * @return this instance
         */
        public Builder setVideoFrameRate(Integer frameRate) {
            videoProperties.setFrameRate(frameRate);
            return this;
        }

        /**
         * Sets the codec name for the encoding process. If null or not specified
         * the encoder will perform a direct stream copy.
         * <p>
         * Be sure the supplied codec name is in the list returned by
         * {@link com.rkgroup.videoconverter.Encoder#getVideoEncoders()}.
         * <p>
         * A special value can be picked from
         * {@link VideoProperties#COPY_ORIGINAL_STREAM}.
         *
         * @param codec The codec name for the encoding process.
         * @return this instance
         */
        public Builder setVideoCodec(String codec) {
            videoProperties.setCodec(codec);
            return this;
        }

        /**
         * Sets the bitrate value for the encoding process. If null or not specified
         * a default value will be picked.
         *
         * @param bitRate The bitrate value for the encoding process.
         * @return this instance
         */

        public Builder setVideoBitRate(Integer bitRate) {
            videoProperties.setBitRate(bitRate);
            return this;
        }

        public Builder addFilterOnVideo(VideoFilter filter) {
            videoProperties.addFilter(filter);
            return this;
        }

        /**
         * Sets the format name for the encoded target media file. Be sure this
         * format is supported (see {@link com.rkgroup.videoconverter.Encoder#getSupportedEncodingFormats()}.
         *
         * @param format The format name for the encoded target multimedia file.
         * @return this instance
         */
        public Builder setOutputFormat(String format) {
            properties.setFormat(format);

            return this;
        }

        /**
         * @param fastStart the faststart to set
         * @return this instance
         */
        public Builder setFastStart(boolean fastStart) {
            videoProperties.setFastStart(fastStart);
            return this;
        }

        /**
         * Number of threads to use for encoding (if supported by codec)
         * -1 means use default of ffmpeg
         *
         * @param threads the encodingThreads to set
         * @return this instance
         */
        public Builder setEncodingThreads(Integer threads) {
            properties.setEncodingThreads(threads);
            return this;
        }

        /**
         * Number of threads to use for decoding (if supported by codec)
         * -1 means use default of ffmpeg
         *
         * @param threads the decodingThreads to set
         * @return this instance
         */
        public Builder setDecodingThreads(Integer threads) {
            properties.setDecodingThreads(threads);
            return this;
        }

        /**
         * Sets the duration (seconds) of the re-encoded stream. If null or not
         * specified the source stream, starting from the offset, will be completely
         * re-encoded in the target stream.
         *
         * @param duration The duration (seconds) of the re-encoded stream.
         * @return this instance
         */
        public Builder setDuration(Float duration) {
            properties.setDuration(duration);
            return this;
        }

        /**
         * ffmpeg uses multiple cores for filtering
         *
         * @param filterThreads Maximum number of cores/cpus to use
         *                      -1 means use default of ffmpeg
         * @return this instance
         */
        public Builder setFilterThreads(Integer filterThreads) {
            properties.setFilterThreads(filterThreads);
            return this;
        }

        /**
         * Sets the start offset time (seconds). If null or not specified no start
         * offset will be applied.
         *
         * @param offset The start offset time (seconds).
         * @return this instance
         */
        public Builder setOffset(Float offset) {
            properties.setOffset(offset);
            return this;
        }

        /**
         * Copy over meta data from original file to new output if possible
         *
         * @param mapMetaData the mapMetaData to set
         * @return this instance
         */
        public Builder setMapMetaData(boolean mapMetaData) {
            properties.setMapMetaData(mapMetaData);
            return this;
        }


        public VideoConverter build() {
            properties.setVideoProperties(videoProperties);
            properties.setAudioProperties(audioProperties);
            return new VideoConverter(mContext, properties);
        }

        @NonNull
        @Override
        public String toString() {
            return "Builder{" +
                    "mContext=" + mContext +
                    ", audioProperties=" + audioProperties +
                    ", videoProperties=" + videoProperties +
                    ", properties=" + properties +
                    '}';
        }
    }
}

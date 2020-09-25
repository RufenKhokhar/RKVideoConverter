package com.rkgroup.videoconverter.videofilters;

import androidx.annotation.NonNull;

import com.rkgroup.videoconverter.Color;
import com.rkgroup.videoconverter.Utils;

import java.io.File;

/**
 * Add a text watermark in the video file
 * <p>
 * -vf "drawtext=text='a watermark':x=10:y=H-th-10:fontfile=/pathto/font.ttf:fontsize=10:fontcolor=white:shadowcolor=black:shadowx=2:shadowy=2"
 *
 * @author Rufen khokhar
 */
public class VideoDrawText extends VideoFilter {

    private String watermarkText;
    private int posX = -1;
    private int posY = -1;

    private String fontName = "Arial";
    private File fontFile = null;
    private float fontSize = 10;
    private Color fontColor;

    private int lineSpacing = 0;

    private Color shadowColor = null;
    private int shadowX = 2;
    private int shadowY = 2;

    private int boxBorderWidth = 0;
    private Color boxColor = null;

    private int borderWidth = 0;
    private Color borderColor = null;
    private String addArgument = null;

    /**
     * @param watermarkText Text to be used as watermark
     * @param fontColor     Color of font
     */
    public VideoDrawText(@NonNull String watermarkText, @NonNull Color fontColor) {
        this.watermarkText = watermarkText;
        this.fontColor = fontColor;
    }

    /**
     * @param watermarkText Text to be used as watermark
     * @param posX          X Position of watermark text (From the left) ignored if posX &amp; posY are both -1
     * @param posY          Y Position of watermark text (From the top) ignored if posX &amp; posY are both -1
     * @param fontName      Use this font (Can be null, but then we need a fontFile)
     * @param fontFile      Truetype font file (Only required when fontName is NULL)
     * @param fontSize      Font size
     * @param fontColor     Color of font
     */
    public VideoDrawText(@NonNull String watermarkText, @NonNull int posX, int posY, @NonNull String fontName, @NonNull File fontFile, float fontSize, @NonNull Color fontColor) {
        this.watermarkText = watermarkText;
        this.posX = posX;
        this.posY = posY;
        this.fontName = fontName;
        this.fontFile = fontFile;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
    }


    /**
     * @param shadowColor Color of shadow
     * @param shadowX     X Position of shadow, relative to text
     * @param shadowY     Y Position of shadow, relative to text
     * @return this instance
     */
    public VideoDrawText setShadow(Color shadowColor, int shadowX, int shadowY) {
        this.shadowColor = shadowColor;
        this.shadowX = shadowX;
        this.shadowY = shadowY;
        return this;
    }

    /**
     * @return the watermarkText
     */
    public String getWatermarkText() {
        return watermarkText;
    }

    /**
     * @param watermarkText the watermarkText to set
     * @return this instance
     */
    public VideoDrawText setWatermarkText(String watermarkText) {
        this.watermarkText = watermarkText;
        return this;
    }

    /**
     * @return the posX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * ignored if posX &amp; posY are both -1
     *
     * @param posX the posX to set
     * @return this instance
     */
    public VideoDrawText setPosX(int posX) {
        this.posX = posX;
        return this;
    }

    /**
     * ignored if posX &amp; posY are both -1
     *
     * @return the posY
     */
    public int getPosY() {
        return posY;
    }

    /**
     * @param posY the posY to set
     * @return this instance
     */
    public VideoDrawText setPosY(int posY) {
        this.posY = posY;
        return this;
    }

    /**
     * @return the fontName
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * @param fontName the fontName to set
     * @return this instance
     */
    public VideoDrawText setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    /**
     * @return the fontFile
     */
    public File getFontFile() {
        return fontFile;
    }

    /**
     * @param fontFile the fontFile to set
     * @return this instance
     */
    public VideoDrawText setFontFile(File fontFile) {
        this.fontFile = fontFile;
        return this;
    }

    /**
     * @return the fontSize
     */
    public float getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     * @return this instance
     */
    public VideoDrawText setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * @return the fontColor
     */
    public Color getFontColor() {
        return fontColor;
    }

    /**
     * @param fontColor the fontColor to set
     * @return this instance
     */
    public VideoDrawText setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    /**
     * @return the shadowColor
     */
    public Color getShadowColor() {
        return shadowColor;
    }

    /**
     * @param shadowColor the shadowColor to set
     * @return this instance
     */
    public VideoDrawText setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    /**
     * @return the shadowX
     */
    public int getShadowX() {
        return shadowX;
    }

    /**
     * @param shadowX the shadowX to set
     * @return this instance
     */
    public VideoDrawText setShadowX(int shadowX) {
        this.shadowX = shadowX;
        return this;
    }

    /**
     * @return the shadowY
     */
    public int getShadowY() {
        return shadowY;
    }

    /**
     * @param shadowY the shadowY to set
     * @return this instance
     */
    public VideoDrawText setShadowY(int shadowY) {
        this.shadowY = shadowY;
        return this;
    }

    public VideoDrawText setBox(int borderWidth, Color color) {
        this.setBoxBorderWidth(borderWidth);
        this.setBoxColor(color);
        return this;
    }

    /**
     * @return the boxBorderWidth
     */
    public int getBoxBorderWidth() {
        return boxBorderWidth;
    }

    /**
     * @param boxBorderWidth the boxBorderWidth to set
     * @return this instance
     */
    public VideoDrawText setBoxBorderWidth(int boxBorderWidth) {
        this.boxBorderWidth = boxBorderWidth;
        return this;
    }

    /**
     * @return the boxColor
     */
    public Color getBoxColor() {
        return boxColor;
    }

    /**
     * @param boxColor the boxColor to set
     * @return this instance
     */
    public VideoDrawText setBoxColor(Color boxColor) {
        this.boxColor = boxColor;
        return this;
    }


    /**
     * @return the borderWidth
     */
    public int getBorderWidth() {
        return borderWidth;
    }

    /**
     * @param borderWidth the borderWidth to set
     * @return this instance
     */
    public VideoDrawText setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    /**
     * @return the borderColor
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor the borderColor to set
     * @return this instance
     */
    public VideoDrawText setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * @return the lineSpacing
     */
    public int getLineSpacing() {
        return lineSpacing;
    }

    /**
     * @param lineSpacing the lineSpacing to set
     * @return this instance
     */
    public VideoDrawText setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
        return this;
    }

    /**
     * @return the addArgument
     */
    public String getAddArgument() {
        return addArgument;
    }

    /**
     * Add an additional argument to the command line
     * https://superuser.com/questions/939357/position-text-on-bottom-right-corner
     * <p>
     * Bottom right
     * x=w-tw:y=h-th
     * Bottom right with 10 pixel padding
     * x=w-tw-10:y=h-th-10
     * Top right
     * x=w-tw
     * Top right with 10 pixel padding
     * x=w-tw-10:y=10
     * Top left
     * x=0:y=0
     * Top left with 10 pixel padding
     * x=10:y=10
     * Bottom left
     * y=h-th
     * Bottom left with 10 pixel padding
     * x=10:h-th-10
     * centered
     * x=(w-text_w)/2:y=(h-text_h)/2
     * <p>
     * Can be used to speicfy other positions like "x=(w-text_w)/2:y=(h-text_h)/2"
     * for centered text water mark
     *
     * @param addArgument the addArgument to set
     * @return this instance
     */
    public VideoDrawText setAddArgument(String addArgument) {
        this.addArgument = addArgument;
        return this;
    }

    @Override
    public String getExpression() {
        StringBuilder sb = new StringBuilder();
        sb.append("drawtext=text='");
        sb.append(Utils.escapeArgument(watermarkText));
        if (posX != -1 && posY != -1) {
            sb.append("':x=");
            sb.append(posX);
            sb.append("':y=");
            sb.append(posY);
        }
        if (fontName != null) {
            sb.append(":font=");
            sb.append(fontName);
        } else if (fontFile != null) {
            sb.append(":fontfile=");
            sb.append(fontFile.getAbsoluteFile());
        } else {
            throw new IllegalArgumentException("Need either fontName or fontFile");
        }
        sb.append(":fontsize=");
        sb.append(fontSize);
        sb.append(":fontcolor=");
        sb.append(fontColor.getFfmpegColor());

        if (lineSpacing != 0) {
            sb.append(":line_spacing:").append(lineSpacing);
        }

        if (shadowColor != null) {
            sb.append(":shadowcolor");
            sb.append(shadowColor.getFfmpegColor());
            sb.append(":shadowx=");
            sb.append(shadowX);
            sb.append(":shadowy=");
            sb.append(shadowY);
        }
        if (boxColor != null) {
            sb.append(":box=1:boxcolor");
            sb.append(boxColor.getFfmpegColor());
            sb.append(":boxborderw=");
            sb.append(boxBorderWidth);
        }
        if (borderWidth != 0) {
            sb.append(":bordercolor");
            sb.append(borderColor.getFfmpegColor());
            sb.append(":borderw=");
            sb.append(boxBorderWidth);
        }
        if (addArgument != null) {
            sb.append(":");
            sb.append(addArgument);
        }

        return sb.toString();
    }

}

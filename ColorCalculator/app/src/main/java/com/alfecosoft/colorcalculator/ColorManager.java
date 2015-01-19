package com.alfecosoft.colorcalculator;

import android.graphics.Color;

import com.alfecosoft.utils.DataConversion.Byte;

import java.util.ArrayList;
import java.util.List;

public class ColorManager {

    public enum RedGreenBlue
    {
        RED,
        GREEN,
        BLUE
    }

    public interface OnColorChangeListener {
        void onColorChange(ColorManager color);
    }

    private List<OnColorChangeListener> listeners = new ArrayList<OnColorChangeListener>();
    public void addOnColorChangeListener(OnColorChangeListener item) {
        listeners.add(item);
    }

    private int color;

    public int getColor() {
        return color;
    }
    public String getRGB() {
        return String.format("%s%s%s", Byte.byteToHex(getRed()), Byte.byteToHex(getGreen()), Byte.byteToHex(getBlue()));
    }
    public int getRed()
    {
        return Color.red(this.getColor());
    }
    public int getGreen()
    {
        return Color.green(this.getColor());
    }
    public int getBlue()
    {
        return Color.blue(this.getColor());
    }

    public void setColor(int value) {
        if (value != this.color) {
            this.color = value;
            for (OnColorChangeListener listener: listeners) {
                listener.onColorChange(this);
            }
        }
    }
    public void setRGB(int red, int green, int blue) {
        if (red <=0 ) red=0;
        if (red > 255) red=255;
        if (green <=0 ) green=0;
        if (green > 255) green=255;
        if (blue <=0 ) blue=0;
        if (blue > 255) blue=255;
        setColor(Color.rgb(red, green, blue));
    }
    public void setRGB(String hex) {
        while (hex.length() < 6) {
            hex = hex + "0";
        }
        long value = Long.valueOf(hex, 16);
        setColor((int)value);
    }
    public void setRed(int v)
    {
        setRGB(v, getGreen(), getBlue());
    }
    public void setGreen(int v)
    {
        setRGB(getRed(), v, getBlue());
    }
    public void setBlue(int v)
    {
        setRGB(getRed(), getGreen(), v);
    }

}

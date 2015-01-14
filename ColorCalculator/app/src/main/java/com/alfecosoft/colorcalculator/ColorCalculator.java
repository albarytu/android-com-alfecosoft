package com.alfecosoft.colorcalculator;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alfecosoft.utils.CharacterFilters.DecimalFilter;
import com.alfecosoft.utils.CharacterFilters.HexadecimalFilter;
import com.alfecosoft.utils.CustomKeyboard;
import com.alfecosoft.utils.DataConversion.Byte;

import static android.widget.SeekBar.OnSeekBarChangeListener;

public class ColorCalculator extends Activity {

    private EditText rgbText;
    private EditText redText;
    private EditText greenText;
    private EditText blueText;
    private SeekBar redBar;
    private SeekBar greenBar;
    private SeekBar blueBar;
    private SurfaceView surface;

    private CustomKeyboard hexKeyboard;
    private CustomKeyboard decKeyboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_calculator);


        decKeyboard = new CustomKeyboard(this, R.id.deckeyboardview, R.xml.deckeyboard);
        hexKeyboard = new CustomKeyboard(this, R.id.hexkeyboardview, R.xml.hexkeyboard);

        DecimalFilter filter = new DecimalFilter();

        rgbText = (EditText) findViewById(R.id.rgb);
        rgbText.setFilters(new InputFilter[]{new HexadecimalFilter()});
        rgbText.addTextChangedListener(new RGBTextWatcher());
        hexKeyboard.attachToItem(rgbText, 6, false, false);

        redText = (EditText) findViewById(R.id.red);
        redText.setFilters(new InputFilter[]{filter});
        decKeyboard.attachToItem(redText, 3, false, false);

        greenText = (EditText) findViewById(R.id.green);
        greenText.setFilters(new InputFilter[]{filter});
        decKeyboard.attachToItem(greenText, 3, false, false);

        blueText = (EditText) findViewById(R.id.blue);
        blueText.setFilters(new InputFilter[]{filter});
        decKeyboard.attachToItem(blueText, 3, false, false);

        redBar = (SeekBar) findViewById(R.id.redBar);

        greenBar = (SeekBar) findViewById(R.id.greenBar);

        blueBar = (SeekBar) findViewById(R.id.blueBar);

        surface = (SurfaceView) findViewById(R.id.surface);
        surface.getHolder().addCallback(new surfacePainter());

        redText.addTextChangedListener(new ColorTextWatcher(RedGreenBlue.RED, redText));
        greenText.addTextChangedListener(new ColorTextWatcher(RedGreenBlue.GREEN, greenText));
        blueText.addTextChangedListener(new ColorTextWatcher(RedGreenBlue.BLUE, blueText));
        redBar.setOnSeekBarChangeListener(new ColorBarWatcher(RedGreenBlue.RED, redBar));
        greenBar.setOnSeekBarChangeListener(new ColorBarWatcher(RedGreenBlue.GREEN, greenBar));
        blueBar.setOnSeekBarChangeListener(new ColorBarWatcher(RedGreenBlue.BLUE, blueBar));

        rgbText.setText("ffffff");
    }


    private int color;
    public int getColor()
    {
        return color;
    }
    private Object avoidToChangeItem=null;
    public void setColor(int value)
    {
        if (value != this.color) {
            this.color = value;
            refreshColor();
            avoidToChangeItem = null;
        }
        paintColor(surface.getHolder());
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
    public void setRGB(int red, int green, int blue)
    {
        setColor(Color.rgb(red, green, blue));
    }
    public void setRed(int v)
    {
        setColor(Color.rgb(v, getGreen(), getBlue()));
    }
    public void setGreen(int v)
    {
        setColor(Color.rgb(getRed(), v, getBlue()));
    }
    public void setBlue(int v)
    {
        setColor(Color.rgb(getRed(), getGreen(), v));
    }

    public void refreshColor()
    {
        if (redText != avoidToChangeItem) {
            redText.setText(Integer.toString(getRed(), 10));
        }
        if (greenText != avoidToChangeItem) {
            greenText.setText(Integer.toString(getGreen(), 10));
        }
        if (blueText != avoidToChangeItem) {
            blueText.setText(Integer.toString(getBlue(), 10));
        }
        if (redBar != avoidToChangeItem) {
            redBar.setProgress(getRed());
        }
        if (greenBar != avoidToChangeItem) {
            greenBar.setProgress(getGreen());
        }
        if (blueBar != avoidToChangeItem) {
            blueBar.setProgress(getBlue());
        }
        if (rgbText != avoidToChangeItem) {
            rgbText.setText(String.format("%s%s%s", Byte.byteToHex(getRed()), Byte.byteToHex(getGreen()), Byte.byteToHex(getBlue())));
        }
    }

    public int getValueFromText(TextView text)
    {
        if (text.getText().toString().length()<=0) {
            return 0;
        }
        int r = Integer.valueOf(text.getText().toString(), 10);
        if (r > 255) {
            return 255;
        }
        return r;
    }

    public void paintColor(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null)
        {
            canvas.drawRGB(this.getRed(), this.getGreen(), this.getBlue());
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private class surfacePainter implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            paintColor(surfaceHolder);
        }
        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            paintColor(surfaceHolder);
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }
    }

    private enum RedGreenBlue
    {
        RED,
        GREEN,
        BLUE
    }

    private class ColorTextWatcher implements TextWatcher
    {
        private RedGreenBlue x;
        private TextView sender;
        public ColorTextWatcher(RedGreenBlue x, TextView item)
        {
            this.x = x;
            this.sender=item;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (avoidToChangeItem!=null) return;
            int red=getRed();
            int green=getGreen();
            int blue=getBlue();
            int value=getValueFromText(this.sender);
            if (value < 255) {
                avoidToChangeItem = this.sender;
            }
            switch (x)
            {
                case RED:
                    setRed(value);
                    break;
                case GREEN:
                    setGreen(value);
                    break;
                case BLUE:
                    setBlue(value);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class ColorBarWatcher implements OnSeekBarChangeListener
    {
        private RedGreenBlue x;
        private SeekBar sender;
        public ColorBarWatcher(RedGreenBlue x, SeekBar item)
        {
            this.sender = item;
            this.x = x;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (avoidToChangeItem!=null) return;
            int red=getRed();
            int green=getGreen();
            int blue=getBlue();
            int value=this.sender.getProgress();
            switch (x)
            {
                case RED:
                    if (value!=red) {
                        avoidToChangeItem=this.sender;
                        setRed(value);
                    } else {
                        return;
                    }
                    break;
                case GREEN:
                    if (value!=green) {
                        avoidToChangeItem=this.sender;
                        setGreen(value);
                    } else {
                        return;
                    }
                    break;
                case BLUE:
                    if (value!=blue) {
                        avoidToChangeItem=this.sender;
                        setBlue(value);
                    } else {
                        return;
                    }
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
    private class RGBTextWatcher implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (avoidToChangeItem!=null) return;

            TextView sender = rgbText;
            String hex = sender.getText().toString();
            while (hex.length() < 6) {
                hex = hex + "0";
            }
            long value = Long.valueOf(hex, 16);
            if (value!=getColor()) {
                avoidToChangeItem=sender;
                setColor((int)value);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}

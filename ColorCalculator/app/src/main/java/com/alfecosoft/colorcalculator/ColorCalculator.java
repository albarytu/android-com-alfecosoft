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

import static android.widget.SeekBar.OnSeekBarChangeListener;

public class ColorCalculator extends Activity
{
    private ColorManager mycolor;

    private EditText rgbText;
    private EditText redText;
    private EditText greenText;
    private EditText blueText;
    private SeekBar redBar;
    private SeekBar greenBar;
    private SeekBar blueBar;

    private ColorTextWatcher redTextListener;
    private ColorTextWatcher greenTextListener;
    private ColorTextWatcher blueTextListener;
    private ColorBarWatcher redBarListener;
    private ColorBarWatcher greenBarListener;
    private ColorBarWatcher blueBarListener;
    private RGBTextWatcher rgbTextListener;

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

        redTextListener = new ColorTextWatcher(ColorManager.RedGreenBlue.RED, redText);
        greenTextListener = new ColorTextWatcher(ColorManager.RedGreenBlue.GREEN, greenText);
        blueTextListener = new ColorTextWatcher(ColorManager.RedGreenBlue.BLUE, blueText);
        redBarListener = new ColorBarWatcher(ColorManager.RedGreenBlue.RED, redBar);
        greenBarListener = new ColorBarWatcher(ColorManager.RedGreenBlue.GREEN, greenBar);
        blueBarListener = new ColorBarWatcher(ColorManager.RedGreenBlue.BLUE, blueBar);
        rgbTextListener = new RGBTextWatcher();

        listenersEnabled=false;

        mycolor = new ColorManager();
        mycolor.addOnColorChangeListener(new ColorManager.OnColorChangeListener() {
            @Override
            public void onColorChange(ColorManager color) {
                DisableListeners();
                setTextPreserveSelection(redText,Integer.toString(color.getRed()));
                setTextPreserveSelection(greenText,Integer.toString(color.getGreen()));
                setTextPreserveSelection(blueText,Integer.toString(color.getBlue()));
                redBar.setProgress(color.getRed());
                greenBar.setProgress(color.getGreen());
                blueBar.setProgress(color.getBlue());
                setTextPreserveSelection(rgbText,color.getRGB());
                EnableListeners();
                paintColor(surface.getHolder());
            }
        });
        mycolor.setColor(Color.WHITE);
        EnableListeners();
    }

    private void setTextPreserveSelection(EditText input, String newText) {
        int s=input.getSelectionStart();
        int e=input.getSelectionEnd();
        int max=newText.length();
        input.setText(newText);
        if (s>max) { s=max; }
        if (e>max) { e=max; }
        input.setSelection(s, e);
    }

    private boolean listenersEnabled;

    private void EnableListeners() {
        if (!listenersEnabled) {
            redText.addTextChangedListener(redTextListener);
            greenText.addTextChangedListener(greenTextListener);
            blueText.addTextChangedListener(blueTextListener);
            redBar.setOnSeekBarChangeListener(redBarListener);
            greenBar.setOnSeekBarChangeListener(greenBarListener);
            blueBar.setOnSeekBarChangeListener(blueBarListener);
            rgbText.addTextChangedListener(rgbTextListener);
            listenersEnabled=true;
        }
    }

    private void DisableListeners() {
        if (listenersEnabled) {
            redText.removeTextChangedListener(redTextListener);
            greenText.removeTextChangedListener(greenTextListener);
            blueText.removeTextChangedListener(blueTextListener);
            redBar.setOnSeekBarChangeListener(null);
            greenBar.setOnSeekBarChangeListener(null);
            blueBar.setOnSeekBarChangeListener(null);
            rgbText.removeTextChangedListener(rgbTextListener);
            listenersEnabled = false;
        }
    }

    public void paintColor(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null)
        {
            canvas.drawRGB(this.mycolor.getRed(), this.mycolor.getGreen(), this.mycolor.getBlue());
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


    private class ColorTextWatcher implements TextWatcher
    {
        private ColorManager.RedGreenBlue x;
        private TextView sender;
        public ColorTextWatcher(ColorManager.RedGreenBlue x, TextView item)
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
            String v = this.sender.getText().toString();
            if (v.length() == 0) {
                return;
            }
            int value= Integer.valueOf(v, 10);
            switch (x)
            {
                case RED:
                    mycolor.setRed(value);
                    break;
                case GREEN:
                    mycolor.setGreen(value);
                    break;
                case BLUE:
                    mycolor.setBlue(value);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class ColorBarWatcher implements OnSeekBarChangeListener
    {
        private ColorManager.RedGreenBlue x;
        private SeekBar sender;
        public ColorBarWatcher(ColorManager.RedGreenBlue x, SeekBar item)
        {
            this.sender = item;
            this.x = x;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int value=this.sender.getProgress();
            switch (x)
            {
                case RED:
                    mycolor.setRed(value);
                    break;
                case GREEN:
                    mycolor.setGreen(value);
                    break;
                case BLUE:
                    mycolor.setBlue(value);
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
            mycolor.setRGB(rgbText.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}

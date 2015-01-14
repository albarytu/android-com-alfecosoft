package com.alfecosoft.ipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alfecosoft.utils.CharacterFilters.DecimalFilter;
import com.alfecosoft.utils.CustomKeyboard;
import com.alfecosoft.utils.DataConversion.IP;

public class IPCalculator extends Activity {
    private EditText byte1;
    private EditText byte2;
    private EditText byte3;
    private EditText byte4;
    private EditText bytemask;
    private EditText output;

    private CustomKeyboard byte1Keyboard;
    private CustomKeyboard byte2Keyboard;
    private CustomKeyboard byte3Keyboard;
    private CustomKeyboard byte4Keyboard;
    private CustomKeyboard maskKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcalculator);

        IPValueWatcher watcher = new IPValueWatcher();
        DecimalFilter filter = new DecimalFilter();

        byte1Keyboard = new CustomKeyboard(this, R.id.byte1keyboardview, R.xml.ipkeyboard);
        byte2Keyboard = byte1Keyboard;
        byte3Keyboard = byte1Keyboard;
        byte4Keyboard = new CustomKeyboard(this, R.id.byte4keyboardview, R.xml.lastbit_ipkeyboard);
        maskKeyboard = new CustomKeyboard(this, R.id.maskkeyboardview, R.xml.deckeyboard);

        byte1 = (EditText) findViewById(R.id.byte1);
        byte1.setFilters(new InputFilter[]{filter});
        byte1.addTextChangedListener(watcher);
        byte1Keyboard.attachToItem(byte1, 3, false, true);

        byte2 = (EditText) findViewById(R.id.byte2);
        byte2.setFilters(new InputFilter[]{filter});
        byte2.addTextChangedListener(watcher);
        byte2Keyboard.attachToItem(byte2, 3, true, true);

        byte3 = (EditText) findViewById(R.id.byte3);
        byte3.setFilters(new InputFilter[]{filter});
        byte3.addTextChangedListener(watcher);
        byte3Keyboard.attachToItem(byte3, 3, true, true);

        byte4 = (EditText) findViewById(R.id.byte4);
        byte4.setFilters(new InputFilter[]{filter});
        byte4.addTextChangedListener(watcher);
        byte4Keyboard.attachToItem(byte4, 3, true, true);

        bytemask = (EditText) findViewById(R.id.bytemask);
        bytemask.setFilters(new InputFilter[]{filter});
        bytemask.addTextChangedListener(watcher);
        maskKeyboard.attachToItem(bytemask, 2, true, false);

        output = (EditText) findViewById(R.id.output);
    }

    private void setIPData(int b1, int b2, int b3, int b4, int leftbits)
    {
        StringBuilder sb = new StringBuilder();
        long ip = b1;
        ip = (ip * 256) + b2;
        ip = (ip * 256) + b3;
        ip = (ip * 256) + b4;

        int rightbits=32-leftbits;
        long bitmask=0;
        long notbitmask=0;
        for (int i=0; i<leftbits; i++) {
            bitmask = (bitmask * 2) + 1;
        }
        for (int i=0; i<rightbits; i++) {
            bitmask = (bitmask * 2);
            notbitmask = (notbitmask * 2) + 1;
        }

        long firstip = ip & bitmask;
        long lastip = firstip | notbitmask;
        long hostcount = notbitmask + 1;

        sb.append(String.format("%s: %s\n", getString(R.string.ip_addr), IP.ipToBinary(ip)));
        sb.append(String.format("%s: %s\n", getString(R.string.ip_mask), IP.ipToBinary(bitmask)));
        sb.append(String.format("%s: %s\n", getString(R.string.ip_first), IP.ipToBinary(firstip)));
        sb.append(String.format("%s: %s\n", getString(R.string.ip_last), IP.ipToBinary(lastip)));
        sb.append(String.format("%s: %s\n", getString(R.string.ip_addr), IP.ipToString(ip)));
        sb.append(String.format("%s: %s\n", getString(R.string.ip_mask), IP.ipToString(bitmask)));
        sb.append(String.format("%s: %s-%s\n", getString(R.string.ip_range), IP.ipToString(firstip), IP.ipToString(lastip)));
        sb.append(String.format("%s: %d\n", getString(R.string.ip_hostcount), hostcount));

        output.setText(sb.toString());
    }

    private class IPValueWatcher implements TextWatcher
    {
        public IPValueWatcher()
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (byte1.getText().length()<1) { return; };
            int b1 = Integer.valueOf(byte1.getText().toString());
            if (b1 > 255)
            {
                byte1.setText("255");
                return;
            }
            if (byte2.getText().length()<1) { return; };
            int b2 = Integer.valueOf(byte2.getText().toString());
            if (b2 > 255)
            {
                byte2.setText("255");
                return;
            }
            if (byte3.getText().length()<1) { return; };
            int b3 = Integer.valueOf(byte3.getText().toString());
            if (b3 > 255)
            {
                byte3.setText("255");
                return;
            }
            if (byte4.getText().length()<1) { return; };
            int b4 = Integer.valueOf(byte4.getText().toString());
            if (b4 > 255)
            {
                byte4.setText("255");
                return;
            }
            if (bytemask.getText().length()<1) { return; };
            int bm = Integer.valueOf(bytemask.getText().toString());
            if (bm > 32)
            {
                byte4.setText("32");
                return;
            }
            setIPData(b1, b2, b3, b4, bm);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}

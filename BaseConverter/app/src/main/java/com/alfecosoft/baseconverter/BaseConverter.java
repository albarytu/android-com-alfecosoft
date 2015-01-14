package com.alfecosoft.baseconverter;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.alfecosoft.utils.CharacterFilters.BinaryFilter;
import com.alfecosoft.utils.CharacterFilters.DecimalFilter;
import com.alfecosoft.utils.CharacterFilters.HexadecimalFilter;
import com.alfecosoft.utils.CharacterFilters.OctalFilter;
import com.alfecosoft.utils.CustomKeyboard;

import java.math.BigInteger;

public class BaseConverter extends Activity {
    private EditText binary;
    private EditText octal;
    private EditText decimal;
    private EditText hexadecimal;

    private CustomKeyboard hexKeyboard;
    private CustomKeyboard decKeyboard;
    private CustomKeyboard octKeyboard;
    private CustomKeyboard binKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_converter);

        binKeyboard = new CustomKeyboard(this, R.id.binkeyboardview, R.xml.binkeyboard);

        binary = (EditText) findViewById(R.id.binary);
        binary.setFilters(new InputFilter[]{new BinaryFilter()});
        binary.addTextChangedListener(new BaseValueWatcher(2, binary));
        binKeyboard.attachToItem(binary);

        octKeyboard = new CustomKeyboard(this, R.id.octkeyboardview, R.xml.octkeyboard);

        octal = (EditText) findViewById(R.id.octal);
        octal.setFilters(new InputFilter[]{new OctalFilter()});
        octal.addTextChangedListener(new BaseValueWatcher(8, octal));
        octKeyboard.attachToItem(octal);

        decKeyboard = new CustomKeyboard(this, R.id.deckeyboardview, R.xml.deckeyboard);

        decimal = (EditText) findViewById(R.id.decimal);
        decimal.setFilters(new InputFilter[] {new DecimalFilter()});
        decimal.addTextChangedListener(new BaseValueWatcher(10, decimal));
        decKeyboard.attachToItem(decimal);

        hexKeyboard = new CustomKeyboard(this, R.id.hexkeyboardview, R.xml.hexkeyboard);

        hexadecimal = (EditText) findViewById(R.id.hexadecimal);
        hexadecimal.setFilters(new InputFilter[]{new HexadecimalFilter()});
        hexadecimal.addTextChangedListener(new BaseValueWatcher(16, hexadecimal));
        hexKeyboard.attachToItem(hexadecimal);
    }

    @Override public void onBackPressed() {
        if (hexKeyboard.isKeyboardVisible()) hexKeyboard.hideKeyboard();
        else if (decKeyboard.isKeyboardVisible()) decKeyboard.hideKeyboard();
        else if (octKeyboard.isKeyboardVisible()) octKeyboard.hideKeyboard();
        else if (binKeyboard.isKeyboardVisible()) binKeyboard.hideKeyboard();
        else super.onBackPressed();
    }


    private BigInteger currentvalue=BigInteger.ZERO;

    private void setValue(BigInteger newvalue, TextView source)
    {
        currentvalue=newvalue;
        if (source != binary) {
            binary.setText(currentvalue.toString(2));
        }
        if (source != octal) {
            octal.setText(currentvalue.toString(8));
        }
        if (source != decimal) {
            decimal.setText(currentvalue.toString(10));
        }
        if (source != hexadecimal) {
            hexadecimal.setText(currentvalue.toString(16));
        }
    }

    private class BaseValueWatcher implements TextWatcher
    {
        private int base;
        private TextView textView;

        public BaseValueWatcher(int base, TextView tv)
        {
            this.base = base;
            this.textView = tv;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = "0" + this.textView.getText().toString();
            BigInteger newvalue = new BigInteger(str, this.base);
            if (newvalue.compareTo(currentvalue)!=0) {
                setValue(newvalue, this.textView);
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }


}

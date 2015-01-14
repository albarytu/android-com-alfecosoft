package com.alfecosoft.utils;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.HashMap;

public class CustomKeyboard {

    class EditTextProperties {
        public int maxLength=0;
        public boolean goToPreviousOnBackspace=false;
        public boolean goToNextOnFull=false;
    }

    private Keyboard mKeyboard;
    private KeyboardView mKeyboardView;
    private Activity mHostActivity;

    public final static int CodeDelete = -5;
    public final static int CodeCancel = -3;

    public final static int CodePrev = 55000;
    public final static int CodeNext = 55005;

    private HashMap<Integer, EditTextProperties> editTextProperties = new HashMap<Integer, EditTextProperties>();

    private EditTextProperties getProperties(EditText input)
    {
        Integer id = input.getId();
        EditTextProperties value;
        if (!editTextProperties.containsKey(id))
        {
            value = new EditTextProperties();
            editTextProperties.put(id, value);
        } else {
            value = editTextProperties.get(id);
        }
        return value;
    }

    public int getMaxLength(EditText input)
    {
        return getProperties(input).maxLength;
    }
    public void setMaxLength(EditText input, int value)
    {
        getProperties(input).maxLength=value;
    }
    public boolean getGoToPreviousOnBackspace(EditText input)
    {
        return getProperties(input).goToPreviousOnBackspace;
    }
    public void setGoToPreviousOnBackspace(EditText input, boolean value)
    {
        getProperties(input).goToPreviousOnBackspace=value;
    }
    public boolean getGoToNextOnFull(EditText input)
    {
        return getProperties(input).goToNextOnFull;
    }
    public void setGoToNextOnFull(EditText input, boolean value)
    {
        getProperties(input).goToNextOnFull=value;
    }

    private void GoToPrevious(View v)
    {
        View prevItem=v.focusSearch(View.FOCUS_LEFT);
        if (prevItem!=null) prevItem.requestFocus();
        if (prevItem.getClass() == EditText.class) {
            EditText box = (EditText)prevItem;
            box.selectAll();
        }
    }
    private void GoToNext(View v)
    {
        View nextItem=v.focusSearch(View.FOCUS_RIGHT);
        if (nextItem!=null) nextItem.requestFocus();
        if (nextItem.getClass() == EditText.class) {
            EditText box = (EditText)nextItem;
            box.selectAll();
        }
    }

    private KeyboardView.OnKeyboardActionListener myOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override public void onPress(int primaryCode) {}
        @Override public void onRelease(int primaryCode) {}
        @Override public void onKey(int primaryCode, int[] keyCodes) {
            View focusedItem = mHostActivity.getWindow().getCurrentFocus();
            if (focusedItem == null || focusedItem.getClass()!= EditText.class) return;
            EditText box = (EditText)focusedItem;
            Editable editable = box.getText();
            int start = box.getSelectionStart();
            int end = box.getSelectionEnd();
            switch (primaryCode)
            {
                case CodeCancel:
                    hideKeyboard();
                    break;
                case CodeDelete:
                    if (editable!= null) {
                        if (getGoToPreviousOnBackspace(box) && (editable.length()==0)) {
                            GoToPrevious(box);
                        } else if ((end == start) && (start>0)) {
                            editable.delete(start - 1, start);
                        } else {
                            editable.delete(start, end);
                        }
                    }
                    break;
                case CodeNext:
                    GoToNext(box);
                    break;
                case CodePrev:
                    GoToPrevious(box);
                    break;
                default:
                    editable.delete(start,end);
                    int maxLen=getMaxLength(box);
                    if ((maxLen > 0) && (editable.length() >= maxLen)) return;
                    editable.insert(start, Character.toString((char)primaryCode));

                    if (getGoToNextOnFull(box) && (editable.length()==maxLen)) {
                        GoToNext(box);
                    }
                    break;
            }
        }
        @Override public void onText(CharSequence text) {}
        @Override public void swipeLeft() {}
        @Override public void swipeRight() {}
        @Override public void swipeDown() {}
        @Override public void swipeUp() {}
    };

    public CustomKeyboard(Activity host, int viewid, int layoutid) {
        this.mHostActivity = host;
        this.mKeyboardView = (KeyboardView)host.findViewById(viewid);
        this.mKeyboard = new Keyboard(host, layoutid);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(myOnKeyboardActionListener);
    }
    public void hideKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }
    public void hideStandardKeyboard(View v) {
        if (v!=null) {
            ((InputMethodManager) mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    public void showKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        hideStandardKeyboard(v);
    }
    public boolean isKeyboardVisible() {
        return mKeyboardView.getVisibility()==View.VISIBLE;
    }

    public void attachToItem(EditText input) {
        attachToItem(input, 0, false, false);
    }
    public void attachToItem(EditText input, int maxLength, boolean previousOnBackspace, boolean nextOnFull) {
        setMaxLength(input, maxLength);
        setGoToPreviousOnBackspace(input, previousOnBackspace);
        setGoToNextOnFull(input, nextOnFull);
        input.setShowSoftInputOnFocus(false);
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(v);
                }
                else {
                    hideKeyboard();
                }
            }
        });
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard(v);
            }
        });
    }
}

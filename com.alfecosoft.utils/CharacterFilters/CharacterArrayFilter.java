package com.alfecosoft.utils.CharacterFilters;

import android.text.InputFilter;
import android.text.Spanned;

public abstract class CharacterArrayFilter implements InputFilter
{
    private String characterList;
    private Boolean lowercase;
    public CharacterArrayFilter(String array, Boolean lowercase)
    {
        this.lowercase = lowercase;
        this.characterList = array;
    }
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String result = "";
        for (int i = start; i < end; i++) {
            char c = Character.toLowerCase(source.charAt(i));
            if (characterList.indexOf(c) < 0 ) {
                return "";
            }
        }
        return null;
    }
}


package br.com.neonpay.neonpayacademy.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneTextWatcher implements TextWatcher {
    private final EditText editText;
    private boolean isUpdating;

    public PhoneTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (isUpdating) return;

        isUpdating = true;

        String clean = s.toString().replaceAll("[^\\d]", "");
        StringBuilder formatted = new StringBuilder();

        int length = clean.length();
        int i = 0;

        if (length > 0) formatted.append("(");
        while (i < length && i < 11) {
            if (i == 2) formatted.append(") ");
            else if (i == 7 && length > 10) formatted.append("-");
            else if (i == 6 && length <= 10) formatted.append("-");
            formatted.append(clean.charAt(i));
            i++;
        }

        editText.setText(formatted.toString());
        editText.setSelection(formatted.length());

        isUpdating = false;
    }
}


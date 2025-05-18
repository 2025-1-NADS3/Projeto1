package br.com.bankpay.bankpayacademy.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CPFTextWatcher implements TextWatcher {
    private final EditText editText;
    private boolean isUpdating;

    public CPFTextWatcher(EditText editText) {
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

        int i = 0;
        while (i < clean.length() && i < 11) {
            if (i == 3 || i == 6) {
                formatted.append(".");
            } else if (i == 9) {
                formatted.append("-");
            }
            formatted.append(clean.charAt(i));
            i++;
        }

        editText.setText(formatted.toString());
        editText.setSelection(formatted.length());

        isUpdating = false;
    }
}


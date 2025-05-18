package br.com.bankpay.bankpayacademy.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DateTextWatcher implements TextWatcher {

    private final EditText editText;
    private boolean isUpdating;

    public DateTextWatcher(EditText editText) {
        this.editText = editText;
        this.isUpdating = false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isUpdating) return;

        isUpdating = true;

        String str = s.toString().replaceAll("[^\\d]", ""); // Remove pontos e traço

        if (str.length() > 8)
            str = str.substring(0, 8);

        StringBuilder data = new StringBuilder();

        int len = str.length();

        for (int i = 0; i < len; i++) {
            data.append(str.charAt(i));
            if ((i == 1 || i == 3) && i < len - 1) {
                data.append("/");
            }
        }

        editText.setText(data.toString());
        editText.setSelection(editText.getText().length());

        isUpdating = false;
    }
}

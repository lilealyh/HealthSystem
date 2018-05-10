package com.textwatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.lilea.healthSystem.R;

public class ClassOfTextWatcher implements TextWatcher {
    private EditText editText;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public ClassOfTextWatcher(View view, Context context) {
        if (mContext == null) {
            this.mContext = context;
        }
        if (sharedPreferences == null) {
            sharedPreferences = mContext.getSharedPreferences("myHealth", Context.MODE_PRIVATE);
        }
        if (view instanceof EditText) {
            this.editText = (EditText) view;
        } else
            throw new ClassCastException("view must be an instance of EditText");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (editText.getId()) {
            case R.id.breakfast_content:
                sharedPreferences.edit().putString("breakfast", s.toString()).apply();
                break;
            case R.id.lunch_content:
                sharedPreferences.edit().putString("lunch", s.toString()).apply();
                break;
            case R.id.dinner_content:
                sharedPreferences.edit().putString("dinner", s.toString()).apply();
                break;
            case R.id.dapi_add_times:
                if (!s.toString().equals("")) {
                    sharedPreferences.edit().putInt("dapi", Integer.valueOf(s.toString())).apply();
                }
                break;
            case R.id.dage_content_morning:
                if (!s.toString().equals("")) {
                    sharedPreferences.edit().putInt("dage_morning", Integer.valueOf(s.toString())).apply();
                }
                break;
            case R.id.dage_content_afternoon:
                if (!s.toString().equals("")) {
                    sharedPreferences.edit().putInt("dage_afternoon", Integer.valueOf(s.toString())).apply();
                }
                break;
            case R.id.dage_content_evening:
                if (!s.toString().equals("")) {
                    sharedPreferences.edit().putInt("dage_evening", Integer.valueOf(s.toString())).apply();
                }
                break;

            default:
                break;
        }
    }
}

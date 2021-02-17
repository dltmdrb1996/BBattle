package com.example.BBATTLE;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class ProgressDialog extends Dialog {
    public ProgressDialog(Context context){
        super(context);
        // 다이얼 로그 제목을 안보이게...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_progress);
    }

    public void setMessage(String 잠시만_기다려_주세요) {
    }
}

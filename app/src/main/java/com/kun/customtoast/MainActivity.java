package com.kun.customtoast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kun.toast.CustomToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    int i = 0;

    public void onClick(View view) {
        CustomToast.showToast(this, "haha" + i++);
    }
}

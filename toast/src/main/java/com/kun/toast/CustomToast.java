
/*
 *
 *  * Copyright (C) 2016 The Android Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

package com.kun.toast;

import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by jiangkun on 2017/8/1.
 */

public class CustomToast {

    private static final String TAG = CustomToast.class.getSimpleName();
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private Context mContext;
    private boolean isShow = false;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
    private final Runnable mHide = new Runnable() {
        @Override
        public void run() {
            handleHide();
        }
    };

    private View mView;
    private final long mDuration = 2000L;
    private static CustomToast mCustomToast;
    private final TextView mTv;


    private CustomToast(Context context) {
        if (!(context instanceof Application)) {
            Log.e(TAG, "context " + context + " memory leak, use ApplicationContext instead!");
            throw new RuntimeException("context " + context + " memory leak, use ApplicationContext instead!");
        }
        mContext = context;
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = -1;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("CustomToast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflate.inflate(R.layout.custom_toast_layout, null);
        mTv = (TextView) mView.findViewById(R.id.text);
    }

    private static CustomToast getCustomToast(Context context) {
        if (mCustomToast == null) {
            mCustomToast = new CustomToast(context);
        }
        return mCustomToast;
    }

    public static void showToast(Context context, CharSequence text) {
        CustomToast result = getCustomToast(context.getApplicationContext());
        result.setText(text);
        result.show();
    }

    private void setText(CharSequence text) {
        if (mTv != null) {
            mTv.setText(text);
        }
    }


    private void show() {
        handleHide();

        if (mContext == null) {
            mContext = mView.getContext();
        }
        mWM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        mWM.addView(mView, mParams);
        mHandler.postDelayed(mHide, mDuration);
        isShow = true;
    }

    public void handleHide() {
        if (mView != null && isShow) {
            mHandler.removeCallbacks(mHide);
            if (mView.getParent() != null) {
                mWM.removeViewImmediate(mView);
            }
            isShow = false;
        }
    }
}

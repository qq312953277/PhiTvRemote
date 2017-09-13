package com.phicomm.remotecontrol.modules.main.spinnerlist;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.phicomm.remotecontrol.R;

/**
 * Created by kang.sun on 2017/9/12.
 */

public class DisconnectSpinnerWindowView extends PopupWindow {
    private LayoutInflater mInflater;
    private ImageView mImageView;
    public DisconnectSpinnerWindowView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }
    private void init() {
        View view = mInflater.inflate(R.layout.disconnect_spinner_window, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mImageView = (ImageView) view.findViewById(R.id.imageview);

    }
}

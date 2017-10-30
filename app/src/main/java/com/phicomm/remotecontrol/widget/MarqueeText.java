package com.phicomm.remotecontrol.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by yong04.zhou on 2017/10/31.
 */

public class MarqueeText extends AppCompatTextView {
    public MarqueeText(Context context) {
        super(context);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
    返回textview是否处在选中的状态
    而只有选中的textview才能够实现跑马灯效果
    */
    @Override
    public boolean isFocused() {
        return true;
    }
}

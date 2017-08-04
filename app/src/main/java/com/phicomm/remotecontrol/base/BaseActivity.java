package com.phicomm.remotecontrol.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hao04.wu on 2017/8/1.
 */

public class BaseActivity extends Activity {


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectViews();
        BaseApplication.getApplication().add(this);
    }

    private void injectViews() {
        ButterKnife.bind(this);
    }


}

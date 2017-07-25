package com.phicomm.remotecontrol.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.util.LogUtil;
import com.phicomm.remotecontrol.fragments.childrenlock.ChildrenLockFragment;
import com.phicomm.remotecontrol.fragments.clean.CleanFragment;
import com.phicomm.remotecontrol.fragments.screenshot.ScreenshotFragment;
import com.phicomm.remotecontrol.fragments.controlpanel.ControlPanelFragment;
import com.phicomm.remotecontrol.fragments.dlna.DlnaFragment;

public class CoreActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 101;
    private Class mFragmentArray[] = {ScreenshotFragment.class, DlnaFragment.class, ControlPanelFragment.class,
            ChildrenLockFragment.class, CleanFragment.class};

    private int mImageViewArray[] = {R.drawable.tab_home_btn, R.drawable.tab_home_btn, R.drawable.tab_home_btn,
            R.drawable.tab_home_btn, R.drawable.tab_home_btn};

    private int mTextIdArray[] = {R.string.tab_screenshot,
            R.string.tab_dlna,
            R.string.tab_control,
            R.string.tab_childrenlock,
            R.string.tab_clean};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        int count = mFragmentArray.length;

        for (int i = 0; i < count; i++) {
            String spec = getResources().getString(mTextIdArray[i]);
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(spec).setIndicator(getTabItemView(i));

            fragmentTabHost.addTab(tabSpec, mFragmentArray[i], null);

            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }

        fragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("activity onresume");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private View getTabItemView(int index) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextIdArray[index]);

        return view;
    }
}

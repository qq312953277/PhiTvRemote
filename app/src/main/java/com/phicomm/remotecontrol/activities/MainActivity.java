package com.phicomm.remotecontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.fragments.spinnerlist.SpinnerListFragment;

import static com.phicomm.remotecontrol.util.ActivityUtils.addFragmentToActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpinnerListFragment spinnerListFragment = (SpinnerListFragment)
                getSupportFragmentManager().findFragmentById(R.id.spinnerlist);

        if (spinnerListFragment == null) {
            spinnerListFragment = SpinnerListFragment.newInstance();
            addFragmentToActivity(getSupportFragmentManager(),
                    spinnerListFragment, R.id.spinnerlist);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
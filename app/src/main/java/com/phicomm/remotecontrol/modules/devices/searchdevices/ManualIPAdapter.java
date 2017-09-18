package com.phicomm.remotecontrol.modules.devices.searchdevices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;

/**
 * Created by kang.sun on 2017/9/14.
 */
public class ManualIPAdapter extends ArrayAdapter<String> {
    private int resourceId;

    public ManualIPAdapter(Context context, int textViewResourceId,
                           String[] objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String str = getItem(position);
        View view = LayoutInflater
                .from(getContext()).inflate(R.layout.manual_input_ip_item, null);
        TextView textView = (TextView) view.findViewById(R.id.ip_button);
        textView.setText(str);
        return view;
    }
}


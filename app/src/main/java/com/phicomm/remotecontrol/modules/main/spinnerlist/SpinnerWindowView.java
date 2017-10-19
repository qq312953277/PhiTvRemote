package com.phicomm.remotecontrol.modules.main.spinnerlist;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;

import java.util.List;

public class SpinnerWindowView extends PopupWindow {
    private LayoutInflater mInflater;
    private ListView mListView;
    private SpinnerListAdapter mSpinnerListAdapter;
    public static final int LISTVIEWSIZE = 5;

    public SpinnerWindowView(Context context, AdapterView.OnItemClickListener clickListener) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init(clickListener);
    }

    private void init(AdapterView.OnItemClickListener clickListener) {
        View view = mInflater.inflate(R.layout.ppw_spinner_window, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);//点击空白处时，隐藏掉pop窗口
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mSpinnerListAdapter = new SpinnerListAdapter();
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setAdapter(mSpinnerListAdapter);
        mListView.setOnItemClickListener(clickListener);

    }

    public void notifyDataChange(List<RemoteBoxDevice> spinner_list) {
        mSpinnerListAdapter.notifyDataChange(spinner_list);
        setListView(mListView);
    }

    public void setListView(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int itemSize = (mSpinnerListAdapter.getCount() > LISTVIEWSIZE) ? LISTVIEWSIZE : mSpinnerListAdapter.getCount();
        for (int i = 0; i < itemSize; i++) {
            View listItem = mSpinnerListAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }
}
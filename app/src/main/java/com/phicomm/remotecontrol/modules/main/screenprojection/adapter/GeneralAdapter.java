package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kang.sun on 2017/8/22.
 */
public abstract class GeneralAdapter<E> extends BaseAdapter {
    private int maxCount = Integer.MAX_VALUE;
    private List<E> data;
    private int resource;
    private Context ctx;
    private final Object mLock = new Object();

    public GeneralAdapter(Context ctx, int resource, List<E> data) {
        this.ctx = ctx;
        this.resource = resource;
        setData(data);
    }

    public void setData(List<E> data) {
        synchronized (mLock) {
            if (data == null) {
                data = new ArrayList<E>();
            }
        }
        this.data = data;
        checkListSize();
    }

    public List<E> getData() {
        return data;
    }

    public void add(E object) {
        synchronized (mLock) {
            if (data != null) {
                data.add(object);
            }
            checkListSize();
        }
    }

    public void addAll(Collection<? extends E> collection) {
        synchronized (mLock) {
            if (data != null) {
                data.addAll(data);
                checkListSize();
            }
        }
    }

    public void insert(E object, int index) {
        synchronized (mLock) {
            if (data != null) {
                data.add(index, object);
            }
        }
        notifyDataSetChanged();
    }

    public void remove(E object) {
        synchronized (mLock) {
            if (data != null) {
                data.remove(object);
            }
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            if (data != null) {
                data.clear();
            }
        }
        notifyDataSetChanged();
    }

    public void sort(Comparator<? super E> comparator) {
        synchronized (mLock) {
            if (data != null) {
                Collections.sort(data, comparator);
            }
        }
        notifyDataSetChanged();
    }

    private void checkListSize() {
        int totalCount = data.size();
        if (totalCount > maxCount) {
            data = data.subList(totalCount - maxCount, totalCount);
        }
        this.notifyDataSetChanged();
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public Context getContext() {
        return ctx;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public E getItem(int position) {
        return data.get(position);
    }

    public int getPosition(E item) {
        return data.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(ctx, resource, convertView);
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, E item, int position);

    public static class ViewHolder {
        private View convertView;
        private SparseArray<View> views;

        private ViewHolder(Context ctx, int resource) {
            convertView = LayoutInflater.from(ctx).inflate(resource, null);
            convertView.setTag(this);
            views = new SparseArray<View>();
        }

        public static ViewHolder get(Context ctx, int resource, View convertView) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder(ctx, resource);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            return vh;
        }

        public View getConvertView() {
            return this.convertView;
        }

        public View getView(int widgetId) {
            View view = views.get(widgetId);
            if (view == null) {
                view = convertView.findViewById(widgetId);
                views.append(widgetId, view);
            }
            return view;
        }

        public void setText(int widgetId, String text) {
            TextView tv = (TextView) getView(widgetId);
            if (tv != null && !TextUtils.isEmpty(text)) {
                tv.setText(text);
            }
        }

        public void setText(int widgetId, int resid) {
            TextView tv = (TextView) getView(widgetId);
            if (tv != null) {
                tv.setText(resid);
            }
        }

        public void setBackgroundColor(int widgetId, int color) {
            View view = getView(widgetId);
            if (view != null) {
                view.setBackgroundColor(color);
            }
        }

        public void setBackgroundResource(int widgetId, int resid) {
            View view = getView(widgetId);
            if (view != null) {
                view.setBackgroundResource(resid);
            }
        }

        public void setImageDrawable(int widgetId, Drawable drawable) {
            ImageView iv = (ImageView) getView(widgetId);
            if (iv != null) {
                iv.setImageDrawable(drawable);
            }
        }

        public void setImageResource(int widgetId, int resid) {
            ImageView iv = (ImageView) getView(widgetId);
            if (iv != null) {
                iv.setImageResource(resid);
            }
        }

        public void setImageBitmap(int widgetId, Bitmap bm) {
            ImageView iv = (ImageView) getView(widgetId);
            if (iv != null) {
                iv.setImageBitmap(bm);
            }
        }
    }
}

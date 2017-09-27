package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PhotoUpImageItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

public class ImageActivity extends BaseActivity {

    @BindView(R.id.imageViewPage)
    ViewPager mViewPager;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    private ImagePagerAdapter mImagePagerAdapter;
    private List<PhotoUpImageItem> mImageList;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();

        mImageList = (ArrayList<PhotoUpImageItem>) intent.getSerializableExtra("imageList");
        mCurrentPosition = intent.getIntExtra("currentPosition", 0);

        initTitleView();

        mImagePagerAdapter = new ImagePagerAdapter(mImageList, this);
        mViewPager.setAdapter(mImagePagerAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvTitle.setText(mImageList.get(position).getmImageId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTitleView() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mTvTitle.setText(mImageList.get(mCurrentPosition).getmImageId());
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private List<PhotoUpImageItem> mBitmapList;
        private Context context;
        private ImageView image;


        public ImagePagerAdapter(List<PhotoUpImageItem> mBitmapList, Context context) {
            this.mBitmapList = mBitmapList;
            this.context = context;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mBitmapList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_image_item, container, false);
            image = (ImageView) view.findViewById(R.id.image1); // view不能少

            Glide.with(BaseApplication.getContext()).load("file://" + mBitmapList.get(position).getmImagePath()).dontAnimate()
                    .error(R.drawable.album_default_loading_pic).fitCenter().placeholder(R.drawable.album_default_loading_pic).priority(Priority.HIGH).into(image);
            container.addView(view);
            return view;
        }

    }
}

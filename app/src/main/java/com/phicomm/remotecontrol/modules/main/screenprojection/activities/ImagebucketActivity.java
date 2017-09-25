package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.AlbumItemAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.GeneralAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PhotoUpImageBucket;

import java.io.Serializable;

import butterknife.BindView;

import static com.phicomm.remotecontrol.constant.PhiConstants.TITLE_BAR_HEIGHT_DP;

public class ImagebucketActivity extends BaseActivity implements LocalMediaItemView {

    @BindView(R.id.album_item_grid)
    GridView mGridView;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    private PhotoUpImageBucket mPhotoUpImageBucket;
    private AlbumItemAdapter mAlbumItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_bucket);
        init();
        //initTitleView();
        setListener();
    }

    private void initTitleView() {
        setMarginForStatusBar(mRlTitle, TITLE_BAR_HEIGHT_DP);
        mTvTitle.setText(mPhotoUpImageBucket.getBucketName());
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        mPhotoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra("imagelist");
        mAlbumItemAdapter = new AlbumItemAdapter(mPhotoUpImageBucket.getImageList(), this);
        mGridView.setAdapter(mAlbumItemAdapter);
    }

    private void setListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                mAlbumItemAdapter.notifyDataSetChanged();

                Intent intent = new Intent(ImagebucketActivity.this, ImageActivity.class);
                intent.putExtra("currentPosition", position);
                intent.putExtra("imageList", (Serializable) mPhotoUpImageBucket.getImageList());
                startActivity(intent);

            }
        });
    }

    @Override
    public void showMessage(Object message) {

    }

    @Override
    public void onSuccess(Object message) {

    }

    @Override
    public void onFailure(Object message) {

    }

    @Override
    public void showItems(GeneralAdapter<ContentItem> mContentAdapter) {
    }
}

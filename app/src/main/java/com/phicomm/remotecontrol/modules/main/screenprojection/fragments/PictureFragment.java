package com.phicomm.remotecontrol.modules.main.screenprojection.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.ImagebucketActivity;
import com.phicomm.remotecontrol.modules.main.screenprojection.adapter.AlbumsAdapter;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PhotoUpImageBucket;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PhotoUpImageItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.utils.PhotoUpAlbumUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yong04.zhou on 2017/9/19.
 */

public class PictureFragment extends BaseFragment {

    @BindView(R.id.album_gridv)
    GridView mGridView;

    private AlbumsAdapter mAdapter;
    private PhotoUpAlbumUtil mPhotoUpAlbumUtil;
    private List<PhotoUpImageBucket> mList;
    private List<PhotoUpImageItem> mAllPhoto;
    private PhotoUpImageBucket mAllOfBucket;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        loadData();
        onItemClick();
    }

    private void init() {
        mGridView = (GridView) getActivity().findViewById(R.id.album_gridv);
        mAdapter = new AlbumsAdapter(getContext());
        mGridView.setAdapter(mAdapter);
    }

    //相册列表中获取所有照片
    private void getAllPhoto() {
        mAllPhoto = new ArrayList();
        for (PhotoUpImageBucket photoUpImageBucket : mList) {
            for (PhotoUpImageItem photoUpImageItem : photoUpImageBucket.getImageList())
                mAllPhoto.add(photoUpImageItem);
        }
        mAllOfBucket = new PhotoUpImageBucket();
        mAllOfBucket.setBucketName("All");
        mAllOfBucket.setCount(mAllPhoto.size());
        mAllOfBucket.setImageList(mAllPhoto);
    }


    private void loadData() {
        mPhotoUpAlbumUtil = PhotoUpAlbumUtil.getHelper();
        mPhotoUpAlbumUtil.init(getContext());
        mPhotoUpAlbumUtil.setGetAlbumList(new PhotoUpAlbumUtil.GetAlbumList() {
            @Override
            public void getAlbumList(List<PhotoUpImageBucket> list) {
                PictureFragment.this.mList = list;//必须先初始化再调getAllPhoto()方法，否则空指针错误
                getAllPhoto();
                list.add(0, mAllOfBucket); //在第一个位置添加所有照片目录
                mAdapter.setArrayList(list);
                mAdapter.notifyDataSetChanged();
                PictureFragment.this.mList = list; //刷新
            }
        });
        mPhotoUpAlbumUtil.execute(false);
    }

    private void onItemClick() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), ImagebucketActivity.class);
                intent.putExtra("imagelist", mList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPhotoUpAlbumUtil.destoryList();//清空相册视频内存
    }
}

package com.phicomm.remotecontrol.modules.personal.personaldetail;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.personal.account.http.CustomSubscriber;
import com.phicomm.remotecontrol.modules.personal.account.http.HttpDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AccountDetailBean;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public class PersonalPresenter implements PersonalContract.Presenter {

    PersonalContract.View mView;

    public PersonalPresenter(PersonalContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getPersonInfoFromServer() {
        String access_token = LocalDataRepository.getInstance(BaseApplication.getContext()).getAccessToken();
        HttpDataRepository.getInstance().accountDetail(new CustomSubscriber<AccountDetailBean>() {
            @Override
            public void onCustomNext(AccountDetailBean accountDetailBean) {
                mView.analysisResponseBean(accountDetailBean);
            }
        }, access_token);
    }

    @Override
    public void setImageAvatarByUrl(String imgPath, ImageView imageView) {
        Glide.with(BaseApplication.getContext()).load(imgPath).dontAnimate()
                .error(R.drawable.default_avatar).placeholder(R.drawable.icon_photo_loading).priority(Priority.HIGH).into(imageView);
    }
}

package com.phicomm.remotecontrol.modules.personal.personaldetail;

import android.widget.ImageView;

import com.phicomm.remotecontrol.modules.personal.account.resultbean.BaseResponseBean;

/**
 * Created by yong04.zhou on 2017/9/14.
 */


public class PersonalContract {
    public interface View {
        //解析http response
        void analysisResponseBean(BaseResponseBean t);

        //刷新UI
        void refreshDataInUI();
    }

    public interface Presenter {
        //显示头像
        void setImageAvatarByUrl(String imgPath, ImageView imageView);

        //从云端获取accountinfo
        void getPersonInfoFromServer();
    }
}

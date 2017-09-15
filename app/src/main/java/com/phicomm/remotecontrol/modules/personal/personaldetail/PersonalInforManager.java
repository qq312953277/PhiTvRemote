package com.phicomm.remotecontrol.modules.personal.personaldetail;

import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.personal.account.local.LocalDataRepository;
import com.phicomm.remotecontrol.modules.personal.account.resultbean.AccountDetailBean;

import java.io.Serializable;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public class PersonalInforManager implements Serializable {
    private AccountDetailBean bean;

    private static PersonalInforManager personalInforManager;

    private PersonalInforManager() {

    }

    public static PersonalInforManager getInstance() {
        if (personalInforManager == null) {
            personalInforManager = new PersonalInforManager();
        }

        return personalInforManager;
    }

    public synchronized AccountDetailBean getAccountDetailBean() {
        bean = LocalDataRepository.getInstance(BaseApplication.getContext()).getAccountDetailInfo();//将账户信息保存本地，
        return bean;
    }

    public synchronized void setAccountAndSave(AccountDetailBean bean) {
        this.bean = bean;
        LocalDataRepository.getInstance(BaseApplication.getContext()).setAccountDetailInfo(this.bean);
    }
}
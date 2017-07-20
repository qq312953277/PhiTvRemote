package com.phicomm.remotecontrol;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chunya02.li on 2017/6/22.
 */

public final class RemoteBoxDevice implements Parcelable {
    private final String mName;
    private final String mAddress;
    private final int mPort;
    private final String mBssid;

    public RemoteBoxDevice(String name, String address, int port, String bssid) {
        if (address == null) {
            throw new NullPointerException("Address is null");
        }
        this.mName = name;
        this.mAddress = address;
        this.mBssid = bssid;
        this.mPort = port;
    }

    /**
     * @return mName of the controlled device.
     */
    public String getName() {
        return mName;
    }

    /**
     * @return mAddress of the controlled device.
     */
    public String getAddress() {
        return mAddress;
    }

    public int getPort() {
        return mPort;
    }

    /**
     * @return port of the controlled device.
     */
    public String getBssid() {
        return mBssid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RemoteBoxDevice)) {
            return false;
        }
        RemoteBoxDevice that = (RemoteBoxDevice) obj;
        return equal(this.mName, that.mName)
                && equal(this.mAddress, that.mAddress)
                && (this.mBssid == that.mBssid);
    }

    @Override
    public int hashCode() {
        int code = 7;
        code = code * 31 + (mName != null ? mName.hashCode() : 0);
        code = code * 31 + (mAddress != null ? mAddress.hashCode() : 0);
        code = code * 31 + (mBssid != null ? mBssid.hashCode() : 0);
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s ：%s：%s", mName, mAddress, mBssid);
    }

    private static <T> boolean equal(T obj1, T obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mName);
        parcel.writeSerializable(mAddress);
        parcel.writeInt(mPort);
        parcel.writeString(mBssid);
    }

    public static final Parcelable.Creator<RemoteBoxDevice> CREATOR =
            new Parcelable.Creator<RemoteBoxDevice>() {

                public RemoteBoxDevice createFromParcel(Parcel parcel) {
                    return new RemoteBoxDevice(parcel);
                }

                public RemoteBoxDevice[] newArray(int size) {
                    return new RemoteBoxDevice[size];
                }
            };

    private RemoteBoxDevice(Parcel parcel) {
        this(parcel.readString(), (String) parcel.readSerializable(), parcel.readInt(),
                parcel.readString());
    }
}
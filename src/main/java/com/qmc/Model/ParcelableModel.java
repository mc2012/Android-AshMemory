package com.qmc.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/2.
 */

public class ParcelableModel implements Parcelable {
    public int id;
    public String name;
    public String uid;
    public String des1;
    public String des2;
    public String des3;
    public String des4;
    public String des5;
    public String des6;
    @Override
    public String toString() {
        return "ParcelableModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(uid);
        dest.writeString(des1);
        dest.writeString(des2);
        dest.writeString(des3);
        dest.writeString(des4);
        dest.writeString(des5);
        dest.writeString(des6);

    }

    public static final Parcelable.Creator<ParcelableModel> CREATOR = new Parcelable.Creator<ParcelableModel>() {
        @Override
        public ParcelableModel createFromParcel(Parcel source) {
            ParcelableModel model = new ParcelableModel();
            model.id = source.readInt();
            model.name = source.readString();
            model.uid = source.readString();
            model.des1 = source.readString();
            model.des2 = source.readString();
            model.des3 = source.readString();
            model.des4 = source.readString();
            model.des5 = source.readString();
            model.des6 = source.readString();
            return model;
        }

        @Override
        public ParcelableModel[] newArray(int size) {
            return new ParcelableModel[size];
        }
    };
}

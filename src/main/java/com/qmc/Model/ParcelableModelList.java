package com.qmc.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/9/2.
 */

public class ParcelableModelList implements Parcelable {
    private List<ParcelableModel> mList = new ArrayList<>();

    @Override
    public String toString() {
        return "ParcelableModelList{" +
                "mList=" + mList +
                '}';
    }

    public ParcelableModelList(Parcel source) {
        source.readTypedList(mList, ParcelableModel.CREATOR);
    }

    public ParcelableModelList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mList);
    }

    public static final Parcelable.Creator<ParcelableModelList> CREATOR = new Parcelable.Creator<ParcelableModelList>() {
        @Override
        public ParcelableModelList createFromParcel(Parcel source) {
            return new ParcelableModelList(source);
        }

        @Override
        public ParcelableModelList[] newArray(int size) {
            return new ParcelableModelList[size];
        }
    };

    public static ParcelableModelList newInstance(int size) {
        ParcelableModelList modelList = new ParcelableModelList();
        ArrayList<ParcelableModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ParcelableModel parcelableModel = new ParcelableModel();
            parcelableModel.id = i;
            parcelableModel.name = "parcelModel" + i;
            parcelableModel.des1 = "parcelModel" + i;
            parcelableModel.des2 = "parcelModel" + i;
            parcelableModel.des3 = "parcelModel" + i;
            parcelableModel.des4 = "parcelModel" + i;
            parcelableModel.des5 = "parcelModel" + i;
            parcelableModel.des6 = "parcelModel" + i;
            parcelableModel.uid = UUID.randomUUID().toString();
            list.add(parcelableModel);
        }
        modelList.mList = list;
        return modelList;
    }
}

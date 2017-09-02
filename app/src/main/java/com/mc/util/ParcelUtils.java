package com.mc.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/2.
 */

public class ParcelUtils {
    public static byte[] parcelableToBytes(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unMarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return parcel;
    }

    public static <T> T bytesToParcelable(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unMarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }
}

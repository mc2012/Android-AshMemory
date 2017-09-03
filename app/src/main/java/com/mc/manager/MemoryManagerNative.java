package com.mc.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.RemoteException;

import com.mc.util.Singleton;

/**
 * Created by Administrator on 2017/8/8.
 */

public abstract class MemoryManagerNative extends Binder implements IMemoryManager {
    private static IMemoryManager memoryManager;

    private static IMemoryManager asInterface(IBinder iBinder) {
        IMemoryManager mm;
        mm = (IMemoryManager) iBinder.queryLocalInterface(descriptor);
        if (mm != null) {
            return mm;
        }
        return new MemoryManagerProxy(iBinder);
    }

    public MemoryManagerNative() {
        attachInterface(this, descriptor);
    }

    public static IMemoryManager getMemoryFileManager() {
        if (memoryManager == null)
            throw new IllegalStateException("binder not set to native manager");
        return memoryManager;
    }

    public static void connectBinder(IBinder binder) {
        memoryManager = asInterface(binder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    private static Singleton<IMemoryManager, IBinder> mDefault = new Singleton<IMemoryManager, IBinder>() {
        @Override
        public IMemoryManager create(IBinder iBinder) {
            return asInterface(iBinder);
        }
    };

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(descriptor);
                return true;
            }
            case SET_FILE_TRANSACTION:
                data.enforceInterface(IMemoryManager.descriptor);
                ParcelFileDescriptor parcel = data.readParcelable(getClass().getClassLoader());
                String name = data.readString();
                int len = data.readInt();
                setFile(parcel, name, len);
                reply.writeNoException();
                return true;
            case START_REMOTE_ACTIVITY_TRANSACTION:
                data.enforceInterface(IMemoryManager.descriptor);
                startRemoteActivity();
                reply.writeNoException();
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class MemoryManagerProxy implements IMemoryManager {
        IBinder mRemote;

        MemoryManagerProxy(IBinder binder) {
            this.mRemote = binder;
        }


        @Override
        public void setFile(ParcelFileDescriptor value, String fileName, int len) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            data.writeInterfaceToken(descriptor);
            data.writeParcelable(value, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            data.writeString(fileName);
            data.writeInt(len);
            mRemote.transact(SET_FILE_TRANSACTION, data, replay, 0);
            replay.readException();
            data.recycle();
            replay.recycle();
        }

        @Override
        public void startRemoteActivity() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            data.writeInterfaceToken(descriptor);
            mRemote.transact(START_REMOTE_ACTIVITY_TRANSACTION, data, replay, 0);
            replay.readException();
            data.recycle();
            replay.recycle();
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }
}

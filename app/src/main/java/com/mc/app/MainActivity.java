package com.mc.app;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.Model.ParcelableModelList;
import com.mc.base.BaseActivity;
import com.mc.manager.MemoryManagerNative;
import com.mc.memory.R;
import com.mc.server.RemoteService;
import com.mc.util.ParcelUtils;

import java.io.FileDescriptor;
import java.lang.reflect.Method;

import com.mc.manager.IMemoryManager;

public class MainActivity extends BaseActivity {
    IMemoryManager mService;
    private TextView mClickBtn;
    View mPbLayout;
    View mConnectView;
    View mStartView;
    private NumberPicker mNumberPicker;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MemoryManagerNative.connectBinder(service);
            mPbLayout.setVisibility(View.INVISIBLE);
            showShortToast("connect to server successfully");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void write() {
        AsyncTaskCompat.executeParallel(new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                if (params == null || (int) params[0] <= 0) {
                    return new WriteResult(-1, "请选择写数目");
                }
                int num = (int) params[0];
                ParcelableModelList modelList = ParcelableModelList.newInstance(num);
                long now = System.currentTimeMillis();
                MemoryFile mf;
                final byte[] bytes = ParcelUtils.parcelableToBytes(modelList);
                Log.d("qmc2", "" + (bytes.length / (1024*1024)));
                WriteResult w = null;
                try {
                    mf = new MemoryFile("test", bytes.length);
                    mf.writeBytes(bytes, 0, 0, bytes.length);
                    publishProgress((System.currentTimeMillis() - now));
                    Method method = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
                    FileDescriptor fd = (FileDescriptor) method.invoke(mf);
                    final ParcelFileDescriptor pfd = ParcelFileDescriptor.dup(fd);
                    MemoryManagerNative.getMemoryFileManager().setFile(pfd, "test", bytes.length);
                    w = new WriteResult(0, "write successfully");
                    mf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    w = new WriteResult(-1, e + "");
                }
                return w;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (o != null) {
                    if (o instanceof WriteResult) {
                        WriteResult w = (WriteResult) o;
                        switch (w.code) {
                            case 0:
                                showShortToast("write successfully");
                                mPbLayout.setVisibility(View.INVISIBLE);
                                break;
                            case -1:
                                showShortToast(w.message);
                                break;
                        }
                    } else {
                        showShortToast(" write error");
                    }
                } else {
                    showShortToast(" write error");
                }
            }

            @Override
            protected void onProgressUpdate(Object... values) {
                super.onProgressUpdate(values);
                showShortToast(" write 耗时: " + values[0]);
            }
        }, mNumberPicker.getValue());
    }

    @Override
    protected void initUi(@Nullable Bundle savedInstanceState) {
        mClickBtn = findView(this, R.id.write);
        mNumberPicker = findView(this, R.id.num);
        mPbLayout = findView(this, R.id.pb_layout);
        mNumberPicker.setMaxValue(2000000);
        mNumberPicker.setMinValue(10);
        mNumberPicker.setValue(2000);
        mClickBtn.setOnClickListener(mClickListener);

        mConnectView = findView(this, R.id.connect);
        mStartView = findView(this, R.id.start_remote_activity);

        mConnectView.setOnClickListener(mClickListener);
        mStartView.setOnClickListener(mClickListener);


    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.write:
                    write();
                    mPbLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.connect:
                    Intent intent = new Intent(MainActivity.this, RemoteService.class);
                    bindService(intent, connection, Service.BIND_AUTO_CREATE);
                    mPbLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.start_remote_activity:
                    if (MemoryManagerNative.getMemoryFileManager() == null) {
                        showShortToast("must connect remote service first");
                        return;
                    }
                    try {
                        MemoryManagerNative.getMemoryFileManager().startRemoteActivity();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private static class WriteResult {
        int code;
        String message;

        public WriteResult(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}

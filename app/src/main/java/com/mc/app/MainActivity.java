package com.mc.app;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.Model.ParcelableModelList;
import com.mc.manager.MemoryManagerNative;
import com.mc.memory.R;
import com.mc.server.RemoteService;
import com.mc.util.ParcelUtils;

import java.io.FileDescriptor;
import java.lang.reflect.Method;

import com.mc.manager.IMemoryManager;

public class MainActivity extends AppCompatActivity {
    IMemoryManager mService;
    private TextView mClickBtn;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MemoryManagerNative.connectBinder(service);
            findViewById(R.id.id).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mClickBtn.performClick();
                }
            }, 1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    MemoryFile mf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClickBtn = findViewById(this, R.id.id);
        Intent intent = new Intent(this, RemoteService.class);
        mClickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_social);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
                    byte[] bitmaps = bs.toByteArray ();*/
                    ParcelableModelList modelList = ParcelableModelList.newInstance(20000);
                    long now = System.currentTimeMillis();
                    final byte[] bytes = ParcelUtils.parcelableToBytes(modelList);
                    mf = new MemoryFile("test", bytes.length);
                    mf.writeBytes(bytes, 0, 0, bytes.length);
                    Log.d("qmc2", " write 耗时: " + (System.currentTimeMillis() - now));
                    Toast.makeText(MainActivity.this," write 耗时: " + (System.currentTimeMillis() - now),Toast.LENGTH_SHORT).show();
                    Method method = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
                    FileDescriptor fd = (FileDescriptor) method.invoke(mf);
                    final ParcelFileDescriptor pfd = ParcelFileDescriptor.dup(fd);
                    findViewById(R.id.id).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MemoryManagerNative.getMemoryFileManager().setFile(pfd, "test", bytes.length);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 2000);




                    Log.d("qmc", " client " );
                } catch (Exception e) {
                    Log.d("qmc", " " + e);
                    e.printStackTrace();
                }
            }
        });
        bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

    protected static <V, T extends Activity> V findViewById(T t, int id) {
        return (V) t.findViewById(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}

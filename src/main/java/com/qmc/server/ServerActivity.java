package com.qmc.server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StateSet;
import android.view.View;
import android.widget.Toast;

import com.qmc.Model.ParcelableModelList;
import com.qmc.memoryfiletest.MainActivity;
import com.qmc.memoryfiletest.R;
import com.qmc.util.ParcelUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemoryManagerService.MemoryFileRecord rt = MemoryManagerService.getInstance().getParcelFileDescriptor("test");
                Log.d("qmc2", "rt " + rt);
                if (rt != null) {
                    try {
                        long now = System.currentTimeMillis();
                        FileInputStream inputStream = new FileInputStream(rt.descriptor.getFileDescriptor());
                        byte[] bytes = new byte[rt.len];
                        int read = inputStream.read(bytes, 0, rt.len);
                        ParcelableModelList parcelableModelList = ParcelUtils.bytesToParcelable(bytes, ParcelableModelList.CREATOR);
                        Toast.makeText(ServerActivity.this," read 耗时: " + (System.currentTimeMillis() - now),Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("qmc2", " " + e);
                    }

                }
            }
        });

    }
}

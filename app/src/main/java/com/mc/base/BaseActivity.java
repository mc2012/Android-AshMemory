package com.mc.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/9/3.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initUi(savedInstanceState);
    }
    protected abstract void initUi(@Nullable Bundle savedInstanceState);
    public int getLayoutId() {
        return 0;
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected static <V,C extends Activity> V findView(C c, int id) {
        return (V) c.findViewById(id);
    }
}

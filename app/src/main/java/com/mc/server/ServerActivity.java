package com.mc.server;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.Model.ParcelableModel;
import com.mc.Model.ParcelableModelList;
import com.mc.base.BaseActivity;
import com.mc.memory.R;
import com.mc.util.ParcelUtils;

import java.io.FileInputStream;
import java.util.List;

public class ServerActivity extends BaseActivity {
    View mReadView;
    ListView mListView;
    View mPbView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_server;
    }

    @Override
    protected void initUi(@Nullable Bundle savedInstanceState) {
        mReadView = findView(this, R.id.read);
        mListView = findView(this, R.id.list);
        mPbView = findView(this, R.id.pb_layout);
        mReadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPbView.setVisibility(View.VISIBLE);
                read();
            }
        });
    }

    private void read() {
        AsyncTaskCompat.executeParallel(new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                MemoryManagerService.MemoryFileRecord rt = MemoryManagerService.getInstance().getParcelFileDescriptor("test");
                ParcelableModelList parcelableModelList = null;
                if (rt != null) {
                    try {
                        long now = System.currentTimeMillis();
                        FileInputStream inputStream = new FileInputStream(rt.descriptor.getFileDescriptor());
                        byte[] bytes = new byte[rt.len];
                        int read = inputStream.read(bytes, 0, rt.len);
                        parcelableModelList = ParcelUtils.bytesToParcelable(bytes, ParcelableModelList.CREATOR);
                        publishProgress((System.currentTimeMillis() - now));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return parcelableModelList;
            }

            @Override
            protected void onProgressUpdate(Object... values) {
                super.onProgressUpdate(values);
                showShortToast("读 耗时 " + values[0]);
                mPbView.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ParcelableModelList ret = (ParcelableModelList) o;
                Adapter a = new Adapter(ServerActivity.this, ret != null ? ret.mList : null);
                mListView.setAdapter(a);
            }
        });
    }

    static class Adapter extends BaseAdapter {
        private List<ParcelableModel> mList;
        private Context mC;
        private LayoutInflater l;

        public Adapter(Context c, List<ParcelableModel> list) {
            this.mC = c;
            this.mList = list;
            l = LayoutInflater.from(mC);
        }

        @Override
        public int getCount() {
            return mList != null ? mList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mList != null ? mList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder m;
            if (convertView == null) {
                convertView = l.inflate(R.layout.layout_item, null);
                m = new ViewHolder();
                m.mText = (TextView) convertView;
                convertView.setTag(m);
            } else {
                m = (ViewHolder) convertView.getTag();
            }
            ParcelableModel md = mList.get(position);
            m.mText.setText("  id = " + md.id + "\n  name = " + md.name + "\n   uid=" + md.uid);
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView mText;
    }
}

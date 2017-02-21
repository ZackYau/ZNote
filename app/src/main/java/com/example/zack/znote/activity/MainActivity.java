package com.example.zack.znote.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zack.znote.R;
import com.example.zack.znote.adapter.RecyclerViewAdapter;
import com.example.zack.znote.model.NotesCard;
import com.example.zack.znote.util.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RecyclerViewAdapter.OnItemClickListener {

    private RecyclerViewAdapter recyclerViewAdapter;
    private List<NotesCard> mDataSet;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    //图片缓存
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private boolean mDiskCacheStarting = true;
    private final Object mDiskCacheLock = new Object();
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCache();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        initData();
        recyclerViewAdapter = new RecyclerViewAdapter(mDataSet,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, EditNotesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(long noteId, int position, View view) {

    }

    @Override
    public void onItemLongClick(long noteId, int position) {

    }

    /**
     * 初始化内存缓存和磁盘缓存
     */
    private void initCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        RetainFragment retainFragment =
                RetainFragment.findOrCreateRetainFragment(getSupportFragmentManager());
        mMemoryCache = retainFragment.mRetainedCache;
        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        // 获取图片缓存路径
        File cacheDir = getDiskCacheDir(this, DISK_CACHE_SUBDIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        // 创建DiskLruCache实例，初始化缓存数据
        new InitDiskCacheTask().execute(cacheDir,this);
    }
    /**
     * 初始化数据
     */
    private void initData() {
        mDataSet = new ArrayList<>();
        List<String> list,list1;
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list.add("JPEG_20160904_123518.jpg");
        list1.add("");
        NotesCard notesCard = new NotesCard(1,"Title","test",0xFFFF6D3F,list,list1);
        mDataSet.add(notesCard);
    }

    /**
     * 将一张图片存储到LruCache中。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }

        synchronized (mDiskCacheLock) {
            try {
                if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
                    String keyHash = hashKeyForDisk(key);
                    DiskLruCache.Editor editor = mDiskLruCache.edit(keyHash);
                    OutputStream os = editor.newOutputStream(0);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    editor.commit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取当前应用程序的版本号。
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }
            if (mDiskLruCache != null) {
                try {
                    String keyHash = hashKeyForDisk(key);
                    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(keyHash);
                    if (snapShot != null) {
                        String path = snapShot.getString(0);
                        return BitmapFactory.decodeFile(path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    class InitDiskCacheTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            synchronized (mDiskCacheLock) {
                try {
                    File cacheDir = (File)params[0];
                    Context context = (Context)params[1];
                    mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, DISK_CACHE_SIZE);
                    mDiskCacheStarting = false;
                    mDiskCacheLock.notifyAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static class RetainFragment extends Fragment {
        private static final String TAG = "RetainFragment";
        public LruCache<String, Bitmap> mRetainedCache;

        public RetainFragment() {}

        public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
            RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(TAG);
            if (fragment == null) {
                fragment = new RetainFragment();
                fm.beginTransaction().add(fragment, TAG).commit();
            }
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }
    }
}

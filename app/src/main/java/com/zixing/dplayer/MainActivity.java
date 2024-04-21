package com.zixing.dplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jieyuebook.online.reader.VideoMediaController;
import com.zving.ipmph.app.widget.DolitVideoView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import cn.dolit.media.player.widget.MediaController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    private AlertDialog dialog;
    boolean havePermission=false;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权

        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")//设置标题
                        .setMessage("请开启文件访问权限，否则无法正常使用本应用！")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(intent);
                            }
                        }).create();
                dialog.show();
            } else {
                havePermission = true;
                Log.i("swyLog", "Android 11以上，当前已有权限");
            }
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    dialog = new AlertDialog.Builder(this)
                            .setTitle("提示")//设置标题
                            .setMessage("请开启文件访问权限，否则无法正常使用本应用！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                                }
                            }).create();
                    dialog.show();
                } else {
                    havePermission = true;
                    Log.i("swyLog", "Android 6.0以上，11以下，当前已有权限");
                }
            } else {
                havePermission = true;
                Log.i("swyLog", "Android 6.0以下，已获取权限");
            }
        }
    }

    ArrayList<String> flvFileList = new ArrayList<>();

    private void refreshList()
    {
        flvFileList.clear();
        File sdCard = Environment.getExternalStorageDirectory();
        File[] files = sdCard.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".flv");
            }
        });
        if (files != null) {
            for (File file : files) {
                flvFileList.add(file.getAbsolutePath());
            }
        }
        File videoDirectory = new File(sdCard.getAbsolutePath() + "/Download/video");

        File[] filesVideo = videoDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".flv");
            }
        });
        if (filesVideo != null) {
            for (File file : filesVideo) {
                flvFileList.add(file.getAbsolutePath());
            }
        }
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flvFileList);
        listView.setAdapter(adapter);

    }
    ProgressBar progressBar;
    TextView textView;
    private void updateProgress(long currentBytes, long totalBytes) {
        int percentage = (int) (((float) currentBytes / totalBytes) * 100);
        String progressText = "Downloading";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 更新进度显示，例如更新进度条或文本视图
                progressBar.setProgress(percentage);

                textView.setText(progressText);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        textView= findViewById(R.id.progressTextView);

        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        checkPermission();
        // 设置 ListView 的适配器和长按监听器
        ListView listView = findViewById(R.id.listView);
        listView.setOnItemLongClickListener(this);

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里执行刷新列表的逻辑
                refreshList();
            }
        });


        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 创建一个AlertDialog.Builder对象来构建对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("输入要下载的URL");

// 创建一个EditText来接收用户输入的URL
                final EditText urlEditText = new EditText(MainActivity.this);
                urlEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
                builder.setView(urlEditText);

// 添加“确定”按钮
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取用户输入的URL
                        String inputUrl = urlEditText.getText().toString();
                        // 在这里执行播放操作或进行其他逻辑处理
//                                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
//                        intent.putExtra("filePath", inputUrl);
//                        startActivity(intent);
                        Uri uri = Uri.parse(inputUrl);
                        String fileName="down.flv";
                        URL fileUrl = null;
                        try {
                            fileUrl = new URL(inputUrl);
                            fileName = new File(fileUrl.getPath()).getName(); ;
                        } catch (MalformedURLException e) {
                           e.printStackTrace();
                        }
                        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/video/";
                        File d = new File(directory);
                        if(!d.exists())
                        {
                            d.mkdirs();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里更新UI视图
                                progressBar.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                            }
                        });


                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder()
                                .url(inputUrl)
                                .build();

                        String finalFileName = fileName;
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                // 处理下载失败的情况
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (!response.isSuccessful()) {
                                    // 处理下载失败的情况
                                    return;
                                }

                                // 创建下载文件
                                File file = new File(directory, finalFileName);

                                BufferedSink sink = null;
                                BufferedSource source = null;

                                try {
                                    sink = Okio.buffer(Okio.sink(file));
                                    source = Okio.buffer(response.body().source());

                                    // 通过逐个读取和写入数据来执行下载
                                    while (source.read(sink.buffer(), 8192) != -1) {
                                        // 更新进度显示
                                        updateProgress(file.length(), response.body().contentLength());
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 下载完成，进行相应的处理
                                            progressBar.setVisibility(View.GONE);
                                            textView.setVisibility(View.GONE);
                                            refreshList();
                                        }
                                    });

                                } catch (IOException e) {
                                    // 处理下载失败的情况
                                } finally {
                                    if (sink != null) {
                                        sink.close();
                                    }
                                    if (source != null) {
                                        source.close();
                                    }
                                }
                            }
                        });
                    }
                });

// 添加“取消”按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

// 创建对话框并显示
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filePath = flvFileList.get(position);
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("filePath", filePath);
                startActivity(intent);
            }
        });
        refreshList();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        // 执行重命名操作
        renameFile(position);
        return true;
    }

    private void renameFile(int position) {
        // 获取选中的文件
        String selectedFile = flvFileList.get(position);



        // 创建一个AlertDialog.Builder对象来构建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("输入要重命名的文件名");

// 创建一个EditText来接收用户输入的URL
        final EditText urlEditText = new EditText(MainActivity.this);
        urlEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(urlEditText);

// 添加“确定”按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取用户输入的URL
                String inputName = urlEditText.getText().toString();

                File ff= new File(selectedFile);
                File file2 = new File(ff.getParent()+"/"+inputName+".flv");
                ff.renameTo(file2);

                refreshList();
            }
        });

// 添加“取消”按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

// 创建对话框并显示
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
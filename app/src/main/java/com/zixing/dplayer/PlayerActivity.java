package com.zixing.dplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.jieyuebook.online.reader.VideoMediaController;
import com.zving.ipmph.app.widget.DolitVideoView;

import cn.dolit.media.player.widget.DolitBaseMediaPlayer;
import cn.dolit.media.player.widget.MediaController;

public class PlayerActivity extends AppCompatActivity {
    private DolitVideoView videoView;
    private SeekBar seekBar;
    private Button pauseButton;
    private Button fullscreenButton;

    /**
     * 操作的是SurfaceHolder，所以定义全局变量
     */
    private SurfaceHolder surfaceHolder;

    static String filePath="";
    boolean isDrag = false;
    LinearLayout controlLayout;

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();

        Log.d(TAG, "----------- onResume------------");
    }
    RelativeLayout relativeLayout;
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();

        Log.d(TAG, "----------- onPause------------");
        //videoView.pause();
    }

    String TAG="zixingtag";
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i(TAG, "当前是横屏");
            controlLayout.setVisibility(View.GONE);
        } else {
            Log.i(TAG, "当前是竖屏");
            controlLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 定义SurfaceView监听回调
     */
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        /**
         * surfaceHolder被创建了
         * @param holder
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "----------- surfaceHolder被创建了"+videoView.getDuration()+"------------");

            Log.d(TAG, "----------- currentProgress "+currentProgress+"------------");
            videoView.seekTo(currentProgress);
        }

        /**
         * surfaceHolder发生改变了
         * @param holder
         * @param format
         * @param width
         * @param height
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "----------- surfaceHolder发生改变了"+videoView.getDuration()+"------------");

        }

        /**
         * surfaceHolder被销毁了
         * @param holder
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            Log.d(TAG, "----------- surfaceHolder被销毁了------------");


            Log.d(TAG, "----------- currentProgress "+currentProgress+"------------");
            videoView.pause();
        }
    };
    TextView txtnowposition;
    TextView txtduring;


    /**
     * 返回日时分秒
     * @param second
     * @return
     */
    private String secondToTime(long second) {

        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数

        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数

        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        String sec = second+"";
        if(sec.length()==1)
        {
            sec="0"+sec;
        }

        if (0 < days){
            return days + ":"+hours+":"+minutes+":"+sec+"";
        }else if(0<hours) {
            return hours+":"+minutes+":"+sec+"";
        }else{
            return minutes+":"+sec+"";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self=this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        txtnowposition=findViewById(R.id.txtnowposition);
        txtduring=findViewById(R.id.txtduring);

        filePath = getIntent().getStringExtra("filePath");
        seekBar = findViewById(R.id.seekBar);
        pauseButton = findViewById(R.id.pauseButton);
        fullscreenButton = findViewById(R.id.fullscreenButton);
        controlLayout = findViewById(R.id.controlLayout);
        relativeLayout = findViewById(R.id.relativeLayout);

        videoView = (DolitVideoView) findViewById(R.id.sv);

        // 不能直接操作SurfaceView，需要通过SurfaceView拿到SurfaceHolder
        surfaceHolder = videoView.getHolder();

        // 使用SurfaceHolder设置屏幕高亮，注意：所有的View都可以设置 设置屏幕高亮
        surfaceHolder.setKeepScreenOn(true);

        // 使用SurfaceHolder设置把画面或缓存 直接显示出来
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        /**
         * 由于要观察SurfaceView生命周期，所以需要设置监听
         * 此监听不一样，是addCallback
         *
         */
        surfaceHolder.addCallback(callback);

        videoView.setAutoPlayAfterSurfaceCreated(true);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(controlLayout.getVisibility()==View.VISIBLE)
                {
                    controlLayout.setVisibility(View.GONE);
                }
                else{
                    controlLayout.setVisibility(View.VISIBLE);
                }
            }
        } );

        videoView.setOnBufferingUpdateListener(new DolitBaseMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(Object obj, int i) {
                currentProgress=videoView.getCurrentPosition();

                txtduring.setText(secondToTime(videoView.getDuration()/1000));
                txtnowposition.setText(secondToTime(videoView.getCurrentPosition() /1000 ));


                if(isDrag)
                {
                    return;
                }
                seekBar.setProgress(videoView.getCurrentPosition() * 100 / videoView.getDuration());
            }
        });

//        videoView.setOnPreparedListener(new DolitBaseMediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(Object obj, int i, int i2) {
//                videoView.seekTo(currentProgress);
//            }
//        });

        // 设置进度条拖动事件监听器
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // 用户拖动进度条时更新播放进度
                    videoView.seekTo(progress *  videoView.getDuration()  / 100  );

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 用户开始拖动进度条时暂停播放
                isDrag=true;
                videoView.pause();
                pauseButton.setText("继续");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 用户停止拖动进度条时继续播放
                isDrag=false;
                videoView.start();
                pauseButton.setText("暂停");
            }
        });

        // 设置暂停按钮点击事件
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    // 如果处于暂停状态，则继续播放
                    videoView.start();
                    pauseButton.setText("暂停");
                } else {
                    // 如果正在播放，则暂停
                    videoView.pause();
                    pauseButton.setText("继续");
                }
            }
        });

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProgress = videoView.getCurrentPosition();
              if (PlayerActivity.this.getResources().getConfiguration().orientation == 1) {
                  PlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                  //controlLayout.setVisibility(View.GONE);
                } else {
                      PlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                      //controlLayout.setVisibility(View.VISIBLE);
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        currentProgress = videoView.getCurrentPosition();
//                    }
//                });
//
//                boolean isFullScreen=getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE;
//                if (isFullScreen) {
//                    exitFullScreen();
//                } else {
//                    enterFullScreen();
//                }
//                refreshVideo();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        videoView.seekTo(currentProgress);
//                    }
//                });
            }
        });

        //MediaController mediaController = new VideoMediaController(this.getApplicationContext());
        refreshVideo();
    }

    public static String key1="MuShiZaiXiANYGJIAOYUEPINGPAI2015";
    public static String key2="kRHBxCJto3zAnO8vmJTE5R7NL1NXIHpC";
    private void refreshVideo()
    {
        videoView.release(true);
        videoView.initVideoView(this.getApplicationContext());
        //dv.setMediaController(mediaController);
        videoView.setIsHardDecode(false);
        videoView.setFLDVKey(key1);
        videoView.setVideoPath(filePath);
        videoView.requestFocus();
        videoView.start();
    }
    static PlayerActivity self;
    public static void refreshVideoKey2()
    {
        PlayerActivity.self.videoView.release(true);
        PlayerActivity.self.videoView.initVideoView(PlayerActivity.self.getApplicationContext());
        //dv.setMediaController(mediaController);
        PlayerActivity.self.videoView.setIsHardDecode(false);
        PlayerActivity.self.videoView.setFLDVKey(key2);
        PlayerActivity.self.videoView.setVideoPath(filePath);
        PlayerActivity.self.videoView.requestFocus();
        PlayerActivity.self.videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (videoView != null) {
            videoView.release(true);
        }

        if (null != callback) surfaceHolder.removeCallback(callback);
    }

    private int getScreenOrientation() {
        // 获取屏幕旋转的角度
        int rotation = getWindowManager().getDefaultDisplay().getRotation();

        // 获取设备的方向
        Configuration config = getResources().getConfiguration();
        int orientation = config.orientation;

        // 根据屏幕旋转的角度和设备的方向来判断当前屏幕的方向
        if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 当前为竖屏
            return Configuration.ORIENTATION_PORTRAIT;
        } else if ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 当前为横屏
            return Configuration.ORIENTATION_LANDSCAPE;
        } else {
            // 其他情况，如倾斜
            return Configuration.ORIENTATION_UNDEFINED;
        }
    }

    private int currentProgress;


    private WindowManager.LayoutParams layoutParams;

    private void enterFullScreen() {
        // 获取视频界面
        DolitVideoView videoView = findViewById(R.id.sv);

        // 保存初始位置和大小
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        initialVideoViewWidth = layoutParams.width;
        initialVideoViewHeight = layoutParams.height;
        initialVideoViewLeftMargin = layoutParams.leftMargin;
        initialVideoViewTopMargin = layoutParams.topMargin;

        // 设置全屏布局参数
        RelativeLayout.LayoutParams fullScreenLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        videoView.setLayoutParams(fullScreenLayoutParams);
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


        // 设置 Activity 的屏幕方向为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    private void exitFullScreen() {
        // 获取视频界面
        DolitVideoView videoView = findViewById(R.id.sv);

        // 恢复初始位置和大小
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        layoutParams.width = initialVideoViewWidth;
        layoutParams.height = initialVideoViewHeight;
        layoutParams.leftMargin = initialVideoViewLeftMargin;
        layoutParams.topMargin = initialVideoViewTopMargin;
        videoView.setLayoutParams(layoutParams);

        // 恢复系统 UI
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        // 恢复 Activity 的屏幕方向为原始方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

    }


    private int initialVideoViewWidth;
    private int initialVideoViewHeight;
    private int initialVideoViewLeftMargin;
    private int initialVideoViewTopMargin;

}

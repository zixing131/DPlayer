package com.zving.ipmph.app.widget;

import android.app.Activity;
import android.content.Context;
import com.github.mikephil.charting.utils.Utils;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
//import cn.dolit.media.player.widget.DebugLog;
import cn.dolit.media.player.widget.DolitBaseMediaPlayer;
import cn.dolit.media.player.widget.HardMediaPlayer;
import cn.dolit.media.player.widget.IMediaPlayerControl;
import cn.dolit.media.player.widget.MediaController;
import cn.dolit.media.player.widget.SoftMediaPlayer;
import com.networkbench.agent.impl.instrumentation.NBSRunnableInspect;
import com.zixing.dplayer.PlayerActivity;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.HelpFormatter;

import tv.danmaku.ijk.media.player.pragma.DebugLog;
import tv.danmaku.ijk.media.player.widget.media.MeasureHelper;
//import tv.danmaku.ijk.media.player.widget.media.MeasureHelper;

/* loaded from: 人卫/classes14.dex */
public class DolitVideoView extends SurfaceView implements IMediaPlayerControl {
    public static final int A_16X9 = 2;
    public static final int A_4X3 = 1;
    public static final int A_DEFALT = 0;
    public static final int A_RAW = 4;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_RESUME = 7;
    private static final int STATE_SUSPEND = 6;
    private static final int STATE_SUSPEND_UNSUPPORTED = 8;
    private static final String TAG = DolitVideoView.class.getName();
    private static final long TIMEOUTDEFAULT = 30000;
    public static final int VIDEO_LAYOUT_ORIGIN = 0;
    public static final int VIDEO_LAYOUT_SCALE = 1;
    public static final int VIDEO_LAYOUT_STRETCH = 2;
    public static final int VIDEO_LAYOUT_ZOOM = 3;
    private Runnable TimeOutError;
    private boolean autoPlayAfterSurfaceCreated;
    Handler handler;
    private View mBufferingIndicator;
    private DolitBaseMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private DolitBaseMediaPlayer.OnCompletionListener mCompletionListener;
    private Context mContext;
    private int mCurrentBufferPercentage;
    private int mCurrentState;
    private long mDuration;
    private DolitBaseMediaPlayer.OnErrorListener mErrorListener;
    private String mFLDVKey;
    private Map<String, String> mHeaders;
    private DolitBaseMediaPlayer.OnInfoListener mInfoListener;
    private boolean mIsHardDecode;
    protected String mLiveCookie;
    protected String mLiveEpg;
    protected String mLiveNextEpg;
    protected String mLiveNextUrl;
    protected String mLiveSeek;
    protected String mLive_Range;
    protected String mLive_Referer;
    protected String mLive_key;
    private MeasureHelper mMeasureHelper;
    private MediaController mMediaController;
    private DolitBaseMediaPlayer mMediaPlayer;
    private DolitBaseMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private DolitBaseMediaPlayer.OnCompletionListener mOnCompletionListener;
    private DolitBaseMediaPlayer.OnErrorListener mOnErrorListener;
    private DolitBaseMediaPlayer.OnInfoListener mOnInfoListener;
    private DolitBaseMediaPlayer.OnPreparedListener mOnPreparedListener;
    private DolitBaseMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    DolitBaseMediaPlayer.OnPreparedListener mPreparedListener;
    SurfaceHolder.Callback mSHCallback;
    private DolitBaseMediaPlayer.OnSeekCompleteListener mSeekCompleteListener;
    private long mSeekWhenPrepared;
    DolitBaseMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    private int mSurfaceHeight;
    private SurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private Uri mUri;
    private String mUserAgent;
    protected String mUser_Mac;
    private int mVideoHeight;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private WindowManager mWindowManager;
    private int m_videoLayoutScale;
    private boolean mediaCodecEnabled;

    public DolitVideoView(Context context) {
        super(context);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.m_videoLayoutScale = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mIsHardDecode = false;
        this.mUser_Mac = "";
        this.mLiveSeek = "0";
        this.mLiveEpg = HelpFormatter.DEFAULT_OPT_PREFIX;
        this.mLiveNextEpg = HelpFormatter.DEFAULT_OPT_PREFIX;
        this.mLiveNextUrl = HelpFormatter.DEFAULT_OPT_PREFIX;
        this.mLiveCookie = "";
        this.mLive_Range = "mediaTV/range";
        this.mLive_Referer = "mediaTV/user/|support|android-tvbox";
        this.mLive_key = "";
        this.autoPlayAfterSurfaceCreated = true;
        this.mediaCodecEnabled = true;
        this.handler = new Handler(Looper.getMainLooper());
        this.mSizeChangedListener = new DolitBaseMediaPlayer.OnVideoSizeChangedListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.1
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(Object mp, int width, int height, int videoWidth, int videoHeight, int sarNum, int sarDen) {
                DebugLog.dfmt(DolitVideoView.TAG, "onVideoSizeChanged: (%dx%d)", Integer.valueOf(width), Integer.valueOf(height));
                DolitVideoView.this.mVideoWidth = videoWidth;
                DolitVideoView.this.mVideoHeight = videoHeight;
                DolitVideoView.this.mVideoSarNum = sarNum;
                DolitVideoView.this.mVideoSarDen = sarDen;
                if (videoWidth > 0 && videoHeight > 0) {
                    DolitVideoView.this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
                    DolitVideoView.this.requestLayout();
                }
                if (sarNum > 0 && sarDen > 0) {
                    DolitVideoView.this.mMeasureHelper.setVideoSampleAspectRatio(sarNum, sarDen);
                    DolitVideoView.this.requestLayout();
                }
                if (DolitVideoView.this.mVideoWidth != 0) {
                    int unused = DolitVideoView.this.mVideoHeight;
                }
            }
        };
        this.mPreparedListener = new DolitBaseMediaPlayer.OnPreparedListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.2
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnPreparedListener
            public void onPrepared(Object mp, int videoWidth, int videoHeight) {
                DebugLog.d(DolitVideoView.TAG, "onPrepared");
                DolitVideoView.this.mCurrentState = 2;
                DolitVideoView.this.mTargetState = 3;
                DolitVideoView dolitVideoView = DolitVideoView.this;
                dolitVideoView.mCanPause = dolitVideoView.mCanSeekBack = dolitVideoView.mCanSeekForward = true;
                DolitVideoView.this.handler.removeCallbacks(DolitVideoView.this.TimeOutError);
                if (DolitVideoView.this.mOnPreparedListener != null) {
                    DolitVideoView.this.mOnPreparedListener.onPrepared(DolitVideoView.this.mMediaPlayer, videoWidth, videoHeight);
                }
                if (DolitVideoView.this.mMediaController != null) {
                    DolitVideoView.this.mMediaController.setEnabled(true);
                }
                DolitVideoView.this.mVideoWidth = videoWidth;
                DolitVideoView.this.mVideoHeight = videoHeight;
                long j = DolitVideoView.this.mSeekWhenPrepared;
                int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
                if (i != 0) {
                    DolitVideoView.this.seekTo(j);
                }
                if (DolitVideoView.this.mVideoWidth == 0 || DolitVideoView.this.mVideoHeight == 0) {
                    if (DolitVideoView.this.mTargetState == 3) {
                        DolitVideoView.this.start();
                        return;
                    }
                    return;
                }
                DolitVideoView.this.getHolder().setFixedSize(DolitVideoView.this.mVideoWidth, DolitVideoView.this.mVideoHeight);
                if (DolitVideoView.this.mSurfaceWidth == DolitVideoView.this.mVideoWidth && DolitVideoView.this.mSurfaceHeight == DolitVideoView.this.mVideoHeight) {
                    if (DolitVideoView.this.mTargetState == 3) {
                        DolitVideoView.this.start();
                        if (DolitVideoView.this.mMediaController != null) {
                            DolitVideoView.this.mMediaController.show();
                        }
                    } else if (DolitVideoView.this.isPlaying()) {
                    } else {
                        if ((i != 0 || DolitVideoView.this.getCurrentPosition() > 0) && DolitVideoView.this.mMediaController != null) {
                            DolitVideoView.this.mMediaController.show(0);
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new DolitBaseMediaPlayer.OnCompletionListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.3
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnCompletionListener
            public void onCompletion(Object mp) {
                DebugLog.d(DolitVideoView.TAG, "onCompletion");
                DolitVideoView.this.mCurrentState = 5;
                DolitVideoView.this.mTargetState = 5;
                if (DolitVideoView.this.mMediaController != null) {
                    DolitVideoView.this.mMediaController.hide();
                }
                if (DolitVideoView.this.mOnCompletionListener != null) {
                    DolitVideoView.this.mOnCompletionListener.onCompletion(DolitVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mErrorListener = new DolitBaseMediaPlayer.OnErrorListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.4
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnErrorListener
            public boolean onError(Object mp, int framework_err, int impl_err, long currentPosition) {

                DebugLog.dfmt(DolitVideoView.TAG, "Error: %d, %d", Integer.valueOf(framework_err), Integer.valueOf(impl_err));


                DolitVideoView.this.mCurrentState = -1;
                DolitVideoView.this.mTargetState = -1;
                if (DolitVideoView.this.mMediaController != null) {
                    DolitVideoView.this.mMediaController.hide();
                }
                if (DolitVideoView.this.mOnErrorListener != null) {
                    DolitVideoView.this.mOnErrorListener.onError(DolitVideoView.this.mMediaPlayer, framework_err, impl_err, currentPosition);
                }
                return true;

            }
        };
        this.mBufferingUpdateListener = new DolitBaseMediaPlayer.OnBufferingUpdateListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.5
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(Object mp, int percent) {
                DolitVideoView.this.mCurrentBufferPercentage = percent;
                if (DolitVideoView.this.mOnBufferingUpdateListener != null) {
                    DolitVideoView.this.mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
                }
            }
        };
        this.mInfoListener = new DolitBaseMediaPlayer.OnInfoListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.6
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnInfoListener
            public boolean onInfo(Object mp, int what, int extra) {
                DebugLog.dfmt(DolitVideoView.TAG, "onInfo: (%d, %d)", Integer.valueOf(what), Integer.valueOf(extra));
                if (what == -700001) {
                    DebugLog.dfmt(DolitVideoView.TAG, "onInfo: (MEDIA_INFO_BUFFERING_START)", new Object[0]);
                    DolitVideoView.this.showBuffingTip();
                } else if (what == -700002) {
                    DebugLog.dfmt(DolitVideoView.TAG, "onInfo: (MEDIA_INFO_BUFFERING_END)", new Object[0]);
                    DolitVideoView.this.hideBuffingTip();
                }
                if (DolitVideoView.this.mOnInfoListener != null) {
                    DolitVideoView.this.mOnInfoListener.onInfo(mp, what, extra);
                }
                return true;
            }
        };
        this.mSeekCompleteListener = new DolitBaseMediaPlayer.OnSeekCompleteListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.7
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(Object mp) {
                DebugLog.d(DolitVideoView.TAG, "onSeekComplete");
                if (DolitVideoView.this.mOnSeekCompleteListener != null) {
                    DolitVideoView.this.mOnSeekCompleteListener.onSeekComplete(mp);
                }
            }
        };
        this.mSHCallback = new SurfaceHolder.Callback() { // from class: com.zving.ipmph.app.widget.DolitVideoView.8
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                DolitVideoView.this.mSurfaceHolder = holder;
                DolitBaseMediaPlayer unused = DolitVideoView.this.mMediaPlayer;
                DolitVideoView.this.mSurfaceWidth = w;
                DolitVideoView.this.mSurfaceHeight = h;
                boolean z = true;
                boolean z2 = DolitVideoView.this.mTargetState == 3;
                z = (DolitVideoView.this.mVideoWidth == w && DolitVideoView.this.mVideoHeight == h) ? false : false;
                if (DolitVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (DolitVideoView.this.mSeekWhenPrepared != 0) {
                        DolitVideoView dolitVideoView = DolitVideoView.this;
                        dolitVideoView.seekTo(dolitVideoView.mSeekWhenPrepared);
                    }
                    DolitVideoView.this.start();
                    if (DolitVideoView.this.mMediaController != null) {
                        if (DolitVideoView.this.mMediaController.isShowing()) {
                            DolitVideoView.this.mMediaController.hide();
                        }
                        DolitVideoView.this.mMediaController.show();
                    }
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) {
                DolitVideoView.this.mSurfaceHolder = holder;
                if (DolitVideoView.this.mMediaPlayer == null || DolitVideoView.this.mCurrentState != 6 || DolitVideoView.this.mTargetState != 7) {
                    if (DolitVideoView.this.autoPlayAfterSurfaceCreated) {
                        DolitVideoView.this.openVideo();
                        return;
                    }
                    return;
                }
                DolitVideoView.this.mMediaPlayer.setDisplay(DolitVideoView.this.mSurfaceHolder);
                if (DolitVideoView.this.autoPlayAfterSurfaceCreated) {
                    DolitVideoView.this.resume();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                DolitVideoView.this.hideBuffingTip();
                DolitVideoView.this.mSurfaceHolder = null;
                if (DolitVideoView.this.mMediaController != null) {
                    DolitVideoView.this.mMediaController.hide();
                }
                if (DolitVideoView.this.mCurrentState != 6) {
                    DolitVideoView.this.release(true);
                }
            }
        };
        this.TimeOutError = new Runnable() { // from class: com.zving.ipmph.app.widget.DolitVideoView.9
            public transient NBSRunnableInspect nbsHandler = new NBSRunnableInspect();

            @Override // java.lang.Runnable
            public void run() {
                NBSRunnableInspect nBSRunnableInspect = this.nbsHandler;
                if (nBSRunnableInspect != null) {
                    nBSRunnableInspect.preRunMethod();
                }
                DolitVideoView.this.mCurrentState = -1;
                DolitVideoView.this.mTargetState = -1;
                if (DolitVideoView.this.mOnErrorListener != null) {
                    DolitVideoView.this.mOnErrorListener.onError(DolitVideoView.this.mMediaPlayer, DolitBaseMediaPlayer.MEDIA_INFO_TIMEOUT, -100, DolitVideoView.this.getCurrentPosition());
                }
                NBSRunnableInspect nBSRunnableInspect2 = this.nbsHandler;
                if (nBSRunnableInspect2 != null) {
                    nBSRunnableInspect2.sufRunMethod();
                }
            }
        };
        initVideoView(context);
    }

    public DolitVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DolitVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.m_videoLayoutScale = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mIsHardDecode = false;
        this.mUser_Mac = "";
        this.mLiveSeek = "0";
        this.mLiveEpg = HelpFormatter.DEFAULT_OPT_PREFIX;
        this.mLiveNextEpg = HelpFormatter.DEFAULT_OPT_PREFIX;
        this.mLiveNextUrl = HelpFormatter.DEFAULT_OPT_PREFIX;
        this.mLiveCookie = "";
        this.mLive_Range = "mediaTV/range";
        this.mLive_Referer = "mediaTV/user/|support|android-tvbox";
        this.mLive_key = "";
        this.autoPlayAfterSurfaceCreated = true;
        this.mediaCodecEnabled = true;
        this.handler = new Handler(Looper.getMainLooper());
        this.mSizeChangedListener = new DolitBaseMediaPlayer.OnVideoSizeChangedListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.1
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(Object mp, int width, int height, int videoWidth, int videoHeight, int sarNum, int sarDen) {
                DebugLog.dfmt(DolitVideoView.TAG, "onVideoSizeChanged: (%dx%d)", Integer.valueOf(width), Integer.valueOf(height));
                DolitVideoView.this.mVideoWidth = videoWidth;
                DolitVideoView.this.mVideoHeight = videoHeight;
                DolitVideoView.this.mVideoSarNum = sarNum;
                DolitVideoView.this.mVideoSarDen = sarDen;
                if (videoWidth > 0 && videoHeight > 0) {
                    DolitVideoView.this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
                    DolitVideoView.this.requestLayout();
                }
                if (sarNum > 0 && sarDen > 0) {
                    DolitVideoView.this.mMeasureHelper.setVideoSampleAspectRatio(sarNum, sarDen);
                    DolitVideoView.this.requestLayout();
                }
                if (DolitVideoView.this.mVideoWidth != 0) {
                    int unused = DolitVideoView.this.mVideoHeight;
                }
            }
        };
        this.mPreparedListener = new DolitBaseMediaPlayer.OnPreparedListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.2
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnPreparedListener
            public void onPrepared(Object mp, int videoWidth, int videoHeight) {
                DebugLog.d(DolitVideoView.TAG, "onPrepared");
                DolitVideoView.this.mCurrentState = 2;
                DolitVideoView.this.mTargetState = 3;
                DolitVideoView dolitVideoView = DolitVideoView.this;
                dolitVideoView.mCanPause = dolitVideoView.mCanSeekBack = dolitVideoView.mCanSeekForward = true;
                DolitVideoView.this.handler.removeCallbacks(DolitVideoView.this.TimeOutError);
                if (DolitVideoView.this.mOnPreparedListener != null) {
                    DolitVideoView.this.mOnPreparedListener.onPrepared(DolitVideoView.this.mMediaPlayer, videoWidth, videoHeight);
                }
                if (DolitVideoView.this.mMediaController != null) {
                    DolitVideoView.this.mMediaController.setEnabled(true);
                }
                DolitVideoView.this.mVideoWidth = videoWidth;
                DolitVideoView.this.mVideoHeight = videoHeight;
                long j = DolitVideoView.this.mSeekWhenPrepared;
                int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
                if (i != 0) {
                    DolitVideoView.this.seekTo(j);
                }
                if (DolitVideoView.this.mVideoWidth == 0 || DolitVideoView.this.mVideoHeight == 0) {
                    if (DolitVideoView.this.mTargetState == 3) {
                        DolitVideoView.this.start();
                        return;
                    }
                    return;
                }
                DolitVideoView.this.getHolder().setFixedSize(DolitVideoView.this.mVideoWidth, DolitVideoView.this.mVideoHeight);
                if (DolitVideoView.this.mSurfaceWidth == DolitVideoView.this.mVideoWidth && DolitVideoView.this.mSurfaceHeight == DolitVideoView.this.mVideoHeight) {
                    if (DolitVideoView.this.mTargetState == 3) {
                        DolitVideoView.this.start();
                        if (DolitVideoView.this.mMediaController != null) {
                            DolitVideoView.this.mMediaController.show();
                        }
                    } else if (DolitVideoView.this.isPlaying()) {
                    } else {
                        if ((i != 0 || DolitVideoView.this.getCurrentPosition() > 0) && DolitVideoView.this.mMediaController != null) {
                            DolitVideoView.this.mMediaController.show(0);
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new DolitBaseMediaPlayer.OnCompletionListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.3
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnCompletionListener
            public void onCompletion(Object mp) {
                DebugLog.d(DolitVideoView.TAG, "onCompletion");
                DolitVideoView.this.mCurrentState = 5;
                DolitVideoView.this.mTargetState = 5;
                if (DolitVideoView.this.mMediaController != null) {
                    DolitVideoView.this.mMediaController.hide();
                }
                if (DolitVideoView.this.mOnCompletionListener != null) {
                    DolitVideoView.this.mOnCompletionListener.onCompletion(DolitVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mErrorListener = new DolitBaseMediaPlayer.OnErrorListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.4
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnErrorListener
            public boolean onError(Object mp, int framework_err, int impl_err, long currentPosition) {
                DebugLog.dfmt(DolitVideoView.TAG, "Error: %d, %d", Integer.valueOf(framework_err), Integer.valueOf(impl_err));
                if(framework_err==-10000)
                {
                    Log.e(DolitVideoView.TAG,"Key可能错误，尝试更换Key");
                    PlayerActivity.refreshVideoKey2();
                    return true;
                }
                DolitVideoView.this.mCurrentState = -1;
                DolitVideoView.this.mTargetState = -1;
                if (DolitVideoView.this.mMediaController != null) {
                    DolitVideoView.this.mMediaController.hide();
                }
                if (DolitVideoView.this.mOnErrorListener != null) {
                    DolitVideoView.this.mOnErrorListener.onError(DolitVideoView.this.mMediaPlayer, framework_err, impl_err, currentPosition);
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new DolitBaseMediaPlayer.OnBufferingUpdateListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.5
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(Object mp, int percent) {
                DolitVideoView.this.mCurrentBufferPercentage = percent;
                if (DolitVideoView.this.mOnBufferingUpdateListener != null) {
                    DolitVideoView.this.mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
                }
            }
        };
        this.mInfoListener = new DolitBaseMediaPlayer.OnInfoListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.6
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnInfoListener
            public boolean onInfo(Object mp, int what, int extra) {
                DebugLog.dfmt(DolitVideoView.TAG, "onInfo: (%d, %d)", Integer.valueOf(what), Integer.valueOf(extra));
                if (what == -700001) {
                    DebugLog.dfmt(DolitVideoView.TAG, "onInfo: (MEDIA_INFO_BUFFERING_START)", new Object[0]);
                    DolitVideoView.this.showBuffingTip();
                } else if (what == -700002) {
                    DebugLog.dfmt(DolitVideoView.TAG, "onInfo: (MEDIA_INFO_BUFFERING_END)", new Object[0]);
                    DolitVideoView.this.hideBuffingTip();
                }
                if (DolitVideoView.this.mOnInfoListener != null) {
                    DolitVideoView.this.mOnInfoListener.onInfo(mp, what, extra);
                }
                return true;
            }
        };
        this.mSeekCompleteListener = new DolitBaseMediaPlayer.OnSeekCompleteListener() { // from class: com.zving.ipmph.app.widget.DolitVideoView.7
            @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(Object mp) {
                DebugLog.d(DolitVideoView.TAG, "onSeekComplete");
                if (DolitVideoView.this.mOnSeekCompleteListener != null) {
                    DolitVideoView.this.mOnSeekCompleteListener.onSeekComplete(mp);
                }
            }
        };
        this.mSHCallback = new SurfaceHolder.Callback() { // from class: com.zving.ipmph.app.widget.DolitVideoView.8
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                DolitVideoView.this.mSurfaceHolder = holder;
                DolitBaseMediaPlayer unused = DolitVideoView.this.mMediaPlayer;
                DolitVideoView.this.mSurfaceWidth = w;
                DolitVideoView.this.mSurfaceHeight = h;
                boolean z = true;
                boolean z2 = DolitVideoView.this.mTargetState == 3;
                z = (DolitVideoView.this.mVideoWidth == w && DolitVideoView.this.mVideoHeight == h) ? false : false;
                if (DolitVideoView.this.mMediaPlayer != null && z2 && z) {
                    if (DolitVideoView.this.mSeekWhenPrepared != 0) {
                        DolitVideoView dolitVideoView = DolitVideoView.this;
                        dolitVideoView.seekTo(dolitVideoView.mSeekWhenPrepared);
                    }
                    DolitVideoView.this.start();
                    if (DolitVideoView.this.mMediaController != null) {
                        if (DolitVideoView.this.mMediaController.isShowing()) {
                            DolitVideoView.this.mMediaController.hide();
                        }
                        DolitVideoView.this.mMediaController.show();
                    }
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) {
                DolitVideoView.this.mSurfaceHolder = holder;
                if (DolitVideoView.this.mMediaPlayer == null || DolitVideoView.this.mCurrentState != 6 || DolitVideoView.this.mTargetState != 7) {
                    if (DolitVideoView.this.autoPlayAfterSurfaceCreated) {
                        DolitVideoView.this.openVideo();
                        return;
                    }
                    return;
                }
                DolitVideoView.this.mMediaPlayer.setDisplay(DolitVideoView.this.mSurfaceHolder);
                if (DolitVideoView.this.autoPlayAfterSurfaceCreated) {
                    DolitVideoView.this.resume();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                DolitVideoView.this.hideBuffingTip();
                DolitVideoView.this.mSurfaceHolder = null;
                if (DolitVideoView.this.mMediaController != null) {
                    DolitVideoView.this.mMediaController.hide();
                }
                if (DolitVideoView.this.mCurrentState != 6) {
                    DolitVideoView.this.release(true);
                }
            }
        };
        this.TimeOutError = new Runnable() { // from class: com.zving.ipmph.app.widget.DolitVideoView.9
            public transient NBSRunnableInspect nbsHandler = new NBSRunnableInspect();

            @Override // java.lang.Runnable
            public void run() {
                NBSRunnableInspect nBSRunnableInspect = this.nbsHandler;
                if (nBSRunnableInspect != null) {
                    nBSRunnableInspect.preRunMethod();
                }
                DolitVideoView.this.mCurrentState = -1;
                DolitVideoView.this.mTargetState = -1;
                if (DolitVideoView.this.mOnErrorListener != null) {
                    DolitVideoView.this.mOnErrorListener.onError(DolitVideoView.this.mMediaPlayer, DolitBaseMediaPlayer.MEDIA_INFO_TIMEOUT, -100, DolitVideoView.this.getCurrentPosition());
                }
                NBSRunnableInspect nBSRunnableInspect2 = this.nbsHandler;
                if (nBSRunnableInspect2 != null) {
                    nBSRunnableInspect2.sufRunMethod();
                }
            }
        };
        initVideoView(context);
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(this.mMeasureHelper.getMeasuredWidth(), this.mMeasureHelper.getMeasuredHeight());
    }

    public void setFLDVKey(String fldvKey) {
        this.mFLDVKey = fldvKey;
    }

    public void setIsHardDecode(boolean bHardDecode) {
        this.mIsHardDecode = bHardDecode;
    }

    public void initVideoView(Context ctx) {
        this.mContext = ctx;
        this.mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.mVideoSarNum = 0;
        this.mVideoSarDen = 0;
        getHolder().addCallback(this.mSHCallback);
        getHolder().setType(3);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        if (ctx instanceof Activity) {
            ((Activity) ctx).setVolumeControlStream(3);
        }
        this.mMeasureHelper = new MeasureHelper(this);
    }

    public boolean isValid() {
        SurfaceHolder surfaceHolder = this.mSurfaceHolder;
        return surfaceHolder != null && surfaceHolder.getSurface().isValid();
    }

    public void setRequestWidthHeight(int width, int height) {
        SurfaceHolder surfaceHolder = this.mSurfaceHolder;
        if (surfaceHolder == null) {
            return;
        }
        surfaceHolder.setFixedSize(width, height);
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        this.mUri = uri;
        this.mSeekWhenPrepared = 0L;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0L;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void setUserAgent(String ua) {
        this.mUserAgent = ua;
    }

    public void setAutoPlayAfterSurfaceCreated(boolean autoPlayAfterSurfaceCreated) {
        this.autoPlayAfterSurfaceCreated = autoPlayAfterSurfaceCreated;
    }

    public void stopPlayback() {
        DolitBaseMediaPlayer dolitBaseMediaPlayer = this.mMediaPlayer;
        if (dolitBaseMediaPlayer != null) {
            if (dolitBaseMediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openVideo() {
        if (this.mUri == null || this.mSurfaceHolder == null) {
            return;
        }
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        this.mContext.sendBroadcast(intent);
        release(false);
        try {
            this.mDuration = -1L;
            this.mCurrentBufferPercentage = 0;
            DolitBaseMediaPlayer dolitBaseMediaPlayer = null;
            if (this.mUri != null) {
                if (this.mIsHardDecode) {
                    dolitBaseMediaPlayer = new HardMediaPlayer();
                } else {
                    dolitBaseMediaPlayer = new SoftMediaPlayer(this.mediaCodecEnabled);
                }
                dolitBaseMediaPlayer.setFLDVKey(this.mFLDVKey);
                if (!dolitBaseMediaPlayer.init(this.mContext)) {
                    DebugLog.e(TAG, "Init ViviTVMediaPlayer engine failed, please check Key!");
                    return;
                } else if (this.mUserAgent != null) {
                    dolitBaseMediaPlayer.setUserAgent(this.mUserAgent);
                }
            }
            this.mMediaPlayer = dolitBaseMediaPlayer;
            dolitBaseMediaPlayer.setLiveKey(this.mLive_key);
            this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
            this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
            this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
            this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
            this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
            this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
            this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
            if (this.mUri != null) {
                this.mMediaPlayer.setDataSource(this.mContext, this.mUri.toString(), this.mHeaders);
            }
            this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.setScreenOnWhilePlaying(true);
            this.mMediaPlayer.prepareAsync();
            this.handler.postDelayed(this.TimeOutError, 30000L);
            this.mCurrentState = 1;
            attachMediaController();
        } catch (IOException e) {
            String str = TAG;
            DebugLog.e(str, "Unable to open content(IOException): " + this.mUri, e);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, DolitBaseMediaPlayer.MEDIA_INFO_ErrorBeforePlay, 0, 0L);
        } catch (IllegalArgumentException e2) {
            String str2 = TAG;
            DebugLog.e(str2, "Unable to open content(IllegalArgumentException): " + this.mUri, e2);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, DolitBaseMediaPlayer.MEDIA_INFO_ErrorBeforePlay, 0, 0L);
        } catch (IllegalStateException e3) {
            String str3 = TAG;
            DebugLog.e(str3, "Unable to open content(IllegalStateException): " + this.mUri, e3);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, DolitBaseMediaPlayer.MEDIA_INFO_ErrorBeforePlay, 0, 0L);
        }
    }

    public void setMediaController(MediaController controller) {
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            mediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    public void setMediaBufferingIndicator(View bufferingIndicator) {
        if (this.mBufferingIndicator != null) {
            hideBuffingTip();
        }
        this.mBufferingIndicator = bufferingIndicator;
    }

    private void attachMediaController() {
        MediaController mediaController;
        if (this.mMediaPlayer == null || (mediaController = this.mMediaController) == null) {
            return;
        }
        mediaController.setMediaPlayer(this);
        this.mMediaController.setAnchorView(this);
        this.mMediaController.setEnabled(isInPlaybackState());
        Uri uri = this.mUri;
        if (uri != null) {
            List<String> pathSegments = uri.getPathSegments();
            this.mMediaController.setFileName((pathSegments == null || pathSegments.isEmpty()) ? "null" : pathSegments.get(pathSegments.size() - 1));
        }
    }

    public void setOnPreparedListener(DolitBaseMediaPlayer.OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(DolitBaseMediaPlayer.OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(DolitBaseMediaPlayer.OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnBufferingUpdateListener(DolitBaseMediaPlayer.OnBufferingUpdateListener l) {
        this.mOnBufferingUpdateListener = l;
    }

    public void setOnSeekCompleteListener(DolitBaseMediaPlayer.OnSeekCompleteListener l) {
        this.mOnSeekCompleteListener = l;
    }

    public void setOnInfoListener(DolitBaseMediaPlayer.OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release(boolean cleartargetstate) {
        DolitBaseMediaPlayer dolitBaseMediaPlayer = this.mMediaPlayer;
        if (dolitBaseMediaPlayer != null) {
            dolitBaseMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
        }
        this.handler.removeCallbacks(this.TimeOutError);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isInPlaybackState() || this.mMediaController == null) {
            return false;
        }
        toggleMediaControlsVisiblity();
        return false;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        if (!isInPlaybackState() || this.mMediaController == null) {
            return false;
        }
        toggleMediaControlsVisiblity();
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && z && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85 || keyCode == 62) {
                if (isPlaying()) {
                    pause();
                    this.mMediaController.show();
                } else {
                    start();
                    this.mMediaController.hide();
                }
                return true;
            } else if (keyCode == 86 && isPlaying()) {
                pause();
                this.mMediaController.show();
            } else {
                toggleMediaControlsVisiblity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public void start() {
        if (isInPlaybackState()) {
            try {
                this.mMediaPlayer.start();
                this.mCurrentState = 3;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        this.mTargetState = 3;
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public void pause() {
        if (isInPlaybackState()) {
            try {
                if (this.mMediaPlayer.isPlaying()) {
                    this.mMediaPlayer.pause();
                    this.mCurrentState = 4;
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        this.mTargetState = 4;
    }

    public void resume() {
        if (this.mSurfaceHolder == null && this.mCurrentState == 6) {
            this.mTargetState = 7;
        } else if (this.mCurrentState == 8) {
            openVideo();
        }
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public int getDuration() {
        long j;
        if (isInPlaybackState()) {
            long j2 = this.mDuration;
            if (j2 > 0) {
                return (int) j2;
            }
            j = this.mMediaPlayer.getDuration();
            this.mDuration = j;
        } else {
            j = -1;
            this.mDuration = -1L;
        }
        return (int) j;
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public void seekTo(long msec) {
        if (isInPlaybackState()) {
            try {
                this.mMediaPlayer.seekTo(msec);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            this.mSeekWhenPrepared = 0L;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    protected boolean isInPlaybackState() {
        int i;
        return (this.mMediaPlayer == null || (i = this.mCurrentState) == -1 || i == 0 || i == 1) ? false : true;
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public boolean canPause() {
        return this.mCanPause;
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public void setScale(int flg) {
        this.m_videoLayoutScale = flg % 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBuffingTip() {
        View view = this.mBufferingIndicator;
        if (view != null) {
            view.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideBuffingTip() {
        View view = this.mBufferingIndicator;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    public void refreshScales() {
        selectScales(this.m_videoLayoutScale);
    }

    public void selectScales(int flg) {
        this.m_videoLayoutScale = flg;
        if (getWindowToken() != null) {
            Rect rect = new Rect();
            getWindowVisibleDisplayFrame(rect);
            double d = rect.bottom - rect.top;
            double d2 = rect.right - rect.left;
            if (d <= Utils.DOUBLE_EPSILON || d2 <= Utils.DOUBLE_EPSILON || this.mVideoHeight <= Utils.DOUBLE_EPSILON || this.mVideoWidth <= Utils.DOUBLE_EPSILON) {
                return;
            }
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (flg == 0) {
                if (d2 / d >= (this.mVideoWidth * 1.0f) / this.mVideoHeight) {
                    layoutParams.height = (int) d;
                    layoutParams.width = (int) ((this.mVideoWidth * d) / this.mVideoHeight);
                } else {
                    layoutParams.width = (int) d2;
                    layoutParams.height = (int) ((this.mVideoHeight * d2) / this.mVideoWidth);
                }
                setLayoutParams(layoutParams);
            } else if (flg == 1) {
                if (d2 / d >= 1.3333333333333333d) {
                    layoutParams.height = (int) d;
                    layoutParams.width = (int) ((d * 4.0d) / 3.0d);
                } else {
                    layoutParams.width = (int) d2;
                    layoutParams.height = (int) ((d2 * 3.0d) / 4.0d);
                }
                setLayoutParams(layoutParams);
            } else if (flg != 2) {
            } else {
                if (d2 / d >= 1.7777777777777777d) {
                    layoutParams.height = (int) d;
                    layoutParams.width = (int) ((d * 16.0d) / 9.0d);
                } else {
                    layoutParams.width = (int) d2;
                    layoutParams.height = (int) ((d2 * 9.0d) / 16.0d);
                }
                setLayoutParams(layoutParams);
            }
        }
    }

    public void setUserMac(String strUserMac) {
        this.mUser_Mac = strUserMac;
    }

    public void setLiveSeek(String strLiveSeek) {
        this.mLiveSeek = strLiveSeek;
    }

    public void setLiveEpg(String strLiveEpg) {
        this.mLiveEpg = strLiveEpg;
    }

    public void setLiveCookie(String strLiveCookie) {
        this.mLiveCookie = strLiveCookie;
    }

    public void setLiveRange(String strLiveRange) {
        this.mLive_Range = strLiveRange;
    }

    public void setLiveReferer(String strLive_Referer) {
        this.mLive_Referer = strLive_Referer;
    }

    public void setLiveKey(String strLivKey) {
        this.mLive_key = strLivKey;
    }

    public void setMediaCodecEnabled(boolean mediaCodecEnabled) {
        this.mediaCodecEnabled = mediaCodecEnabled;
    }

    @Override // cn.dolit.media.player.widget.IMediaPlayerControl
    public void setSpeed(float speed) {
        DolitBaseMediaPlayer dolitBaseMediaPlayer = this.mMediaPlayer;
        if (dolitBaseMediaPlayer != null) {
            dolitBaseMediaPlayer.setSpeed(speed);
        }
    }

    public void solveError() {
        openVideo();
    }
}
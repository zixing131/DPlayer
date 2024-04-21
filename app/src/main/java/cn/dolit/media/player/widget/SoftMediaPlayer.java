package cn.dolit.media.player.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.alipay.sdk.cons.b;
import java.io.IOException;
import java.util.Map;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: 人卫/classes09.dex */
public class SoftMediaPlayer extends DolitBaseMediaPlayer {
    public static final String TAG = SoftMediaPlayer.class.getName();
    public static final String strPlayEngineKey1 = "GipZHOUL6Dvz25KF8Wf4Rb3wgsldmTSX";
    public static final String strPlayEngineKey2 = "sx4HB1t2mIcFku3AGQU8XvJqVjw5z7yd";
    private String mFLDVKey;
    private IMediaPlayer mMediaPlayer;
    private boolean mediaCodecEnabled;
    private DolitBaseMediaPlayer.OnInfoListener mThisOnInfoListener = null;
    private DolitBaseMediaPlayer.OnCompletionListener mThisOnCompletionListener = null;
    private DolitBaseMediaPlayer.OnPreparedListener mThisOnPreparedListener = null;
    private DolitBaseMediaPlayer.OnBufferingUpdateListener mThisOnBufferingUpdateListener = null;
    private DolitBaseMediaPlayer.OnSeekCompleteListener mThisOnSeekCompleteListener = null;
    private DolitBaseMediaPlayer.OnErrorListener mThisOnErrorListener = null;
    private DolitBaseMediaPlayer.OnVideoSizeChangedListener mThisOnVideoSizeChangedListener = null;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: cn.dolit.media.player.widget.SoftMediaPlayer.1
        {
            //SoftMediaPlayer.this = this;
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i2, int i3, int i4) {
            if (SoftMediaPlayer.this.mThisOnVideoSizeChangedListener != null) {
                SoftMediaPlayer.this.mThisOnVideoSizeChangedListener.onVideoSizeChanged(iMediaPlayer, i, i2, iMediaPlayer.getVideoWidth(), iMediaPlayer.getVideoHeight(), i3, i4);
            }
        }
    };
    private final IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() { // from class: cn.dolit.media.player.widget.SoftMediaPlayer.2
        {
            //SoftMediaPlayer.this = this;
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i2) {
            if (SoftMediaPlayer.this.mThisOnInfoListener != null) {
                if (i == 701) {
                    i = DolitBaseMediaPlayer.MEDIA_INFO_BUFFERING_START;
                } else if (i == 702) {
                    i = DolitBaseMediaPlayer.MEDIA_INFO_BUFFERING_END;
                }
                return SoftMediaPlayer.this.mThisOnInfoListener.onInfo(iMediaPlayer, i, i2);
            }
            return false;
        }
    };
    private final IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: cn.dolit.media.player.widget.SoftMediaPlayer.3
        {
            //SoftMediaPlayer.this = this;
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            if (SoftMediaPlayer.this.mThisOnCompletionListener != null) {
                SoftMediaPlayer.this.mThisOnCompletionListener.onCompletion(iMediaPlayer);
            }
        }
    };
    private final IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() { // from class: cn.dolit.media.player.widget.SoftMediaPlayer.4
        {
            //SoftMediaPlayer.this = this;
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
            if (SoftMediaPlayer.this.mThisOnErrorListener != null) {
                return SoftMediaPlayer.this.mThisOnErrorListener.onError(iMediaPlayer, i, i2, iMediaPlayer.getCurrentPosition());
            }
            return false;
        }
    };
    private final IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: cn.dolit.media.player.widget.SoftMediaPlayer.5
        {
            //SoftMediaPlayer.this = this;
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            if (SoftMediaPlayer.this.mThisOnPreparedListener != null) {
                SoftMediaPlayer.this.mThisOnPreparedListener.onPrepared(iMediaPlayer, iMediaPlayer.getVideoWidth(), iMediaPlayer.getVideoHeight());
            }
        }
    };
    private final IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: cn.dolit.media.player.widget.SoftMediaPlayer.6
        {
            //SoftMediaPlayer.this = this;
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
            if (SoftMediaPlayer.this.mThisOnBufferingUpdateListener != null) {
                SoftMediaPlayer.this.mThisOnBufferingUpdateListener.onBufferingUpdate(iMediaPlayer, i);
            }
        }
    };
    private final IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: cn.dolit.media.player.widget.SoftMediaPlayer.7
        {
            //SoftMediaPlayer.this = this;
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {
            if (SoftMediaPlayer.this.mThisOnSeekCompleteListener != null) {
                SoftMediaPlayer.this.mThisOnSeekCompleteListener.onSeekComplete(iMediaPlayer);
            }
        }
    };

    public SoftMediaPlayer(boolean z) {
        this.mMediaPlayer = null;
        this.mediaCodecEnabled = false;
        this.mMediaPlayer = new IjkMediaPlayer();
        this.mediaCodecEnabled = z;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setDisplay(SurfaceHolder surfaceHolder) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setDataSource(Context context, String str, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mContext = context;
        if (this.mMediaPlayer != null) {
            this.mDataSource = str;
            this.mMediaPlayer.setDataSource(this.mDataSource);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public String getDataSource() {
        return this.mDataSource;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void prepareAsync() throws IllegalStateException {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.prepareAsync();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void start() throws IllegalStateException {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.start();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void stop() throws IllegalStateException {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.stop();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void pause() throws IllegalStateException {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.pause();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setScreenOnWhilePlaying(boolean z) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.setScreenOnWhilePlaying(z);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public int getVideoWidth() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            return iMediaPlayer.getVideoWidth();
        }
        return 0;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public int getVideoHeight() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            return iMediaPlayer.getVideoHeight();
        }
        return 0;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public boolean isPlaying() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            return iMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void seekTo(long j) throws IllegalStateException {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.seekTo((int) j);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public long getCurrentPosition() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            return iMediaPlayer.getCurrentPosition();
        }
        return 0L;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public long getDuration() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            return iMediaPlayer.getDuration();
        }
        return 0L;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void release() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.release();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void reset() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.getVideoHeight();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setVolume(float f, float f2) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.setVolume(f, f2);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public Object getRealMediaPlayer() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            return iMediaPlayer;
        }
        return null;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setAudioStreamType(int i) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer == null) {
            iMediaPlayer.setAudioStreamType(i);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setWakeMode(Context context, int i) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.setWakeMode(context, i);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setSurface(Surface surface) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.setSurface(surface);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setOnPreparedListener(DolitBaseMediaPlayer.OnPreparedListener onPreparedListener) {
        this.mThisOnPreparedListener = onPreparedListener;
        this.mMediaPlayer.setOnPreparedListener(this.mOnPreparedListener);
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setOnCompletionListener(DolitBaseMediaPlayer.OnCompletionListener onCompletionListener) {
        this.mThisOnCompletionListener = onCompletionListener;
        this.mMediaPlayer.setOnCompletionListener(this.mOnCompletionListener);
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setOnBufferingUpdateListener(DolitBaseMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener) {
        this.mThisOnBufferingUpdateListener = onBufferingUpdateListener;
        this.mMediaPlayer.setOnBufferingUpdateListener(this.mOnBufferingUpdateListener);
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setOnSeekCompleteListener(DolitBaseMediaPlayer.OnSeekCompleteListener onSeekCompleteListener) {
        this.mThisOnSeekCompleteListener = onSeekCompleteListener;
        this.mMediaPlayer.setOnSeekCompleteListener(this.mOnSeekCompleteListener);
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setOnVideoSizeChangedListener(DolitBaseMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.mThisOnVideoSizeChangedListener = onVideoSizeChangedListener;
        this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setOnErrorListener(DolitBaseMediaPlayer.OnErrorListener onErrorListener) {
        this.mThisOnErrorListener = onErrorListener;
        this.mMediaPlayer.setOnErrorListener(this.mOnErrorListener);
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setOnInfoListener(DolitBaseMediaPlayer.OnInfoListener onInfoListener) {
        this.mThisOnInfoListener = onInfoListener;
        this.mMediaPlayer.setOnInfoListener(this.mOnInfoListener);
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setSpeed(float f) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        IjkMediaPlayer ijkMediaPlayer = iMediaPlayer instanceof IjkMediaPlayer ? (IjkMediaPlayer) iMediaPlayer : null;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.setSpeed(f);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setFLDVKey(String str) {
        this.mFLDVKey = str;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public boolean init(Context context) {
        IjkMediaPlayer ijkMediaPlayer = (IjkMediaPlayer) this.mMediaPlayer;
        if (ijkMediaPlayer.Init(strPlayEngineKey1, strPlayEngineKey2, context) != 0) {
            return false;
        }
        ijkMediaPlayer.setOption(1, "http-detect-range-support", 0L);
        ijkMediaPlayer.setOption(4, "overlay-format", 842225234L);
        ijkMediaPlayer.setOption(2, "skip_loop_filter", 48L);
        ijkMediaPlayer.setOption(4, "framedrop", 12L);
        ijkMediaPlayer.setOption(4, "mediacodec", this.mediaCodecEnabled ? 1L : 0L);
        ijkMediaPlayer.setOption(4, "soundtouch", 1L);
        ijkMediaPlayer.setOption(4, "enable-accurate-seek", 1L);
        ijkMediaPlayer.setOption(1, "fldv_key", TextUtils.isEmpty(this.mFLDVKey) ? "" : this.mFLDVKey);
        return true;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setUserAgent(String str) {
        if (str != null) {
            ((IjkMediaPlayer) this.mMediaPlayer).setOption(1, b.b, str);
        }
    }
}
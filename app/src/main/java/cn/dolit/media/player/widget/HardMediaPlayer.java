package cn.dolit.media.player.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;
import cn.dolit.media.player.widget.DolitBaseMediaPlayer;
import java.io.IOException;
import java.util.Map;

/* loaded from: 人卫/classes09.dex */
public class HardMediaPlayer extends DolitBaseMediaPlayer {
    private MediaPlayer mMediaPlayer;
    private DolitBaseMediaPlayer.OnInfoListener mThisOnInfoListener = null;
    private DolitBaseMediaPlayer.OnCompletionListener mThisOnCompletionListener = null;
    private DolitBaseMediaPlayer.OnPreparedListener mThisOnPreparedListener = null;
    private DolitBaseMediaPlayer.OnBufferingUpdateListener mThisOnBufferingUpdateListener = null;
    private DolitBaseMediaPlayer.OnSeekCompleteListener mThisOnSeekCompleteListener = null;
    private DolitBaseMediaPlayer.OnErrorListener mThisOnErrorListener = null;
    private DolitBaseMediaPlayer.OnVideoSizeChangedListener mThisOnVideoSizeChangedListener = null;
    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() { // from class: cn.dolit.media.player.widget.HardMediaPlayer.1
        @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
            if (HardMediaPlayer.this.mThisOnVideoSizeChangedListener != null) {
                HardMediaPlayer.this.mThisOnVideoSizeChangedListener.onVideoSizeChanged(mediaPlayer, i, i2, mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight(), 0, 0);
            }
        }
    };
    private final MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() { // from class: cn.dolit.media.player.widget.HardMediaPlayer.2
        @Override // android.media.MediaPlayer.OnInfoListener
        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
            if (HardMediaPlayer.this.mThisOnInfoListener != null) {
                if (i == 701) {
                    i = DolitBaseMediaPlayer.MEDIA_INFO_BUFFERING_START;
                } else if (i == 702) {
                    i = DolitBaseMediaPlayer.MEDIA_INFO_BUFFERING_END;
                }
                return HardMediaPlayer.this.mThisOnInfoListener.onInfo(mediaPlayer, i, i2);
            }
            return false;
        }
    };
    private final MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: cn.dolit.media.player.widget.HardMediaPlayer.3
        @Override // android.media.MediaPlayer.OnCompletionListener
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (HardMediaPlayer.this.mThisOnCompletionListener != null) {
                HardMediaPlayer.this.mThisOnCompletionListener.onCompletion(mediaPlayer);
            }
        }
    };
    private final MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() { // from class: cn.dolit.media.player.widget.HardMediaPlayer.4
        @Override // android.media.MediaPlayer.OnErrorListener
        public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
            if (HardMediaPlayer.this.mThisOnErrorListener != null) {
                return HardMediaPlayer.this.mThisOnErrorListener.onError(mediaPlayer, i, i2, mediaPlayer.getCurrentPosition());
            }
            return false;
        }
    };
    private final MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: cn.dolit.media.player.widget.HardMediaPlayer.5
        @Override // android.media.MediaPlayer.OnPreparedListener
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (HardMediaPlayer.this.mThisOnPreparedListener != null) {
                HardMediaPlayer.this.mThisOnPreparedListener.onPrepared(mediaPlayer, mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
            }
        }
    };
    private final MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: cn.dolit.media.player.widget.HardMediaPlayer.6
        @Override // android.media.MediaPlayer.OnBufferingUpdateListener
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            if (HardMediaPlayer.this.mThisOnBufferingUpdateListener != null) {
                HardMediaPlayer.this.mThisOnBufferingUpdateListener.onBufferingUpdate(mediaPlayer, i);
            }
        }
    };
    private final MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() { // from class: cn.dolit.media.player.widget.HardMediaPlayer.7
        @Override // android.media.MediaPlayer.OnSeekCompleteListener
        public void onSeekComplete(MediaPlayer mediaPlayer) {
            if (HardMediaPlayer.this.mThisOnSeekCompleteListener != null) {
                HardMediaPlayer.this.mThisOnSeekCompleteListener.onSeekComplete(mediaPlayer);
            }
        }
    };

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setFLDVKey(String str) {
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setUserAgent(String str) {
    }

    public HardMediaPlayer() {
        this.mMediaPlayer = null;
        this.mMediaPlayer = new MediaPlayer();
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setDisplay(SurfaceHolder surfaceHolder) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setDataSource(Context context, String str, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mContext = context;
        if (this.mMediaPlayer != null) {
            this.mDataSource = str;
            this.mMediaPlayer.setDataSource(this.mContext, Uri.parse(this.mDataSource), getPlayer_headers(this.mDataSource, map));
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public String getDataSource() {
        return this.mDataSource;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void prepareAsync() throws IllegalStateException {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.prepareAsync();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void start() throws IllegalStateException {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void stop() throws IllegalStateException {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void pause() throws IllegalStateException {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setScreenOnWhilePlaying(boolean z) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setScreenOnWhilePlaying(z);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public int getVideoWidth() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getVideoWidth();
        }
        return 0;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public int getVideoHeight() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getVideoHeight();
        }
        return 0;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public boolean isPlaying() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            try {
                return mediaPlayer.isPlaying();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void seekTo(long j) throws IllegalStateException {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.seekTo((int) j);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public long getCurrentPosition() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0L;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public long getDuration() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0L;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void release() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void reset() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.getVideoHeight();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setVolume(float f, float f2) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(f, f2);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public Object getRealMediaPlayer() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer;
        }
        return null;
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setAudioStreamType(int i) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            mediaPlayer.setAudioStreamType(i);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setWakeMode(Context context, int i) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setWakeMode(context, i);
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public void setSurface(Surface surface) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setSurface(surface);
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
        super.setSpeed(f);
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.mMediaPlayer.isPlaying()) {
                MediaPlayer mediaPlayer = this.mMediaPlayer;
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(f));
                return;
            }
            MediaPlayer mediaPlayer2 = this.mMediaPlayer;
            mediaPlayer2.setPlaybackParams(mediaPlayer2.getPlaybackParams().setSpeed(f));
            this.mMediaPlayer.pause();
        }
    }

    @Override // cn.dolit.media.player.widget.DolitBaseMediaPlayer
    public boolean init(Context context) {
        return this.mMediaPlayer != null;
    }

    public static boolean isHardDecodeError(int i, int i2) {
        if (Build.VERSION.SDK_INT >= 17) {
            return (i2 == -1004 || i2 == -110) ? false : true;
        }
        return true;
    }
}
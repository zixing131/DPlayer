package cn.dolit.media.player.widget;

/* loaded from: 人卫/classes09.dex */
public interface IMediaPlayerControl {
    boolean canPause();

    boolean canSeekBackward();

    boolean canSeekForward();

    int getBufferPercentage();

    int getCurrentPosition();

    int getDuration();

    boolean isPlaying();

    void pause();

    void seekTo(long j);

    void setSpeed(float f);

    void start();
}
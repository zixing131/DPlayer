package cn.dolit.media.player.widget;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.HelpFormatter;
import org.slf4j.Marker;

/* loaded from: 人卫/classes09.dex */
public abstract class DolitBaseMediaPlayer {
    public static final int MEDIA_INFO_BUFFERING_END = -700002;
    public static final int MEDIA_INFO_BUFFERING_START = -700001;
    public static final int MEDIA_INFO_ErrorBeforePlay = -700003;
    public static final int MEDIA_INFO_TIMEOUT = -700004;
    public static final String TAG = DolitBaseMediaPlayer.class.getName();
    protected String mDataSource;
    protected Context mContext = null;
    protected String mUser_Mac = "";
    protected String mLiveSeek = "0";
    protected String mLiveEpg = HelpFormatter.DEFAULT_OPT_PREFIX;
    protected String mLiveNextEpg = HelpFormatter.DEFAULT_OPT_PREFIX;
    protected String mLiveNextUrl = HelpFormatter.DEFAULT_OPT_PREFIX;
    protected String mLiveCookie = "";
    protected String mLive_Range = "mediaTV/range";
    protected String mLive_Referer = "mediaTV/user/|support|android-tvbox";
    protected String mLive_key = "";
    protected float speed = 1.0f;

    /* loaded from: 人卫/classes09.dex */
    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(Object obj, int i);
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnCompletionListener {
        void onCompletion(Object obj);
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnErrorListener {
        boolean onError(Object obj, int i, int i2, long j);
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnInfoListener {
        boolean onInfo(Object obj, int i, int i2);
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnPreparedListener {
        void onPrepared(Object obj, int i, int i2);
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnSeekCompleteListener {
        void onSeekComplete(Object obj);
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(Object obj, int i, int i2, int i3, int i4, int i5, int i6);
    }

    public abstract long getCurrentPosition();

    public abstract String getDataSource();

    public abstract long getDuration();

    public abstract Object getRealMediaPlayer();

    public abstract int getVideoHeight();

    public abstract int getVideoWidth();

    public abstract boolean init(Context context);

    public abstract boolean isPlaying();

    public abstract void pause() throws IllegalStateException;

    public abstract void prepareAsync() throws IllegalStateException;

    public abstract void release();

    public abstract void reset();

    public abstract void seekTo(long j) throws IllegalStateException;

    public abstract void setAudioStreamType(int i);

    public abstract void setDataSource(Context context, String str, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    public abstract void setDisplay(SurfaceHolder surfaceHolder);

    public abstract void setFLDVKey(String str);

    public abstract void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener);

    public abstract void setOnCompletionListener(OnCompletionListener onCompletionListener);

    public abstract void setOnErrorListener(OnErrorListener onErrorListener);

    public abstract void setOnInfoListener(OnInfoListener onInfoListener);

    public abstract void setOnPreparedListener(OnPreparedListener onPreparedListener);

    public abstract void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener);

    public abstract void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener);

    public abstract void setScreenOnWhilePlaying(boolean z);

    public abstract void setSurface(Surface surface);

    public abstract void setUserAgent(String str);

    public abstract void setVolume(float f, float f2);

    @Deprecated
    public abstract void setWakeMode(Context context, int i);

    public abstract void start() throws IllegalStateException;

    public abstract void stop() throws IllegalStateException;

    public void setUserMac(String str) {
        this.mUser_Mac = str;
    }

    public void setLiveSeek(String str) {
        this.mLiveSeek = str;
    }

    public void setLiveEpg(String str) {
        this.mLiveEpg = str;
    }

    public void setLiveCookie(String str) {
        this.mLiveCookie = str;
    }

    public void setLiveRange(String str) {
        this.mLive_Range = str;
    }

    public void setLiveReferer(String str) {
        this.mLive_Referer = str;
    }

    public void setLiveKey(String str) {
        this.mLive_key = str;
    }

    public String GetEncodeUrl(String str) {
        try {
            return formatEncodeUrl(str);
        } catch (UnsupportedEncodingException e) {
            String str2 = TAG;
            Log.e(str2, "encodeUrl[" + str + "] error:" + e.getMessage());
            return str;
        }
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float f) {
        this.speed = f;
    }

    private String formatEncodeUrl(String str) throws UnsupportedEncodingException {
        String[] split = str.split("/");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            if (!split[i].contains("%")) {
                split[i] = URLEncoder.encode(split[i], "UTF-8");
            }
            split[i] = split[i].replace(" ", "%20");
            split[i] = split[i].replace(Marker.ANY_NON_NULL_MARKER, "%20");
            split[i] = split[i].replace("%3A", ":");
            split[i] = split[i].replace("%3a", ":");
            stringBuffer.append(split[i]);
            stringBuffer.append("/");
        }
        return stringBuffer.toString().substring(0, stringBuffer.length() - 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<String, String> getPlayer_headers(String str, Map<String, String> map) {
        String[] split;
        if (map != null) {
            return map;
        }
        HashMap hashMap = new HashMap();
        if (!this.mUser_Mac.isEmpty()) {
            hashMap.put("User-Mac", this.mUser_Mac);
        }
        hashMap.put("User-Key", this.mLive_key);
        hashMap.put("User-Ver", "MediaTVPlayer/MediaTV (" + Build.MODEL + ")");
        int i = 0;
        if (is_str(this.mLive_Referer, "|")) {
            for (String str2 : this.mLive_Referer.split("\\|")) {
                if (is_str(str2, "@")) {
                    String[] split2 = str2.split("@");
                    if (is_str(str, split2[0])) {
                        hashMap.put(HttpHeaders.REFERER, split2[1]);
                    }
                } else if (is_str(str, str2)) {
                    hashMap.put(HttpHeaders.REFERER, str);
                }
            }
        } else if (is_str(this.mLive_Referer, "@")) {
            String[] split3 = this.mLive_Referer.split("@");
            if (is_str(str, split3[0])) {
                hashMap.put(HttpHeaders.REFERER, split3[1]);
            }
        } else if (is_str(str, this.mLive_Referer)) {
            hashMap.put(HttpHeaders.REFERER, str);
        }
        String str3 = this.mLiveCookie;
        if (str3 != null && str3 != "" && str3 != HelpFormatter.DEFAULT_OPT_PREFIX) {
            hashMap.put("Cookie", str3);
        }
        String str4 = this.mLive_Range;
        if (str4 != null && str4 != "" && str4 != HelpFormatter.DEFAULT_OPT_PREFIX && is_str(str4, "|")) {
            String[] split4 = this.mLive_Range.split("\\|");
            while (true) {
                if (i >= split4.length) {
                    break;
                } else if (is_str(str, split4[i])) {
                    hashMap.put("Range", "bytes=");
                    break;
                } else {
                    i++;
                }
            }
        } else if (is_str(str, this.mLive_Range)) {
            hashMap.put("Range", "bytes=");
        }
        return hashMap;
    }

    private static boolean is_str(String str, String str2) {
        return str.contains(str2);
    }
}
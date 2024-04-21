package tv.danmaku.ijk.media.player.widget.media;

import android.content.Context;
import android.view.View;
import java.lang.ref.WeakReference;

/* loaded from: 人卫/classes15.dex */
public final class MeasureHelper {
    private int mCurrentAspectRatio = 0;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private final WeakReference<View> mWeakView;

    public static String getAspectRatioText(Context context, int i) {
        return "";
    }

    public MeasureHelper(View view) {
        this.mWeakView = new WeakReference<>(view);
    }

    public View getView() {
        WeakReference<View> weakReference = this.mWeakView;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public void setVideoSize(int i, int i2) {
        this.mVideoWidth = i;
        this.mVideoHeight = i2;
    }

    public void setVideoSampleAspectRatio(int i, int i2) {
        this.mVideoSarNum = i;
        this.mVideoSarDen = i2;
    }

    public void setVideoRotation(int i) {
        this.mVideoRotationDegree = i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:133:0x00ae, code lost:
        if (r4 != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x00b1, code lost:
        if (r4 != false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x00b3, code lost:
        r12 = (int) (r0 / r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x00b7, code lost:
        r11 = (int) (r3 * r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x00f7, code lost:
        if (r1 > r11) goto L75;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void doMeasure(int i, int i2) {
        int i3;
        float f;
        int i4;
        int i5 = this.mVideoRotationDegree;
        if (i5 == 90 || i5 == 270) {
            i2 = i;
            i = i2;
        }
        int defaultSize = View.getDefaultSize(this.mVideoWidth, i);
        int defaultSize2 = View.getDefaultSize(this.mVideoHeight, i2);
        if (this.mCurrentAspectRatio != 3) {
            if (this.mVideoWidth <= 0 || this.mVideoHeight <= 0) {
                i = defaultSize;
                i2 = defaultSize2;
            } else {
                int mode = View.MeasureSpec.getMode(i);
                i = View.MeasureSpec.getSize(i);
                int mode2 = View.MeasureSpec.getMode(i2);
                i2 = View.MeasureSpec.getSize(i2);
                if (mode == Integer.MIN_VALUE && mode2 == Integer.MIN_VALUE) {
                    float f2 = i;
                    float f3 = i2;
                    float f4 = f2 / f3;
                    int i6 = this.mCurrentAspectRatio;
                    if (i6 == 4) {
                        int i7 = this.mVideoRotationDegree;
                        f = (i7 == 90 || i7 == 270) ? 0.5625f : 1.7777778f;
                    } else if (i6 == 5) {
                        int i8 = this.mVideoRotationDegree;
                        f = (i8 == 90 || i8 == 270) ? 0.75f : 1.3333334f;
                    } else {
                        f = this.mVideoWidth / this.mVideoHeight;
                        int i9 = this.mVideoSarNum;
                        if (i9 > 0 && (i4 = this.mVideoSarDen) > 0) {
                            f = (f * i9) / i4;
                        }
                    }
                    boolean z = f > f4;
                    int i10 = this.mCurrentAspectRatio;
                    if (i10 != 0) {
                        if (i10 != 1) {
                            if (i10 != 4 && i10 != 5) {
                                if (z) {
                                    i = Math.min(this.mVideoWidth, i);
                                    i2 = (int) (i / f);
                                } else {
                                    int min = Math.min(this.mVideoHeight, i2);
                                    i2 = min;
                                    i = (int) (min * f);
                                }
                            }
                        }
                    }
                } else if (mode == 1073741824 && mode2 == 1073741824) {
                    int i11 = this.mVideoWidth;
                    int i12 = i11 * i2;
                    int i13 = this.mVideoHeight;
                    if (i12 < i * i13) {
                        i = (i11 * i2) / i13;
                    } else if (i11 * i2 > i * i13) {
                        i2 = (i13 * i) / i11;
                    }
                } else if (mode == 1073741824) {
                    int i14 = (this.mVideoHeight * i) / this.mVideoWidth;
                    if (mode2 != Integer.MIN_VALUE || i14 <= i2) {
                        i2 = i14;
                    }
                } else if (mode2 == 1073741824) {
                    i3 = (this.mVideoWidth * i2) / this.mVideoHeight;
                    if (mode == Integer.MIN_VALUE) {
                    }
                    i = i3;
                } else {
                    i3 = this.mVideoWidth;
                    int i15 = this.mVideoHeight;
                    if (mode2 != Integer.MIN_VALUE || i15 <= i2) {
                        i2 = i15;
                    } else {
                        i3 = (i3 * i2) / i15;
                    }
                    if (mode == Integer.MIN_VALUE && i3 > i) {
                        i2 = (this.mVideoHeight * i) / this.mVideoWidth;
                    }
                    i = i3;
                }
            }
        }
        this.mMeasuredWidth = i;
        this.mMeasuredHeight = i2;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public void setAspectRatio(int i) {
        this.mCurrentAspectRatio = i;
    }
}
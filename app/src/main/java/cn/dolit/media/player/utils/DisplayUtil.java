package cn.dolit.media.player.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

/* loaded from: 人卫/classes09.dex */
public class DisplayUtil {
    private Context afContext;

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int sp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int px2sp(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int[] calculatePopWindowPos(View view, View view2) {
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        view.getLocationOnScreen(iArr2);
        int height = view.getHeight();
        int screenHeight = getScreenHeight(view.getContext());
        int screenWidth = getScreenWidth(view.getContext());
        view2.measure(0, 0);
        int measuredHeight = view2.getMeasuredHeight();
        int measuredWidth = view2.getMeasuredWidth();
        if ((screenHeight - iArr2[1]) - height < measuredHeight) {
            iArr[0] = screenWidth - measuredWidth;
            iArr[1] = iArr2[1] - measuredHeight;
        } else {
            iArr[0] = screenWidth - measuredWidth;
            iArr[1] = iArr2[1] + height;
        }
        return iArr;
    }

    public static int getDimensionPixbyid(Context context, int i) {
        return context.getResources().getDimensionPixelSize(i);
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        return resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
    }
}
package cn.dolit.media.player.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import cn.dolit.media.player.utils.DisplayUtil;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

import com.networkbench.agent.impl.instrumentation.NBSInstrumented;
import com.networkbench.agent.impl.instrumentation.NBSRunnableInspect;
import com.zixing.dplayer.R;

import java.util.Locale;

@NBSInstrumented
/* loaded from: 人卫/classes09.dex */
public class MediaController extends FrameLayout implements View.OnClickListener {
    protected static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    protected static final int SPEED_OUT = 3;
    private static final String TAG = MediaController.class.getSimpleName();
    private static final int sDefaultTimeout = 3000;
    private View anchorForLocation;
    protected IMediaControllerCallback callback;
    private ImageView endPoint;
    protected IMediaPlayOrPauseListener iMediaPlayOrPauseListener;
    private AudioManager mAM;
    private View mAnchor;
    private int mAnimStyle;
    private Context mContext;
    private TextView mCurrentTime;
    private boolean mDragging;
    private long mDuration;
    private TextView mEndTime;
    private TextView mFileName;
    private boolean mFromXml;
    protected final Handler mHandler;
    private OnHiddenListener mHiddenListener;
    private OutlineTextView mInfoView;
    private boolean mInstantSeeking;
    private OnNoFastForwordListener mNoFastForwordListener;
    private ImageButton mPauseButton;
    private final View.OnClickListener mPauseListener;
    protected IMediaPlayerControl mPlayer;
    private SeekBar mProgress;
    private View mRoot;
    private final SeekBar.OnSeekBarChangeListener mSeekListener;
    private boolean mShowing;
    private OnShownListener mShownListener;
    private String mTitle;
    private PopupWindow mWindow;
    public ListPopupWindow popupWindow;
    private boolean seekBarEnable;
    private final String[] speedItem;
    private ImageView startPoint;
    private TextView tvSpeed;

    /* loaded from: 人卫/classes09.dex */
    public interface IMediaControllerCallback {
        void handleMediaControllerFullScreenClick(View view);

        void handleMediaControllerSpeedClick(String str);
    }

    /* loaded from: 人卫/classes09.dex */
    public interface IMediaPlayOrPauseListener {
        void pause();

        void play();
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnHiddenListener {
        void onHidden();
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnNoFastForwordListener {
        void onShowMessage(boolean z);
    }

    /* loaded from: 人卫/classes09.dex */
    public interface OnShownListener {
        void onShown();
    }

    public MediaController(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.seekBarEnable = true;
        this.mInstantSeeking = false;
        this.mFromXml = false;
        this.speedItem = new String[]{"0.75", "1", "1.25", "1.75", "2"};
        this.mHandler = new Handler() { // from class: cn.dolit.media.player.widget.MediaController.3
            public transient NBSRunnableInspect nbsHandler = new NBSRunnableInspect();

            {
                // MediaController.this = this;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                NBSRunnableInspect nBSRunnableInspect = this.nbsHandler;
                if (nBSRunnableInspect != null) {
                    nBSRunnableInspect.preRunMethod();
                }
                int i = message.what;
                if (i == 1) {
                    MediaController.this.hide();
                } else if (i == 2) {
                    long progress = MediaController.this.setProgress();
                    if (!MediaController.this.mDragging && MediaController.this.mShowing) {
                        sendMessageDelayed(obtainMessage(2), 1000 - (progress % 1000));
                        MediaController.this.updatePausePlay();
                    }
                } else if (i == 3 && MediaController.this.popupWindow != null && MediaController.this.popupWindow.isShowing()) {
                    MediaController.this.popupWindow.dismiss();
                }
                NBSRunnableInspect nBSRunnableInspect2 = this.nbsHandler;
                if (nBSRunnableInspect2 != null) {
                    nBSRunnableInspect2.sufRunMethod();
                }
            }
        };
        this.mPauseListener = new View.OnClickListener() { // from class: cn.dolit.media.player.widget.MediaController.4
            {
                // MediaController.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NBSActionInstrumentation.onClickEventEnter(view);
                MediaController.this.doPauseResume();
                MediaController.this.show(3000);
                NBSActionInstrumentation.onClickEventExit();
                SensorsDataAutoTrackHelper.trackViewOnClick(view);
            }
        };
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: cn.dolit.media.player.widget.MediaController.5
            {
                // MediaController.this = this;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                MediaController.this.mDragging = true;
                MediaController.this.show(3600000);
                MediaController.this.mHandler.removeMessages(2);
                if (MediaController.this.mInstantSeeking) {
                    MediaController.this.mAM.setStreamMute(3, true);
                }
                if (MediaController.this.mInfoView != null) {
                    MediaController.this.mInfoView.setText("");
                    MediaController.this.mInfoView.setVisibility(VISIBLE);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    long j = (MediaController.this.mDuration * i) / 1000;
                    String generateTime = MediaController.generateTime(j);
                    if (MediaController.this.mInstantSeeking) {
                        MediaController.this.mPlayer.seekTo(j);
                    }
                    if (MediaController.this.mInfoView != null) {
                        MediaController.this.mInfoView.setText(generateTime);
                    }
                    if (MediaController.this.mCurrentTime != null) {
                        MediaController.this.mCurrentTime.setText(generateTime);
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!MediaController.this.mInstantSeeking) {
                    MediaController.this.mPlayer.seekTo((MediaController.this.mDuration * seekBar.getProgress()) / 1000);
                }
                if (MediaController.this.mInfoView != null) {
                    MediaController.this.mInfoView.setText("");
                    MediaController.this.mInfoView.setVisibility(GONE);
                }
                MediaController.this.show(3000);
                MediaController.this.mHandler.removeMessages(2);
                MediaController.this.mAM.setStreamMute(3, false);
                MediaController.this.mDragging = false;
                MediaController.this.mHandler.sendEmptyMessageDelayed(2, 1000L);
                SensorsDataAutoTrackHelper.trackViewOnClick(seekBar);
            }
        };
        this.mRoot = this;
        this.mFromXml = true;
        initController(context);
    }

    public MediaController(Context context) {
        super(context);
        this.seekBarEnable = true;
        this.mInstantSeeking = false;
        this.mFromXml = false;
        this.speedItem = new String[]{"0.75", "1", "1.25", "1.75", "2"};
        this.mHandler = new Handler() { // from class: cn.dolit.media.player.widget.MediaController.3
            public transient NBSRunnableInspect nbsHandler = new NBSRunnableInspect();

            {
                // MediaController.this = this;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                NBSRunnableInspect nBSRunnableInspect = this.nbsHandler;
                if (nBSRunnableInspect != null) {
                    nBSRunnableInspect.preRunMethod();
                }
                int i = message.what;
                if (i == 1) {
                    MediaController.this.hide();
                } else if (i == 2) {
                    long progress = MediaController.this.setProgress();
                    if (!MediaController.this.mDragging && MediaController.this.mShowing) {
                        sendMessageDelayed(obtainMessage(2), 1000 - (progress % 1000));
                        MediaController.this.updatePausePlay();
                    }
                } else if (i == 3 && MediaController.this.popupWindow != null && MediaController.this.popupWindow.isShowing()) {
                    MediaController.this.popupWindow.dismiss();
                }
                NBSRunnableInspect nBSRunnableInspect2 = this.nbsHandler;
                if (nBSRunnableInspect2 != null) {
                    nBSRunnableInspect2.sufRunMethod();
                }
            }
        };
        this.mPauseListener = new View.OnClickListener() { // from class: cn.dolit.media.player.widget.MediaController.4
            {
                // MediaController.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NBSActionInstrumentation.onClickEventEnter(view);
                MediaController.this.doPauseResume();
                MediaController.this.show(3000);
                NBSActionInstrumentation.onClickEventExit();
                SensorsDataAutoTrackHelper.trackViewOnClick(view);
            }
        };
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: cn.dolit.media.player.widget.MediaController.5
            {
                // MediaController.this = this;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                MediaController.this.mDragging = true;
                MediaController.this.show(3600000);
                MediaController.this.mHandler.removeMessages(2);
                if (MediaController.this.mInstantSeeking) {
                    MediaController.this.mAM.setStreamMute(3, true);
                }
                if (MediaController.this.mInfoView != null) {
                    MediaController.this.mInfoView.setText("");
                    MediaController.this.mInfoView.setVisibility(VISIBLE);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    long j = (MediaController.this.mDuration * i) / 1000;
                    String generateTime = MediaController.generateTime(j);
                    if (MediaController.this.mInstantSeeking) {
                        MediaController.this.mPlayer.seekTo(j);
                    }
                    if (MediaController.this.mInfoView != null) {
                        MediaController.this.mInfoView.setText(generateTime);
                    }
                    if (MediaController.this.mCurrentTime != null) {
                        MediaController.this.mCurrentTime.setText(generateTime);
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!MediaController.this.mInstantSeeking) {
                    MediaController.this.mPlayer.seekTo((MediaController.this.mDuration * seekBar.getProgress()) / 1000);
                }
                if (MediaController.this.mInfoView != null) {
                    MediaController.this.mInfoView.setText("");
                    MediaController.this.mInfoView.setVisibility(GONE);
                }
                MediaController.this.show(3000);
                MediaController.this.mHandler.removeMessages(2);
                MediaController.this.mAM.setStreamMute(3, false);
                MediaController.this.mDragging = false;
                MediaController.this.mHandler.sendEmptyMessageDelayed(2, 1000L);
                SensorsDataAutoTrackHelper.trackViewOnClick(seekBar);
            }
        };
        if (this.mFromXml || !initController(context)) {
            return;
        }
        initFloatingWindow();
    }

    public void setCallback(IMediaControllerCallback iMediaControllerCallback) {
        this.callback = iMediaControllerCallback;
    }

    public void setiMediaPlayOrPauseListener(IMediaPlayOrPauseListener iMediaPlayOrPauseListener) {
        this.iMediaPlayOrPauseListener = iMediaPlayOrPauseListener;
    }

    private boolean initController(Context context) {
        this.mContext = context;
        this.mAM = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return true;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        View view = this.mRoot;
        if (view != null) {
            initControllerView(view);
        }
    }

    private void initFloatingWindow() {
        PopupWindow popupWindow = new PopupWindow(this.mContext);
        this.mWindow = popupWindow;
        popupWindow.setFocusable(false);
        this.mWindow.setBackgroundDrawable(null);
        this.mWindow.setOutsideTouchable(true);
        this.mAnimStyle = 16973824;
    }

    public void setAnchorView(View view) {
        this.mAnchor = view;
        if (!this.mFromXml) {
            removeAllViews();
            View makeControllerView = makeControllerView();
            this.mRoot = makeControllerView;
            this.mWindow.setContentView(makeControllerView);
            this.mWindow.setWidth(-1);
            this.mWindow.setHeight(-2);
        }
        initControllerView(this.mRoot);
    }

    public View getAnchor() {
        return this.mAnchor;
    }

    protected View makeControllerView() {
        return ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mediacontroller, this);
    }

    private void initControllerView(View view) {
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.mediacontroller_play_pause);
        this.mPauseButton = imageButton;
        if (imageButton != null) {
            imageButton.requestFocus();
            this.mPauseButton.setOnClickListener(this.mPauseListener);
        }
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.mediacontroller_seekbar);
        this.mProgress = seekBar;
        if (seekBar != null) {
            if (seekBar instanceof SeekBar) {
                seekBar.setOnSeekBarChangeListener(this.mSeekListener);
                seekBar.setOnTouchListener(new View.OnTouchListener() { // from class: cn.dolit.media.player.widget.MediaController.1
                    {
                        // MediaController.this = this;
                    }

                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view2, MotionEvent motionEvent) {
                        if (MediaController.this.mNoFastForwordListener != null) {
                            MediaController.this.mNoFastForwordListener.onShowMessage(!MediaController.this.seekBarEnable);
                        }
                        return !MediaController.this.seekBarEnable;
                    }
                });
                seekBar.setThumbOffset(1);
            }
            this.mProgress.setMax(1000);
        }
        this.mEndTime = (TextView) view.findViewById(R.id.mediacontroller_time_total);
        this.mCurrentTime = (TextView) view.findViewById(R.id.mediacontroller_time_current);
        TextView textView = (TextView) view.findViewById(R.id.mediacontroller_right_text_view);
        this.mFileName = textView;
        if (textView != null) {
            textView.setText(this.mTitle);
        }
        TextView textView2 = (TextView) view.findViewById(R.id.tv_media_controller_right_speed);
        this.tvSpeed = textView2;
        textView2.setOnClickListener(this);
        this.startPoint = (ImageView) view.findViewById(R.id.start_point);
        this.endPoint = (ImageView) view.findViewById(R.id.end_point);
    }

    public void setStartPoint(float f) {
        if (f > this.startPoint.getLayoutParams().width / 2) {
            f -= this.startPoint.getLayoutParams().width / 2;
        }
        ((RelativeLayout.LayoutParams) this.startPoint.getLayoutParams()).leftMargin = (int) f;
        this.startPoint.setVisibility(VISIBLE);
    }

    public void setEndPoint(float f) {
        if (f > this.startPoint.getLayoutParams().width / 2) {
            f -= this.startPoint.getLayoutParams().width / 2;
        }
        ((RelativeLayout.LayoutParams) this.endPoint.getLayoutParams()).leftMargin = (int) f;
        this.endPoint.setVisibility(VISIBLE);
    }

    public void setMediaPlayer(IMediaPlayerControl iMediaPlayerControl) {
        this.mPlayer = iMediaPlayerControl;
        updatePausePlay();
    }

    public void setInstantSeeking(boolean z) {
        this.mInstantSeeking = z;
    }

    public void show() {
        show(3000);
    }

    public void setFileName(String str) {
        this.mTitle = str;
        TextView textView = this.mFileName;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setInfoView(OutlineTextView outlineTextView) {
        this.mInfoView = outlineTextView;
    }

    private void disableUnsupportedButtons() {
        try {
            if (this.mPauseButton == null || this.mPlayer.canPause()) {
                return;
            }
            this.mPauseButton.setEnabled(false);
        } catch (IncompatibleClassChangeError unused) {
        }
    }

    public void setAnimationStyle(int i) {
        this.mAnimStyle = i;
    }

    public void show(int i) {
        View view;
        if (!this.mShowing && (view = this.mAnchor) != null && view.getWindowToken() != null) {
            if (Build.VERSION.SDK_INT >= 14) {
                this.mAnchor.setSystemUiVisibility(0);
            }
            ImageButton imageButton = this.mPauseButton;
            if (imageButton != null) {
                imageButton.requestFocus();
            }
            disableUnsupportedButtons();
            if (this.mFromXml) {
                setVisibility(VISIBLE);
            } else {
                int[] iArr = new int[2];
                this.mAnchor.getLocationOnScreen(iArr);
                Rect rect = new Rect(iArr[0], iArr[1], iArr[0] + this.mAnchor.getWidth(), iArr[1] + this.mAnchor.getHeight());
                this.mWindow.getContentView().measure(0, 0);
                int measuredHeight = this.mWindow.getContentView().getMeasuredHeight();
                this.mWindow.setAnimationStyle(this.mAnimStyle);
                this.mWindow.showAtLocation(this.mAnchor, 48, rect.left, rect.bottom - measuredHeight);
            }
            this.mShowing = true;
            OnShownListener onShownListener = this.mShownListener;
            if (onShownListener != null) {
                onShownListener.onShown();
            }
        }
        updatePausePlay();
        this.mHandler.sendEmptyMessage(2);
        if (i != 0) {
            this.mHandler.removeMessages(1);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1), i);
        }
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void hide() {
        if (this.mAnchor != null && this.mShowing) {
            if (Build.VERSION.SDK_INT >= 14) {
                this.mAnchor.setSystemUiVisibility(4);
            }
            hideDirectly();
        }
    }

    public void hideDirectly() {
        if (this.mShowing) {
            try {
                this.mHandler.removeMessages(2);
                if (this.mFromXml) {
                    setVisibility(GONE);
                } else {
                    this.mWindow.dismiss();
                }
                if (this.popupWindow != null && this.popupWindow.isShowing()) {
                    this.popupWindow.dismiss();
                }
            } catch (IllegalArgumentException unused) {
                DebugLog.d(TAG, "MediaController already removed");
            }
            this.mShowing = false;
            OnHiddenListener onHiddenListener = this.mHiddenListener;
            if (onHiddenListener != null) {
                onHiddenListener.onHidden();
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        IMediaControllerCallback iMediaControllerCallback;
        NBSActionInstrumentation.onClickEventEnter(view);
        if (view == this.tvSpeed && (iMediaControllerCallback = this.callback) != null) {
            iMediaControllerCallback.handleMediaControllerFullScreenClick(view);
        }
        NBSActionInstrumentation.onClickEventExit();
        SensorsDataAutoTrackHelper.trackViewOnClick(view);
    }

    public void showSpeedView(final View view, View view2) {
        if (this.popupWindow == null) {
            this.popupWindow = new ListPopupWindow(getContext());
            this.popupWindow.setAdapter(new ArrayAdapter(getContext(), R.layout.mediacontroller_speed, this.speedItem));
            this.popupWindow.setWidth(DisplayUtil.dip2px(getContext(), 80.0f));
            this.popupWindow.setHeight(DisplayUtil.dip2px(getContext(), this.speedItem.length * 35));
            this.popupWindow.setHorizontalOffset(DisplayUtil.dip2px(getContext(), -40.0f));
            this.popupWindow.setVerticalOffset(DisplayUtil.dip2px(getContext(), (this.speedItem.length * (-35)) - 40));
            this.popupWindow.setBackgroundDrawable(new ColorDrawable(2130706432));
            this.popupWindow.setModal(true);
            this.popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: cn.dolit.media.player.widget.MediaController.2
                {
                    // MediaController.this = this;
                }

                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view3, int i, long j) {
                    NBSActionInstrumentation.onItemClickEnter(view3, i, this);
                    if (MediaController.this.callback != null) {
                        String str = MediaController.this.speedItem[i];
                        MediaController.this.callback.handleMediaControllerSpeedClick(str);
                        MediaController.this.popupWindow.dismiss();
                        MediaController.this.mHandler.removeMessages(3);
                        View view4 = view;
                        if (view4 != null) {
                            ((TextView) view4).setText(String.format("%sX", str));
                        }
                        NBSActionInstrumentation.onItemClickExit();
                        SensorsDataAutoTrackHelper.trackListView(adapterView, view3, i);
                        return;
                    }
                    NBSActionInstrumentation.onItemClickExit();
                    SensorsDataAutoTrackHelper.trackListView(adapterView, view3, i);
                }
            });
            if (Build.VERSION.SDK_INT >= 19) {
                this.popupWindow.setDropDownGravity(GravityCompat.END);
            }
        }
        this.popupWindow.setAnchorView(view2);
        this.popupWindow.show();
        this.mHandler.sendEmptyMessageDelayed(3, 5000L);
    }

    public void setOnShownListener(OnShownListener onShownListener) {
        this.mShownListener = onShownListener;
    }

    public void setOnNoFastForwordListener(OnNoFastForwordListener onNoFastForwordListener) {
        this.mNoFastForwordListener = onNoFastForwordListener;
    }

    public void setOnHiddenListener(OnHiddenListener onHiddenListener) {
        this.mHiddenListener = onHiddenListener;
    }

    public long setProgress() {
        IMediaPlayerControl iMediaPlayerControl = this.mPlayer;
        if (iMediaPlayerControl == null || this.mDragging) {
            return 0L;
        }
        int currentPosition = iMediaPlayerControl.getCurrentPosition();
        int duration = this.mPlayer.getDuration();
        SeekBar seekBar = this.mProgress;
        if (seekBar != null) {
            if (duration > 0) {
                seekBar.setProgress((int) ((currentPosition * 1000) / duration));
            }
            this.mProgress.setSecondaryProgress(this.mPlayer.getBufferPercentage() * 10);
        }
        long j = duration;
        this.mDuration = j;
        TextView textView = this.mEndTime;
        if (textView != null) {
            textView.setText(generateTime(j));
        }
        TextView textView2 = this.mCurrentTime;
        if (textView2 != null) {
            textView2.setText(generateTime(currentPosition));
        }
        return currentPosition;
    }

    public static String generateTime(long j) {
        int i = (int) (j / 1000);
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        int i4 = i / 3600;
        return i4 > 0 ? String.format(Locale.US, "%02d:%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(i2)) : String.format(Locale.US, "%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        show(3000);
        return true;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent motionEvent) {
        show(3000);
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getRepeatCount() == 0 && (keyCode == 79 || keyCode == 85 || keyCode == 62)) {
            doPauseResume();
            show(3000);
            ImageButton imageButton = this.mPauseButton;
            if (imageButton != null) {
                imageButton.requestFocus();
            }
            return true;
        } else if (keyCode == 86) {
            if (this.mPlayer.isPlaying()) {
                this.mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == 4 || keyCode == 82) {
            hide();
            return true;
        } else {
            show(3000);
            return super.dispatchKeyEvent(keyEvent);
        }
    }

    public void updatePausePlay() {
        if (this.mRoot == null || this.mPauseButton == null) {
            return;
        }
        if (this.mPlayer.isPlaying()) {
            this.mPauseButton.setImageResource(R.drawable.mediacontroller_pause_button);
        } else {
            this.mPauseButton.setImageResource(R.drawable.mediacontroller_play_button);
        }
    }

    public void doPauseResume() {
        if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
            IMediaPlayOrPauseListener iMediaPlayOrPauseListener = this.iMediaPlayOrPauseListener;
            if (iMediaPlayOrPauseListener != null) {
                iMediaPlayOrPauseListener.pause();
            }
        } else {
            this.mPlayer.start();
            IMediaPlayOrPauseListener iMediaPlayOrPauseListener2 = this.iMediaPlayOrPauseListener;
            if (iMediaPlayOrPauseListener2 != null) {
                iMediaPlayOrPauseListener2.play();
            }
        }
        updatePausePlay();
    }

    public void setSeekBarEnable(boolean z) {
        this.seekBarEnable = z;
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        ImageButton imageButton = this.mPauseButton;
        if (imageButton != null) {
            imageButton.setEnabled(z);
        }
        SeekBar seekBar = this.mProgress;
        if (seekBar != null) {
            seekBar.setEnabled(z);
        }
        disableUnsupportedButtons();
        super.setEnabled(z);
    }
}
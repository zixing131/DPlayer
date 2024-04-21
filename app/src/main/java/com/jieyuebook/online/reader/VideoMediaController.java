package com.jieyuebook.online.reader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import cn.dolit.media.player.widget.MediaController;
import cn.dolit.media.player.widget.NBSActionInstrumentation;
import cn.dolit.media.player.widget.SensorsDataAutoTrackHelper;

import com.networkbench.agent.impl.instrumentation.NBSInstrumented;
import com.zixing.dplayer.R;

@NBSInstrumented
/* loaded from: 人卫/classes13.dex */
public class VideoMediaController extends MediaController {
    private IMediaControllerCatalogCallback catalogCallback;
    private ImageView endPoint;
    private ImageView iv_video_hv;
    private Context mContext;
    private View mMyRoot;
    private SeekBar mSeekBar;
    private ImageView startPoint;

    /* loaded from: 人卫/classes13.dex */
    public interface IMediaControllerCatalogCallback {
        void screenOritentation(View view);
    }

    public SeekBar getmSeekBar() {
        return this.mSeekBar;
    }

    public void setCatalogCallback(IMediaControllerCatalogCallback iMediaControllerCatalogCallback) {
        this.catalogCallback = iMediaControllerCatalogCallback;
    }

    public VideoMediaController(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.mMyRoot = this;
    }

    public VideoMediaController(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setScreenOritentation(IMediaControllerCatalogCallback iMediaControllerCatalogCallback) {
        this.catalogCallback = iMediaControllerCatalogCallback;
    }

    private void initMyControllerView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_video_hv);
        this.iv_video_hv = imageView;
        imageView.setOnClickListener(this);
        this.mSeekBar = (SeekBar) view.findViewById(R.id.mediacontroller_seekbar);
        this.startPoint = (ImageView) view.findViewById(R.id.start_point);
        this.endPoint = (ImageView) view.findViewById(R.id.end_point);
    }

    @Override // cn.dolit.media.player.widget.MediaController
    protected View makeControllerView() {
        return ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.av_reader_video_mediacontroller, this);
    }

    @Override // cn.dolit.media.player.widget.MediaController, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        View view = this.mMyRoot;
        if (view != null) {
            initMyControllerView(view);
        }
    }

    @Override // cn.dolit.media.player.widget.MediaController
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        initMyControllerView(this);
    }

    @Override // cn.dolit.media.player.widget.MediaController, android.view.View.OnClickListener
    public void onClick(View view) {
        IMediaControllerCatalogCallback iMediaControllerCatalogCallback;
        NBSActionInstrumentation.onClickEventEnter(view);
        super.onClick(view);
        if (view == this.iv_video_hv && (iMediaControllerCatalogCallback = this.catalogCallback) != null) {
            iMediaControllerCatalogCallback.screenOritentation(view);
        }
        NBSActionInstrumentation.onClickEventExit();
        SensorsDataAutoTrackHelper.trackViewOnClick(view);
    }

    public void clearStartAndEndPoint() {
        ImageView imageView = this.startPoint;
        if (imageView != null) {
            imageView.setVisibility(GONE);
        }
        ImageView imageView2 = this.endPoint;
        if (imageView2 != null) {
            imageView2.setVisibility(GONE);
        }
    }
}
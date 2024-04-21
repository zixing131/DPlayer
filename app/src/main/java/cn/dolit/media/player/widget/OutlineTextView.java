package cn.dolit.media.player.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/* loaded from: 人卫/classes09.dex */
public class OutlineTextView extends androidx.appcompat.widget.AppCompatTextView {
    private int mAscent;
    private int mBorderColor;
    private float mBorderSize;
    private int mColor;
    private final boolean mIncludePad;
    private final float mSpacingAdd;
    private final float mSpacingMult;
    private String mText;
    private TextPaint mTextPaint;
    private TextPaint mTextPaintOutline;

    public OutlineTextView(Context context) {
        super(context);
        this.mText = "";
        this.mAscent = 0;
        this.mSpacingMult = 1.0f;
        this.mSpacingAdd = 0.0f;
        this.mIncludePad = true;
        initPaint();
    }

    public OutlineTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mText = "";
        this.mAscent = 0;
        this.mSpacingMult = 1.0f;
        this.mSpacingAdd = 0.0f;
        this.mIncludePad = true;
        initPaint();
    }

    public OutlineTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mText = "";
        this.mAscent = 0;
        this.mSpacingMult = 1.0f;
        this.mSpacingAdd = 0.0f;
        this.mIncludePad = true;
        initPaint();
    }

    private void initPaint() {
        TextPaint textPaint = new TextPaint();
        this.mTextPaint = textPaint;
        textPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(getTextSize());
        this.mTextPaint.setColor(this.mColor);
        this.mTextPaint.setStyle(Paint.Style.FILL);
        this.mTextPaint.setTypeface(getTypeface());
        TextPaint textPaint2 = new TextPaint();
        this.mTextPaintOutline = textPaint2;
        textPaint2.setAntiAlias(true);
        this.mTextPaintOutline.setTextSize(getTextSize());
        this.mTextPaintOutline.setColor(this.mBorderColor);
        this.mTextPaintOutline.setStyle(Paint.Style.STROKE);
        this.mTextPaintOutline.setTypeface(getTypeface());
        this.mTextPaintOutline.setStrokeWidth(this.mBorderSize);
    }

    public void setText(String str) {
        super.setText((CharSequence) str);
        this.mText = str;
        requestLayout();
        invalidate();
    }

    @Override // android.widget.TextView
    public void setTextSize(float f) {
        super.setTextSize(f);
        requestLayout();
        invalidate();
        initPaint();
    }

    @Override // android.widget.TextView
    public void setTextColor(int i) {
        super.setTextColor(i);
        this.mColor = i;
        invalidate();
        initPaint();
    }

    @Override // android.widget.TextView
    public void setShadowLayer(float f, float f2, float f3, int i) {
        super.setShadowLayer(f, f2, f3, i);
        this.mBorderSize = f;
        this.mBorderColor = i;
        requestLayout();
        invalidate();
        initPaint();
    }

    @Override // android.widget.TextView
    public void setTypeface(Typeface typeface, int i) {
        super.setTypeface(typeface, i);
        requestLayout();
        invalidate();
        initPaint();
    }

    @Override // android.widget.TextView
    public void setTypeface(Typeface typeface) {
        super.setTypeface(typeface);
        requestLayout();
        invalidate();
        initPaint();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        new StaticLayout(getText(), this.mTextPaintOutline, getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true).draw(canvas);
        new StaticLayout(getText(), this.mTextPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true).draw(canvas);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        StaticLayout staticLayout = new StaticLayout(getText(), this.mTextPaintOutline, measureWidth(i), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        int i3 = (int) ((this.mBorderSize * 2.0f) + 1.0f);
        setMeasuredDimension(measureWidth(i) + i3, (measureHeight(i2) * staticLayout.getLineCount()) + i3);
    }

    private int measureWidth(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        int measureText = ((int) this.mTextPaintOutline.measureText(this.mText)) + getPaddingLeft() + getPaddingRight();
        return mode == Integer.MIN_VALUE ? Math.min(measureText, size) : measureText;
    }

    private int measureHeight(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int ascent = (int) this.mTextPaintOutline.ascent();
        this.mAscent = ascent;
        if (mode == 1073741824) {
            return size;
        }
        int descent = ((int) ((-ascent) + this.mTextPaintOutline.descent())) + getPaddingTop() + getPaddingBottom();
        return mode == Integer.MIN_VALUE ? Math.min(descent, size) : descent;
    }
}
package com.codebyashish;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.codebyashish.customswitchbutton.R.styleable;
import com.codebyashish.customswitchbutton.R.attr;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class CustomSwitchButton extends View{
    protected int width;
    protected int height;
    protected int speed = 20;
    protected int textSize = 0;
    protected int fontFamilyId = 0;
    protected String textOn;
    protected String textOff;
    protected boolean checked = false;
    private float border = 0.0F;
    private float strokeBorder;
    private float innerStrokeBorder;
    private float circleDiameter;
    private float innercircleDiameter;
    private Path path;
    private Path bgPath;
    private Rect textBounds = new Rect();
    private float circleX = 0.0F;
    private float circleY = 0.0F;
    private float textX = 0.0F;
    private float textY = 0.0F;
    private RectF outerRect;
    private Paint mPaint;
    private Paint mPaintCircle;
    private Paint bgPaint;
    private Paint textPaint;
    private ValueAnimator animator;
    private boolean isTurningOn = false;
    private boolean isTurningOff = false;
    private boolean isOn = false;
    private boolean isFirstTime = true;
    private boolean showText = false;
    private int strokeColorOnSwitchOn = Color.parseColor("#FF6200EE");
    private int strokeColorOnSwitchOff = Color.parseColor("#FF6200EE");
    private int textColorOnSwitchOn = Color.parseColor("#FFFFFF");
    private int textColorOnSwitchOff = Color.parseColor("#FF6200EE");
    private int bgColorOnSwitchOn = Color.parseColor("#FF6200EE");
    private int bgColorOnSwitchOff = Color.parseColor("#FFFFFF");
    private int thumbColorOnSwitchOn = Color.parseColor("#FFFFFF");
    private int thumbColorOnSwitchOff = Color.parseColor("#FF6200EE");
    private CustomSwitchButton.OnStatusChangedListener onStatusChangedListener;

    public CustomSwitchButton(Context context) {
        super(context);
        new TextView(context);
        this.outerRect = new RectF();
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.strokeColorOnSwitchOn);
        this.mPaint.setAntiAlias(true);
        this.mPaintCircle = new Paint();
        this.mPaintCircle.setStyle(Paint.Style.FILL);
        this.mPaintCircle.setColor(this.thumbColorOnSwitchOff);
        this.mPaintCircle.setAntiAlias(true);
        this.bgPaint = new Paint();
        this.bgPaint.setStyle(Paint.Style.FILL);
        this.bgPaint.setColor(this.bgColorOnSwitchOn);
        this.bgPaint.setAntiAlias(true);
        this.textPaint = new Paint();
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setColor(this.bgColorOnSwitchOn);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textOn = "ON";
        this.textOff = "OFF";
        this.path = new Path();
        this.bgPath = new Path();
    }

    public CustomSwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Resources r = context.getResources();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, styleable.custom_switch_button, 0, 0);

        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(attr.themeColor, typedValue, true);
            int color = typedValue.data;
            this.speed = a.getInteger(styleable.custom_switch_button_speed, 20);
            this.textOn = a.getString(styleable.custom_switch_button_textOn);
            this.textOff = a.getString(styleable.custom_switch_button_textOff);
            this.showText = a.getBoolean(styleable.custom_switch_button_showText, false);
            this.checked = a.getBoolean(styleable.custom_switch_button_android_checked, false);
            this.fontFamilyId = a.getResourceId(styleable.custom_switch_button_android_fontFamily, 0);
            this.border = (float)a.getDimensionPixelSize(styleable.custom_switch_button_strokeWidth, 0);
            this.textSize = a.getDimensionPixelSize(styleable.custom_switch_button_android_textSize, 0);
            this.strokeColorOnSwitchOn = a.getColor(styleable.custom_switch_button_strokeColorOnSwitchOn, color);
            this.strokeColorOnSwitchOff = a.getColor(styleable.custom_switch_button_strokeColorOnSwitchOff, color);
            this.bgColorOnSwitchOn = a.getColor(styleable.custom_switch_button_backgroundColorOnSwitchOn, color);
            this.bgColorOnSwitchOff = a.getColor(styleable.custom_switch_button_backgroundColorOnSwitchOff, Color.parseColor("#FFFFFF"));
            this.textColorOnSwitchOn = a.getColor(styleable.custom_switch_button_textColorOnSwitchOn, Color.parseColor("#FFFFFF"));
            this.textColorOnSwitchOff = a.getColor(styleable.custom_switch_button_textColorOnSwitchOff, color);
            this.thumbColorOnSwitchOn = a.getColor(styleable.custom_switch_button_thumbColorOnSwitchOn, Color.parseColor("#FFFFFF"));
            this.thumbColorOnSwitchOff = a.getColor(styleable.custom_switch_button_thumbColorOnSwitchOff, color);
        } finally {
            a.recycle();
        }

        this.outerRect = new RectF();
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        if (this.checked) {
            this.mPaint.setColor(this.strokeColorOnSwitchOn);
        } else {
            this.mPaint.setColor(this.strokeColorOnSwitchOff);
        }

        this.mPaint.setAntiAlias(true);
        this.mPaintCircle = new Paint();
        this.mPaintCircle.setStyle(Paint.Style.FILL);
        if (this.checked) {
            this.mPaintCircle.setColor(this.thumbColorOnSwitchOn);
        } else {
            this.mPaintCircle.setColor(this.thumbColorOnSwitchOff);
        }

        this.mPaintCircle.setAntiAlias(true);
        this.bgPaint = new Paint();
        this.bgPaint.setStyle(Paint.Style.FILL);
        if (this.checked) {
            this.bgPaint.setColor(this.bgColorOnSwitchOn);
        } else {
            this.bgPaint.setColor(this.bgColorOnSwitchOff);
        }

        this.bgPaint.setAntiAlias(true);
        this.textPaint = new Paint();
        this.textPaint.setStyle(Paint.Style.FILL);
        if (this.checked) {
            this.textPaint.setColor(this.bgColorOnSwitchOn);
        } else {
            this.textPaint.setColor(this.bgColorOnSwitchOff);
        }

        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        float px;
        if (this.textSize > 0) {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (float)this.textSize, r.getDisplayMetrics());
            this.textPaint.setTextSize(px);
        } else {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 50.0F, r.getDisplayMetrics());
            this.textPaint.setTextSize(px);
        }

        if (this.border > 0.0F) {
            this.border = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.border, r.getDisplayMetrics());
        }

        if (this.fontFamilyId > 0) {
            this.textPaint.setTypeface(ResourcesCompat.getFont(this.getContext(), this.fontFamilyId));
        }

        if (null == this.textOn) {
            this.textOn = "ON";
        }

        if (null == this.textOff) {
            this.textOff = "OFF";
        }

        this.path = new Path();
        this.bgPath = new Path();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.width = this.getWidth();
        this.height = this.getHeight();
        if (0 != this.width || 0 != this.height) {
            this.init();
            float tempSpeed = this.innerStrokeBorder * ((float)this.speed / 100.0F);
            if (this.isTurningOn) {
                this.mPaint.setColor(this.strokeColorOnSwitchOn);
                this.mPaintCircle.setColor(this.thumbColorOnSwitchOn);
                this.bgPaint.setColor(this.bgColorOnSwitchOn);
                this.textPaint.setColor(this.textColorOnSwitchOn);
                if (this.circleX + tempSpeed + this.innercircleDiameter / 2.0F < (float)this.width - this.innerStrokeBorder / 2.0F) {
                    this.circleX += tempSpeed;
                } else {
                    this.turnOffAnimation();
                    this.isOn = true;
                    this.isTurningOn = false;
                    this.isTurningOff = false;
                    this.circleX = (float)this.width - (this.innerStrokeBorder / 2.0F + this.innercircleDiameter / 2.0F);
                    if (null != this.onStatusChangedListener) {
                        this.onStatusChangedListener.onStatusChanged(this.isOn);
                    }
                }

                this.textX = this.strokeBorder / 2.0F + this.circleDiameter / 2.0F + ((float)this.width - (this.strokeBorder + this.circleDiameter + this.circleDiameter / 2.0F)) / 2.0F;
                this.textY = (float)this.height / 2.0F;
            } else if (this.isTurningOff) {
                this.mPaint.setColor(this.strokeColorOnSwitchOff);
                this.mPaintCircle.setColor(this.thumbColorOnSwitchOff);
                this.bgPaint.setColor(this.bgColorOnSwitchOff);
                this.textPaint.setColor(this.textColorOnSwitchOff);
                if (this.circleX - tempSpeed - this.innercircleDiameter / 2.0F > this.innerStrokeBorder / 2.0F) {
                    this.circleX -= tempSpeed;
                } else {
                    this.turnOffAnimation();
                    this.isOn = false;
                    this.isTurningOn = false;
                    this.isTurningOff = false;
                    this.circleX = this.innerStrokeBorder / 2.0F + this.innercircleDiameter / 2.0F;
                    if (null != this.onStatusChangedListener) {
                        this.onStatusChangedListener.onStatusChanged(this.isOn);
                    }
                }

                this.textX = this.strokeBorder / 2.0F + this.circleDiameter + ((float)this.width - (this.strokeBorder + this.circleDiameter + this.circleDiameter / 2.0F)) / 2.0F;
                this.textY = (float)this.height / 2.0F;
            }

            canvas.drawPath(this.bgPath, this.bgPaint);
            if (this.showText && !this.isTurningOn && !this.isTurningOff) {
                if (this.isOn) {
                    this.textPaint.getTextBounds(this.textOn, 0, this.textOn.length(), this.textBounds);
                    canvas.drawText(this.textOn, this.textX, this.textY + (float)(this.textBounds.height() - this.textBounds.bottom) / 2.0F, this.textPaint);
                } else {
                    this.textPaint.getTextBounds(this.textOff, 0, this.textOff.length(), this.textBounds);
                    canvas.drawText(this.textOff, this.textX, this.textY + (float)(this.textBounds.height() - this.textBounds.bottom) / 2.0F, this.textPaint);
                }
            }

            canvas.drawCircle(this.circleX, this.circleY, this.innercircleDiameter / 2.0F, this.mPaintCircle);
            canvas.drawPath(this.path, this.mPaint);
        }
    }

    private void init() {
        if (this.isFirstTime) {
            if (this.border == 0.0F) {
                if (this.width <= this.height) {
                    this.border = (float)(this.width / 6);
                } else {
                    this.border = (float)(this.height / 6);
                }
            }

            this.strokeBorder = this.border / 4.0F;
            this.innerStrokeBorder = this.strokeBorder * 8.0F;
            this.mPaint.setStrokeWidth(this.strokeBorder);
            this.circleDiameter = (float)this.height - this.strokeBorder;
            this.innercircleDiameter = (float)this.height - this.innerStrokeBorder;
            this.path.reset();
            this.outerRect.set(this.strokeBorder / 2.0F, this.strokeBorder / 2.0F, this.strokeBorder / 2.0F + this.circleDiameter, (float)this.height - this.strokeBorder / 2.0F);
            this.path.addArc(this.outerRect, -180.0F, 90.0F);
            this.outerRect.set((float)this.width - this.strokeBorder / 2.0F - this.circleDiameter, this.strokeBorder / 2.0F, (float)this.width - this.strokeBorder / 2.0F, (float)this.height - this.strokeBorder / 2.0F);
            this.path.arcTo(this.outerRect, -90.0F, 180.0F);
            this.outerRect.set(this.strokeBorder / 2.0F, this.strokeBorder / 2.0F, this.strokeBorder / 2.0F + this.circleDiameter, (float)this.height - this.strokeBorder / 2.0F);
            this.path.arcTo(this.outerRect, 90.0F, 90.0F);
            this.path.close();
            this.isFirstTime = false;
            this.bgPath.reset();
            this.bgPath.addPath(this.path);
            if (!this.checked) {
                this.isOn = false;
                this.mPaint.setColor(this.strokeColorOnSwitchOff);
                this.mPaintCircle.setColor(this.thumbColorOnSwitchOff);
                this.bgPaint.setColor(this.bgColorOnSwitchOff);
                this.textPaint.setColor(this.textColorOnSwitchOff);
                this.circleX = this.innerStrokeBorder / 2.0F + this.innercircleDiameter / 2.0F;
                this.textX = this.strokeBorder / 2.0F + this.circleDiameter + ((float)this.width - (this.strokeBorder + this.circleDiameter + this.circleDiameter / 2.0F)) / 2.0F;
                this.textY = (float)this.height / 2.0F;
            } else {
                this.isOn = true;
                this.mPaint.setColor(this.strokeColorOnSwitchOn);
                this.mPaintCircle.setColor(this.thumbColorOnSwitchOn);
                this.bgPaint.setColor(this.bgColorOnSwitchOn);
                this.textPaint.setColor(this.textColorOnSwitchOn);
                this.circleX = (float)this.width - (this.innerStrokeBorder / 2.0F + this.innercircleDiameter / 2.0F);
                this.textX = this.strokeBorder / 2.0F + this.circleDiameter / 2.0F + ((float)this.width - (this.strokeBorder + this.circleDiameter + this.circleDiameter / 2.0F)) / 2.0F;
                this.textY = (float)this.height / 2.0F;
            }

            this.circleY = (float)this.height / 2.0F;
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case 1:
                if (!this.isOn) {
                    if (!this.isTurningOff && !this.isTurningOn) {
                        this.isTurningOn = true;
                        this.isTurningOff = false;
                        this.turnOnAnimation();
                    } else {
                        this.isTurningOn = true;
                        this.isTurningOff = false;
                    }
                } else if (!this.isTurningOff && !this.isTurningOn) {
                    this.isTurningOff = true;
                    this.isTurningOn = false;
                    this.turnOnAnimation();
                } else {
                    this.isTurningOff = true;
                    this.isTurningOn = false;
                }
            case 0:
            case 2:
            default:
                return true;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 1073741824) {
            this.width = widthSize;
        } else if (widthMode == -2147483648) {
            this.width = Math.min(72, widthSize);
        } else {
            this.width = 72;
        }

        if (heightMode == 1073741824) {
            this.height = heightSize;
        } else if (heightMode == -2147483648) {
            this.height = Math.min(32, heightSize);
        } else {
            this.height = 32;
        }

        if (!this.isFirstTime) {
            this.isFirstTime = true;
            this.border = 0.0F;
            Resources r = this.getContext().getResources();
            if (null != r) {
                float px;
                if (this.height <= this.width) {
                    px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (float)(this.height / 3), r.getDisplayMetrics());
                    this.textPaint.setTextSize(px);
                } else {
                    px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (float)(this.width / 3), r.getDisplayMetrics());
                    this.textPaint.setTextSize(px);
                }
            }
        }

        this.setMeasuredDimension(this.width, this.height);
    }

    private void turnOnAnimation() {
        if (null == this.animator) {
            this.animator = ValueAnimator.ofInt(new int[]{0, 1});
            this.animator.addUpdateListener((animation) -> {
                if (this.isTurningOn || this.isTurningOff) {
                    this.invalidate();
                }

            });
            this.animator.setDuration(1000L);
            this.animator.setRepeatCount(-1);
            this.animator.start();
        } else {
            this.animator.start();
        }

    }

    private void turnOffAnimation() {
        if (null != this.animator) {
            this.animator.cancel();
            this.animator.removeAllUpdateListeners();
            this.animator = null;
        }

    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public void setTextSize(int textSize, int size) {
        this.textSize = size;
        Context c = this.getContext();
        if (null != c) {
            Resources r = c.getResources();
            float px = TypedValue.applyDimension(size, (float)textSize, r.getDisplayMetrics());
            this.textPaint.setTextSize(px);
        }

        this.invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.border = strokeWidth;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void setStrokeWidth(float strokeWidth, int size) {
        Context c = this.getContext();
        if (null != c) {
            this.isFirstTime = true;
            Resources r = c.getResources();
            float px = TypedValue.applyDimension(size, strokeWidth, r.getDisplayMetrics());
            this.border = px;
        }

        this.invalidate();
    }

    public void setFontFamilyId(int fontFamilyId) {
        this.fontFamilyId = fontFamilyId;
        Context c = this.getContext();
        if (null != c && fontFamilyId > 0) {
            this.textPaint.setTypeface(ResourcesCompat.getFont(c, fontFamilyId));
        }

        this.invalidate();
    }

    public void setTextOn(String textOn) {
        this.textOn = textOn;
        this.invalidate();
    }

    public void setTextOff(String textOff) {
        this.textOff = textOff;
        this.invalidate();
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (this.isOn != checked) {
            if (!this.isOn) {
                if (!this.isTurningOff && !this.isTurningOn) {
                    this.isTurningOn = true;
                    this.isTurningOff = false;
                    this.turnOnAnimation();
                } else {
                    this.isTurningOn = true;
                    this.isTurningOff = false;
                }
            } else if (!this.isTurningOff && !this.isTurningOn) {
                this.isTurningOff = true;
                this.isTurningOn = false;
                this.turnOnAnimation();
            } else {
                this.isTurningOff = true;
                this.isTurningOn = false;
            }
        }

    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setStrokeColorOnSwitchOn(int strokeColorOnSwitchOn) {
        this.strokeColorOnSwitchOn = strokeColorOnSwitchOn;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void setStrokeColorOnSwitchOff(int strokeColorOnSwitchOff) {
        this.strokeColorOnSwitchOff = strokeColorOnSwitchOff;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void setTextColorOnSwitchOn(int textColorOnSwitchOn) {
        this.textColorOnSwitchOn = textColorOnSwitchOn;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void setTextColorOnSwitchOff(int textColorOnSwitchOff) {
        this.textColorOnSwitchOff = textColorOnSwitchOff;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void setBackgroundColorOnSwitchOn(int bgColorOnSwitchOn) {
        this.bgColorOnSwitchOn = bgColorOnSwitchOn;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void setBackgroundColorOnSwitchOff(int bgColorOnSwitchOff) {
        this.bgColorOnSwitchOff = bgColorOnSwitchOff;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void setThumbColorOnSwitchOn(int thumbColorOnSwitchOn) {
        this.thumbColorOnSwitchOn = thumbColorOnSwitchOn;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void setThumbColorOnSwitchOff(int thumbColorOnSwitchOff) {
        this.thumbColorOnSwitchOff = thumbColorOnSwitchOff;
        this.isFirstTime = true;
        this.invalidate();
    }

    public void addOnStatusChangedListener(CustomSwitchButton.OnStatusChangedListener onStatusChangedListener) {
        this.onStatusChangedListener = onStatusChangedListener;
    }

    public interface OnStatusChangedListener {
        void onStatusChanged(boolean var1);
    }
}

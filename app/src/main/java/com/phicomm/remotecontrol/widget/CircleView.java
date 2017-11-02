package com.phicomm.remotecontrol.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.util.SettingUtil;

public class CircleView extends View {

    public static final int REFRESH_FLAG = 100;
    private int mCircleColor;
    private int mInnerCircleColor;
    private int mInnerCircleColorPress;
    private int mBackgroundColor;
    private Paint mPaint;
    private int mCenter;
    private int mOutRadius;
    private int mInnerCircleRadius;
    public Dir dir = Dir.UNDEFINE;
    private TouchListener mListentr;
    private boolean mFlag = true; //中间小圆未点击标志

    private Handler mRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH_FLAG) {
                if (dir != Dir.OUT) {
                    if (dir == Dir.CENTER) {
                        mFlag = true;  //抬手后将中间小圆还原
                    }
                    dir = Dir.UNDEFINE;
                    invalidate();
                }
            }
        }
    };

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }

    private void init() {
        mCircleColor = getResources().getColor(R.color.color_272727);
        mInnerCircleColor = getResources().getColor(R.color.core_activity_bg);
        mInnerCircleColorPress = getResources().getColor(R.color.bottom_text_color);
        mBackgroundColor = getResources().getColor(R.color.core_activity_bg);
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);

        mCenter = getWidth() / 2;
        mOutRadius = mCenter - 5;//大圆不能填满父View，否则安卓7.0点击按键后出现边缘橙色阴影
        mInnerCircleRadius = mCenter / 2;
        setOnTouchListener(onTouchListener);
    }

    /**
     * 测量宽度
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 0;

        if (specMode == MeasureSpec.AT_MOST) {
            result = getWidth();
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    /**
     * 测量高度
     *
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 0;

        if (specMode == MeasureSpec.AT_MOST) {

            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    /**
     * 开始绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBackground(canvas);
        drawDirTriangle(canvas, dir);
        if (mFlag) { //点击中间时置为false，不要覆盖橙色
            drawInnerCircle(canvas);
        }
    }

    /**
     * @param canvas
     */
    private void drawDirTriangle(Canvas canvas, Dir dir) {
        switch (dir) {
            case UP:
                drawOnclikColor(canvas, Dir.UP);
                break;
            case DOWN:
                drawOnclikColor(canvas, Dir.DOWN);
                break;
            case LEFT:
                drawOnclikColor(canvas, Dir.LEFT);
                break;
            case RIGHT:
                drawOnclikColor(canvas, Dir.RIGHT);
                break;
            case CENTER:
                drawOnclikColor(canvas, Dir.CENTER);
            default:
                break;
        }
    }


    /**
     * 点击的时候绘制黑色的扇形
     *
     * @param canvas
     * @param dir
     */
    private void drawOnclikColor(Canvas canvas, Dir dir) {
        mPaint.setColor(getResources().getColor(R.color.bottom_text_color));
        mPaint.setAntiAlias(true);//抗锯齿功能
        mPaint.setStyle(Paint.Style.FILL);//设置填充样式
        switch (dir) {
            //canvas.drawArc:startAngle -> 最右为0° 下90° 左180° 上-90或270
            case UP:
                mFlag = true;
                canvas.drawArc(new RectF(mCenter - mOutRadius, mCenter - mOutRadius, mCenter + mOutRadius, mCenter
                        + mOutRadius), 225, 90, true, mPaint);
                break;
            case DOWN:
                mFlag = true;
                canvas.drawArc(new RectF(mCenter - mOutRadius, mCenter - mOutRadius, mCenter + mOutRadius, mCenter
                        + mOutRadius), 45, 90, true, mPaint);
                break;
            case LEFT:
                mFlag = true;
                canvas.drawArc(new RectF(mCenter - mOutRadius, mCenter - mOutRadius, mCenter + mOutRadius, mCenter
                        + mOutRadius), 135, 90, true, mPaint);
                break;
            case RIGHT:
                mFlag = true;
                canvas.drawArc(new RectF(mCenter - mOutRadius, mCenter - mOutRadius, mCenter + mOutRadius, mCenter
                        + mOutRadius), -45, 90, true, mPaint);
                break;
            case CENTER:
                mFlag = false;
                drawPressInnerCircle(canvas);
                break;
            default:
                break;
        }
    }

    private void initBackground(Canvas canvas) {
        clearCanvas(canvas);
        drawBackCircle(canvas);
    }

    /**
     * 绘制中心红色小圆
     *
     * @param canvas
     */
    private void drawInnerCircle(Canvas canvas) {
        mPaint.setColor(mInnerCircleColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenter, mCenter, mInnerCircleRadius, mPaint);
        mPaint.setColor(getResources().getColor(R.color.color_96968D));
        mPaint.setTextSize(sp2px(35));
        mPaint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText("OK", 4 * mCenter / 5, 9 * mCenter / 8, mPaint);
    }

    private void drawPressInnerCircle(Canvas canvas) {
        mPaint.setColor(mInnerCircleColorPress);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenter, mCenter, mInnerCircleRadius, mPaint);
        mPaint.setColor(getResources().getColor(R.color.white_normal));
        mPaint.setTextSize(sp2px(35));
        mPaint.setTypeface(Typeface.SANS_SERIF);//设置字体
        canvas.drawText("OK", 4 * mCenter / 5, 9 * mCenter / 8, mPaint);
    }

    /**
     * 绘制背景的圆圈和隔线
     *
     * @param canvas
     */
    private void drawBackCircle(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenter, mCenter, mOutRadius, mPaint); // 绘制圆圈

        //4个小圆
        mPaint.setColor(mInnerCircleColor);
        canvas.drawCircle(5 * mCenter / 18, mCenter, dp2px(7f), mPaint);//不能使用+ -数字，否则出现手机适配问题，必须相对center * /
        canvas.drawCircle(7 * mCenter / 4, mCenter, dp2px(7f), mPaint);
        canvas.drawCircle(mCenter, 7 * mCenter / 4, dp2px(7f), mPaint);
        canvas.drawCircle(mCenter, 5 * mCenter / 18, dp2px(7f), mPaint);

        //4条线都以原点为起点
        mPaint.setColor(mBackgroundColor);
        mPaint.setStrokeWidth(1);
        canvas.drawLine(mCenter, mCenter, 0, 0, mPaint);
        canvas.drawLine(mCenter, mCenter, mCenter * 2, 0, mPaint);
        canvas.drawLine(mCenter, mCenter, 0, mCenter * 2, mPaint);
        canvas.drawLine(mCenter, mCenter, mCenter * 2, mCenter * 2, mPaint);

    }

    /**
     * 清空画布
     *
     * @param canvas
     */
    private void clearCanvas(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
    }

    //只要有点击事件且在大圆区域都会从新画
    OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            final int action = event.getAction();
            switch (action) {
                //按下时变色
                case MotionEvent.ACTION_DOWN:
                    SettingUtil.checkVibrate();//震动

                    dir = checkDir(event.getX(), event.getY());
                    if (dir != Dir.OUT) {
                        if (dir == Dir.CENTER) {
                            mFlag = false;
                        }
                        invalidate();
                    }
                    break;
                //抬手时还原背景
                case MotionEvent.ACTION_UP:
                    getParent().requestDisallowInterceptTouchEvent(false);//可以拦截
                    mRefreshHandler.sendEmptyMessageDelayed(REFRESH_FLAG, 100); //完美解决：快速滑动时会执行UP事件，延时0.1S将布局还原
                    break;
                case MotionEvent.ACTION_MOVE:
                    getParent().requestDisallowInterceptTouchEvent(true);//告诉父View滑动的时候都不要拦截我的事件
                    break;
                case MotionEvent.ACTION_CANCEL:
                    getParent().requestDisallowInterceptTouchEvent(false);//可以拦截
                    mRefreshHandler.sendEmptyMessage(REFRESH_FLAG); //布局还原
                    break;
            }

            return true;
        }
    };

    /**
     * 检测方向
     *
     * @param x
     * @param y
     * @return
     */
    private Dir checkDir(float x, float y) {
        Dir dir = Dir.UNDEFINE;
        if (Math.sqrt(Math.pow(y - mCenter, 2) + Math.pow(x - mCenter, 2)) < mInnerCircleRadius) {// 判断在中心圆圈内
            dir = Dir.CENTER;
        } else if (Math.sqrt(Math.pow(y - mCenter, 2) + Math.pow(x - mCenter, 2)) > mOutRadius) {
            dir = Dir.OUT;
        } else if (y < x && y + x < 2 * mCenter) {
            dir = Dir.UP;
        } else if (y < x && y + x > 2 * mCenter) {
            dir = Dir.RIGHT;
        } else if (y > x && y + x < 2 * mCenter) {
            dir = Dir.LEFT;
        } else if (y > x && y + x > 2 * mCenter) {
            dir = Dir.DOWN;
        }
        getTouchAction(dir);
        return dir;
    }

    public enum Dir {
        UP, DOWN, LEFT, RIGHT, CENTER, OUT, UNDEFINE
    }

    public interface TouchListener {

        void onTouchRight();

        void onTouchLeft();

        void onTouchUp();

        void onTouchDown();

        void onTouchCenter();

        void onTouchOut();
    }

    public void setTouchListener(TouchListener listener) {
        mListentr = listener;
    }

    private void getTouchAction(Dir dir) {
        if (null != mListentr) {
            switch (dir) {
                case CENTER:
                    mListentr.onTouchCenter();
                    break;
                case OUT:
                    mListentr.onTouchOut();
                    break;
                case UP:
                    mListentr.onTouchUp();
                    break;
                case DOWN:
                    mListentr.onTouchDown();
                    break;
                case LEFT:
                    mListentr.onTouchLeft();
                    break;
                case RIGHT:
                    mListentr.onTouchRight();
                    break;
                default:
                    break;
            }
        }
    }

    private float sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }

    private float dp2px(float values) {
        float scale = getResources().getDisplayMetrics().density;
        return (values * scale + 0.5f);
    }

}
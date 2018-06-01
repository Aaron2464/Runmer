package com.aaron.runmer.DashBoardPackage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.aaron.runmer.R;
import com.aaron.runmer.util.Constants;

public class RunnerDashBoard extends View {

    private int mRadius;                    // 扇形半徑
    private int mStartAngle = 150;          // 起始角度
    private int mSweepAngle = 240;         // 繪製角度
    private int mMin = 0;                   // 最小值
    private int mMax = 70;                 // 最大值
    private int mSection = 9;                // 值域（mMax-mMin）等分份数
    private int mPortion = 2;                // 一個mSection等分份數
    private String mHeaderText = "km/h";   // 表頭
    private int mVelocity = mMin;           // 實時速度
    private int mStrokeWidth;                // 畫面寬度
    private int mLength1;                    // 長刻度的相對圓弧的長度
    private int mLength2;                    // 刻度讀數頂部的相對圓弧的長度
    private int mPLRadius;                   // 指針長半徑
    private int mPSRadius;                   // 指針短半徑

    private int mPadding;
    private float mCenterX, mCenterY;       //圓心座標
    private Paint mPaint;
    private RectF mRectFArc;
    private RectF mRectFInnerArc;
    private Rect mRectText;
    private String[] mTexts;
    private int[] mColors;

    public RunnerDashBoard(Context context) {
        this(context, null);
        Log.d(Constants.TAG_DASHBOARD, "DashBoard: " + "1");
    }

    public RunnerDashBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d(Constants.TAG_DASHBOARD, "DashBoard: " + "2");
    }

    public RunnerDashBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(Constants.TAG_DASHBOARD, "DashBoard: " + "3");
        init();
    }

    private void init() {
        mStrokeWidth = dp2px(3);        //TODO 線寬
        mLength1 = dp2px(5) + mStrokeWidth;     //TODO 刻度線長
        mLength2 = mLength1 + dp2px(4);         //TODO 刻度數值與刻度線距

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectFArc = new RectF();
        mRectFInnerArc = new RectF();
        mRectText = new Rect();

        mTexts = new String[mSection + 1]; // 需要顯示mSectioon+1個刻度讀數
        for (int i = 0; i < mTexts.length; i++) {
            int n = (mMax - mMin) / mSection;
            mTexts[i] = String.valueOf(mMin + i * n);
        }

        mColors = new int[]{ContextCompat.getColor(getContext(), R.color.color_green),
                ContextCompat.getColor(getContext(), R.color.color_yellow),
                ContextCompat.getColor(getContext(), R.color.color_red)};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(Constants.TAG_DASHBOARD, "DashBoard: " + "onMeasure");
        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);

        int width = resolveSize(dp2px(260), widthMeasureSpec);
        mRadius = (width - mPadding * 2 - mStrokeWidth * 2) / 2;

        // 由起始角度確定的高度
        float[] point1 = getCoordinatePoint(mRadius, mStartAngle);
        // 由结束角度確定的高度
        float[] point2 = getCoordinatePoint(mRadius, mStartAngle + mSweepAngle);
        int height = (int) Math.max(point1[1] + mRadius + mStrokeWidth * 2,
                point2[1] + mRadius + mStrokeWidth * 2);
        setMeasuredDimension(width, height + getPaddingTop() + getPaddingBottom());

        mCenterX = mCenterY = getMeasuredWidth() / 2f;

        Log.d(Constants.TAG_DASHBOARD, "mCenterX: " + mCenterX);
        Log.d(Constants.TAG_DASHBOARD, "mCenterY: " + mCenterY);

//        mCenterX = getMeasuredWidth() / 2f;                 //TODO 修正背景不符長寬，但是也直接影響整個畫面的協調
//        mCenterY = getMeasuredHeight() / 2f;
//        Log.d(Constants.TAG_DASHBOARD,"mCenterX: " + mCenterX);
//        Log.d(Constants.TAG_DASHBOARD,"mCenterY: " + mCenterY);       //TODO 不適用未被約束的ConstraintLayout
        mRectFArc.set(
                getPaddingLeft() + mStrokeWidth,
                getPaddingTop() + mStrokeWidth,
                getMeasuredWidth() - getPaddingRight() - mStrokeWidth,
                getMeasuredWidth() - getPaddingBottom() - mStrokeWidth
        );

        mPaint.setTextSize(sp2px(16));          //TODO 中間彩虹大小
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
        mRectFInnerArc.set(
                getPaddingLeft() + mLength2 + mRectText.height() + dp2px(30),
                getPaddingTop() + mLength2 + mRectText.height() + dp2px(30),
                getMeasuredWidth() - getPaddingRight() - mLength2 - mRectText.height() - dp2px(30),
                getMeasuredWidth() - getPaddingBottom() - mLength2 - mRectText.height() - dp2px(30)
        );
        //TODO 中間彩虹的位置/位移
        Log.d(Constants.TAG_DASHBOARD, "Left: " + (getPaddingLeft() + mLength2 + mRectText.height() + dp2px(30)));
        Log.d(Constants.TAG_DASHBOARD, "Top: " + (getPaddingTop() + mLength2 + mRectText.height() + dp2px(30)));
        Log.d(Constants.TAG_DASHBOARD, "Right: " + (getMeasuredWidth() - getPaddingRight() - mLength2 - mRectText.height() - dp2px(30)));
        Log.d(Constants.TAG_DASHBOARD, "Bottom: " + (getMeasuredWidth() - getPaddingBottom() - mLength2 - mRectText.height() - dp2px(30)));

        mPLRadius = mRadius - dp2px(25);
        mPSRadius = dp2px(10);              //TODO 短指針長度
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.transparent));           //TODO background
        /**
         * 畫圓弧
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_blue));          //TODO 刻度線顏色
        canvas.drawArc(mRectFArc, mStartAngle, mSweepAngle, false, mPaint);

        /**
         * 畫長刻度
         * 畫好起始角度的一條刻度后通過canvas繞着原點旋轉來畫剩下的長刻度
         */
        double cos = Math.cos(Math.toRadians(mStartAngle - 180));
        double sin = Math.sin(Math.toRadians(mStartAngle - 180));
        float x0 = (float) (mPadding + mStrokeWidth + mRadius * (1 - cos));
        float y0 = (float) (mPadding + mStrokeWidth + mRadius * (1 - sin));
        float x1 = (float) (mPadding + mStrokeWidth + mRadius - (mRadius - mLength1) * cos);
        float y1 = (float) (mPadding + mStrokeWidth + mRadius - (mRadius - mLength1) * sin);

        canvas.save();
        canvas.drawLine(x0, y0, x1, y1, mPaint);
        float angle = mSweepAngle * 1f / mSection;
        for (int i = 0; i < mSection; i++) {
            canvas.rotate(angle, mCenterX, mCenterY);
            canvas.drawLine(x0, y0, x1, y1, mPaint);
        }
        canvas.restore();

        /**
         * 畫短刻度
         * 同樣採用canvas的旋轉原理
         */
        canvas.save();
        mPaint.setStrokeWidth(mStrokeWidth / 2f);
        float x2 = (float) (mPadding + mStrokeWidth + mRadius - (mRadius - 2 * mLength1 / 3f) * cos);
        float y2 = (float) (mPadding + mStrokeWidth + mRadius - (mRadius - 2 * mLength1 / 3f) * sin);
        canvas.drawLine(x0, y0, x2, y2, mPaint);
        angle = mSweepAngle * 1f / (mSection * mPortion);
        for (int i = 1; i < mSection * mPortion; i++) {
            canvas.rotate(angle, mCenterX, mCenterY);
            if (i % mPortion == 0) { // 避免與長刻度畫重合
                continue;
            }
            canvas.drawLine(x0, y0, x2, y2, mPaint);
        }
        canvas.restore();

        //畫長刻度讀數
        mPaint.setTextSize(sp2px(10));          //TODO 刻度數值字型大小
        mPaint.setStyle(Paint.Style.FILL);
        float α;
        float[] p;
        angle = mSweepAngle * 1f / mSection;
        for (int i = 0; i <= mSection; i++) {
            α = mStartAngle + angle * i;
            p = getCoordinatePoint(mRadius - mLength2, α);
            if (α % 360 > 135 && α % 360 < 225) {
                mPaint.setTextAlign(Paint.Align.LEFT);
            } else if ((α % 360 >= 0 && α % 360 < 45) || (α % 360 > 315 && α % 360 <= 360)) {
                mPaint.setTextAlign(Paint.Align.RIGHT);
            } else {
                mPaint.setTextAlign(Paint.Align.CENTER);
            }
            mPaint.getTextBounds(mHeaderText, 0, mTexts[i].length(), mRectText);
            int txtH = mRectText.height();
            if (i <= 1 || i >= mSection - 1) {
                canvas.drawText(mTexts[i], p[0], p[1] + txtH / 2, mPaint);
                Log.d(Constants.TAG_DASHBOARD, "drawText: " + mTexts[i]);            //TODO 畫刻度數值
            } else if (i == 3) {
                canvas.drawText(mTexts[i], p[0] + txtH / 2, p[1] + txtH, mPaint);
                Log.d(Constants.TAG_DASHBOARD, "drawText: " + mTexts[i]);
            } else if (i == mSection - 3) {
                canvas.drawText(mTexts[i], p[0] - txtH / 2, p[1] + txtH, mPaint);
                Log.d(Constants.TAG_DASHBOARD, "drawText: " + mTexts[i]);
            } else {
                canvas.drawText(mTexts[i], p[0], p[1] + txtH, mPaint);
                Log.d(Constants.TAG_DASHBOARD, "drawText: " + mTexts[i]);
            }
        }

        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(15));       //TODO 中間色盤寬
        mPaint.setShader(generateSweepGradient());
        canvas.drawArc(mRectFInnerArc, mStartAngle + 1, mSweepAngle - 2, false, mPaint);        //TODO 中間色盤轉度

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(null);

        //畫表頭
//        if (!TextUtils.isEmpty(mHeaderText)) {                    //TODO 取消單位 KM/H
//            mPaint.setTextSize(sp2px(16));
//            mPaint.setTextAlign(Paint.Align.CENTER);
//            mPaint.getTextBounds(mHeaderText, 0, mHeaderText.length(), mRectText);
//            canvas.drawText(mHeaderText, mCenterX, mCenterY - mRectText.height() * 3, mPaint);
//        }

        //畫指針
        float θ = mStartAngle + mSweepAngle * (mVelocity - mMin) / (mMax - mMin); // 指针与水平线夹角
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_dark_light));        //TODO 指針固定座
        int r = mRadius / 8;
        canvas.drawCircle(mCenterX, mCenterY, r, mPaint);
        mPaint.setStrokeWidth(r / 3);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_dark));      //TODO 指針顏色
        float[] p1 = getCoordinatePoint(mPLRadius, θ);                     //TODO 長指針
        canvas.drawLine(p1[0], p1[1], mCenterX, mCenterY, mPaint);
        float[] p2 = getCoordinatePoint(mPSRadius, θ + 180);        //TODO 短指針
        canvas.drawLine(mCenterX, mCenterY, p2[0], p2[1], mPaint);

        //畫實時度數值

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mPaint.setStrokeWidth(dp2px(3));        //TODO 數值線寬
        int xOffset = dp2px(22);                 //TODO 數字距離
        if (mVelocity >= 100) {
            drawDigitalTube(canvas, mVelocity / 100, -xOffset);
            drawDigitalTube(canvas, (mVelocity - 100) / 10, 0);
            drawDigitalTube(canvas, mVelocity % 100 % 10, xOffset);
        } else if (mVelocity >= 10) {
            drawDigitalTube(canvas, -1, -xOffset);
            drawDigitalTube(canvas, mVelocity / 10, 0);
            drawDigitalTube(canvas, mVelocity % 10, xOffset);
        } else {
            drawDigitalTube(canvas, -1, -xOffset);
            drawDigitalTube(canvas, -1, 0);
            drawDigitalTube(canvas, mVelocity, xOffset);
        }
    }

    /**
     * 數碼管樣式
     */
    //      1
    //      ——
    //   2 |  | 3
    //      —— 4
    //   5 |  | 6
    //      ——
    //       7
    private void drawDigitalTube(Canvas canvas, int num, int xOffset) {     //TODO 速度
        float x = mCenterX + xOffset;      // TODO 數字左右位移
        float y = mCenterY + dp2px(35);   //TODO 數字位移高度
        int lx = dp2px(3);                  //TODO 數字長度
        int ly = dp2px(6);                  //TODO 數字高度
        int gap = dp2px(2);                  //TODO 數字內寬

        // 1
        mPaint.setAlpha(num == -1 || num == 1 || num == 4 ? 25 : 255);
        canvas.drawLine(x - lx, y, x + lx, y, mPaint);
        // 2
        mPaint.setAlpha(num == -1 || num == 1 || num == 2 || num == 3 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx - gap, y + gap, x - lx - gap, y + gap + ly, mPaint);
        // 3
        mPaint.setAlpha(num == -1 || num == 5 || num == 6 ? 25 : 255);
        canvas.drawLine(x + lx + gap, y + gap, x + lx + gap, y + gap + ly, mPaint);
        // 4
        mPaint.setAlpha(num == -1 || num == 0 || num == 1 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx, y + gap * 2 + ly, x + lx, y + gap * 2 + ly, mPaint);
        // 5
        mPaint.setAlpha(num == -1 || num == 1 || num == 3 || num == 4 || num == 5 || num == 7
                || num == 9 ? 25 : 255);
        canvas.drawLine(x - lx - gap, y + gap * 3 + ly,
                x - lx - gap, y + gap * 3 + ly * 2, mPaint);
        // 6
        mPaint.setAlpha(num == -1 || num == 2 ? 25 : 255);
        canvas.drawLine(x + lx + gap, y + gap * 3 + ly,
                x + lx + gap, y + gap * 3 + ly * 2, mPaint);
        // 7
        mPaint.setAlpha(num == -1 || num == 1 || num == 4 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx, y + gap * 4 + ly * 2, x + lx, y + gap * 4 + ly * 2, mPaint);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public float[] getCoordinatePoint(int radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //將角度轉換為弧度
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }
        return point;
    }

    private SweepGradient generateSweepGradient() {
        SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY,
                mColors,
                new float[]{0, 140 / 360f, mSweepAngle / 360f}
        );

        Matrix matrix = new Matrix();
        matrix.setRotate(mStartAngle - 3, mCenterX, mCenterY);
        sweepGradient.setLocalMatrix(matrix);

        return sweepGradient;
    }

    public int getVelocity() {
        return mVelocity;
    }

    public void setVelocity(int velocity) {
        if (mVelocity == velocity || velocity < mMin || velocity > mMax) {
            return;
        }

        mVelocity = velocity;
        postInvalidate();
    }
}

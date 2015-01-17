package com.ryeeeeee.progressbutton;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.Button;

/**
 * Created by Ryeeeeee on 1/17/15.
 */
public class ProgressButton extends Button{

    private final int DEFAULT_MIN_PROGRESS = 0;
    private final int DEFAULT_MAX_PROGRESS = 100;

    private int progress = DEFAULT_MIN_PROGRESS;
    private int maxProgress = DEFAULT_MAX_PROGRESS;
    private int minProgress = DEFAULT_MIN_PROGRESS;

    private Drawable progressDrawable;
    private Drawable completeDrawable;
    private Drawable errorDrawable;
    private Drawable normalDrawable;

    private CharSequence progressText;
    private CharSequence completeText;
    private CharSequence errorText;
    private CharSequence normalText;

    private Context context;

    public ProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ProgressButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    @SuppressWarnings("NewApi")
    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;

        // 初始化进度
        minProgress = DEFAULT_MIN_PROGRESS;
        maxProgress = DEFAULT_MAX_PROGRESS;

        // 获得不同状态下的图片
        normalDrawable = createNormalDrawable(attrs);
        progressDrawable = context.getResources().getDrawable(R.drawable.rectangle_progress).mutate();
        errorDrawable = context.getResources().getDrawable(R.drawable.rectangle_error).mutate();
        completeDrawable = context.getResources().getDrawable(R.drawable.rectangle_complete).mutate();

        parseAttributes(context, attrs, defStyle);
        this.setBackground(normalDrawable);
    }

    private Drawable createNormalDrawable(AttributeSet attrs){
        StateListDrawable normalDrawable = new StateListDrawable();

        Drawable unpressedDrawable = getResources().getDrawable(R.drawable.retangle_normal).mutate();
        Drawable pressedDrawable = getResources().getDrawable(R.drawable.rectangle_pressed).mutate();

        normalDrawable.addState(new int[]{ android.R.attr.state_pressed }, pressedDrawable);
        normalDrawable.addState(new int[]{}, unpressedDrawable);
        return normalDrawable;
    }

    private void parseAttributes(Context context, AttributeSet attrs, int defStyle) {
        if(attrs == null)
            return;

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressButton, defStyle, 0);
        // 获取设置的属性
        normalText = attributes.getString(R.styleable.ProgressButton_on_normal_text);
        errorText = attributes.getString(R.styleable.ProgressButton_on_error_text);
        progressText = attributes.getString(R.styleable.ProgressButton_on_progress_text);
        completeText = attributes.getString(R.styleable.ProgressButton_on_complete_text);
        //

        // TODO 解析组件 自定义属性
        attributes.recycle();

    }

    public void setProgress(int progress) {
        this.progress = progress;

        if(progress == this.maxProgress) {
            onComplete();
        } else if (progress == this.minProgress) {
            onNormal();
        } else if (progress < this.minProgress) {
            onError();
        } else {
            onProgress();
        }

        this.invalidate();
    }

    @SuppressWarnings("NewApi")
    protected void onError() {
        if(getErrorText() != null && !getErrorText().equals(""))
            this.setText(getErrorText());
        this.setBackground(getErrorDrawable());
    }

    @SuppressWarnings("NewApi")
    protected void onProgress() {
        if(getProgressText() != null && !getProgressText().equals(""))
            this.setText(getProgressText());
        this.setBackground(getProgressDrawable());
    }

    @SuppressWarnings("NewApi")
    protected void onComplete() {
        if(getCompleteText() != null && !getCompleteText().equals(""))
            this.setText(getCompleteText());
        this.setBackground(getCompleteDrawable());
    }

    @SuppressWarnings("NewApi")
    protected void onNormal(){
        if(getNormalText() != null && !getNormalText().equals(""))
            this.setText(getNormalText());
        this.setBackground(getNormalDrawable());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(progress > minProgress && progress < maxProgress) {
            drawProgress(canvas);
        }
        super.onDraw(canvas);
    }

    protected void drawProgress(Canvas canvas) {
        float percent = (float) getProgress() / (float) getMaxProgress();
        float finishedWidth = (float) getMeasuredWidth() * percent;

        double indicatorHeightPercent = 0.05; // 5%
        int bottom = (int) (getMeasuredHeight() - getMeasuredHeight() * indicatorHeightPercent);
        getProgressDrawable().setBounds(0, bottom, (int) finishedWidth, getMeasuredHeight());
        getProgressDrawable().draw(canvas);
    }

    // ****************************** getter ******************************

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getMinProgress() {
        return minProgress;
    }

    public Drawable getProgressDrawable() {
        return progressDrawable;
    }

    public Drawable getCompleteDrawable() {
        return completeDrawable;
    }

    public Drawable getErrorDrawable() {
        return errorDrawable;
    }

    public CharSequence getProgressText() {
        return progressText;
    }

    public CharSequence getCompleteText() {
        return completeText;
    }

    public CharSequence getErrorText() {
        return errorText;
    }

    public CharSequence getNormalText() {
        return normalText;
    }

    public Drawable getNormalDrawable() {
        return normalDrawable;
    }

    // ****************************** setter ******************************

    public void setErrorText(CharSequence errorText) {
        this.errorText = errorText;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setMinProgress(int minProgress) {
        this.minProgress = minProgress;
    }

    public void setProgressDrawable(Drawable progressDrawable) {
        this.progressDrawable = progressDrawable;
    }

    public void setCompleteDrawable(Drawable completeDrawable) {
        this.completeDrawable = completeDrawable;
    }

    public void setErrorDrawable(Drawable errorDrawable) {
        this.errorDrawable = errorDrawable;
    }

    public void setProgressText(CharSequence progressText) {
        this.progressText = progressText;
    }

    public void setCompleteText(CharSequence completeText) {
        this.completeText = completeText;
    }

    public void setNormalText(CharSequence normalText) {
        this.normalText = normalText;
    }

    public void setNormalDrawable(Drawable normalDrawable) {
        this.normalDrawable = normalDrawable;
    }
}

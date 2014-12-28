package com.ryeeeeee.lock;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryeeeeee on 12/23/14.
 */
public class LockView extends ViewGroup{

    /**
     * 默认为九宫格
     */
    private int blockNum = 9;
    /**
     * 正方形X宫格的宽度
     */
    private int lockLength;
    /**
     * X宫格一个方向的格点的个数
     */
    private int blockNumPerDirection;
    /**
     * 所有格点的集合
     */
    private List<Block> blocks;
    /**
     * 上下文环境
     */
    private Context context;
    /**
     * 轨迹
     */
    private TrackView trackView;

    private String correctGesture;

    public LockView(Context context){
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs){
        this(context, attrs, 0);

    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        parseAttributes(attrs, defStyleAttr);

        this.blockNumPerDirection = (int)Math.sqrt(blockNum);
        this.blocks = new ArrayList<>();
        getScreenParam();
        initBlocks();
        initTrack();
        this.setBackgroundResource(R.drawable.background);

    }

    private void parseAttributes(AttributeSet attrs, int defStyleAttr){
        if(attrs != null) {
            final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LockView, defStyleAttr, 0);
            this.blockNum = attributes.getInt(R.styleable.LockView_block_name, 9);
            attributes.recycle();
        }
    }

    /**
     * 初始化格点，并添加到锁屏组件中
     */
    private void initBlocks(){

        for(int i = 0; i < blockNum; i++){

            int row = i / blockNumPerDirection; // 所处第几行
            int col = i % blockNumPerDirection; // 所处第几列

            ImageView blockImage = new ImageView(this.context);
            blockImage.setBackgroundResource(R.drawable.block_normal);
            this.addView(blockImage, i);

            int centerX = ( 1 + 2 * col ) * (this.lockLength / this.blockNumPerDirection / 2);
            int centerY = ( 1 + 2 * row ) * (this.lockLength / this.blockNumPerDirection / 2);
            int width = (int)((this.lockLength / this.blockNumPerDirection / 2) * 1.2);
            int height = (int)((this.lockLength / this.blockNumPerDirection / 2) * 1.2);

            Block block = new Block(centerX, centerY, width, height, blockImage, (char)(i + 1 + '0'));
            this.blocks.add(block);

        }

    }

    /**
     * 初始化轨迹组件
     */
    private void initTrack(){
        this.trackView = new TrackView(context, blocks, this.lockLength);
        this.addView(trackView, lockLength, lockLength);
    }

    /**
     * 根据屏幕尺寸设置锁屏界面的尺寸
     */
    private void getScreenParam(){
        int[] screenWidthAndHeight = ScreenUtils.getScreenWidthAndHeight(context);
        int screenWidth = screenWidthAndHeight[0];
        int screenHeight = screenWidthAndHeight[1];

        this.lockLength = screenHeight > screenWidth ? screenWidth : screenHeight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for(int i = 0; i < blockNum; i++){

            ImageView blockImage = (ImageView)this.getChildAt(i);

            int leftX = this.blocks.get(i).getLeftX();
            int rightX = this.blocks.get(i).getRightX();
            int topY = this.blocks.get(i).getTopY();
            int bottomY = this.blocks.get(i).getBottomY();

            blockImage.layout(leftX, topY, rightX, bottomY);
        }

        trackView.layout(0, 0, lockLength, lockLength);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for(int i = 0; i < blockNum; i++){
            ImageView blockImage = (ImageView)this.getChildAt(i);
            blockImage.measure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    public void setGesture(String gesture){
        this.correctGesture = gesture;
        trackView.setGesture(gesture);
    }


    /**
     * 设置手势监听器
     * @param gestureListener
     */
    public void setGestureListener(GestureListener gestureListener){
        trackView.setGestureListener(gestureListener);
    }

    /**
     * 手势回调接口
     */
    public interface GestureListener{

        /**
         * 手势校验成功
         */
        public abstract void onSuccess();

        /**
         * 手势校验失败
         */
        public abstract void onFailed();
    }
}

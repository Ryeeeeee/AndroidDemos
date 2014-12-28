package com.ryeeeeee.lock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryeeeeee on 12/23/14.
 */
public class TrackView extends View {

    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;

    private Context context;

    /**
     * 记录已经绘制完成的轨道
     */
    private List<Pair<Block, Block>> drawnTrack = new ArrayList<>();

    /**
     * 格点集合
     */
    private List<Block> blocks;

    /**
     * 前一个已经选中的格点
     */
    private Block preBlock;
    /**
     * 画布的宽度
     */
    private int paintLength;
    /**
     * 手势监听器
     */
    private LockView.GestureListener gestureListener;
    /**
     * 正确的手势密码
     */
    private String correctGesture;
    /**
     * 用于记录当前的手势
     */
    private StringBuilder gestureStringBuilder = new StringBuilder();

    public TrackView(Context context, List<Block> blocks, int paintLength){
        super(context);

        this.context = context;
        this.blocks = blocks;
        this.paintLength = paintLength;

        reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{

                int eventX = (int)event.getX();
                int eventY = (int)event.getY();

                // 如果点击在格点上，则将这个格点设置为初始点
                Block currentBlock = getBlockAt(eventX, eventY);
                if(currentBlock != null){
                    currentBlock.enableHighlight(true);
                    preBlock = currentBlock;
                    gestureStringBuilder.append(currentBlock.getTag());
                }

                // 刷新组件
                this.invalidate();
                break;
            }
            case MotionEvent.ACTION_MOVE:{

                clearScreenAndRedrawTrack();

                int eventX = (int)event.getX();
                int eventY = (int)event.getY();

                Block currentBlock = getBlockAt(eventX, eventY);

                if(preBlock == null){ // 如果不存在已经记录的点

                    if(currentBlock == null ) {
                        // 如果当前也不是触摸在格点上，则什么都不绘制，直接返回
                        return true;
                    } else {
                        // 如果当前触摸在格点上，则记录此格点，并高亮
                        preBlock = currentBlock;
                        preBlock.enableHighlight(true);
                        gestureStringBuilder.append(currentBlock.getTag());
                    }

                } else { // 如果已经存在记录的点

                    if(currentBlock == null){
                        // 如果当前不是触摸在格点上，这绘制最后记录的点到此触摸点的轨迹
                        canvas.drawLine(preBlock.getCenterX(), preBlock.getCenterY(), eventX, eventY, paint);
                    } else if(currentBlock.isHighlight() == false){
                        // 如果当前触摸在格点上，并且此格点未被记录，则记录此点并高亮
                        drawnTrack.add(Pair.create(preBlock, currentBlock));
                        currentBlock.enableHighlight(true);
                        gestureStringBuilder.append(currentBlock.getTag());
                        preBlock = currentBlock;

                    } else{
                        // 如果当前触摸在格点上，并且此点已经被记录，则忽略此点，直接绘制最后记录的点到触摸点的轨迹
                        canvas.drawLine(preBlock.getCenterX(), preBlock.getCenterY(), currentBlock.getCenterX(), currentBlock.getCenterY(), paint);
                    }

                }

                this.invalidate();
                break;
            }

            case MotionEvent.ACTION_UP: {

                Log.d("Ryeeeeee", "correct:" + correctGesture);
                Log.d("Ryeeeeee", "now: " + gestureStringBuilder.toString());

                if(gestureStringBuilder.toString().equals(correctGesture)){
                    gestureListener.onSuccess();
                } else {
                    gestureListener.onFailed();
                }

                reset();
                clearScreenAndRedrawTrack();

                this.invalidate();
                break;

            }
        }

        return true;
    }

    /**
     * 获得位于锁所给定坐标处的格点
     * @param X
     * @param Y
     * @return
     */
    public Block getBlockAt(int X, int Y){
        if(blocks == null)
            return null;

        for(Block block : blocks){

            int leftX = block.getLeftX();
            int rightX = block.getRightX();
            if(X < leftX || X > rightX){
                continue;
            }

            int topY = block.getTopY();
            int bottomY = block.getBottomY();
            if(Y < topY || Y > bottomY){
                continue;
            }

            return block;
        }

        return null;
    }

    /**
     * 清除屏幕上的轨迹，并且重绘已经记录的点
     */
    public void clearScreenAndRedrawTrack(){
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for(Pair<Block, Block> pair : drawnTrack){
            Block preBlock = pair.first;
            Block currentBlock = pair.second;
            canvas.drawLine(preBlock.getCenterX(), preBlock.getCenterY(), currentBlock.getCenterX(), currentBlock.getCenterY(), paint);
        }
    }


    /**
     * 重置所有绘图状态
     */
    public void reset(){

        paint = new Paint(Paint.DITHER_FLAG);// 防抖
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(4, 115, 157));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        bitmap = Bitmap.createBitmap(paintLength, paintLength, Bitmap.Config.ARGB_8888);//

        canvas = new Canvas();
        canvas.setBitmap(bitmap);// 设置需要写入的Bitmap

        preBlock = null;

        for(Block block : blocks){
            block.enableHighlight(false);
        }
        drawnTrack.clear();

        gestureStringBuilder.delete(0, gestureStringBuilder.length());
    }

    /**
     *
     * @param gesture
     */
    public void setGesture(String gesture){
        this.correctGesture = gesture;
    }

    /**
     * 设置手势监听器
     * @param gestureListener
     */
    public void setGestureListener(LockView.GestureListener gestureListener){
        this.gestureListener = gestureListener;
    }
}

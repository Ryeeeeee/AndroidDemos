package com.ryeeeeee.lock;

import android.widget.ImageView;

/**
 * Created by Ryeeeeee on 12/23/14.
 */
public class Block {

    private int leftX;
    private int rightX;
    private int topY;
    private int bottomY;

    private int centerX;
    private int centerY;
    private int width;
    private int height;
    /**
     * 此格点的tag
     */
    private char tag;
    /**
     * 是否高亮
     */
    private boolean isHighlight = false;
    /**
     * 格点中的图片
     */
    private ImageView imageView;

    public Block(int centerX, int centerY, int width, int height, ImageView imageView, char tag){

        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;

        this.imageView = imageView;
        this.tag = tag;

        this.leftX = centerX - width / 2;
        this.rightX = centerX + width / 2;
        this.topY = centerY - height / 2;
        this.bottomY = centerY + height / 2;
    }

    /**
     * 设置是否高亮格点
     * @param enable
     */
    public void enableHighlight(boolean enable){

        this.isHighlight = enable;

        if(enable == true)
            imageView.setBackgroundResource(R.drawable.block_pressed);
        else
            imageView.setBackgroundResource(R.drawable.block_normal);

    }

    /**
     * 判断格点是否高亮
     * @return
     */
    public boolean isHighlight(){
        return this.isHighlight;
    }

    /******************** getter ********************/
    public int getLeftX() { return leftX; }

    public int getRightX() {
        return rightX;
    }

    public int getTopY() {
        return topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char getTag() { return tag; }

    /******************** setter ********************/
    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setTag(char tag) { this.tag = tag; }
}

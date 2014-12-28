package com.ryeeeeee.lock;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Ryeeeeee on 12/23/14.
 */
public class ScreenUtils {

    /**
     * 获取屏幕宽度和高度
     */
    @Deprecated
    public static int[] getScreenWidthAndHeight(Context context){

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        int widthAndHeight[] = {width, height};

        return widthAndHeight;
    }

}

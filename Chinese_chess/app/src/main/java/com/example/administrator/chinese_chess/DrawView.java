package com.example.administrator.chinese_chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

import static com.example.administrator.chinese_chess.MainActivity.Winner;
import static com.example.administrator.chinese_chess.MainActivity.arrayListPlayer;
import static com.example.administrator.chinese_chess.MainActivity.arrayListX;
import static com.example.administrator.chinese_chess.MainActivity.arrayListY;
import static com.example.administrator.chinese_chess.Welcome.Mode;

/**
 * Created by Administrator on 2018/2/10.
 */

public class DrawView extends View {
    public DrawView(Context context){
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //http://blog.csdn.net/u010661782/article/details/52805870  绘制各种形状的函数
        // 创建画笔
        Paint p = new Paint();

        int screenWidth=1100,screenHeight=1200;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.background1);
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth,screenHeight, true);
        canvas.drawBitmap(bitmap,0,200,p);

        p.setColor(Color.BLACK);// 设置红色
        p.setTextSize(30);//画笔绘制的字体大小
       // canvas.drawText("画圆：", 10, 50, p);// 画文本(字符串，x，y，paint)
        for(int i=0;i<12;i++){
            canvas.drawText(Integer.toString(i*100),i*100, 50, p);// 画文本(字符串，x，y，paint)
        }
        for(int i=0;i<30;i++){
            canvas.drawText(Integer.toString(i*100),12, i*100, p);// 画文本(字符串，x，y，paint)
        }
        p.setColor(Color.BLACK);// 设置颜色
        p.setStrokeWidth(8);
        canvas.drawLine(200, 400, 800, 400, p);
        canvas.drawLine(200, 600, 800, 600, p);// 画线（startx,starty,stopx.stopy,paint）
        canvas.drawLine(200, 800, 800, 800, p);
        canvas.drawLine(200, 1000, 800, 1000, p);

        canvas.drawLine(200, 400, 200, 1000, p);
        canvas.drawLine(400, 400, 400, 1000, p);
        canvas.drawLine(600, 400, 600, 1000, p);
        canvas.drawLine(800, 400, 800, 1000, p);
        for(int i=0;i<arrayListX.size();i++){
            Bitmap bitmap_tick= BitmapFactory.decodeResource(getResources(),R.mipmap.tick);
            if(arrayListPlayer.get(i)==2) bitmap_tick= BitmapFactory.decodeResource(getResources(),R.mipmap.circle);
            bitmap_tick = Bitmap.createScaledBitmap(bitmap_tick, 200,200, true);
            canvas.drawBitmap(bitmap_tick,arrayListX.get(i),arrayListY.get(i),p);
        }

        if(Winner==1&&Mode==1){
            p.setColor(Color.RED);// 设置红色
            p.setTextSize(130);//画笔绘制的字体大小
            canvas.drawText("PLAYER1 WIN!!!", 100, 170, p);// 画文本(字符串，x，y，paint)
        }
        else if(Winner==2&&Mode==1){
            p.setColor(Color.RED);// 设置红色
            p.setTextSize(150);//画笔绘制的字体大小
            canvas.drawText("PLAYER2 WIN!!!", 100, 170, p);// 画文本(字符串，x，y，paint)
        }
        if(Winner==2&&Mode==2){
            p.setColor(Color.GREEN);// 设置红色
            p.setTextSize(130);//画笔绘制的字体大小
            canvas.drawText("You WIN!!!", 100, 170, p);// 画文本(字符串，x，y，paint)
        }
        else if(Winner==1&&Mode==2){
            p.setColor(Color.RED);// 设置红色
            p.setTextSize(150);//画笔绘制的字体大小
            canvas.drawText("You Lose!!!", 100, 170, p);// 画文本(字符串，x，y，paint)
        }
        else if(Winner==0&&arrayListX.size()==9){
            p.setColor(Color.RED);// 设置红色
            p.setTextSize(150);//画笔绘制的字体大小
            canvas.drawText("和棋", 400, 170, p);// 画文本(字符串，x，y，paint)
        }

    }

}
//        canvas.drawCircle(100, 50, 50, p);// 小圆，(圆心x，圆心y，半径r，paint)
//        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
//        canvas.drawCircle(200, 100, 100, p);// 大圆
//
//        canvas.drawText("画线及弧线：", 10, 200, p);
//        p.setColor(Color.GREEN);// 设置绿色
//        p.setStrokeWidth(8);
//        canvas.drawLine(60, 500, 160, 500, p);// 画线（startx,starty,stopx.stopy,paint）
//        canvas.drawLine(110, 400, 190, 80, p);// 斜线

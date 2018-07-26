package com.example.administrator.chinese_chess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    static public int Winner=0;
    static int[][] chess_board=new int[3][3];
    static public ArrayList<Float> arrayListX=new ArrayList<>();
    static public ArrayList<Float> arrayListY=new ArrayList<>();
    static public ArrayList<Integer> arrayListPlayer=new ArrayList<>();
    static public int current_player=1;
    static public Float[] LineX={Float.valueOf(200),Float.valueOf(400),Float.valueOf(600),Float.valueOf(800)};
    static public Float[] LineY={Float.valueOf(400),Float.valueOf(600),Float.valueOf(800),Float.valueOf(1000)};
    private SoundPool mSoundPool = null;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSoundPool = new SoundPool(10, 0, 0);
        soundID.put(1, mSoundPool.load(this, R.raw.sword, 1));
        soundID.put(2, mSoundPool.load(this, R.raw.short_music, 1));
        init();
        Start();
    }
    static public void init() {
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++) chess_board[i][j]=0;
        }
        Winner=0;
        arrayListX.clear();arrayListY.clear();arrayListPlayer.clear();
        current_player=1;
    }
    private void Start() {
        final TextView show=(TextView)findViewById(R.id.show);
        LinearLayout layout=(LinearLayout) findViewById(R.id.root);
        final DrawView view=new DrawView(this);
        view.invalidate(); //通知view组件重绘
        layout.addView(view);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("---action down-----");
                        show.setText("起始位置为："+"("+event.getX()+" , "+event.getY()+")");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("---action move-----");
                        show.setText("移动中坐标为："+"("+event.getX()+" , "+event.getY()+")");
                        break;
                    case MotionEvent.ACTION_UP:


                        System.out.println("---action up-----");
                        show.setText("最后位置为："+"("+event.getX()+" , "+event.getY()+")");
                        Record(event.getX(),event.getY());
                        view.requestLayout();
                }
                return true;
            }
        });
        Button restart=(Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                view.requestLayout();
            }
        });
        Button undo=(Button)findViewById(R.id.undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayListX!=null&&arrayListX.size()!=0) {
                    for(int i=0;i<3;i++){
                        for(int j=0;j<3;j++){
                            if(arrayListX.get(arrayListX.size()-1).equals(LineX[i])&&arrayListY.get(arrayListX.size()-1).equals(LineY[j])){
                                chess_board[j][i]=0;break;
                            }
                        }
                    }
                    arrayListX.remove(arrayListX.size()-1);
                    arrayListY.remove(arrayListY.size()-1);
                    arrayListPlayer.remove(arrayListPlayer.size()-1);
                    if(current_player==1) current_player=2;
                    else if(current_player==2) current_player=1;
                    view.requestLayout();
                }
            }
        });
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void Record(final Float x,Float y){
        Float xx=Float.valueOf(0),yy=Float.valueOf(0);
        boolean flag=true;
        if(x>LineX[0]&&x<LineX[1]) xx=Float.valueOf(LineX[0]);
        else if(x>=LineX[1]&&x<=LineX[2])  xx=Float.valueOf(LineX[1]);
        else if(x>LineX[2]&&x<LineX[3])   xx=Float.valueOf(LineX[2]);
        else flag=false;

        if(y>=LineY[0]&&y<LineY[1]) yy=Float.valueOf(LineY[0]);
        else if(y>=LineY[1]&&y<=LineY[2]) yy=Float.valueOf(LineY[1]);
        else if(y>LineY[2]&&y<=LineY[3]) yy=Float.valueOf(LineY[2]);
        else flag=false;


        for(int i=0;i<arrayListX.size();i++){
            if(arrayListX.get(i).equals(xx)&&arrayListY.get(i).equals(yy))  flag=false;
        }
        if(flag&&arrayListX.size()<=9){
            if(current_player==1) mSoundPool.play(soundID.get(1), (float)0.3,(float)0.3, 0, 0, 1);
            else if(current_player==2) mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
            //显示在棋盘数组上
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(xx.equals(LineX[i])&&yy.equals(LineY[j])){
                        chess_board[j][i]=current_player;//这里i、j要换过来
                    }
                }
            }
            //判断谁赢了
            int winner=CheckTheBoard();
            if(winner==1) {
                Log.e("ZZH","player1 win!!!");
                Winner=1;
            }
            else if(winner==2){
                Log.e("ZZH","player2 win!!!");
                Winner=2;
            }
            Log.e("ZZH","棋盘");
            for(int i=0;i<3;i++){
                Log.e("ZZH",String.valueOf(chess_board[i][0])+String.valueOf(chess_board[i][1])+String.valueOf(chess_board[i][2]));
            }
            arrayListX.add(xx);
            arrayListY.add(yy);
            arrayListPlayer.add(current_player);
            if(current_player==1) current_player=2;
            else if(current_player==2) current_player=1;
        }
    }
    public int CheckTheBoard(){
        int winner=0;
        //i就是第几行，j就是第几列
        //横向判断
        for(int i=0;i<3;i++){
            int tem=chess_board[i][0];
            winner=0;
            for(int j=1;j<3;j++){
                if(chess_board[i][j]==tem){
                    winner=tem;
                }
                else {
                    winner=0;break;
                }
            }
            if(winner!=0) break;
        }
        if(winner!=0) return winner;
        //纵向判断
        for(int i=0;i<3;i++){
            int tem=chess_board[0][i];
            winner=0;
            for(int j=1;j<3;j++){
                if(chess_board[j][i]==tem){
                    winner=tem;
                }
                else {
                    winner=0;break;
                }
            }
            if(winner!=0) break;
        }
        if(winner!=0) return winner;
        //两条对角线
        if(chess_board[0][0]==chess_board[1][1]&&chess_board[1][1]==chess_board[2][2]) winner=chess_board[1][1];
        if(chess_board[0][2]==chess_board[1][1]&&chess_board[1][1]==chess_board[2][0]) winner=chess_board[1][1];
        return winner;
    }
}



















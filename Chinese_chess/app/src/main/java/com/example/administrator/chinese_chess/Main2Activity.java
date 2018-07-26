package com.example.administrator.chinese_chess;

import android.content.Intent;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

import static com.example.administrator.chinese_chess.MainActivity.LineX;
import static com.example.administrator.chinese_chess.MainActivity.LineY;
import static com.example.administrator.chinese_chess.MainActivity.arrayListPlayer;
import static com.example.administrator.chinese_chess.MainActivity.arrayListX;
import static com.example.administrator.chinese_chess.MainActivity.arrayListY;
import static com.example.administrator.chinese_chess.MainActivity.chess_board;
import static com.example.administrator.chinese_chess.MainActivity.Winner;
import static com.example.administrator.chinese_chess.MainActivity.current_player;
public class Main2Activity extends AppCompatActivity {
    public final static int COMPUTER= 1;
    public final static int PLAYER = 2;
    public final static int CLEVER= 1;
    public final static int SILLY = 2;
    public int OffensiveOne=0;
    public int Intelligence=CLEVER;
    private SoundPool mSoundPool = null;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if(getIntent().getStringExtra("WhoFirst").equals("computer")) OffensiveOne=COMPUTER;
        else if(getIntent().getStringExtra("WhoFirst").equals("player")) OffensiveOne=PLAYER;
        init();
        Start();
    }
    private void init() {
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++) chess_board[i][j]=0;
        }
        Winner=0;
        arrayListX.clear();arrayListY.clear();arrayListPlayer.clear();
        current_player=OffensiveOne;
    }
    private void Start() {
        mSoundPool = new SoundPool(10, 0, 0);
        soundID.put(1, mSoundPool.load(this, R.raw.sword, 1));
        soundID.put(2, mSoundPool.load(this, R.raw.short_music, 1));
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
                        Check(event.getX(),event.getY());
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
                if(OffensiveOne==COMPUTER) Computer_play();
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
                    for(int i=0;i<3;i++){
                        for(int j=0;j<3;j++){
                            if(arrayListX.get(arrayListX.size()-2).equals(LineX[i])&&arrayListY.get(arrayListX.size()-2).equals(LineY[j])){
                                chess_board[j][i]=0;break;
                            }
                        }
                    }
                    arrayListX.remove(arrayListX.size()-1);
                    arrayListX.remove(arrayListX.size()-1);
                    arrayListY.remove(arrayListY.size()-1);
                    arrayListY.remove(arrayListY.size()-1);
                    arrayListPlayer.remove(arrayListPlayer.size()-1);
                    arrayListPlayer.remove(arrayListPlayer.size()-1);
                    view.requestLayout();
                }
            }
        });
        final Button btn_intelligence=(Button)findViewById(R.id.intelligence);
        final TextView txt_intelligence=(TextView)findViewById(R.id.mode);
        btn_intelligence.setText("开启智障模式");
        btn_intelligence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Intelligence==CLEVER){
                    Intelligence=SILLY;
                    btn_intelligence.setText("开启智能模式");
                    txt_intelligence.setText("当前模式：智障模式");
                }
                else if(Intelligence==SILLY){
                    Intelligence=CLEVER;
                    btn_intelligence.setText("开启智障模式");
                    txt_intelligence.setText("当前模式：智能模式");
                }
                init();
                if(OffensiveOne==COMPUTER) Computer_play();
                view.requestLayout();
            }
        });
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(OffensiveOne==COMPUTER){//电脑先手则电脑先下棋
            Computer_play();
            view.requestLayout();
        }
    }
    private void Computer_play(){
        if(Intelligence==SILLY){
            reply_depending_on_actual_situation();
            return;
        }
        if(arrayListX!=null&&arrayListX.size()==0){//第一回合电脑先手
            Random rand =new Random();
            int randomInt=rand.nextInt(3);
            Log.e("ZZH","rand"+String.valueOf(randomInt));
            if(randomInt==0) Check(Float.valueOf(LineX[0]),Float.valueOf(LineY[0]));
            else if(randomInt==1) Check(Float.valueOf(LineX[0]),Float.valueOf(LineY[2]));
            else if(randomInt==2) Check(Float.valueOf(LineX[2]),Float.valueOf(LineY[2]));
            else if(randomInt==3) Check(Float.valueOf(LineX[2]),Float.valueOf(LineY[0]));
        }
        else if(arrayListX!=null&&arrayListX.size()==2){//第二回合
            if(chess_board[1][0]==PLAYER||chess_board[2][1]==PLAYER||chess_board[0][1]==PLAYER||chess_board[1][2]==PLAYER){
                Check(Float.valueOf(LineX[1]),Float.valueOf(LineY[1]));//下中间
            }
            else{//玩家不下四个边中间
                int computer_i=0,computer_j=0;
                for(int i=0;i<3;i++)
                    for(int j=0;j<3;j++){
                        if(chess_board[i][j]==COMPUTER){
                            computer_i=i;computer_j=j;break;
                        }
                    }
                boolean flag=false;
                if(chess_board[computer_i][2-computer_j]==PLAYER){//同一行的另一个角即邻近角有玩家下的棋，
                    Check(Float.valueOf(LineX[2-computer_j]),Float.valueOf(LineY[2-computer_i]));//下对角
                    flag=true;
                }

                if(flag==false){//如果上面已经下棋了就不用再判断了，第二回合电脑不再下棋
                    if(chess_board[2-computer_i][computer_j]==PLAYER){//同一列的另一个角即邻近角有玩家下的棋，
                        Check(Float.valueOf(LineX[2-computer_j]),Float.valueOf(LineY[2-computer_i]));//下对角
                    }
                }
                if(flag==false){
                    if(chess_board[2-computer_i][2-computer_j]==PLAYER){//对角即邻近角有玩家下的棋，
                        Check(Float.valueOf(LineX[2-computer_j]),Float.valueOf(LineY[computer_i]));//下邻近角
                    }
                }
                if(chess_board[1][1]==PLAYER){//玩家下在正中间
                    Check(Float.valueOf(LineX[2-computer_j]),Float.valueOf(LineY[2-computer_i]));//下对角
                }
            }
        }
        else reply_depending_on_actual_situation();
    }
    private void Computer_reply(){
        if(Intelligence==SILLY){
            reply_depending_on_actual_situation();
            return;
        }
        if(arrayListX!=null&&arrayListX.size()==1){
            if(chess_board[1][1]==PLAYER){//玩家第一步棋下在正中间
                Random rand =new Random();
                int randomInt=rand.nextInt(3);
                Log.e("ZZH","rand"+String.valueOf(randomInt));
                if(randomInt==0) Check(Float.valueOf(LineX[0]),Float.valueOf(LineY[0]));
                else if(randomInt==1) Check(Float.valueOf(LineX[0]),Float.valueOf(LineY[2]));
                else if(randomInt==2) Check(Float.valueOf(LineX[2]),Float.valueOf(LineY[2]));
                else if(randomInt==3) Check(Float.valueOf(LineX[2]),Float.valueOf(LineY[0]));
            }
            else if(chess_board[0][0]==PLAYER||chess_board[2][0]==PLAYER||chess_board[0][2]==PLAYER||chess_board[2][2]==PLAYER) {//玩家第一步下在四个角
                Check(Float.valueOf(LineX[1]),Float.valueOf(LineY[1]));
            }
            else Check(Float.valueOf(LineX[1]),Float.valueOf(LineY[1]));
        }
        else reply_depending_on_actual_situation();
    }
    private void reply_depending_on_actual_situation(){
        int[][] weight=new int[3][3];//权重向量
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++) weight[i][j]=-100;
        for(int i=0;i<3;i++){//第几行
            for(int j=0;j<3;j++){//第几列
                int num_row=0;//棋格所在这一行有多少个电脑下的棋子
                int num_column=0;//棋格所在这一列有多少个电脑下的棋子
                int num_diagonal1=0;//棋格所在对角线1有多少个电脑下的棋子
                int num_diagonal2=0;//棋格所在对角线2有多少个电脑下的棋子
                int P_num_row=0;//棋格所在这一行有多少个玩家下的棋子
                int P_num_column=0;//棋格所在这一列有多少个玩家下的棋子
                int P_num_diagonal1=0;//棋格所在对角线1有多少个玩家下的棋子
                int P_num_diagonal2=0;//棋格所在对角线2有多少个玩家下的棋子
                if(chess_board[i][j]==0){//这个棋格还是空的

                    for(int k=0;k<3;k++){//检查有没有可能组成横向直线
                        if(chess_board[i][k]==COMPUTER) num_row++;
                        else if(chess_board[i][k]==PLAYER){
                            P_num_row++;
                        }
                    }
                    for(int k=0;k<3;k++) {//检查有没有可能组成竖向直线
                        if(chess_board[k][j]==COMPUTER) num_column++;
                        else if(chess_board[k][j]==PLAYER){
                            P_num_column++;
                        }
                    }
                    //检查有没有可能组成对角线直线
                    if((i==0&&j==0)||(i==2&&j==2)){//左上角  右下角
                        for(int k=0;k<3;k++){
                            if(chess_board[k][k]==COMPUTER) num_diagonal1++;
                            else if(chess_board[k][k]==PLAYER){
                                P_num_diagonal1++;
                            }
                        }
                    }
                    else if((i==2&&j==0)||(i==0&&j==2)){//右上角  左下角
                        for(int k=0;k<3;k++){
                            if(chess_board[k][2-k]==COMPUTER) num_diagonal2++;
                            else if(chess_board[k][2-k]==PLAYER){
                                P_num_diagonal2++;
                            }
                        }
                    }
                    else if(i==1&&j==1){//中心点
                        for(int k=0;k<3;k++){
                            if(chess_board[k][k]==COMPUTER) num_diagonal1++;
                            else if(chess_board[k][k]==PLAYER){
                                P_num_diagonal1++;
                            }
                        }
                        for(int k=0;k<3;k++){
                            if(chess_board[k][2-k]==COMPUTER) num_diagonal2++;
                            else if(chess_board[k][2-k]==PLAYER){
                                P_num_diagonal2++;
                            }
                        }
                    }
                    if(P_num_row!=0) num_row=0;
                    if(P_num_column!=0) num_column=0;
                    if(P_num_diagonal1!=0) num_diagonal1=0;
                    if(P_num_diagonal2!=0) num_diagonal2=0;
                    weight[i][j]=num_row+num_column+num_diagonal1+num_diagonal2;
                    if(num_row==2||num_column==2||num_diagonal1==2||num_diagonal2==2) weight[i][j]=10000;
                    if(Intelligence==CLEVER&&(P_num_row==2||P_num_column==2||P_num_diagonal1==2||P_num_diagonal2==2)) weight[i][j]=1000;
                }

            }
        }
        int bestI=0,bestJ=0,MaxWeight=-1000;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++){
                if(weight[i][j]>MaxWeight){
                    MaxWeight=weight[i][j];
                    bestI=i;bestJ=j;
                }
            }
        Check(Float.valueOf(LineX[bestJ]),Float.valueOf(LineY[bestI]));
    }
    private void Check(final Float x,Float y){
        Float xx=Float.valueOf(0),yy=Float.valueOf(0);
        boolean flag=true;
        if(x>=LineX[0]&&x<LineX[1]) xx=Float.valueOf(LineX[0]);
        else if(x>=LineX[1]&&x<LineX[2])  xx=Float.valueOf(LineX[1]);
        else if(x>=LineX[2]&&x<=LineX[3])   xx=Float.valueOf(LineX[2]);
        else flag=false;

        if(y>=LineY[0]&&y<LineY[1]) yy=Float.valueOf(LineY[0]);
        else if(y>=LineY[1]&&y<LineY[2]) yy=Float.valueOf(LineY[1]);
        else if(y>=LineY[2]&&y<=LineY[3]) yy=Float.valueOf(LineY[2]);
        else flag=false;


        for(int i=0;i<arrayListX.size();i++){
            if(arrayListX.get(i).equals(xx)&&arrayListY.get(i).equals(yy))  flag=false;
        }
        if(flag&&arrayListX.size()<=9){
            if(current_player==1) mSoundPool.play(soundID.get(1), (float)0.3,(float)0.3, 0, 0, 1);
            else if(current_player==2) mSoundPool.play(soundID.get(2),1 , 1, 0, 0, 1);
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
            if(winner==COMPUTER) {
                Log.e("ZZH","You Lose");
                Winner=1;
            }
            else if(winner==PLAYER){
                Log.e("ZZH","You Win");
                Winner=2;
            }
            Log.e("ZZH","棋盘");
            for(int i=0;i<3;i++){
                Log.e("ZZH",String.valueOf(chess_board[i][0])+String.valueOf(chess_board[i][1])+String.valueOf(chess_board[i][2]));
            }
            arrayListX.add(xx);
            arrayListY.add(yy);
            arrayListPlayer.add(current_player);
            if(current_player==PLAYER) current_player=COMPUTER;
            else if(current_player==COMPUTER) current_player=PLAYER;
            if(Winner==0&&current_player==COMPUTER){
                if(OffensiveOne==COMPUTER) Computer_play();
                else if(OffensiveOne==PLAYER) Computer_reply();
            }
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

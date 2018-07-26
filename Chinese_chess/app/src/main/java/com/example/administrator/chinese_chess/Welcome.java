package com.example.administrator.chinese_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2018/2/12.
 */

public class Welcome extends AppCompatActivity {
    static public int Mode=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Button pVp=(Button)findViewById(R.id.player_vs_player);
        Button pVc1=(Button)findViewById(R.id.player_vs_computer1);
        Button pVc2=(Button)findViewById(R.id.player_vs_computer2);
        pVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mode=1;
                Intent intent=new Intent(Welcome.this,MainActivity.class);
                startActivity(intent);
            }
        });
        pVc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mode=2;
                Intent intent=new Intent(Welcome.this,Main2Activity.class);
                intent.putExtra("WhoFirst","computer");
                startActivity(intent);
            }
        });
        pVc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mode=2;
                Intent intent=new Intent(Welcome.this,Main2Activity.class);
                intent.putExtra("WhoFirst","player");
                startActivity(intent);
            }
        });
    }
}

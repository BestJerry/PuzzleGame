package com.example.puzzlegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.puzzlegame.PintuView.GamePintuLayout;

public class MainActivity extends Activity {

    private GamePintuLayout mgamePintuLayout;

    private TextView mlevel;
    private TextView mtime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ActivityCollector.addAcitivity(this);

        mgamePintuLayout = (GamePintuLayout) findViewById(R.id.id_GamePintu);
        mgamePintuLayout.setIstimeenable(true);
        mlevel = (TextView) findViewById(R.id.id_level);
        mtime = (TextView) findViewById(R.id.id_time);

        mgamePintuLayout.setOnGamePintulistener(new GamePintuLayout.Gamepintulistener() {
            @Override
            public void nextlevel(final int nextlevel) {

                new AlertDialog.Builder(MainActivity.this).setTitle("Information").setMessage("闯关成功！")
                        .setCancelable(false).setPositiveButton("继续闯关", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mgamePintuLayout.nextlevel();
                        mlevel.setText("当前关卡：" + nextlevel);
                    }
                }).show();
            }

            @Override
            public void timechanged(int currenttime) {

                mtime.setText("剩余时间：" + currenttime);

            }

            @Override
            public void gameover() {

                new AlertDialog.Builder(MainActivity.this).setTitle("Information").setMessage("游戏结束！")
                        .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                mgamePintuLayout.restart();

                            }
                        }).setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ActivityCollector.finishAll();
                    }
                }).show();
            }
        });
    }

    @Override
    protected void onPause() {

        super.onPause();
        mgamePintuLayout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mgamePintuLayout.resume();
    }
}

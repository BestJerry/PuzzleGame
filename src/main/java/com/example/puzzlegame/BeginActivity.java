package com.example.puzzlegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by jerry on 16-9-19.
 */
public class BeginActivity extends Activity {

    private Button startgame;
    private Button gamehelp;
    private Button finshgame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.beginactivity);
        ActivityCollector.addAcitivity(this);

        startgame = (Button) findViewById(R.id.start_game);
        gamehelp = (Button) findViewById(R.id.game_help);
        finshgame = (Button) findViewById(R.id.finish_game);

        startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BeginActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        gamehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(BeginActivity.this, GameHelp.class);
                startActivity(intent2);
            }
        });

        finshgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.finishAll();
            }
        });

    }

}

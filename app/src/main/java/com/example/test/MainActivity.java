package com.example.test;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static TextView txt_score,txt_best_score,Txt_best_score_dialog,txt_score_over;
    public static Button txt_restart;
    public static ImageView txt_F_B, btn_play;
    public static ImageView btn_trophy;
    public static ImageView btn_musicOn, btn_musicOff, btn_audioOn, btn_audioOff;
    public static ImageView btn_skinChange;
    public static RelativeLayout rl_over;
    public static RelativeLayout rl_start;
    public static MediaPlayer media;
    public static ImageView btn_bird1, btn_bird2, btn_bird3;
    public static String bscore;
    int bird_a, bird_b;
    private GameView gv;
    Dialog skinDialog, bestScoreDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constaint.SCREEN_HEIGHT = dm.heightPixels;
        Constaint.SCREEN_WIDTH = dm.widthPixels;
        setContentView(R.layout.activity_main);

        //Get views id
        txt_restart = findViewById(R.id.txt_restart);
        txt_score = findViewById(R.id.txt_score);
        txt_F_B = findViewById(R.id.txt_F_bird);
        txt_best_score = findViewById(R.id.txt_best_score);
        txt_score_over = findViewById(R.id.txt_score_over);
        rl_over = findViewById(R.id.rl_over);
        rl_start = findViewById(R.id.rl_start);
        btn_musicOn = findViewById(R.id.btn_musicOn);
        btn_musicOff = findViewById(R.id.btn_musicOff);
        btn_audioOn = findViewById(R.id.btn_audioOn);
        btn_audioOff = findViewById(R.id.btn_audioOff);
        btn_play = findViewById(R.id.btn_play);
        btn_skinChange = findViewById(R.id.btn_skinChange);
        btn_trophy = findViewById(R.id.btn_trophy);
        gv = findViewById(R.id.gv);

        //music
        media = MediaPlayer.create(this, R.raw.sillychipsong);
        media.setLooping(true);
        media.start();

        //set default bird
        bird_a = R.drawable.bird_new1;
        bird_b = R.drawable.bird_new2;
        gv.intBird(bird_a, bird_b);
        bscore = "0";
        btn_musicOn.setOnClickListener(view -> {
            btn_musicOff.setVisibility(View.VISIBLE);
            media.pause();
        });

        btn_musicOff.setOnClickListener(view -> {
            btn_musicOff.setVisibility(View.INVISIBLE);
            media.start();
        });
        btn_audioOn.setOnClickListener(view -> {
            btn_audioOff.setVisibility(View.VISIBLE);
            GameView.Loadsound = false;
        });
        btn_audioOff.setOnClickListener(view -> {
            btn_audioOff.setVisibility(View.INVISIBLE);
            GameView.Loadsound = true;
        });

        btn_play.setOnClickListener(view -> {
            gv.setStart(true);
            rl_start.setVisibility((View.INVISIBLE));
            txt_score.setVisibility(View.VISIBLE);
            txt_F_B.setVisibility(View.INVISIBLE);
        });

        txt_restart.setOnClickListener(view -> {
            txt_F_B.setVisibility(View.VISIBLE);
            rl_over.setVisibility(View.INVISIBLE);
            rl_start.setVisibility(View.VISIBLE);
            gv.setStart(false);
            gv.intBird(bird_a, bird_b);
            gv.reset();
        });

        btn_skinChange.setOnClickListener(view -> skinDialog.show());
        showSkinDialog();

        btn_trophy.setOnClickListener(view -> bestScoreDialog.show());
        showScoreDialog(bscore);
    }

    protected void onResume() {
        super.onResume();
        media.start();
    }

    protected void onPause() {
        super.onPause();
        media.pause();
    }

    public void showScoreDialog(String bscore) {
        bestScoreDialog = new Dialog(MainActivity.this);
        bestScoreDialog.setContentView(R.layout.best_score_dialog);
        bestScoreDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg_dialog));
        bestScoreDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bestScoreDialog.setCancelable(true);
        Txt_best_score_dialog = bestScoreDialog.findViewById(R.id.txt_best_score_dialog);
        Txt_best_score_dialog.setText(bscore);
    }

    public void showSkinDialog() {
        skinDialog = new Dialog(MainActivity.this);
        skinDialog.setContentView(R.layout.skindialog);
        skinDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg_dialog));
        skinDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        skinDialog.setCancelable(true);

        btn_bird1 = skinDialog.findViewById(R.id.bird1);
        btn_bird2 = skinDialog.findViewById(R.id.bird2);
        btn_bird3 = skinDialog.findViewById(R.id.bird3);

        btn_bird1.setOnClickListener(view -> {
            bird_a = R.drawable.bird1;
            bird_b = R.drawable.bird2;
            gv.intBird(bird_a, bird_b);
            skinDialog.dismiss();
        });

        btn_bird2.setOnClickListener(view -> {
            bird_a = R.drawable.bird_new1;
            bird_b = R.drawable.bird_new2;
            gv.intBird(bird_a, bird_b);
            skinDialog.dismiss();
        });

        btn_bird3.setOnClickListener(view -> {
            bird_a = R.drawable.twitterlogo;
            bird_b = R.drawable.twitterlogo;
            gv.intBird(bird_a, bird_b);
            skinDialog.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        if(bestScoreDialog.isShowing() || skinDialog.isShowing()) {
            bestScoreDialog.dismiss();
            skinDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
package com.example.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrPipes;
    public static int speedOfPipe;
    private int sumpipe, distance;
    private int score,bestscore;
    private boolean start;
    private Context context;
    private int soundjump;
    public static boolean Loadsound;
    private SoundPool soundPool;

    @SuppressLint("WrongViewCast")
    public GameView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp!=null) {
            bestscore = sp.getInt("bestscore", 0);
        }

        initPipe();
        score=0;
        bestscore=0;
        start=false;
        handler= new Handler();
        r= new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        if(Build.VERSION.SDK_INT>=21) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool = builder.build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Loadsound=true;
            }
        });
        soundjump = this.soundPool.load(context,R.raw.jump_02,1);
    }

    public void intBird(int bird_a,int bird_b) {
        bird = new Bird();
        bird.setWidth(110 * Constaint.SCREEN_WIDTH / 1080);
        bird.setHeight(70 * Constaint.SCREEN_HEIGHT / 1920);
        bird.setX(100 * Constaint.SCREEN_WIDTH / 1080);
        bird.setY(Constaint.SCREEN_HEIGHT / 2 - bird.getHeight() / 2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), bird_a));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), bird_b));
        bird.setArrBms(arrBms);
    }

    private void initPipe() {
        sumpipe = 6;
        speedOfPipe = 5;
        distance = 400 * Constaint.SCREEN_HEIGHT/1920;
        arrPipes = new ArrayList<>();
        for (int i=0; i<sumpipe; i++)
        {
            if(i<sumpipe/2)
            {
                this.arrPipes.add(new Pipe(Constaint.SCREEN_WIDTH+i*((Constaint.SCREEN_WIDTH+200*Constaint.SCREEN_WIDTH/1080)/(sumpipe/2)),
                        0,200*Constaint.SCREEN_WIDTH/1080, Constaint.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.pipe2));
                this.arrPipes.get(this.arrPipes.size()-1).RandomY();
            }
            else
            {
                this.arrPipes.add(new Pipe(this.arrPipes.get(i-sumpipe/2).getX(),this.arrPipes.get(i-sumpipe/2).getY() + this.arrPipes.get(i-sumpipe/2).getHeight()+this.distance,200*Constaint.SCREEN_WIDTH/1080,Constaint.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.pipe1));
            }
        }
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if(start)
        {
            bird.draw(canvas);
            for(int i=0; i<sumpipe;i++)
            {
                if(bird.getRect().intersect(arrPipes.get(i).getRect()) || bird.getY()-bird.getHeight()<0 || bird.getY()>Constaint.SCREEN_HEIGHT)
                {
                    Pipe.speed=0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("Highscore:"+bestscore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.rl_over.setVisibility(VISIBLE);
                    bird.stop();
                }
                if(this.bird.getX()+this.bird.getWidth()>arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2
                        && this.bird.getX()+this.bird.getWidth()<=arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2 + Pipe.speed
                        && i<sumpipe/2 )
                {
                    score++;
                    speedOfPipe = speedOfPipe + 1;
                    Pipe.speed = speedOfPipe * Constaint.SCREEN_WIDTH / 1080;

                    if(score > bestscore) {
                        bestscore = score;
                        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("Highscore:", bestscore);
                        editor.apply();
                        MainActivity.bscore = "" + bestscore;
                    }
                    MainActivity.txt_score.setText("" + score);

                }
                if(this.arrPipes.get(i).getX() < -arrPipes.get(i).getWidth())
                {
                    this.arrPipes.get(i).setX(Constaint.SCREEN_WIDTH);
                    if(i < sumpipe/2)
                    {
                        arrPipes.get(i).RandomY();
                    }
                    else
                    {
                        arrPipes.get(i).setY(this.arrPipes.get(i-sumpipe/2).getY() + this.arrPipes.get(i-sumpipe/2).getHeight()+this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);

            }
        }
        else{
            if(bird.getY()>Constaint.SCREEN_HEIGHT/2.25 && bird.getY()-bird.getHeight()>=0)
            {
                bird.setDrop(-12*Constaint.SCREEN_HEIGHT/1920);
            }
            bird.draw(canvas);
        }
        handler.postDelayed(r, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            bird.setDrop(-12);
        }
        if(Loadsound)
        {
            this.soundPool.play(this.soundjump,(float)0.5, (float)0.5,1,0,1f);
        }
        return true;
    }
    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.txt_score.setText("0");
        score = 0;
        speedOfPipe = 5;
        initPipe();

    }
}

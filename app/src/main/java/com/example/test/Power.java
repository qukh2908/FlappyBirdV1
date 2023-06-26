//package com.example.test;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//
//import java.util.Random;
//
//public class Power extends BaseObject {
//    int speed;
//
//    public Power(float x,float y, int width, int height)
//    {
//        super(x, y, width, height);
//        speed=5*Constaint.SCREEN_WIDTH/1080;
//    }
//
//    public void draw (Canvas canvas)
//    {
//        this.x -= speed;
//        canvas.drawBitmap(this.bm,this.x,this.y,null);
//    }
//
//    public void RandomY()
//    {
//        Random r = new Random();
//        this.y = r.nextInt((0+this.height/4)+1)-this.height/4;
//    }
//
//    @Override
//    public void setBm(Bitmap bm) { this.bm = Bitmap.createScaledBitmap(bm,width ,height ,true); }
//}

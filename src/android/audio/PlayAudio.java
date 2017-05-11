package org.apache.cordova.media.audio;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

import com.example.helloworld.R;

/**
 * Created by Administrator on 2017-05-11.
 */
public class PlayAudio extends Activity{
    private static final int REQ_CODE = 110;
    private String path;
    private ImageButton ib;
    private boolean flag=false;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case  1:
                   ib.setBackground(getResources().getDrawable(R.drawable.qx));
                   break;
               case 2:
                   ib.setBackground(getResources().getDrawable(R.drawable.bf));
                   break;

           }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplay);
        ib= (ImageButton) findViewById(R.id.imageButton);
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getExtras().getString("path", "");
        }
    }
    public void bf(View v){
        if(!flag){
            handler.sendEmptyMessage(1);
            MediaManage.playSound(path, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    flag=false;
                    handler.sendEmptyMessage(2);
                }
            });
        }
        else{
            handler.sendEmptyMessage(2);
            MediaManage.pause();
        }
           flag=!flag;
    }
    public void cok(View v){
        MediaManage.release();
        Intent intent=new Intent();
        intent.putExtra("url",path);
        setResult(REQ_CODE,intent);
        finish();
    }
    public void cno(View v){
        MediaManage.release();
        setResult(REQ_CODE);
        finish();
    }
}

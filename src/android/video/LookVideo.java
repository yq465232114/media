package org.apache.cordova.media.video;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.helloworld.R;

import java.io.File;

/**
 * Created by Administrator on 2017-05-10.
 */
public class LookVideo extends Activity{
    private static final int REQ_CODE = 110;
    private VideoView vv;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookvideo);
        vv= (VideoView) findViewById(R.id.videoView);
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getExtras().getString("path", "");
           File file = new File(path);
            vv.setVideoPath(path);
            vv.start();
            vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);

                }
            });

            vv
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            vv.setVideoPath(path);
                            vv.start();

                        }
                    });
            vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(LookVideo.this, "视频无法播放", Toast.LENGTH_SHORT).show();
                  mp.pause();
                    return true;
                }
            });
        }
    }
   public void cok(View v){

       Intent intent=new Intent();
       intent.putExtra("url",path);
     setResult(REQ_CODE,intent);
       finish();
    }
    public void cno(View v){

        setResult(REQ_CODE);
        finish();
    }
}

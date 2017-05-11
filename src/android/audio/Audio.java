package org.apache.cordova.media.audio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.helloworld.R;

/**
 * Created by Administrator on 2017-05-10.
 */
public class Audio extends Activity{
    private AudioRecorderButton arb;
    private static final int REQ_CODE = 110;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mact);
        arb= (AudioRecorderButton) findViewById(R.id.recorderButton);
        arb.setAudioFinishRecorderListenter(new AudioRecorderButton.AudioFinishRecorderListenter() {
            @Override
            public void onFinish(float seconds, String FilePath) {
                Intent intent=new Intent(Audio.this, PlayAudio.class);
                intent.putExtra("path",FilePath);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE ) {
            String path ="";
            if (data != null) {
                path = data.getExtras().getString("url", "");
            }
            Intent intent=new Intent();
            intent.putExtra("url",path);
            setResult(REQ_CODE,intent);
            finish();
        }
    }
}

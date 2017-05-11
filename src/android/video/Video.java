package org.apache.cordova.media.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.R;

import java.io.IOException;

/**
 * Created by Administrator on 2017-05-10.
 */
public class Video extends Activity {
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private TextView tv;
    private MovieRecorderView mRecorderView;
    private boolean isFinish = true;
    private Button mShootBtn;
    private Handler mHandler = new Handler();
    private static final int REQ_CODE = 110;
    private static final int RES_CODE = 111;
    private boolean flag=true;
    private boolean aflag=false;
    /**
     * 当前进度
     */
    private int currentTime = 0;
    /**
     * 录制进度
     */
    private static final int RECORD_PROGRESS = 100;
    /**
     * 录制结束
     */
    private static final int RECORD_FINISH = 101;
    /**
     * 按下的位置
     */
    private float startY;
    /**
     * 是否触摸在松开取消的状态
     */
    private boolean isTouchOnUpToCancel = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tv.setVisibility(View.INVISIBLE);
                    break;
                case RECORD_PROGRESS:
                    mProgress.setProgress(10-currentTime);
                    break;
                case RECORD_FINISH:
                        isFinish = true;
                        finishActivity();
                    break;
            }
        }
    };
 /*   private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finishActivity();
        }
    };*/
    private void finishActivity() {
        if (isFinish) {
            tv.setVisibility(View.INVISIBLE);
            mRecorderView.stop();
            Log.i("yq",mRecorderView.getRecordFile().toString());
            Intent intent = new Intent(this, LookVideo.class);
            intent.putExtra("path", mRecorderView.getRecordFile().getAbsolutePath());
            startActivityForResult(intent, REQ_CODE);
            // VideoPlayerActivity.startActivity(this, mRecorderView.getVecordFile().toString());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mProgress= (ProgressBar) findViewById(R.id.progressBar);
        tv= (TextView) findViewById(R.id.textView);
        mRecorderView = (MovieRecorderView) findViewById(R.id.surfaceView);
        mShootBtn = (Button) findViewById(R.id.button);
        mShootBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.sendEmptyMessage(1);
                    isFinish = false;//开始录制
                    startY = event.getY();//记录按下的坐标
                    mRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            aflag=true;
                            handler.sendEmptyMessage(RECORD_FINISH);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (startY - event.getY() > 100) {//上移超过一定距离取消录制，删除文件
                        if (!isFinish) {
                            resetData();
                        }
                    } else {
                    if(!aflag)
                    {
                        if (mRecorderView.getTimeCount() > 1)
                            handler.sendEmptyMessage(RECORD_FINISH);
                        else {
                            resetData();
                            Toast.makeText(Video.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
                        }
                    }}
                    aflag=false;
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //根据触摸上移状态切换提示
                    if (startY - event.getY() > 100) {
                        isTouchOnUpToCancel = true;//触摸在松开就取消的位置

                    } else {
                        isTouchOnUpToCancel = false;//触摸在正常录制的位置
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    resetData();
                }
                return true;
            }
        });
        mProgress.setMax(10);
        mProgress.setProgress(10);
        mRecorderView.setOnRecordProgressListener(new MovieRecorderView.OnRecordProgressListener() {
            @Override
            public void onProgressChanged(int maxTime, int currentTime) {
                Video.this.currentTime = currentTime;
                handler.sendEmptyMessage(RECORD_PROGRESS);
            }
        });
    /*    new Thread(new Runnable() {
            @Override
            public void run() {
                while(mProgressStatus < 10){
                    mProgressStatus ++;

                    mHandler.post(new Runnable(){
                        @Override
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                            if(mProgressStatus>3)
                                tv.setVisibility(View.VISIBLE);
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }
    public void pic(View v){
       this.flag=!flag;
        resetData();
    }
    @Override
    public void onResume() {
        super.onResume();
        checkCameraPermission();
    }
    /**
     * 检测摄像头和录音权限
     */
    private void checkCameraPermission() {
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            Toast.makeText(this, "视频录制和录音没有授权", Toast.LENGTH_LONG);
            this.finish();
        } else {
            resetData();
        }*/
        resetData();
    }
    /**
     * 重置状态
     */
    private void resetData() {
        if (mRecorderView.getRecordFile() != null)
            mRecorderView.getRecordFile().delete();
        mRecorderView.stop();
        isFinish = true;
        currentTime = 0;
        mProgress.setProgress(10);
        try {
            mRecorderView.initCamera(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(2);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        mRecorderView.stop();
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

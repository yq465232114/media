package org.apache.cordova.media;

import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.media.audio.Audio;
import org.apache.cordova.media.video.Video;
import org.json.JSONException;

/**
 * Created by Administrator on 2017-05-11.
 */
public class Media extends CordovaPlugin {
    private static final int REQ_CODE = 110;
    private  CallbackContext context;
    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        this.context=callbackContext;
        //根据action判断调用方法
              if(action.equals("audio")){
                      //通过Intent绑定将要调用的Activity
                        Intent intent=new Intent(this.cordova.getActivity(), Audio.class);
                         //加入将要传输到activity中的参数
                       // intent.putExtra("province", args.getString(0));
                        //启动activity
                       this.cordova.startActivityForResult(this, intent, 0);
                    }
        if(action.equals("video")){
            //通过Intent绑定将要调用的Activity
            Intent intent=new Intent(this.cordova.getActivity(), Video.class);
            //加入将要传输到activity中的参数
            // intent.putExtra("province", args.getString(0));
            //启动activity
            this.cordova.startActivityForResult(this, intent, 0);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
       if(resultCode==REQ_CODE){
           String path ="";
           if (intent != null) {
               path = intent.getExtras().getString("url", "");
           }
          if(path.length()>0)
           context.success(path);
           else
              context.error("没有获取到相关媒体文件！");
       }
    }
}

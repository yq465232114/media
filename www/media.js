//cordova.define("Media",
   // function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            audio: function(bk1,bk2){
                exec(
                function(message){//成功回调function
                if(bk1)
                   bk1(message)
                },
                function(message){//失败回调function
                 if(bk2)
                    bk2(message)
                },
                "Media",//feature name
                "audio",//action
                []//要传递的参数，json格式
                );
            },
                video: function(bk1,bk2){
                            exec(
                            function(message){//成功回调function
                              if(bk1)
                              bk1(message)
                            },
                            function(message){//失败回调function
                                  if(bk2)
                                   bk2(message)
                            },
                            "Media",//feature name
                            "video",//action
                            []//要传递的参数，json格式
                            );
                        }
        }
//});
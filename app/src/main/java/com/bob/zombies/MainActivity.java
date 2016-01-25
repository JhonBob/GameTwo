package com.bob.zombies;

import android.app.Activity;
import android.os.Bundle;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends Activity {

    private CCGLSurfaceView ccglSurfaceView;
    private CCDirector ccDirector;
    private CCScene ccScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ccglSurfaceView = new CCGLSurfaceView(this);
        setContentView(ccglSurfaceView);
        //导演(单列模式)
        ccDirector=CCDirector.sharedDirector();
        //开启线程
        ccDirector.attachInView(ccglSurfaceView);
        //设置方向
        ccDirector.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
        //设置屏幕分辨率(自动适配)
        ccDirector.setScreenSize(480,320);
        //显示当前游戏的帧率 30
        ccDirector.setDisplayFPS(true);
        //创建场景
        ccScene=CCScene.node();
        //添加图层
        ccScene.addChild(new MapLayer());
        //导演运行场景
        ccDirector.runWithScene(ccScene);
    }



    @Override
    protected void onResume() {
        super.onResume();
        //绑定生命周期
        ccDirector.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ccDirector.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ccDirector.end();
    }
}

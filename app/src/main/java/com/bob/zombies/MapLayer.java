package com.bob.zombies;

import android.view.MotionEvent;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.particlesystem.CCParticleSnow;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.util.CGPointUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Administrator on 2016/1/25.
 */
public class MapLayer extends CCLayer {

    private CCTMXTiledMap map;
    private List<CGPoint> roads=new ArrayList<>();
    private CCSprite zombie;
    private CCParticleSystem system;

    public MapLayer() {
        setIsTouchEnabled(true);
        init();
    }

    private void init() {
        loadMap();
        loadRood();
        move();

//        for (CGPoint cgPoint:roads){
//            System.out.println(cgPoint);
//        }
        particleSystem();
    }

    //下着鹅毛大雪
    private void particleSystem(){
        system= CCParticleSnow.node();
        system.setTexture(CCTextureCache.sharedTextureCache().addImage("f.png"));
        this.addChild(system, 1);
    }

    private void move() {
        //创建僵尸
        zombie=CCSprite.sprite("z_1_01.png");
        zombie.setPosition(roads.get(0));//起点
        zombie.setScale(0.65f);
        zombie.setFlipX(true);
        zombie.setAnchorPoint(0.5f, 0);
        //地图添加僵尸
        map.addChild(zombie);

        //走起来
        animate();
        //移动
        moveToNext();
    }


    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        map.touchMove(event, map);
        return super.ccTouchesMoved(event);
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        this.onExit();// 游戏暂停了 让整个图层全部暂停
        // 获取到DemoLayer 所在的场景 添加PauseLayer
        this.getParent().addChild(new PauseLayer());
        this.setIsTouchEnabled(false);
        return super.ccTouchesBegan(event);

    }


    // 暂停的图层
    private class PauseLayer extends CCLayer {
        private CCSprite heart;

        public PauseLayer() {
            setIsTouchEnabled(true);// PauseLayer 响应点击事件
            heart = CCSprite.sprite("heart.png");
            // 获取到了屏幕的尺寸
            CGSize winSize = CCDirector.sharedDirector().getWinSize();
            heart.setPosition(winSize.width / 2, winSize.height / 2);
            this.addChild(heart);
        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            CGRect boundingBox = heart.getBoundingBox();
            CGPoint convertTouchToNodeSpace = this
                    .convertTouchToNodeSpace(event);
            if (CGRect.containsPoint(boundingBox, convertTouchToNodeSpace)) {
               MapLayer.this.onEnter(); // 游戏继续
                this.removeSelf();// 隐藏PauseLayer 回收PauseLayer
                MapLayer.this.setIsTouchEnabled(true);

            }
            return super.ccTouchesBegan(event);
        }
    }


    int index=0;
    int speed=50;
    public void moveToNext() {
        index++;
        if (index<roads.size()){
            float distance=CGPointUtil.distance(roads.get(index-1),roads.get(index));
            CCMoveTo ccMoveTo=CCMoveTo.action(distance/speed,roads.get(index));
            //反射调用
            CCCallFunc ccCallFunc=CCCallFunc.action(this,"moveToNext");
            CCSequence ccSequence=CCSequence.actions(ccMoveTo,ccCallFunc);
            zombie.runAction(ccSequence);
        }else {
            system.stopSystem();
            SoundEngine sharedEngine = SoundEngine.sharedEngine();
            // 参数1 上下文 参数2 音乐资源的id 参数3 是否循环播放
            sharedEngine.playSound(CCDirector.theApp, R.raw.psy, true);

            // 停止之前所有的动作
            zombie.stopAllActions();
            // 跳舞蹈
            CCJumpBy by = CCJumpBy.action(1, ccp(20, 20), 20, 2);
            CCSequence ccSequence = CCSequence.actions(by, by.reverse());
            CCRepeatForever forever = CCRepeatForever.action(ccSequence);
            zombie.runAction(forever);
        }
    }

    private void animate() {
        //创建
        ArrayList<CCSpriteFrame> frames=new ArrayList<CCSpriteFrame>();
        String str="z_1_%02d.png";
        for (int i=1;i<=7;i++){
            CCSpriteFrame zombies=CCSprite.sprite(String.format(str,i)).displayedFrame();//转换序列
            frames.add(zombies);
        }
        //加入
        CCAnimation ccAnimation=CCAnimation.animation("walk", 0.2f, frames);
        CCAnimate ccAnimate=CCAnimate.action(ccAnimation);
        CCRepeatForever ccRepeatForever=CCRepeatForever.action(ccAnimate);
        zombie.runAction(ccRepeatForever);
    }

    //解析路径
    private void loadRood() {
        CCTMXObjectGroup objectGroup=map.objectGroupNamed("road");
        ArrayList<HashMap<String,String>> objects=objectGroup.objects;
        for (HashMap<String,String> map:objects){
            int x=Integer.parseInt(map.get("x"));
            int y=Integer.parseInt(map.get("y"));
            CGPoint point=ccp(x,y);
            //记录路径
            roads.add(point);
        }
    }

    private void loadMap() {
        //加载地图
        map=CCTMXTiledMap.tiledMap("map.tmx");
        map.setAnchorPoint(0.5f,0.5f);
        CGSize cgSize=map.getContentSize();
        map.setPosition(cgSize.width/2,cgSize.height/2);
        this.addChild(map);
    }
}

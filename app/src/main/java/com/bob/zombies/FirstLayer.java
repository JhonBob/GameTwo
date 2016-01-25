package com.bob.zombies;

import android.view.MotionEvent;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * Created by Administrator on 2016/1/25.
 */

//图层
public class FirstLayer extends CCLayer {

    public FirstLayer() {
        //点击事件的开关
        setIsTouchEnabled(true);
        //创建精灵
        CCSprite ccSprite=CCSprite.sprite("z_1_attack_01.png");
        //水平翻转
        //ccSprite.setFlipX(true);
        //缩放
        //ccSprite.setScale(3);
        //透明0~255
        //ccSprite.setOpacity(100);
        //设置图片的坐标点
        //ccSprite.setPosition(x,y);
        //设置图片的锚点
        ccSprite.setAnchorPoint(0,0);
        //添加精灵
        //this.addChild(ccSprite);
        //z标识优先级
        //this.addChild(ccSprite,1);
        //tag为精灵标签
        this.addChild(ccSprite,0,10);
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        //android坐标系
//        event.getRawX();
//        event.getRawY();

        CCSprite ccSprite=(CCSprite)this.getChildByTag(10);
        CGRect boundingBox = ccSprite.getBoundingBox();

        //android坐标系转换为cocos2d坐标系,CGPoint是cocos2d坐标系的点
        CGPoint cgPoint=this.convertTouchToNodeSpace(event);
        //影藏
        if (boundingBox.contains(cgPoint.x,cgPoint.y)){
            ccSprite.setOpacity(100);
            //ccSprite.removeSelf();//回收
        }
        return super.ccTouchesBegan(event);
    }
}

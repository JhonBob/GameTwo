package com.bob.zombies;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseIn;
import org.cocos2d.actions.interval.CCBezierBy;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.actions.interval.CCTintBy;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CCBezierConfig;
import org.cocos2d.types.CGSize;

/**
 * Created by Administrator on 2016/1/25.
 */
public class ActionLayer extends CCLayer {


    public ActionLayer() {
        init();
    }

    public void init(){
//        moveTo();
//        moveBy();
//        jumpTO();
//        jumpBy();
        //贝塞尔曲线
//        bezier();
        //加速运动
        //ease();
        //scale();
        //rotateTo();
        //rotateBy();
        //tint();
         //blink();
        //fade();
        //复杂的动作
        zuhe();
    }


    private void zuhe() {
        CCSprite ccSprite=getSprite();
        ccSprite.setAnchorPoint(0.5f,0.5f);
        ccSprite.setPosition(50, 50);// 为了可以正常展示所有僵尸
        // 僵尸跳跃的同时  再加上一个旋转动画
        CCJumpBy by=CCJumpBy.action(4, ccp(200, 200), 50, 2);
        CCRotateBy ccRotateBy=CCRotateBy.action(2, 360);  // 以锚点为中心店 旋转
        CCSpawn ccSpawn=CCSpawn.actions(by, ccRotateBy);//  同时执行两个动作
        CCIntervalAction reverse = ccSpawn.reverse();
        CCSequence ccSequence=CCSequence.actions(ccSpawn, reverse);
        CCRepeatForever ccRepeatForever=CCRepeatForever.action(ccSequence);
        ccSprite.runAction(ccRepeatForever);
    }



    private void fade() {
        CCFadeIn ccFadeIn=CCFadeIn.action(3);
        getSprite().runAction(ccFadeIn);
        CCFadeOut ccFadeOut=CCFadeOut.action(3);
        getSprite().runAction(ccFadeOut);
		ccFadeIn.reverse();
    }

    private void blink() {
        // 参数1  闪烁的时间
        // 参数2 闪烁的次数
        CCBlink blink=CCBlink.action(2, 3);

        getSprite().runAction(blink);
    }

    private void tint() {
        // 创建动作
        // 参数1 变化的时间
        //参数2  变化的颜色改变
        CCTintBy ccTintBy=CCTintBy.action(1, ccc3(100, 50, -100));  //  0-255
        // 特殊的精灵 专门用来显示文字
        // 参数1 要显示的文字
        // 参数2   显示什么字体
        //  参数3 字体的大小
        CCLabel ccLabel =CCLabel.labelWithString("那些年撸代码的日子", "hkbd.ttf", 20);
        ccLabel.setColor(ccc3(0, 50, 200));
        this.addChild(ccLabel); //如果不添加到图层上 是没法显示的
        CCTintBy reverse = ccTintBy.reverse();
        CCSequence actions = CCSequence.actions(ccTintBy, reverse);
        CCRepeatForever action = CCRepeatForever.action(actions);
        // 如何获取到屏幕中间的位置
        CCDirector ccDirector=CCDirector.sharedDirector();
        CGSize winSize = ccDirector.getWinSize();// 获取到了游戏屏幕的尺寸
        ccLabel.setPosition(winSize.width/2, winSize.height/2);
        ccLabel.runAction(action);

    }


    private void rotateBy() {
        CCRotateBy ccRotateBy=CCRotateBy.action(5, 270);
        CCSprite heart = getHeart();
        heart.setPosition(200, 100);
        heart.setAnchorPoint(ccp(0.5f, 0.5f));
        heart.runAction(ccRotateBy);
    }

    private void rotateTo() {  // 偷懒
        //参数1 时间
        // 参数2 旋转角度
        CCRotateTo ccRotateTo=CCRotateTo.action(5, 270);  // 旋转基于 锚点进行旋转
        CCSprite heart = getHeart();
        heart.setPosition(100, 100);
        heart.setAnchorPoint(ccp(0.5f, 0.5f));

        heart.runAction(ccRotateTo);

    }

    private void scale() {
        // 参数1 缩放时间
        // 参数2 缩放后的大小
        CCScaleBy ccScaleBy=CCScaleBy.action(1, 2);      //缩放基于锚点进行缩放
        CCScaleBy reverse = ccScaleBy.reverse();
        CCSequence ccSequence=CCSequence.actions(ccScaleBy, reverse);
        CCRepeatForever forever=CCRepeatForever.action(ccSequence);
        CCSprite heart = getHeart();
        heart.setPosition(100, 100);
        heart.runAction(forever);
    }

    private void ease() {
        CCMoveBy ccMoveBy=CCMoveBy.action(2,ccp(200,0));
        CCEaseIn ccEaseIn=CCEaseIn.action(ccMoveBy,2);
        CCSprite ccSprite=getSprite();
        ccSprite.runAction(ccEaseIn);
    }

    private void bezier() {
        CCBezierConfig c=new CCBezierConfig();
        c.controlPoint_1=ccp(0,0);
        c.controlPoint_2=ccp(100,100);
        c.endPosition=ccp(200,0);
        CCBezierBy bezierBy=CCBezierBy.action(2,c);
        CCSprite ccSprite=getSprite();
        ccSprite.runAction(bezierBy);
    }

    private void jumpBy() {
        CCJumpBy ccJumpBy=CCJumpBy.action(2, ccp(200, 200), 100, 2);
        CCJumpBy reverse=ccJumpBy.reverse();
        //串动作
        CCSequence ccSequence=CCSequence.actions(ccJumpBy,reverse);
        //循环
        CCRepeatForever ccRepeatForever=CCRepeatForever.action(ccSequence);
        CCSprite ccSprite=getSprite();
        ccSprite.runAction(ccRepeatForever);
    }

    private void jumpTO() {
        CCJumpTo ccJumpTo=CCJumpTo.action(2,ccp(200,200),200,1);
        CCSprite ccSprite=getSprite();
        ccSprite.runAction(ccJumpTo);
    }

    private void moveBy() {
        //CCMoveBy有相反动作reverse
        CCMoveBy ccMoveBy=CCMoveBy.action(2,ccp(200,0));
        CCSprite ccSprite=getSprite();
        ccSprite.setPosition(0,100);
        ccSprite.runAction(ccMoveBy);
    }

    public void moveTo(){
        //秒，coco2d坐标点
        CCMoveTo ccMoveTo=CCMoveTo.action(2,ccp(200,0));
        CCSprite ccSprite=getSprite();
        ccSprite.runAction(ccMoveTo);
    }

    public CCSprite getSprite(){
        CCSprite ccSprite=CCSprite.sprite("z_1_attack_01.png");
        ccSprite.setAnchorPoint(0, 0);
        this.addChild(ccSprite);
        return ccSprite;
    }

    public CCSprite getHeart(){
        CCSprite ccSprite=CCSprite.sprite("z_1_01.png");
        ccSprite.setAnchorPoint(0, 0);
        this.addChild(ccSprite);
        return ccSprite;

    }
}

package com.altamirasoft.glanimationutil;

import java.util.ArrayList;

/**
 * Created by bdhwan on 2017. 1. 20..
 */

public class GLAnimatorManager implements GLAnimatorFrameListener,GLValueAnimator.AnimatorListener{

    static GLAnimatorManager instance;


    ArrayList<GLAnimatorFrameListener> animatorList;


    public static GLAnimatorManager getInstance(){
        if(instance==null){
            instance = new GLAnimatorManager();
            instance.animatorList = new ArrayList<GLAnimatorFrameListener>();
        }
        return instance;
    }


    public void addFrameListener(GLAnimatorFrameListener listener){
        this.animatorList.add(listener);
    }



    public void removeFrameListener(GLAnimatorFrameListener listener){
        this.animatorList.remove(listener);
    }





    public GLValueAnimator createValueAnimator(float from, float to){
        GLValueAnimator animator = GLValueAnimator.ofFloat(from,to);
        addFrameListener(animator);

        animator.addListener(this);

        return animator;
    }


    public GLEasingHelper createEasingHelper(){

        GLEasingHelper helper = new GLEasingHelper();
        addFrameListener(helper);
        return helper;

    }




    @Override
    public void doFrame() {

        for(int i =0;i<animatorList.size();i++){
            animatorList.get(i).doFrame();
        }
    }


    @Override
    public void onAnimationStart(GLValueAnimator animation) {

    }

    @Override
    public void onAnimationEnd(GLValueAnimator animation) {
        removeFrameListener(animation);
    }

    @Override
    public void onAnimationCancel(GLValueAnimator animation) {

    }

    @Override
    public void onAnimationRepeat(GLValueAnimator animation) {

    }
}

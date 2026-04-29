// PropObserver.java
package edu.hitsz.observer;

/**
 * 道具观察者接口
 * 所有能受到特殊道具影响的实体（各种敌机、敌机子弹）都需要实现此接口
 */
public interface PropObserver {

    /**
     * 炸弹道具生效时的响应逻辑
     */
    void onBombActive();

    /**
     * 冰冻道具生效时的响应逻辑
     */
    void onIceActive();
}
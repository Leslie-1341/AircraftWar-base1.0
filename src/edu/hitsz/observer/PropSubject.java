// PropSubject.java
package edu.hitsz.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 道具抽象观察目标
 * 负责管理观察者列表，并在自身状态改变时通知它们
 */
public abstract class PropSubject {

    // 维护一个观察者列表
    protected List<PropObserver> observers = new ArrayList<>();

    /**
     * 增加观察者
     */
    public void addObserver(PropObserver observer) {
        observers.add(observer);
    }

    /**
     * 移除观察者
     */
    public void removeObserver(PropObserver observer) {
        observers.remove(observer);
    }

    /**
     * 抽象方法：通知所有观察者
     * 具体通知逻辑由子类（如 BombSupply, IceSupply）实现
     */
    public abstract void notifyObservers();
}
package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.observer.PropObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 炸弹道具 (观察目标 Subject)
 */
public class BombSupply extends AbstractProp {

    // 维护一个观察者列表
    private List<PropObserver> observers = new ArrayList<>();

    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    // 增加观察者
    public void addObserver(PropObserver observer) {
        observers.add(observer);
    }

    // 通知所有观察者
    public void notifyObservers() {
        for (PropObserver observer : observers) {
            observer.onBombActive();
        }
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active! 炸弹引爆，全屏冲击波！");
        // 道具生效时，立刻通知所有观察者
        notifyObservers();
    }
}
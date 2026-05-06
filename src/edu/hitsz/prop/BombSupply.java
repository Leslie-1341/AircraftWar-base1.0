package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.observer.PropObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 炸弹道具 (观察目标 Subject)
 */
public class BombSupply extends AbstractProp {
    
    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    // 通知所有观察者
    public void notifyObservers() {
        for (PropObserver observer : observers) {
            observer.onBombActive();
        }
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active!");
        // 道具生效时，立刻通知所有观察者
        notifyObservers();
    }
}
package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.observer.PropObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 冰冻道具 (观察目标 Subject)
 */
public class FreezeSupply extends AbstractProp {

    public FreezeSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void notifyObservers() {
        for (PropObserver observer : observers) {
            observer.onIceActive();
        }
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        System.out.println("FreezeSupply active! ");
        notifyObservers();
    }
}
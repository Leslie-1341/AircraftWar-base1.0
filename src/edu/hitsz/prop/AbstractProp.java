package edu.hitsz.prop;

import java.util.ArrayList;
import java.util.List;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.observer.PropObserver;

public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 【多态接口】：道具生效
     * @param heroAircraft
     */
    public abstract void active(HeroAircraft heroAircraft);

    @Override
    public void forward() {
        super.forward();
        // 道具向下移动，超出屏幕边界的操作等可以在这里完善
    }

    // ==============================================
    // 将观察者列表上提到父类，供所有道具复用
    // ==============================================
    protected List<PropObserver> observers = new ArrayList<>();

    public void addObserver(PropObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PropObserver observer) {
        observers.remove(observer);
    }
}
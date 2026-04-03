package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;

public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 【新增多态接口】：道具生效
     * @param heroAircraft 吃到道具的英雄机
     */
    public abstract void active(HeroAircraft heroAircraft);

    @Override
    public void forward() {
        super.forward();
        // 道具向下移动，超出屏幕边界的操作等可以在这里完善
    }
}
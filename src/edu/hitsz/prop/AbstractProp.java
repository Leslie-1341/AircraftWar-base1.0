package edu.hitsz.prop;

import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 第一次实验课 搭建道具抽象父类
 */
public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        // 道具目前只需要向下直飞即可
        locationY += speedY;
    }
}
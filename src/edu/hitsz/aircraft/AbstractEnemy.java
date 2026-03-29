package edu.hitsz.aircraft;

/**
 * 敌机抽象父类
 * 所有敌机（普通、精英、Boss等）均继承自此类
 */
public abstract class AbstractEnemy extends AbstractAircraft {

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    // 目前不需要在这个层级添加额外的方法，先把它作为一个干净的中间层
}
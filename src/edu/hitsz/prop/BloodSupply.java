package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class BloodSupply extends AbstractProp {

    public BloodSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        // 【核心重构】：直接调用飞机自身的加血方法，传入回血量 20
        // 上限判断已经由飞机自身 (AbstractAircraft.increaseHp) 完美处理
        heroAircraft.increaseHp(20);

        System.out.println("BloodSupply active! HP +20");
    }
}
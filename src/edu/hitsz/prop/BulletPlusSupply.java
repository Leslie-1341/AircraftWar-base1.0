package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RingShootStrategy;

public class BulletPlusSupply extends AbstractProp {

    public BulletPlusSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        // 【策略模式应用】：超级火力道具生效，切换为环射弹道，子弹设为 10 发
        heroAircraft.setShootNum(10);
        heroAircraft.setShootStrategy(new RingShootStrategy());

        System.out.println("FirePlusSupply active! 切换为环射弹道！");
    }
}
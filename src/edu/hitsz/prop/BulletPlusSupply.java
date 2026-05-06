package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RingShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

public class BulletPlusSupply extends AbstractProp {

    public BulletPlusSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        // 1.【策略模式】：超级火力道具生效，切换为环射弹道
        heroAircraft.setShootNum(10);
        heroAircraft.setShootStrategy(new RingShootStrategy());
        System.out.println("FirePlusSupply active! 切换为环射弹道！");

    // 2. 开启限时恢复线程
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                heroAircraft.setShootStrategy(new StraightShootStrategy());
                System.out.println("超级火力道具已过期，恢复初始射击状态");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();    
    }
}
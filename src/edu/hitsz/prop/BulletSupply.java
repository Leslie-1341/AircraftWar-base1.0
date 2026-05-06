package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

public class BulletSupply extends AbstractProp {

    public BulletSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        // 1.【策略模式】：火力道具生效，切换为散射弹道
        heroAircraft.setShootNum(3);
        heroAircraft.setShootStrategy(new ScatterShootStrategy());

        System.out.println("FireSupply active! 切换为散射弹道！");

    // 2. 开启后台倒计时线程，5秒后恢复默认直射
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                // 恢复为直射策略
                heroAircraft.setShootStrategy(new StraightShootStrategy());
                System.out.println("火力道具已过期，恢复初始射击状态");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();    
    }
}
package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
// 【新增导包】
import edu.hitsz.strategy.ScatterShootStrategy;

public class BulletSupply extends AbstractProp {

    public BulletSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        // 【策略模式应用】：火力道具生效，切换为散射弹道，子弹设为 3 发
        heroAircraft.setShootNum(3);
        heroAircraft.setShootStrategy(new ScatterShootStrategy());

        System.out.println("FireSupply active! 切换为散射弹道！");
    }
}
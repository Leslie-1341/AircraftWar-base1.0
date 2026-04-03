package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class BulletPlusSupply extends AbstractProp {

    public BulletPlusSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

        @Override
        public void active(HeroAircraft heroAircraft) {
            System.out.println("BulletPlusSupply active!");
        }
    // 本次迭代只需定义，暂不需要写具体生效逻辑
}
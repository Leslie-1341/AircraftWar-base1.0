package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 加血道具
 */
public class FreezeSupply extends AbstractProp {

    public FreezeSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    @Override
    public void active(HeroAircraft heroAircraft) {
        System.out.println("FreezeSupply active!");
    }
    // 本次迭代只需定义，暂不需要写具体生效逻辑
}
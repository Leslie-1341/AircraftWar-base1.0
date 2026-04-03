package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class BloodSupply extends AbstractProp {

    public BloodSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        // 加血逻辑转移到这里
        heroAircraft.decreaseHp(-20);
        if (heroAircraft.getHp() > 100) {
            heroAircraft.decreaseHp(heroAircraft.getHp() - 100);
        }
        System.out.println("BloodSupply active! HP +20");
    }
}
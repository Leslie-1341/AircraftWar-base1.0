package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 直射弹道策略
 * 支持单排、双排、多排的直线射击
 */
public class StraightShootStrategy implements ShootStrategy {

    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();

        // 1. 获取当前飞机的基础射击属性
        int direction = aircraft.getDirection();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction * 5;
        int shootNum = aircraft.getShootNum();
        int power = aircraft.getPower();

        // 2. 生成子弹
        for (int i = 0; i < shootNum; i++) {
            BaseBullet bullet;
            // 当 shootNum=1 时，偏移量为 0（正中间）
            // 当 shootNum=2 时，偏移量为 -10 和 10（左右双排）
            int bulletX = x + (i * 2 - shootNum + 1) * 10;

            // 根据飞机的身份生成对应的子弹
            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(bulletX, y, speedX, speedY, power);
            } else {
                bullet = new EnemyBullet(bulletX, y, speedX, speedY, power);
            }
            res.add(bullet);
        }

        return res;
    }
}
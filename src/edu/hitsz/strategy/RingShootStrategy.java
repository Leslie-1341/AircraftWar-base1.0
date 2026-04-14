package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 环射弹道策略 (Strategy Pattern)
 * 向四周 360 度均匀发射子弹
 */
public class RingShootStrategy implements ShootStrategy {

    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();

        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection() * 2;
        int shootNum = aircraft.getShootNum();
        int power = aircraft.getPower();

        // 环射的基础飞行速度（半径扩散速度）
        int baseSpeed = 5;

        for (int i = 0; i < shootNum; i++) {
            // 【数学公式】：将 360 度（2π）平均分成 shootNum 份
            double angle = i * 2 * Math.PI / shootNum;

            // 利用三角函数计算 X 和 Y 方向的分量速度
            int speedX = (int) (baseSpeed * Math.cos(angle));
            int speedY = (int) (baseSpeed * Math.sin(angle));

            BaseBullet bullet;
            if (aircraft instanceof HeroAircraft) {
                bullet = new HeroBullet(x, y, speedX, speedY, power);
            } else {
                bullet = new EnemyBullet(x, y, speedX, speedY, power);
            }
            res.add(bullet);
        }

        return res;
    }
}
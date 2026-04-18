package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 散射弹道策略 (Strategy Pattern)
 * 向前发射扇形分布的子弹
 */
public class ScatterShootStrategy implements ShootStrategy {

    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();

        int direction = aircraft.getDirection();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedY = aircraft.getSpeedY() + direction * 5;

        // 获取飞机的发射数量（王牌敌机在构造函数里设置了 shootNum = 3）
        int shootNum = aircraft.getShootNum();
        int power = aircraft.getPower();

        for (int i = 0; i < shootNum; i++) {
            // 【数学公式】：计算 X 轴的横向散射速度
            // 当 shootNum = 3 时，(i - 1) * 3 会依次生成 -3, 0, 3 的横向速度 speedX
            int speedX = (int) ((i - (shootNum - 1) / 2.0) * 3);

            BaseBullet bullet;
            // 散射的所有子弹初始 x 坐标都是飞机的中心点，由 speedX 实现散开效果
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
package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 不发射子弹策略
 * 专门供普通敌机使用
 */
public class NoneShootStrategy implements ShootStrategy {

    @Override
    public List<BaseBullet> doShoot(AbstractAircraft aircraft) {
        // 返回空列表，不生成任何子弹
        return new LinkedList<>();
    }
}
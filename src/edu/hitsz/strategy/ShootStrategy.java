package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import java.util.List;

/**
 * 射击策略接口 (Strategy Pattern)
 * 充当抽象策略角色，规范所有的射击算法
 */
public interface ShootStrategy {

    /**
     * 执行射击算法
     * * @param aircraft 调用该策略的飞机上下文。
     * 【核心设计】：将飞机自身作为参数传入，这样具体的策略实现类
     * 就可以通过 aircraft.getLocationX()、aircraft.getDirection()
     * 等方法获取当前飞机的实时状态数据，从而计算出子弹的生成位置。
     * @return 生成的子弹列表
     */
    List<BaseBullet> doShoot(AbstractAircraft aircraft);

}
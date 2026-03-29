package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控  继承于飞机类
 * @author hitsz
 */
//第一次实验课，重构英雄机代码，改为双重锁的单例模式
public class HeroAircraft extends AbstractAircraft {

    // 每次射击发射子弹数量
    private int shootNum = 3;
    // 子弹威力
    private int power = 30;
    // 子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = -1;

    // 【修改 1】声明一个私有、静态、带有 volatile 关键字的唯一实例变量
    private volatile static HeroAircraft instance;

    // 【修改 2】将构造函数的 public 改为 private，禁止外部直接 new
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    // 【修改 3】提供全局唯一的访问点（双重检查锁定 DCL）
    public static HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp) {
        // 第一重检查：如果不为空，直接返回，提高效率
        if (instance == null) {
            // 加上同步锁，确保多线程环境下的线程安全
            synchronized (HeroAircraft.class) {
                // 第二重检查：拿到锁后再判断一次，防止多个线程同时通过了第一重检查
                if (instance == null) {
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }
    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }

}

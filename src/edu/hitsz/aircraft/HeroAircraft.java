package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.StraightShootStrategy;

/**
 * 英雄飞机，游戏玩家操控  继承于飞机类
 * @author hitsz
 */
//第一次实验课，重构英雄机代码，改为双重锁的单例模式
public class HeroAircraft extends AbstractAircraft {
    // 【修改 1】声明一个私有、静态、带有 volatile 关键字的唯一实例变量
    private volatile static HeroAircraft instance;

    // 【修改 2】将构造函数的 public 改为 private，禁止外部直接 new
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // ==========================================
        // 【策略模式修改】：在这里初始化继承自父类的射击属性
        // ==========================================
        // 每次射击发射子弹数量 (初始无道具状态下，改为单发直射)
        this.shootNum = 1;
        // 子弹威力
        this.power = 30;
        // 子弹射击方向 (向上发射：-1，向下发射：1)
        this.direction = -1;

        // 【核心操作】：初始时装备默认的直射策略
        this.shootStrategy = new StraightShootStrategy();
    }

    // 【修改 3】提供全局唯一的访问点（双重检查锁定 DCL）
    // 【第二次实验课已修改】取消外部传参，将坐标、速度和血量的初始化逻辑移入内部，实现创建与使用彻底分离
    public static HeroAircraft getInstance() {
        // 第一重检查：如果不为空，直接返回，提高效率
        if (instance == null) {
            // 加上同步锁，确保多线程环境下的线程安全
            synchronized (HeroAircraft.class) {
                // 第二重检查：拿到锁后再判断一次，防止多个线程同时通过了第一重检查
                if (instance == null) {
                    // 在内部完成实例化参数的计算和赋值
                    instance = new HeroAircraft(
                            Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0,
                            0,
                            200
                    );
                }
            }
        }
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

}
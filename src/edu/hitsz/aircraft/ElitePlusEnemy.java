package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
// 【新增导包】引入直射策略
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.LinkedList;
import java.util.List;

// 注意：移除了 BaseBullet 和 EnemyBullet 的导包

public class ElitePlusEnemy extends AbstractEnemy {

    // 【修改】属性提取任务已在 AbstractAircraft 中完成，此处的私有属性声明已删除！

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // ==========================================
        // 【策略模式修改】：初始化继承自父类的射击属性
        // ==========================================
        // 每次射击发射子弹数量 (精锐敌机为双排直射)
        this.shootNum = 2;
        // 子弹威力
        this.power = 20;
        // 子弹射击方向 (向上发射：-1，向下发射：1)
        this.direction = 1;

        // 【核心操作】：装备直射策略武器！策略类会根据 shootNum=2 自动计算双排偏移量
        this.shootStrategy = new StraightShootStrategy();
    }

    @Override
    public void forward() {
        super.forward();

        // 【核心修复】：解决边缘抖动和飞出边界的问题
        // 1. 使用 Math.abs 强制设定方向，防止在边界处来回抽搐
        // 2. 在右侧边界减去一定的机身余量（比如 60 像素），防止右半边机身飞出屏幕
        if (locationX <= 0) {
            speedX = Math.abs(speedX); // 强制向右飞
        } else if (locationX >= Main.WINDOW_WIDTH - 60) {
            speedX = -Math.abs(speedX); // 强制向左飞
        }

        // 向下飞出屏幕边界后回收名额
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    // ==========================================
    // 【代码大瘦身】
    // 原有硬编码两发子弹的 shoot() 方法已被彻底删除！
    // ==========================================

    @Override
    public int getScore() {
        return 30; // 精锐敌机 30 分
    }

//    @Override
//    public List<AbstractProp> dropProps() {
//        List<AbstractProp> res = new LinkedList<>();
//        if (Math.random() < 0.5) {
//            String[] propTypes = {"Blood", "Bullet", "BulletPlus", "Bomb"}; // 4种道具
//            String selectedType = propTypes[(int) (Math.random() * propTypes.length)];
//            res.add(PropFactory.createProp(selectedType, this.getLocationX(), this.getLocationY(), 0, 5));
//        }
//        return res;
//    }
    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> res = new LinkedList<>();

        // 前提：精锐敌机本身有 50% 的概率决定“是否爆装备”
        if (Math.random() < 0.5) {

            // 生成一个 0.0 到 1.0 之间的随机数，用于决定“爆什么装备”
            double prob = Math.random();
            String selectedType;

            // 【核心逻辑：区间划分法】
            if (prob < 0.5) {
                // 落入 [0.0, 0.5) 区间，概率 30%
                selectedType = "Blood";
            } else if (prob < 0.8) {
                // 落入 [0.3, 0.6) 区间，概率 30%
                selectedType = "Bullet";
            } else {
                // 落入 [0.6, 0.8) 区间，概率 20%
                selectedType = "BulletPlus";
            }

            res.add(PropFactory.createProp(selectedType, this.getLocationX(), this.getLocationY(), 0, 5));
        }
        return res;
    }
}
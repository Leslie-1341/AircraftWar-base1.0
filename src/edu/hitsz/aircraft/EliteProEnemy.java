package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
// 【新增导包】引入即将编写的散射策略
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.LinkedList;
import java.util.List;

// 注意：移除了 BaseBullet 和 EnemyBullet 的导包

public class EliteProEnemy extends AbstractEnemy {

    // 【修改】属性提取任务已在 AbstractAircraft 中完成，此处的私有属性声明已删除！

    public EliteProEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // ==========================================
        // 【策略模式修改】：初始化继承自父类的射击属性
        // ==========================================
        // 每次射击发射子弹数量 (王牌敌机为3发扇形散射)
        this.shootNum = 3;
        // 子弹威力
        this.power = 30; // 伤害更高
        // 子弹射击方向
        this.direction = 1;

        // 【核心操作】：装备散射策略武器！
        this.shootStrategy = new ScatterShootStrategy();
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
    // 原有扇形散射的 shoot() 方法已被彻底删除！
    // 具体的发射逻辑将由父类委托给 ScatterShootStrategy 处理。
    // ==========================================

    @Override
    public int getScore() {
        return 50; // 王牌敌机 50 分
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> res = new LinkedList<>();

        // 前提：王牌敌机本身有 50% 的概率决定“是否爆装备”
        if (Math.random() < 0.5) {

            // 生成一个 0.0 到 1.0 之间的随机数，用于决定“爆什么装备”
            double prob = Math.random();
            String selectedType;

            // 【核心逻辑：区间划分法】
            if (prob < 0.3) {
                // 落入 [0.0, 0.3) 区间，概率 30%
                selectedType = "Blood";
            } else if (prob < 0.6) {
                // 落入 [0.3, 0.6) 区间，概率 30%
                selectedType = "Bullet";
            } else if (prob < 0.8) {
                // 落入 [0.6, 0.8) 区间，概率 20%
                selectedType = "Bomb";
            } else if (prob < 0.9) {
                // 落入 [0.8, 0.9) 区间，概率 10%
                selectedType = "Freeze";
            } else {
                // 落入 [0.9, 1.0) 区间，概率 10%
                selectedType = "BulletPlus";
            }

            res.add(PropFactory.createProp(selectedType, this.getLocationX(), this.getLocationY(), 0, 5));
        }
        return res;
    }
}
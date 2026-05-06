package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class ElitePlusEnemy extends AbstractEnemy {
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

        // 【核心操作】：装备直射策略武器，策略类会根据 shootNum=2 自动计算双排偏移量
        this.shootStrategy = new StraightShootStrategy();
    }

    @Override
    public void forward() {
        super.forward();

        // 【核心修复】：解决边缘抖动和飞出边界的问题
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

    @Override
    public int getScore() {
        return 30; // 精锐敌机 30 分
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> res = new LinkedList<>();

        // 精锐敌机有 50% 的概率决定是否掉落道具
        if (Math.random() < 0.5) {
            // 生成一个 0.0 到 1.0 之间的随机数，用于决定掉落哪种道具
            double prob = Math.random();
            String selectedType;

            if (prob < 0.5) {
                selectedType = "Blood";
            } else if (prob < 0.8) {
                selectedType = "Bullet";
            } else {
                selectedType = "BulletPlus";
            }

            res.add(PropFactory.createProp(selectedType, this.getLocationX(), this.getLocationY(), 0, 5));
        }
        return res;
    }

    @Override
    public void onIceActive() {
        this.freezeWithRecovery(3000);
    }
}
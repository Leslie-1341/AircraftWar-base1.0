package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
// 引入环射策略
import edu.hitsz.strategy.RingShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends AbstractEnemy {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // ==========================================
        // 【策略模式配置】
        // ==========================================
        this.shootNum = 20;  // 单次同时发射 20 颗子弹
        this.power = 10;     // 子弹伤害
        this.direction = 1;  // 向下射击

        // 装备环射弹道策略！
        this.shootStrategy = new RingShootStrategy();
    }

    @Override
    public void forward() {
        super.forward();

        // 【Boss敌机移动逻辑】：悬浮于界面上方，仅左右移动，不向下移动
        // 触壁反弹
        if (locationX <= 0) {
            speedX = Math.abs(speedX); // 撞左墙，向右飞
        } else if (locationX >= Main.WINDOW_WIDTH - 60) {
            speedX = -Math.abs(speedX); // 撞右墙，向左飞
        }
    }

    @Override
    public int getScore() {
        return 100; // 击毁 Boss 得 100 分 (分值可自定义)
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> res = new LinkedList<>();

        // 【Boss敌机掉落道具】：必定掉落 3 个道具，采用最新设计的差异化掉落概率
        for (int i = 0; i < 3; i++) {

            // 生成一个 0.0 到 1.0 之间的随机数
            double prob = Math.random();
            String selectedType;

            // 【区间划分法确定掉落哪种道具】
            if (prob < 0.3) {
                selectedType = "Blood";       // 30% 概率掉落加血
            } else if (prob < 0.6) {
                selectedType = "Bullet";      // 30% 概率掉落火力
            } else if (prob < 0.8) {
                selectedType = "Bomb";        // 20% 概率掉落炸弹
            } else if (prob < 0.9) {
                selectedType = "Freeze";      // 10% 概率掉落冰冻
            } else {
                selectedType = "BulletPlus";  // 10% 概率掉落超级火力
            }

            // 给 3 个道具的 x 坐标加上 (-30, 0, 30) 的横向偏移量，使其散开不完全重叠
            int dropX = this.getLocationX() + (i - 1) * 30;

            AbstractProp prop = PropFactory.createProp(selectedType, dropX, this.getLocationY(), 0, 5);
            if (prop != null) {
                res.add(prop);
            }
        }
        return res;
    }
}
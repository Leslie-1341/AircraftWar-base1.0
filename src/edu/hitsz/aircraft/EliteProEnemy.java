package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
// 【新增导包】引入即将编写的散射策略
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class EliteProEnemy extends AbstractEnemy {
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

        // 【核心操作】：装备散射策略
        this.shootStrategy = new ScatterShootStrategy();
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
        return 50; // 王牌敌机 50 分
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> res = new LinkedList<>();

        // 牌敌机有 50% 的概率决定是否掉落道具
        if (Math.random() < 0.5) {
            double prob = Math.random();
            String selectedType;

            if (prob < 0.3) {
                selectedType = "Blood";
            } else if (prob < 0.6) {
                selectedType = "Bullet";
            } else if (prob < 0.8) {
                selectedType = "Bomb";
            } else if (prob < 0.9) {
                selectedType = "Freeze";
            } else {
                selectedType = "BulletPlus";
            }

            res.add(PropFactory.createProp(selectedType, this.getLocationX(), this.getLocationY(), 0, 5));
        }
        return res;
    }
}
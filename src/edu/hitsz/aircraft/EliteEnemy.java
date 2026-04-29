package edu.hitsz.aircraft;

import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractEnemy {
    // TODO：把子类中共有的参数提取到父类中（第三次实验课已完成）

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // ==========================================
        // 【策略模式修改】：初始化继承自父类的射击属性
        // ==========================================
        // 每次射击发射子弹数量
        this.shootNum = 1;
        // 子弹威力
        this.power = 10;
        // 子弹射击方向 (向上发射：-1，向下发射：1)
        this.direction = 1;

        // 【核心操作】：装备直射策略武器
        this.shootStrategy = new StraightShootStrategy();
    }

    @Override
    public void forward() {
        super.forward();
        // 敌机向下移动
        locationY += speedY;
    }

    @Override
    public int getScore() {
        return 20; // 精英敌机 20 分
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> res = new LinkedList<>();
        // 50% 概率掉落
        if (Math.random() < 0.5) {
            String[] propTypes = {"Blood", "Bullet", "BulletPlus"}; // 3种基础道具
            String selectedType = propTypes[(int) (Math.random() * propTypes.length)];
            AbstractProp prop = PropFactory.createProp(selectedType, this.getLocationX(), this.getLocationY(), 0, 5);
            if (prop != null) {
                res.add(prop);
            }
        }
        return res;
    }

    @Override
    public void onIceActive() {
        // 精英敌机静止 4s[cite: 2]
        this.freezeWithRecovery(4000);
    }

    // SuperEliteEnemy 同理，只要在它的类里写 this.freezeWithRecovery(3000); 即可[cite: 2]
    // 炸弹逻辑都不用写，直接继承父类的坠毁。
}
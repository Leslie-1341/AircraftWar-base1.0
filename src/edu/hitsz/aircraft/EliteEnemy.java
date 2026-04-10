package edu.hitsz.aircraft;

import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
// 【新增导包】引入直射策略
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.LinkedList;
import java.util.List;

// 注意：这里移除了 BaseBullet 和 EnemyBullet 的导包，因为不需要自己造子弹了

public class EliteEnemy extends AbstractEnemy {
    // TODO：把子类中共有的参数提取到父类中（第三次实验课需完成）
    // 【修改】属性提取任务已在 AbstractAircraft 中完成，此处的私有属性声明已删除！

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // ==========================================
        // 【策略模式修改】：初始化继承自父类的射击属性
        // ==========================================
        // 每次射击发射子弹数量 (精英敌机为单排直射)
        this.shootNum = 1;
        // 子弹威力
        this.power = 10;
        // 子弹射击方向 (向上发射：-1，向下发射：1)
        this.direction = 1;

        // 【核心操作】：装备直射策略武器！
        this.shootStrategy = new StraightShootStrategy();
    }

    @Override
    public void forward() {
        super.forward();
        // 敌机向下移动
        locationY += speedY;
    }

    // ==========================================
    // 【代码大瘦身】
    // 原有 shoot() 方法已被彻底删除！
    // 具体的发射逻辑将由父类委托给 StraightShootStrategy 处理。
    // ==========================================

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
}
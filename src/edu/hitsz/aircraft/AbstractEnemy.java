package edu.hitsz.aircraft;

import edu.hitsz.prop.AbstractProp;
import java.util.List;

/**
 * 敌机抽象父类
 * 继承自飞行物抽象类，并为所有敌机定义了特有的多态规范
 */
//子类共有属性提取至父类中
public abstract class AbstractEnemy extends AbstractAircraft {

    /**
     * 敌机类的构造函数
     * 直接调用父类 AbstractAircraft 的构造函数完成基础属性的初始化
     *
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX    X 轴速度
     * @param speedY    Y 轴速度
     * @param hp        初始血量
     */
    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    /**
     * 【多态接口 1】获取击毁该敌机获得的分数
     * 交由各个具体的敌机子类去实现自己对应的分数
     * * @return 击毁分数
     */
    public abstract int getScore();

    /**
     * 【多态接口 2】获取该敌机掉落的道具集合
     * 交由各个具体的敌机子类去实现自己的掉落概率和掉落种类
     * * @return 掉落的道具列表（如果不掉落任何道具，应返回空列表而非 null）
     */
    public abstract List<AbstractProp> dropProps();

    // 注意：像 forward() 和 shoot() 等飞行和射击的基本抽象方法，
    // 已经在更上层的 AbstractAircraft 中定义过了，这里不需要重复声明，
    // 具体的敌机子类（如 EliteEnemy）会直接重写那些方法。
}
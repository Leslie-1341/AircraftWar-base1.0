package edu.hitsz.factory;

import edu.hitsz.prop.*;

/**
 * 道具简单工厂类
 * 严格参照指导书简单工厂模式示例
 */
public class PropFactory {

    /**
     * 静态生产方法
     * @param propType 道具类型字符串 (如 "Blood", "Bullet", "Bomb" 等)
     * @param x X坐标
     * @param y Y坐标
     * @param speedX X轴速度
     * @param speedY Y轴速度
     * @return 对应的道具实例
     */
    public static AbstractProp createProp(String propType, int x, int y, int speedX, int speedY) {
        switch (propType) {
            case "Blood":
                return new BloodSupply(x, y, speedX, speedY);
            case "Bullet":
                return new BulletSupply(x, y, speedX, speedY);
            case "BulletPlus":
                return new BulletPlusSupply(x, y, speedX, speedY);
            case "Bomb":
                return new BombSupply(x, y, speedX, speedY);
            case "Freeze":
                return new FreezeSupply(x, y, speedX, speedY);
            default:
                // 严格对齐指导书：处理未知类型时抛出异常
                throw new IllegalArgumentException("Unknown product type！Type: " + propType);
        }
    }
}
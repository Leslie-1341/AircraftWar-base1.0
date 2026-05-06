package edu.hitsz.aircraft;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 所有飞机的抽象父类
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {

    //最大生命值
    protected int maxHp;
    protected int hp;
    //传参给shoot方法（参数列表不超过4个）
    // ==========================================
    // 将射击属性统一上提至父类
    // ==========================================
    protected int shootNum = 1;  // 默认发弹数量
    protected int power = 10;    // 默认子弹威力
    protected int direction = 1; // 默认射击方向 (1向下，-1向上)

    // ==========================================
    // 维护一个策略接口的引用 (Context 聚合 Strategy)
    // ==========================================
    protected ShootStrategy shootStrategy;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    // 原有的扣血方法
    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    // ==========================================
    // 【新增】：利用自带的 maxHp 属性进行动态上限限制
    // ==========================================
    public void increaseHp(int increase) {
        hp += increase;
        // 如果加血后超过了这架飞机的最大血量，就回退到最大血量
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public int getHp() {
        return hp;
    }

    // ==========================================
    // 【新增】提供动态设置/切换策略的方法
    // ==========================================
    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    /**
     * 飞机射击方法
     * 【核心修改】：移除 abstract 关键字，改为委托给策略类执行
     * @return 子弹列表
     */
    public List<BaseBullet> shoot() {
        // 如果当前飞机没有配置策略（例如普通敌机），则不发射子弹
        if (this.shootStrategy == null) {
            return new LinkedList<>();
        }
        // 委托给具体的策略对象执行，并将自身传递过去
        return this.shootStrategy.doShoot(this);
    }

    // ==========================================
    // 【新增 4】为外部策略类提供获取自身状态的接口
    // ==========================================
    public int getShootNum() {
        return shootNum;
    }

    public int getPower() {
        return power;
    }

    public int getDirection() {
        return direction;
    }
    // ==========================================
    // 提供修改子弹数量的方法，供道具生效时调用
    // ==========================================
    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }
}
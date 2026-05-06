package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.NoneShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机 继承于飞机类
 * 不可射击、不掉落道具
 * @author hitsz
 */
public class MobEnemy extends AbstractEnemy {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);

        // ==========================================
        // 【策略模式】：普通敌机不发子弹，不需要配 power 等参数
        // 直接为其装备“不发射策略”即可
        // ==========================================
        this.shootStrategy = new NoneShootStrategy();
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public int getScore() {
        return 10; // 普通敌机 10 分
    }

    @Override
    public List<AbstractProp> dropProps() {
        // 普通敌机不掉落道具，返回空列表
        return new LinkedList<>();
    }

    @Override
    public void onIceActive() {
        System.out.println("普通敌机被永久冰冻！");
        // 永久静止，直接把速度设为0，不需要开线程恢复
        this.setSpeedY(0);
        this.setSpeedX(0);
    }
    // onBombActive 不用写，继承父类的“直接坠毁”即可
}
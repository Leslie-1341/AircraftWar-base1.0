package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
// 【修改导包】移除了 BaseBullet 的导入，因为不用亲自造子弹了
import edu.hitsz.prop.AbstractProp;
// 【新增导包】引入不发射策略
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
        // 【策略模式修改】：普通敌机不发子弹，所以不需要配 power 等参数
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
}
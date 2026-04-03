package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends AbstractEnemy {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // Boss 的移动逻辑（通常是停留在屏幕上方左右移动，本次迭代暂不需要完善）
    }

    @Override
    public List<BaseBullet> shoot() {
        // Boss 的射击逻辑（本次迭代暂不出场，返回空列表）
        return new LinkedList<>();
    }

    @Override
    public int getScore() {
        return 100; // 击毁 Boss 得 100 分
    }

    @Override
    public List<AbstractProp> dropProps() {
        // 返回空列表占位，满足编译器要求
        return new LinkedList<>();
    }
}
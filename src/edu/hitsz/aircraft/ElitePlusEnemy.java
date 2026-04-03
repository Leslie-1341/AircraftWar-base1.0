package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

public class ElitePlusEnemy extends AbstractEnemy {

    private int power = 20; // 子弹伤害
    private int direction = 1; // 子弹向下射击

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();

        // 【核心修复】：解决边缘抖动和飞出边界的问题
        // 1. 使用 Math.abs 强制设定方向，防止在边界处来回抽搐
        // 2. 在右侧边界减去一定的机身余量（比如 60 像素），防止右半边机身飞出屏幕
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
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * (this.getHeight() / 2 + 10);

        // 【任务5】双排子弹 (X坐标分别向左和向右偏移20)
        res.add(new EnemyBullet(x - 20, y, 0, 15, power));
        res.add(new EnemyBullet(x + 20, y, 0, 15, power));
        return res;
    }

    @Override
    public int getScore() {
        return 30; // 精锐敌机 30 分
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> res = new LinkedList<>();
        if (Math.random() < 0.5) {
            String[] propTypes = {"Blood", "Bullet", "BulletPlus", "Bomb"}; // 4种道具
            String selectedType = propTypes[(int) (Math.random() * propTypes.length)];
            res.add(PropFactory.createProp(selectedType, this.getLocationX(), this.getLocationY(), 0, 5));
        }
        return res;
    }
}
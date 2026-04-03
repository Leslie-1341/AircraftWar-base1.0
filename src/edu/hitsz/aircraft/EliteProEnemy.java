package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

public class EliteProEnemy extends AbstractEnemy {

    private int power = 30; // 伤害更高
    private int direction = 1;

    public EliteProEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
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

        // 【任务5】扇形散射弹道 (分别向左斜、直下、右斜发射)
        res.add(new EnemyBullet(x, y, -3, 15, power)); // 左侧斜射 (speedX为-3)
        res.add(new EnemyBullet(x, y, 0, 15, power));  // 直射
        res.add(new EnemyBullet(x, y, 3, 15, power));  // 右侧斜射 (speedX为3)
        return res;
    }

    @Override
    public int getScore() {
        return 50; // 王牌敌机 50 分
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> res = new LinkedList<>();
        if (Math.random() < 0.5) {
            String[] propTypes = {"Blood", "Bullet", "BulletPlus", "Bomb", "Freeze"}; // 全部5种道具
            String selectedType = propTypes[(int) (Math.random() * propTypes.length)];
            res.add(PropFactory.createProp(selectedType, this.getLocationX(), this.getLocationY(), 0, 5));
        }
        return res;
    }
}
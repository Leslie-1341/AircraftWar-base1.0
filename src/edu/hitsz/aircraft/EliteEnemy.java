package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractEnemy {

    // 每次射击发射子弹数量
    private int shootNum = 1;
    // 子弹威力
    private int power = 10;
    // 子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = 1;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 敌机向下移动
        locationY += speedY;
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        // 利用 getHeight()/2 把子弹初始位置推到机头，再稍微加一点偏移
        int y = this.getLocationY() + direction * (this.getHeight() / 2 + 10);
        int speedX = 0;
        int speedY = this.getSpeedY() + direction * 4;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
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
}
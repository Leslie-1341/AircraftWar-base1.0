package edu.hitsz.bullet;

import edu.hitsz.observer.PropObserver;

/**
 * 敌机子弹
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements PropObserver {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    // ==========================================
    // 观察者模式响应方法
    // ==========================================

    @Override
    public void onBombActive() {
        System.out.println("敌机子弹被炸弹冲击波清空！");
        this.vanish(); // 敌机子弹直接消失[cite: 2]
    }

    @Override
    public void onIceActive() {
        // 敌机子弹静止 5s 后恢复[cite: 2]
        System.out.println("敌机子弹被冰冻 5 秒！");
        Runnable r = () -> {
            int originalSpeedY = this.getSpeedY();
            int originalSpeedX = this.getSpeedX();
            this.setSpeedY(0);
            this.setSpeedX(0);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.notValid()) {
                this.setSpeedY(originalSpeedY);
                this.setSpeedX(originalSpeedX);
            }
        };
        new Thread(r).start();
    }
}
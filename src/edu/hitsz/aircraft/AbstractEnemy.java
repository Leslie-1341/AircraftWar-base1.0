package edu.hitsz.aircraft;

import edu.hitsz.observer.PropObserver;
import edu.hitsz.prop.AbstractProp;

import java.util.List;

/**
 * 敌机抽象父类
 * 继承自飞行物抽象类，并为所有敌机定义了特有的多态规范
 */
// 接入 PropObserver 接口，使其能够接收道具生效通知
public abstract class AbstractEnemy extends AbstractAircraft implements PropObserver {

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public abstract int getScore();

    public abstract List<AbstractProp> dropProps();

    // ==========================================
    // 观察者模式响应方法
    // ==========================================

    /**
     * 默认的炸弹响应：绝大多数敌机（普通、精英、精锐）遭到炸弹攻击直接坠毁
     * 子类（Boss、王牌）可重写此方法以实现特殊响应
     */
    @Override
    public void onBombActive() {
        System.out.println(this.getClass().getSimpleName() + " 遭到炸弹攻击，敌机坠毁！");
        this.vanish(); // 标记为无效，等待后续清理
    }

    /**
     * 通用的冰冻恢复机制（多线程）
     * 由于不同敌机冰冻时间不同，在父类中提供此模板方法供子类调用
     *
     * @param sleepTimeMs 冰冻时间（毫秒）
     */
    protected void freezeWithRecovery(int sleepTimeMs) {
        System.out.println(this.getClass().getSimpleName() + " 被冰冻 " + sleepTimeMs + " 毫秒！");
        Runnable r = () -> {
            // 1. 记录原速度
            int originalSpeedY = this.getSpeedY();
            int originalSpeedX = this.getSpeedX();
            // 2. 速度归零（冻结）
            this.setSpeedY(0);
            this.setSpeedX(0);
            try {
                // 3. 阻塞当前倒计时线程
                Thread.sleep(sleepTimeMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 4. 恢复原速度 
            if (!this.notValid()) {
                this.setSpeedY(originalSpeedY);
                this.setSpeedX(originalSpeedX);
                System.out.println(this.getClass().getSimpleName() + " 冰冻解除！");
            }
        };
        new Thread(r).start();
    }

    // 注意：onIceActive() 方法不需要在这里提供默认实现，
    // 每种敌机的冰冻响应几乎都不一样，强制要求具体的敌机子类去实现 onIceActive()
}
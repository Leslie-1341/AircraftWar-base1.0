package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
// 【清理】移除了没用到的 EliteEnemy 导包
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * Boss敌机具体工厂
 */
public class BossFactory implements EnemyFactory {

    @Override
    public AbstractEnemy createEnemy() {
        return new BossEnemy(
                // 1. X 坐标：屏幕正中间霸气登场
                Main.WINDOW_WIDTH / 2,
                // 2. Y 坐标：刚好露出一个机身的高度
                ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2,
                // 3. X 速度：横向移动速度设为 3，配合反弹逻辑左右摇摆
                3,
                // 4. Y 速度：【关键要求】悬浮在上方，不向下移动！
                0,
                // 5. HP：做个名副其实的血牛！(你可以改成 1000 甚至 5000 试试)
                500
        );
    }
}
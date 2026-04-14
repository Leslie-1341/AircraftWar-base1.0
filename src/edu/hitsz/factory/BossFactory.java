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
                // 1. X 坐标
                Main.WINDOW_WIDTH / 2,
                // 2. Y 坐标
                ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2,
                // 3. X 速度：横向移动速度设为 3
                3,
                // 4. Y 速度：悬浮在上方，不向下移动
                0,
                // 5. HP：血量设为 500
                500
        );
    }
}
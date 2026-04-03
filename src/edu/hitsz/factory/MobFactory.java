package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * 普通敌机具体工厂
 */
public class MobFactory implements EnemyFactory {

    @Override
    public AbstractEnemy createEnemy() {
        return new MobEnemy(
                // 随机 X 坐标
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                // 顶部随机 Y 坐标
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,  // 横向速度 0
                10, // 向下速度 10
                30  // 普通敌机初始血量 30
        );
    }
}
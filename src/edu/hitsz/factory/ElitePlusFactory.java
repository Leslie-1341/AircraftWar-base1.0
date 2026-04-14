package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class ElitePlusFactory implements EnemyFactory {
    @Override
    public AbstractEnemy createEnemy() {
        // 随机决定初始是向左飞还是向右飞
        int initialSpeedX = Math.random() < 0.5 ? 3 : -3;
        return new ElitePlusEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                initialSpeedX, // 横向速度
                8, // 下落速度
                90 // 精锐敌机血量
        );
    }
}
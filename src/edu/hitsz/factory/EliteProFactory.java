package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteProFactory implements EnemyFactory {
    @Override
    public AbstractEnemy createEnemy() {
        int initialSpeedX = Math.random() < 0.5 ? 4 : -4;
        return new EliteProEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                initialSpeedX,
                7,
                120 // 王牌敌机血量最厚
        );
    }
}
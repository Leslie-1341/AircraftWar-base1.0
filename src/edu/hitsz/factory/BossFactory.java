package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossFactory implements EnemyFactory {

    // 保存动态血量
    private int hp;

    // 带参数的构造函数
    public BossFactory(int hp) {
        this.hp = hp;
    }

    @Override
    public AbstractEnemy createEnemy() {
        return new BossEnemy(
                Main.WINDOW_WIDTH / 2,
                ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2,
                3,
                0,
                this.hp // 【修改】使用传入的血量
        );
    }
}
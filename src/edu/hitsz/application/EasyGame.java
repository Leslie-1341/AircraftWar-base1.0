package edu.hitsz.application;

import edu.hitsz.factory.EliteFactory;
import edu.hitsz.factory.ElitePlusFactory;
import edu.hitsz.factory.EliteProFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.MobFactory;

public class EasyGame extends AbstractGame {

    public EasyGame(String difficulty, boolean musicEnabled) {
        super(difficulty, musicEnabled);
        // 设置简单模式专属地图
        this.backgroundImage = ImageManager.BACKGROUND_IMAGE_EASY;
        // 初始化简单模式的参数
        this.enemyMaxNumber = 5;
        this.enemySpawnCycle = 20;
        this.bossThreshold = Integer.MAX_VALUE; // 阈值设为无限大，永远达不到
        this.bossHp = 0;
        System.out.println("当前游戏模式：简单模式。祝您游戏愉快！");
    }

    @Override
    protected void levelUpLogic() {
        // 简单难度不随游戏时间变化，全程保持初始设定
    }

    @Override
    protected boolean supportBoss() {
        // 无法生成 Boss 敌机
        return false;
    }

    @Override
    protected void adjustBossHp() {
        // 简单模式没有 Boss，此方法不会被调用
    }

    // ==========================================
    // 重写钩子 4：生成敌机的工厂及概率控制
    // ==========================================
    @Override
    protected EnemyFactory createEnemyFactory() {
        double randomValue = Math.random();
        // 普通模式概率：5%王牌，10%精锐，25%精英，60%普通
        if (randomValue < 0.05) {
            return new EliteProFactory();
        } else if (randomValue < 0.15) {
            return new ElitePlusFactory();
        } else if (randomValue < 0.40) {
            return new EliteFactory();
        } else {
            return new MobFactory();
        }
    }
}
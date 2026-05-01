package edu.hitsz.application;

public class EasyGame extends AbstractGame {

    public EasyGame(String difficulty, boolean musicEnabled) {
        super(difficulty, musicEnabled);
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
}
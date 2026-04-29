package edu.hitsz.application;

public class HardGame extends AbstractGame {

    private int lastLevelUpTime = 0;

    public HardGame(String difficulty, boolean musicEnabled) {
        super(difficulty, musicEnabled);
        this.enemyMaxNumber = 6;      // 同屏最多6架
        this.enemySpawnCycle = 15;   // 初始刷新更快
        this.bossThreshold = 300;     // 每 300 分召唤一次 Boss (比普通更频繁)
        this.bossHp = 800;            // Boss 初始血量更高
        System.out.println("当前游戏模式：困难模式。深渊正在凝视你！");
    }

    @Override
    protected void levelUpLogic() {
        if (timeCount - lastLevelUpTime >= 10000) {
            lastLevelUpTime = timeCount;

            // 【修改】控制极限和每次缩减的幅度
            if (enemySpawnCycle > 5) { // 最快 5*40=200ms 刷一架，极度丧心病狂
                enemySpawnCycle -= 3;  // 每次缩短 3 个周期(120ms)

                System.out.println("====== 狂暴难度飙升 ======");
                System.out.println("敌机产生周期缩短至: " + enemySpawnCycle);
                System.out.println("敌机速度提升、敌机血量增加！");
                System.out.println("敌机射击频率提升，英雄机火力压制减弱！");
            }
        }
    }

    @Override
    protected boolean supportBoss() {
        return true; // 允许生成 Boss[cite: 2]
    }

    @Override
    protected void adjustBossHp() {
        // 每次召唤 Boss 机时，提升 Boss 机血量[cite: 2]
        this.bossHp += 200;
        System.out.println("检测到超强能量反应！Boss 血量强化至: " + bossHp);
    }
}
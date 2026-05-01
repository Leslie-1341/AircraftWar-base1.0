package edu.hitsz.application;

public class MediumGame extends AbstractGame {

    // 记录上一次提升难度的时间点
    private int lastLevelUpTime = 0;

    public MediumGame(String difficulty, boolean musicEnabled) {
        super(difficulty, musicEnabled);
        this.enemyMaxNumber = 5;
        this.enemySpawnCycle = 20;
        this.bossThreshold = 400; // 每 400 分召唤一次 Boss
        this.bossHp = 500; // Boss 初始血量
        System.out.println("当前游戏模式：普通模式。祝您游戏愉快！");
    }

    @Override
    protected void levelUpLogic() {
        if (timeCount - lastLevelUpTime >= 10000) {
            lastLevelUpTime = timeCount;

            // 【修改】控制极限和每次缩减的幅度
            if (enemySpawnCycle > 10) { // 最快 10*40=400ms 刷一架
                enemySpawnCycle -= 2; // 每次缩短 2 个周期(80ms)

                System.out.println("====== 难度提升 ======");
                System.out.println("敌机产生周期缩短至: " + enemySpawnCycle);
                System.out.println("敌机速度提升、血量增加！");
            }
        }
    }

    @Override
    protected boolean supportBoss() {
        return true; // 允许生成 Boss
    }

    @Override
    protected void adjustBossHp() {
        // 每次召唤 Boss 机时，不改变 Boss 机血量，维持 bossHp = 500
    }
}
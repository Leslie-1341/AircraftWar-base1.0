package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodSupply;
import edu.hitsz.prop.BulletPlusSupply;
import edu.hitsz.prop.BulletSupply;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;


/**
 * 游戏主面板，游戏启动  游戏总构造函数
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    //调度器, 用于定时任务调度
    private final Timer timer;
    //时间间隔(ms)，控制刷新频率
    private final int timeInterval = 40;
    //TODO：抽象出敌机父类，敌机列表改成抽象敌机类型（第二次实验课已完成）
    private final HeroAircraft heroAircraft;
    private final List<AbstractEnemy> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    // 第一次实验课 声明道具集合
    private final List<AbstractProp> props;
    //屏幕中出现的敌机最大数量
    private final int enemyMaxNumber = 5;

    //英雄机和敌机射击周期
    protected double shootCycle = 10;
    private int shootCounter = 0;

    //敌机生成周期
    private int enemySpawnCycle = 20;
    private int enemySpawnCounter = 0;

    //当前玩家分数
    private int score = 0;

    //游戏结束标志
    private boolean gameOverFlag = false;

    // ==========================================
    // 【新增】Boss 敌机生成控制机制
    // ==========================================
    private int bossThreshold = 300;   // 设定触发 Boss 生成的分数阈值（每 300 分生成一次）
    private int lastBossScore = 0;     // 记录上一次触发 Boss 时的分数档位
    private boolean hasBoss = false;   // 标记当前屏幕上是否已经存在 Boss

    public Game() {
        //game类用于游戏的控制，英雄机的创建与之无关，在game中创建英雄级违反单一对象原则，不能保证英雄机的唯一性
        //TODO：使用单例模式改进代码，把对象的使用和创建分离，坐标和速度从game中分离（第二次实验课已完成）
        heroAircraft = HeroAircraft.getInstance();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        this.timer = new Timer("game-action-timer", true);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                enemySpawnCounter++;
                //TODO:在game中创建敌机违反单一职责、开闭原则和依赖倒转（第二次实验课已改正）
                if (enemySpawnCounter >= enemySpawnCycle && enemyAircrafts.size() < enemyMaxNumber) {
                    enemySpawnCounter = 0;

                    // ==========================================
                    // 【策略模式 & 工厂模式结合】：Boss 敌机生成逻辑
                    // ==========================================
                    // 判断当前分数是否跨越了下一个 Boss 的触发阈值
                    if (score >= lastBossScore + bossThreshold) {
                        // 如果当前屏幕上没有 Boss，则生成它
                        if (!hasBoss) {
                            EnemyFactory bossFactory = new BossFactory();
                            enemyAircrafts.add(bossFactory.createEnemy());

                            hasBoss = true;                  // 锁定状态，防止同时生成多架
                            lastBossScore += bossThreshold;  // 抬高下一个 Boss 的触发门槛
                            System.out.println("警告：分数达到 " + score + "，Boss 敌机降临！");
                        }
                    } else {
                        // 原有的普通、精英、精锐、王牌敌机随机生成逻辑
                        EnemyFactory enemyFactory;
                        double randomValue = Math.random();
                        if (randomValue < 0.10) {
                            enemyFactory = new EliteProFactory();
                        } else if (randomValue < 0.25) {
                            enemyFactory = new ElitePlusFactory();
                        } else if (randomValue < 0.50) {
                            enemyFactory = new EliteFactory();
                        } else {
                            enemyFactory = new MobFactory();
                        }
                        enemyAircrafts.add(enemyFactory.createEnemy());
                    }
                }
                // 飞机发射子弹
                shootAction();
                // 子弹移动
                bulletsMoveAction();
                // 飞机移动
                aircraftsMoveAction();
                // 【新增】调用道具移动的方法
                propsMoveAction();
                // 撞击检测
                crashCheckAction();
                // 后处理
                postProcessAction();
                // 重绘界面
                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        timer.schedule(task,0,timeInterval);

    }

    //***********************
    //      Action 各部分
    //***********************

    private void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            // 英雄机射击
            heroBullets.addAll(heroAircraft.shoot());
            // 敌机射击
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }
    // 【新增】完整的道具移动方法
    private void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    // TODO 敌机子弹攻击英雄机
    //TODO：多态表现，提供统一接口，对于不同类型的敌机，返回对应的击毁分数和道具掉落概率（第二次实验课已完成）
    // ===============================================
    // 1. 英雄子弹攻击敌机
    // ===============================================
    private void crashCheckAction() {

        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            // 注意：循环变量必须使用 AbstractEnemy 类型，以便调用它特有的多态方法
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    continue;
                }
                //使用【多态】实现击毁敌机加分和掉落道具
                // 子弹击中敌机
                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    if (enemyAircraft.notValid()) {

                        // ==========================================
                        // 【新增】：如果被击毁的是 Boss，解除 Boss 锁定状态
                        // ==========================================
                        if (enemyAircraft instanceof BossEnemy) {
                            hasBoss = false;
                            System.out.println("Boss 敌机被击毁！准备迎接下一波挑战！");
                        }

                        enemyAircraft.vanish();  // 标记敌机为无效

                        // 【多态重构核心】：彻底消灭 instanceof！
                        // 1. 获取这架敌机专属的分数
                        score += enemyAircraft.getScore();

                        // 2. 获取这架敌机掉落的道具，并统统加入游离道具集合中
                        // Game 类不再关心它到底是什么飞机、概率是多少、该掉什么道具
                        props.addAll(enemyAircraft.dropProps());
                    }
                }
            }
        }
        // 英雄机与敌机相撞
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.notValid()) {
                continue;
            }
            if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                enemyAircraft.vanish();
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
            }
        }
        // ===============================================
        // 敌机子弹攻击英雄机
        // ===============================================
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }
        // Todo: 我方获得道具，道具生效
        //  道具的创建过程放在game里面违反单一职责原则，放到敌机父类中（第二次实验课已完成）
        // ===============================================
        // 2. 我方获得道具，道具生效
        // ===============================================
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            // 当英雄机和道具发生碰撞
            if (heroAircraft.crash(prop)) {

                // 【核心重构】：利用多态，让道具自己决定自己该干什么！
                // Game 类从此不再关心道具具体的加血数值或效果机制
                prop.active(heroAircraft);

                // 道具被吸收后，标记为失效并消失
                prop.vanish();
            }
        }
    }


    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        // Todo: 删除无效道具
        // 【把清理道具的代码放在这个方法内部的最下面】
        props.removeIf(AbstractFlyingObject::notValid);
    }

    /**
     * 检查游戏是否结束，若结束：关闭线程池
     */
    private void checkResultAction() {
        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.cancel(); // 取消定时器并终止所有调度任务
            gameOverFlag = true;
            System.out.println("Game Over!");
        }
    }

    //***********************
    //      Paint 各部分
    //***********************
    /**
     * 重写 paint方法
     * 通过重复调用paint方法，实现游戏动画
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        // Todo: 绘制道具
        // 【修改这里：把绘制道具的代码放在这里，一定要在方法内部】
        paintImageWithPositionRevised(g, props);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

}
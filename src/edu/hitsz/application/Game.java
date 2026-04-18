package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodSupply;
import edu.hitsz.prop.BulletPlusSupply;
import edu.hitsz.prop.BulletSupply;
import edu.hitsz.record.Record;
import edu.hitsz.record.RecordDao;
import edu.hitsz.record.RecordDaoImpl;
import javax.swing.JOptionPane; // 用于弹出输入框
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;

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
    //抽象出敌机父类，敌机列表改成抽象敌机类型（第二次实验课已完成）
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

    // 【新增】DAO 接口引用
    private RecordDao recordDao;
    // 【新增】游戏难度标识（默认设为 MEDIUM，后续实验再做难度选择）
    private String difficulty = "MEDIUM";

    public Game() {
        //使用单例模式改进代码，把对象的使用和创建分离，坐标和速度从game中分离（第二次实验课已完成）
        heroAircraft = HeroAircraft.getInstance();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        this.timer = new Timer("game-action-timer", true);
        // 【新增】初始化 DAO，传入当前难度，它会自动去读取对应的文件
        this.recordDao = new RecordDaoImpl(this.difficulty);
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
                //在game中创建敌机违反单一职责、开闭原则和依赖倒转（第二次实验课已改正）
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
                        // 普通、精英、精锐、王牌敌机随机生成逻辑
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
                // 打印排行榜
                printLeaderboard();
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
    // 敌机子弹攻击英雄机（第二次实验课已完成）
    // 多态，提供统一接口，对于不同类型的敌机，返回对应的击毁分数和道具掉落概率（第二次实验课已完成）

    // ===============================================
    // 1. 英雄子弹攻击敌机
    // ===============================================
    private void crashCheckAction() {

        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }

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

                        // 【多态重构】：删除 instanceof
                        // 1. 获取这架敌机专属的分数
                        score += enemyAircraft.getScore();

                        // 2. 获取这架敌机掉落的道具，并加入游离道具集合中
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
        // 我方获得道具，道具生效
        // 道具的创建过程放在game里面违反单一职责原则，应当放到敌机父类中（第二次实验课已完成）
        // ===============================================
        // 2. 我方获得道具，道具生效
        // ===============================================
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            // 当英雄机和道具发生碰撞
            if (heroAircraft.crash(prop)) {
                // 【核心重构】：利用多态，让道具自行决定效果
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
        // 删除无效道具（第三次实验课已完成）
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

            // ==========================================
            // 【新增】第四次实验：保存记录与控制台打印排行榜
            // ==========================================

            // 1. 弹窗提示玩家输入名字
            String userName = JOptionPane.showInputDialog(
                    null,
                    "游戏结束，你的得分为 " + score + "。\n请输入名字记录得分：",
                    "输入",
                    JOptionPane.QUESTION_MESSAGE
            );

            // 2. 如果玩家没输入或者点了取消，使用指导书要求的默认名
            if (userName == null || userName.trim().isEmpty()) {
                userName = "testUserName";
            }

            // 3. 格式化当前时间为 "MM-dd HH:mm"
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
            String currentTime = formatter.format(new Date());

            // 4. 组装数据并调用 DAO 保存到文件
            Record newRecord = new Record(userName, score, currentTime);
            recordDao.doAdd(newRecord);

            // 5. 打印排行榜到控制台
            printLeaderboard();
        }
    }
    /**
     * 【新增】在控制台按指导书格式打印排行榜
     */
    private void printLeaderboard() {
        // 1. 从 DAO 获取所有记录
        List<Record> allRecords = recordDao.getAllRecords();

        // 2. 对记录按分数从大到小排序
        Collections.sort(allRecords, new Comparator<Record>() {
            @Override
            public int compare(Record r1, Record r2) {
                // 降序排序
                return Integer.compare(r2.getScore(), r1.getScore());
            }
        });

        // 3. 按照指导书要求的格式打印输出
        System.out.println("得分排行榜");
        System.out.println("******************");

        for (int i = 0; i < allRecords.size(); i++) {
            Record r = allRecords.get(i);
            System.out.printf("第%d名: %s, %d, %s\n",
                    (i + 1), r.getUserName(), r.getScore(), r.getTime());
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

        // 绘制道具（第二次实验课已完成）
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
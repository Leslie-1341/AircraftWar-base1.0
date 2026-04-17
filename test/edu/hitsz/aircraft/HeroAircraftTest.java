package edu.hitsz.aircraft; // 请确保与你的 HeroAircraft 所在包名一致

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 英雄机单元测试类
 * 覆盖单例模式、自身属性变更（扣血边界）、飞行物碰撞逻辑
 */
class HeroAircraftTest {

    private HeroAircraft hero;

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method ---**");
        // 获取英雄机的单例实例
        hero = HeroAircraft.getInstance();
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method ---**\n");
    }

    // ==========================================
    // 测试用例 1：白盒 - 单例模式测试
    // ==========================================
    @Test
    @DisplayName("Test getInstance method: 测试英雄机单例模式")
    void getInstance() {
        System.out.println("Testing getInstance...");
        HeroAircraft hero1 = HeroAircraft.getInstance();
        HeroAircraft hero2 = HeroAircraft.getInstance();

        // 断言 1：实例不为空
        assertNotNull(hero1, "英雄机实例不应为空");
        // 断言 2：两次获取的必须是同一个对象地址（即内存中唯一实例）
        assertSame(hero1, hero2, "单例模式失败，两次获取的不是同一个实例");
    }

    // ==========================================
    // 测试用例 2：白盒 - 自身属性变更逻辑测试 (父类方法)
    // ==========================================
    @Test
    @DisplayName("Test decreaseHp method: 测试扣血逻辑及边界值")
    void decreaseHp() {
        System.out.println("Testing decreaseHp...");
        // 初始血量设为 100
        hero.increaseHp(100);
        int initialHp = hero.getHp();

        // 1. 测试正常扣血
        int damage = 50;
        hero.decreaseHp(damage);
        assertEquals(initialHp - damage, hero.getHp(), "正常扣血逻辑计算错误");

        // 2. 测试致命伤害（边界值/异常值处理）
        // 给一个极大伤害值，期望血量最低只能降到 0，不会变成负数
        hero.decreaseHp(1000);
        assertEquals(0, hero.getHp(), "受到致命伤害后，血量应限制为0，不能为负数");
    }

    // ============================================
    // 测试用例 3：白盒 - 飞行物体碰撞检测逻辑测试 (父类方法)
    // ============================================
    @Test
    @DisplayName("Test crash method: 测试两个飞行物体的碰撞检测")
    void crash() {
        System.out.println("Testing crash...");
        // 获取英雄机当前坐标
        int heroX = hero.getLocationX();
        int heroY = hero.getLocationY();

        // 1. 构造一个与之相撞的敌机 (坐标重合)
        MobEnemy crashEnemy = new MobEnemy(heroX, heroY, 0, 0, 10);
        // 断言 3：必定发生碰撞，期望返回 true
        assertTrue(hero.crash(crashEnemy), "坐标重合，应该检测到碰撞");

        // 2. 构造一个必定不相撞的敌机 (坐标距离极远)
        MobEnemy safeEnemy = new MobEnemy(heroX + 1000, heroY + 1000, 0, 0, 10);
        // 断言 4：必定不发生碰撞，期望返回 false
        assertFalse(hero.crash(safeEnemy), "距离极远，不应检测到碰撞");
    }

    // ==========================================
    // 测试用例 4：黑盒 - 移动逻辑测试 (针对需求规格)
    // ==========================================
    @Test
    @DisplayName("Test forward method: 黑盒测试 - 验证英雄机移动不受forward控制")
    void forward() {
        System.out.println("Testing forward (Black-box)...");
        // 记录初始坐标
        int initialX = hero.getLocationX();
        int initialY = hero.getLocationY();

        // 触发动作：调用移动方法
        hero.forward();

        // 断言：根据业务需求，英雄机的坐标不应该发生任何变化
        // 使用 JUnit5 的 assertAll 分组断言，即使X报错，也会继续检测Y
        assertAll("验证坐标未改变",
                () -> assertEquals(initialX, hero.getLocationX(), "X坐标不应随 forward() 改变"),
                () -> assertEquals(initialY, hero.getLocationY(), "Y坐标不应随 forward() 改变")
        );
    }

    // ==========================================
    // 测试用例 5：黑盒 - 初始射击行为测试 (针对功能输出)
    // ==========================================
    @Test
    @DisplayName("Test shoot method: 黑盒测试 - 验证初始状态下的射击输出")
    void shoot() {
        System.out.println("Testing shoot (Black-box)...");

        // 触发动作：执行射击
        var bullets = hero.shoot();

        // 断言：根据初始规格，验证返回结果的外部表现，不关心内部用的什么策略
        assertNotNull(bullets, "射击结果列表不应为 null");
        assertEquals(1, bullets.size(), "初始状态下，每次射击应只产生 1 颗子弹");

        if (!bullets.isEmpty()) {
            var bullet = bullets.get(0);
            assertEquals(30, bullet.getPower(), "初始状态下，子弹威力应为 30");
            assertTrue(bullet.getSpeedY() < 0, "英雄机发射的子弹应该向上飞行 (速度 < 0)");
        }
    }
}
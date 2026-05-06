package edu.hitsz.application;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.BloodSupply;
import edu.hitsz.prop.BombSupply;
import edu.hitsz.prop.BulletPlusSupply;
import edu.hitsz.prop.BulletSupply;
import edu.hitsz.prop.FreezeSupply;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, BufferedImage> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static BufferedImage BACKGROUND_IMAGE;
    public static BufferedImage HERO_IMAGE;
    public static BufferedImage HERO_BULLET_IMAGE;
    public static BufferedImage ENEMY_BULLET_IMAGE;
    public static BufferedImage MOB_ENEMY_IMAGE;
    public static BufferedImage ELITE_ENEMY_IMAGE;
    public static BufferedImage ELITE_PLUS_ENEMY_IMAGE;
    public static BufferedImage ELITE_PRO_ENEMY_IMAGE;
    public static BufferedImage BOSS_ENEMY_IMAGE; 
    public static BufferedImage PROP_BLOOD_IMAGE;
    public static BufferedImage PROP_BULLET_IMAGE;
    public static BufferedImage PROP_BULLET_PLUS_IMAGE;
    public static BufferedImage PROP_BOMB_IMAGE;
    public static BufferedImage PROP_FREEZE_IMAGE;
    // 定义三种难度的背景图片变量
    public static BufferedImage BACKGROUND_IMAGE_EASY;
    public static BufferedImage BACKGROUND_IMAGE_NORMAL;
    public static BufferedImage BACKGROUND_IMAGE_HARD;

    static {
        try {
            BACKGROUND_IMAGE_EASY = loadImage("/images/bg2.jpg");
            BACKGROUND_IMAGE_NORMAL = loadImage("/images/bg3.jpg");
            BACKGROUND_IMAGE_HARD = loadImage("/images/bg5.jpg");
            HERO_IMAGE = loadImage("/images/hero.png");
            MOB_ENEMY_IMAGE = loadImage("/images/mob.png");
            ELITE_ENEMY_IMAGE = loadImage("/images/elite.png");
            ELITE_PLUS_ENEMY_IMAGE = loadImage("/images/elitePlus.png");
            ELITE_PRO_ENEMY_IMAGE = loadImage("/images/elitePro.png");
            BOSS_ENEMY_IMAGE = loadImage("/images/boss.png"); 
            HERO_BULLET_IMAGE = loadImage("/images/bullet_hero.png");
            ENEMY_BULLET_IMAGE = loadImage("/images/bullet_enemy.png");
            PROP_BLOOD_IMAGE = loadImage("/images/prop_blood.png");
            PROP_BULLET_IMAGE = loadImage("/images/prop_bullet.png");
            PROP_BULLET_PLUS_IMAGE = loadImage("/images/prop_bulletPlus.png");
            PROP_BOMB_IMAGE = loadImage("/images/prop_bomb.png");
            PROP_FREEZE_IMAGE = loadImage("/images/prop_freeze.png");

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(ElitePlusEnemy.class.getName(), ELITE_PLUS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteProEnemy.class.getName(), ELITE_PRO_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE); // 映射 Boss 敌机图片
            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);

            CLASSNAME_IMAGE_MAP.put(BloodSupply.class.getName(), PROP_BLOOD_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BulletSupply.class.getName(), PROP_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BulletPlusSupply.class.getName(), PROP_BULLET_PLUS_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BombSupply.class.getName(), PROP_BOMB_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FreezeSupply.class.getName(), PROP_FREEZE_IMAGE);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static BufferedImage loadImage(String path) throws IOException {
        var url = ImageManager.class.getResource(path);
        if (url == null) {
            throw new IOException("Resource not found: " + path);
        }
        BufferedImage img = ImageIO.read(url);
        if (img == null) {
            throw new IOException("Failed to load image: " + path);
        }
        return img;
    }

    public static BufferedImage get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static BufferedImage get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }
}
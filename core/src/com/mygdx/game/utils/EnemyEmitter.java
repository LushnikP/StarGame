package com.mygdx.game.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.math.Rect;
import com.mygdx.game.math.Rnd;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.sprite.EnemyShip;

public class EnemyEmitter {

    private static final float GENERATE_INTERVAL = 3f;

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 1f;
    private static final int ENEMY_SMALL_HP = 2;
    private static final float ENEMY_SMALL_SCORE = 3;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.15f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static final int ENEMY_MEDIUM_BULLET_DAMAGE = 5;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 1.5f;
    private static final int ENEMY_MEDIUM_HP = 5;
    private static final float ENEMY_MEDIUM_SCORE = 5;

    private static final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static final int ENEMY_BIG_BULLET_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 2f;
    private static final int ENEMY_BIG_HP = 10;
    private static final float ENEMY_BIG_SCORE = 10;

    private final Rect worldBounds;
    private final Sound bulletSound;
    private final TextureRegion bulletRegion;
    private final EnemyPool enemyPool;

    private final TextureRegion[] enemySmallRegions;
    private final TextureRegion[] enemyMediumRegions;
    private final TextureRegion[] enemyBigRegions;

    private final Vector2 enemySmallV = new Vector2(0f, -0.2f);
    private static final Vector2 enemySmallBulletV = new Vector2(0, -0.4f);

    private final Vector2 enemyMediumV = new Vector2(0f, -0.03f);
    private static final Vector2 enemyMediumBulletV = new Vector2(0, -0.3f);

    private final Vector2 enemyBigV = new Vector2(0f, -0.005f);
    private static final Vector2 enemyBigBulletV = new Vector2(0, -0.25f);

    private float generateTimer;

    private int level;
    private float levelMultiplier;

    public EnemyEmitter(Rect worldBounds, Sound bulletSound, EnemyPool enemyPool, TextureAtlas atlas) {
        this.worldBounds = worldBounds;
        this.bulletSound = bulletSound;
        this.enemyPool = enemyPool;
        bulletRegion = atlas.findRegion("bulletEnemy");
        enemySmallRegions = Regions.split(atlas.findRegion("enemy0"), 1, 2, 2);
        enemyMediumRegions = Regions.split(atlas.findRegion("enemy1"), 1, 2, 2);
        enemyBigRegions = Regions.split(atlas.findRegion("enemy2"), 1, 2, 2);
    }

    public void generate(float delta, int frags){
        level = frags / 10 + 1;
        levelMultiplier = level * 1.1f;
        generateTimer += delta;
        if(generateTimer >= GENERATE_INTERVAL){
            System.out.println(levelMultiplier);
            generateTimer = 0f;
            EnemyShip enemy = enemyPool.obtain();
            float type = (float) Math.random();
            if(type < 0.5f) {
                enemy.set(
                        enemySmallRegions,
                        enemySmallV,
                        bulletRegion,
                        enemySmallBulletV,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_BULLET_DAMAGE * level,
                        bulletSound,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP,
                        ENEMY_SMALL_SCORE * levelMultiplier
                );
            } else if(type < 0.8f){
                enemy.set(
                        enemyMediumRegions,
                        enemyMediumV,
                        bulletRegion,
                        enemyMediumBulletV,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_DAMAGE * level,
                        bulletSound,
                        ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY_MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_HP,
                        ENEMY_MEDIUM_SCORE * levelMultiplier
                );
            } else{
                enemy.set(
                        enemyBigRegions,
                        enemyBigV,
                        bulletRegion,
                        enemyBigBulletV,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_DAMAGE * level,
                        bulletSound,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP,
                        ENEMY_BIG_SCORE * levelMultiplier
                );
            }
            float posX = Rnd.nextFloat(
                    worldBounds.getLeft() + enemy.getHalfWidth(),
                    worldBounds.getRight() - enemy.getHalfWidth());
            enemy.setPos(posX, worldBounds.getTop());
        }
    }

    public int getLevel() {
        return level;
    }
}

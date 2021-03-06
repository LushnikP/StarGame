package com.mygdx.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Ship;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;

public class EnemyShip extends Ship {

    private float score;

    public EnemyShip(Rect worldBounce, BulletPool bulletPool, ExplosionPool explosionPool) {
        super();
        this.worldBounce = worldBounce;
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y - getHalfHeight());
        if(getTop() < worldBounce.getTop()){
            v.set(v0);
        } else {
            reloadTimer = reloadInterval * 0.8f;
        }
        if(getTop() < worldBounce.getBottom()){
            destroy();
        }

    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            Vector2 bulletV,
            float bulletHeight,
            int bulletDamage,
            Sound bulletSound,
            float reloadInterval,
            float height,
            int hp,
            float score
    ){
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletV.set(bulletV);
        this.bulletHeight = bulletHeight;
        this.bulletDamage = bulletDamage;
        this.bulletSound = bulletSound;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
        this.hp = hp;
        this.score = score;
        v.set(0, -0.4f);
    }

    public void setPos(float x, float y){
        pos.set(x, y);
        bulletPos.set(pos.x, pos.y - getHalfHeight());
    }

    public float getScore() {
        return score;
    }

    @Override
    public boolean isBulletCollision(Bullet bullet){
        return !(
                bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                ||bullet.getTop() < pos.y
                );
    }

    @Override
    public void destroy() {
        super.destroy();
        reloadTimer = 0f;
    }


}

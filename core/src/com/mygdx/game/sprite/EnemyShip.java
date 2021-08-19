package com.mygdx.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Ship;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;

public class EnemyShip extends Ship {

    public EnemyShip(Rect worldBounce, BulletPool bulletPool) {
        super();
        this.worldBounce = worldBounce;
        this.bulletPool = bulletPool;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(getTop() < worldBounce.getTop()){
            v.set(v0);
        } else {
            reloadTimer = reloadInterval * 0.8f;
        }
        if(getBottom() < worldBounce.getBottom()){
            destroy();
        }
        bulletPos.set(pos.x, pos.y - getHalfHeight());
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
            int hp
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
        v.set(0, -0.4f);
        bulletPos.set(pos.x, pos.y + getHalfHeight());
    }
}

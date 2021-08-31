package com.mygdx.game.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;
import com.mygdx.game.sprite.Bullet;
import com.mygdx.game.sprite.ExitButton;
import com.mygdx.game.sprite.Explosion;

public abstract class Ship extends Sprite{

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;

    protected final Vector2 v0;
    protected final Vector2 v;

    protected Rect worldBounce;
    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
    protected TextureRegion bulletRegion;
    protected  Vector2 bulletPos;
    protected Vector2 bulletV;
    protected float bulletHeight;
    protected int bulletDamage;
    protected Sound bulletSound;
    protected int hp;

    protected float reloadInterval;
    protected float reloadTimer;

    private float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    public Ship() {
        v0 = new Vector2();
        v = new Vector2();
        bulletPos = new Vector2();
        bulletV = new Vector2();
    }

    public Ship(TextureRegion region, int rows, int cols, int frame) {
        super(region, rows, cols, frame);
        v0 = new Vector2();
        v = new Vector2();
        bulletPos = new Vector2();
        bulletV = new Vector2();

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if(reloadTimer >= reloadInterval){
            reloadTimer = 0f;
            shoot();
        }
        damageAnimateTimer += delta;
        if(damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL){
            frame = 0;
        }
    }

    public void damage(int damage){
            hp -= damage;
            if(hp <= 0){
                hp = 0;
                destroy();
            }
            frame = 1;
            damageAnimateTimer = 0f;
    }

    public abstract boolean isBulletCollision(Bullet bullet);

    public int getBulletDamage() {
        return bulletDamage;
    }

    private void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounce, bulletDamage);
        bulletSound.play();
    }

    private void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(pos, getHeight());
    }

    @Override
    public void destroy() {
        super.destroy();
        if(hp <= 0){
            boom();
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int heal){
        hp += heal;
    }
}

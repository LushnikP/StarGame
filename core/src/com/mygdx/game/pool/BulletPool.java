package com.mygdx.game.pool;

import com.mygdx.game.base.SpritePool;
import com.mygdx.game.sprite.Bullet;

public class BulletPool extends SpritePool<Bullet> {

    @Override
    protected Bullet newSprite() {
        return new Bullet();
    }
}

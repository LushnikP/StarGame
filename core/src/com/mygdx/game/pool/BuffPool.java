package com.mygdx.game.pool;

import com.mygdx.game.base.SpritePool;
import com.mygdx.game.sprite.MedKit;

public class BuffPool extends SpritePool<MedKit> {
    @Override
    protected MedKit newSprite() {
        return new MedKit();
    }
}

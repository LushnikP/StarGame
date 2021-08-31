package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseBuff;
import com.mygdx.game.math.Rect;

public class MedKit extends BaseBuff {

    private int heal;

    public MedKit(){
        regions = new TextureRegion[1];
        v = new Vector2();
    }

    public void set(
            TextureRegion[] region,
            Vector2 v0,
            float height,
            Rect worldBounds,
            int heal
    ) {
        this.regions = region;
        this.v.set(v0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.heal = heal;
    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(v, delta);
        if(isOutside(worldBounds)){
            destroy();
        }
    }

    public void setPos(float x, float y) {
        pos.set(x, y);
    }

    public int getHeal() {
        return heal;
    }
}

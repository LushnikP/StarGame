package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.math.Rect;
import com.mygdx.game.math.Rnd;

public class Star extends Sprite {

    final private Vector2 v;
    private Rect worldBounds;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        v = new Vector2();
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        checkAndHandleBounds();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float height = Rnd.nextFloat(0.005f, 0.015f);
        setHeightProportion(height);
        float x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(x, y);
        v.set(Rnd.nextFloat(-0.005f, 0.005f), getHeight() * - 7);
    }

    private void  checkAndHandleBounds(){
        if(getRight() < worldBounds.getLeft()){
            setLeft(worldBounds.getRight());
        }
        if(getLeft() > worldBounds.getRight()){
            setRight((worldBounds.getLeft()));
        }
        if(getTop() < worldBounds.getBottom()){
            setBottom((worldBounds.getTop()));
        }
        if(getBottom() > worldBounds.getTop()){
            setTop(worldBounds.getBottom());
        }
    }
}

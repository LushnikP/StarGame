package com.mygdx.game.utils;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.math.Rect;
import com.mygdx.game.math.Rnd;
import com.mygdx.game.pool.BuffPool;
import com.mygdx.game.sprite.MedKit;

public class BuffEmitter {

    private static final float GENERATE_INTERVAL = 30f;

    private static final float MED_KIT_HEIGHT = 0.05f;
    private static final int MED_KIT_HEAL = 15;

    private final Rect worldBounds;
    private final BuffPool buffPool;

    private final TextureRegion[] medKitRegions;

    private final Vector2 medKitSmallV = new Vector2(0f, -0.3f);

    int index = 0;

    public BuffEmitter(Rect worldBounds, BuffPool buffPool, TextureAtlas atlas){
        this.worldBounds = worldBounds;
        this.buffPool = buffPool;
        medKitRegions = Regions.split(atlas.findRegion("heal"), 1, 1, 1);
    }

    public void generate(float delta, int score){
        float res = score / 50;
        if (res > index) {
            index++;
            MedKit medKit = buffPool.obtain();
            medKit.set(
                    medKitRegions,
                    medKitSmallV,
                    MED_KIT_HEIGHT,
                    worldBounds,
                    MED_KIT_HEAL
            );
            float posX = Rnd.nextFloat(
                    worldBounds.getLeft() + medKit.getHalfWidth(),
                    worldBounds.getRight() - medKit.getHalfWidth());
            medKit.setPos(posX, worldBounds.getTop());
        }
    }

}

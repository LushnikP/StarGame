package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.math.Rect;

public class GameOver extends Sprite {

    private static float HEIGHT = 0.07f;
    private static float BOTTOM_MARGIN = 0.009f;

    public GameOver(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setBottom(BOTTOM_MARGIN);
    }
}

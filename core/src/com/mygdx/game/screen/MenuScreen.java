package com.mygdx.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.math.Rect;
import com.mygdx.game.sprite.Background;


public class MenuScreen extends BaseScreen {

    private Texture bg;
    private Background background;

    private Vector2 pos;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        pos = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return super.touchDown(touch, pointer, button);
    }
}

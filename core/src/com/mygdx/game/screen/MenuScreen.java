package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Texture background;
    private Vector2 pos;
    private Vector2 pointClick;
    private  Vector2 deltaV;
    private int v;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        background = new Texture("sky.jpg");
        pos = new Vector2();
        pointClick = new Vector2();
        deltaV = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        batch.draw(background,   0, 0);
        batch.draw(img, pos.x, pos.y);
        batch.end();
        pos.add(deltaV);
        if((int)pos.x == v){
            deltaV.set(0,0);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        v = screenX;
        pointClick.set(screenX, Gdx.graphics.getHeight() - screenY);
        deltaV = pointClick.sub(pos);
        deltaV.nor();
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
        background.dispose();
    }

}

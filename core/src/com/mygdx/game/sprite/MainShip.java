package com.mygdx.game.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Ship;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;

public class MainShip extends Ship {

    private static final float HEIGHT = 0.15f;
    private static final float BUTTON_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;
    private static final float RELOAD_INTERVAL = 0.2f;

    private static final float PADDING = 0.01f;

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletSound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletSound = bulletSound;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV.set(0, 0.5f);
        bulletHeight = 0.01f;
        bulletDamage = 1;
        reloadInterval = RELOAD_INTERVAL;
        v0.set(0.5f, 0);
        hp = 1;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if((getRight() + PADDING) > worldBounce.getRight()){
            setRight(worldBounce.getRight() - PADDING);
            stop();
        }
        if((getLeft() - PADDING) < worldBounce.getLeft()){
            setLeft(worldBounce.getLeft() + PADDING);
            stop();
        }
        bulletPos.set(pos.x, pos.y + getHalfHeight());
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        this.worldBounce = worldBounds;
        setBottom(worldBounds.getBottom() + BUTTON_MARGIN);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(touch.x < worldBounce.pos.x){
            if(leftPointer != INVALID_POINTER){
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if(rightPointer != INVALID_POINTER){
                return false;
            }
            rightPointer= pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if(rightPointer != INVALID_POINTER){
                moveRight();
            } else {
                stop();
            }
        } else if(pointer == rightPointer){
            rightPointer = INVALID_POINTER;
            if(leftPointer != INVALID_POINTER){
                moveLeft();
            } else {
                stop();
            }
        }

        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode){
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight){
                    moveRight();
                } else stop();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft){
                    moveLeft();
                } else stop();
                break;
        }
        return false;
    }

    @Override
    public boolean isBulletCollision(Bullet bullet){
        return !(
                bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom()
        );
    }

    private void moveRight(){
        v.set(v0);
    }

    private void moveLeft(){
        v.set(v0).rotateDeg(180);
    }

    private void stop(){
        v.setZero();
    }

}

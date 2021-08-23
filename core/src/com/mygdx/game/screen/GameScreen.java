package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.pool.ExplosionPool;
import com.mygdx.game.sprite.Background;
import com.mygdx.game.sprite.Bullet;
import com.mygdx.game.sprite.EnemyShip;
import com.mygdx.game.sprite.GameOver;
import com.mygdx.game.sprite.MainShip;
import com.mygdx.game.sprite.NewGameButton;
import com.mygdx.game.sprite.Star;
import com.mygdx.game.utils.EnemyEmitter;

import java.util.List;

import javax.swing.MenuElement;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;

    private Star[] stars;
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;
    private MainShip mainShip;

    private Sound bulletSound;
    private Sound laserSound;
    private Sound explosionSound;
    private Music music;

    private EnemyEmitter enemyEmitter;

    private GameOver gameOver;
    private NewGameButton newGameButton;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);

        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\explosion.wav"));
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(worldBounds, bulletPool, explosionPool);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\laser.wav"));
        mainShip = new MainShip(atlas, bulletPool, explosionPool, laserSound);

        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\bullet.wav"));
        enemyEmitter = new EnemyEmitter(worldBounds, bulletSound, enemyPool, atlas);

        gameOver = new GameOver(atlas);
        newGameButton = new NewGameButton(atlas, this);

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
    }

    public void startNewGame(){
        mainShip.startNewGame();
        bulletPool.freeAllActiveSprites();
        enemyPool.freeAllActiveSprites();
        explosionPool.freeAllActiveSprites();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyed();
        draw();
        checkCollisions();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for(Star star : stars){
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGameButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        enemyPool.dispose();
        explosionSound.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        music.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(mainShip.isDestroyed()){
            newGameButton.touchDown(touch, pointer, button);
        } else {
            mainShip.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(mainShip.isDestroyed()){
            newGameButton.touchUp(touch, pointer, button);
        } else {
            mainShip.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void update(float delta){
        for(Star star : stars){
            star.update(delta);
        }
        explosionPool.updateActiveSprite(delta);
        if(!mainShip.isDestroyed()){
            mainShip.update(delta);
            bulletPool.updateActiveSprite(delta);
            enemyPool.updateActiveSprite(delta);
            enemyEmitter.generate(delta);
        }
    }

    private void checkCollisions(){
        if(mainShip.isDestroyed()){
            return;
        }
        List<EnemyShip> enemyShipList = enemyPool.getActiveSprites();
        for(EnemyShip enemyShip : enemyShipList){
            if(enemyShip.isDestroyed()){
                continue;
            }
            float minDist = enemyShip.getHalfWidth() + mainShip.getHalfWidth();
            if(mainShip.pos.dst(enemyShip.pos) < minDist){
                mainShip.damage(enemyShip.getBulletDamage() * 2);
                enemyShip.destroy();
            }
        }

        List<Bullet> bulletList = bulletPool.getActiveSprites();
        for(Bullet bullet : bulletList){
            if(bullet.isDestroyed()){
                continue;
            }
            for(EnemyShip enemyShip : enemyShipList){
                if(enemyShip.isDestroyed() || bullet.getOwner() != mainShip){
                    continue;
                }
                if(enemyShip.isBulletCollision(bullet)){
                    enemyShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
            if(bullet.getOwner() != mainShip && mainShip.isBulletCollision(bullet)){
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
    }

    private void freeAllDestroyed(){
        bulletPool.freeAllDestroyedActiveSprite();
        explosionPool.freeAllDestroyedActiveSprite();
        enemyPool.freeAllDestroyedActiveSprite();
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        if(!mainShip.isDestroyed()) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprite(batch);
            enemyPool.drawActiveSprite(batch);
        } else {
            gameOver.draw(batch);
            newGameButton.draw(batch);
        }
        explosionPool.drawActiveSprite(batch);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }
}

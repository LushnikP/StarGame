package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.base.Font;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BuffPool;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.pool.ExplosionPool;
import com.mygdx.game.sprite.Background;
import com.mygdx.game.sprite.Bullet;
import com.mygdx.game.sprite.EnemyShip;
import com.mygdx.game.sprite.GameOver;
import com.mygdx.game.sprite.MainShip;
import com.mygdx.game.sprite.MedKit;
import com.mygdx.game.sprite.NewGameButton;
import com.mygdx.game.sprite.Star;
import com.mygdx.game.utils.BuffEmitter;
import com.mygdx.game.utils.EnemyEmitter;

import java.util.List;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;
    private static final float PADDING = 0.01f;

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private static final String SCORE = "Score: ";

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;
    private TextureAtlas atlas2;

    private Star[] stars;
    private BuffPool buffPool;
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;
    private MainShip mainShip;

    private Sound bulletSound;
    private Sound laserSound;
    private Sound explosionSound;
    private Music music;

    private EnemyEmitter enemyEmitter;
    private BuffEmitter buffEmitter;

    private GameOver gameOver;
    private NewGameButton newGameButton;

    private Font font;
    private int frags;
    private int score;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;
    private StringBuilder sbScore;


    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);

        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        atlas2 = new TextureAtlas("textures\\buff.pack");

        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\explosion.wav"));
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(worldBounds, bulletPool, explosionPool);
        buffPool = new BuffPool();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\laser.wav"));
        mainShip = new MainShip(atlas, bulletPool, explosionPool, laserSound);

        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\bullet.wav"));
        enemyEmitter = new EnemyEmitter(worldBounds, bulletSound, enemyPool, atlas);
        buffEmitter = new BuffEmitter(worldBounds, buffPool, atlas2);

        gameOver = new GameOver(atlas);
        newGameButton = new NewGameButton(atlas, this);

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        font = new Font("font\\font.fnt", "font\\font.png");
        font.setSize(0.02f);
        frags = 0;
        score = 0;
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
        sbScore = new StringBuilder();
    }

    public void startNewGame(){
        mainShip.startNewGame();
        bulletPool.freeAllActiveSprites();
        enemyPool.freeAllActiveSprites();
        buffPool.freeAllActiveSprites();
        explosionPool.freeAllActiveSprites();
        frags = 0;
        score = 0;
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
        buffPool.dispose();
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

            buffPool.updateActiveSprite(delta);
            buffEmitter.generate(delta, score);

            bulletPool.updateActiveSprite(delta);
            enemyPool.updateActiveSprite(delta);
            enemyEmitter.generate(delta, frags);

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
                enemyShip.damage(mainShip.RAM_DAMAGE);
                mainShip.damage(enemyShip.getBulletDamage() * 2);
                enemyShip.destroy();
            }
        }

        List<MedKit> medKitsList = buffPool.getActiveSprites();
        for(MedKit medKit : medKitsList){
            if(medKit.isDestroyed()){
                continue;
            }
            float minDist = medKit.getHalfWidth() + mainShip.getHalfWidth();
            if(mainShip.pos.dst(medKit.pos) < minDist && mainShip.isCollision(medKit)){
                mainShip.setHp(medKit.getHeal());
                medKit.destroy();
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
                    if(enemyShip.isDestroyed()){
                        frags++;
                        score += enemyShip.getScore();
                    }
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
        buffPool.freeAllDestroyedActiveSprite();
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        if(!mainShip.isDestroyed()) {
            mainShip.draw(batch);
            buffPool.drawActiveSprite(batch);
            bulletPool.drawActiveSprite(batch);
            enemyPool.drawActiveSprite(batch);
        } else {
            gameOver.draw(batch);
            if(explosionPool.getActiveSprites().isEmpty()){
                newGameButton.draw(batch);
            }
        }
        explosionPool.drawActiveSprite(batch);
        printInfo();
        batch.end();
    }

    private void printInfo(){
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + PADDING, worldBounds.getTop() - PADDING);
        sbScore.setLength(0);
        font.draw(batch, sbScore.append(SCORE).append(score), worldBounds.getLeft() + PADDING, worldBounds.getTop() - 0.05f);

        sbHp.setLength(0);
        font.draw(batch, sbHp.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - PADDING, Align.center);
        sbLevel.setLength(0);

        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - PADDING, worldBounds.getTop() - PADDING, Align.right);
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

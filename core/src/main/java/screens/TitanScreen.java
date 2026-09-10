package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.orion.echoes.mars.EchoesMarsGame;

import java.util.ArrayList;
import java.util.List;

import managers.AssetManager;
import managers.SaveManager;

public class TitanScreen implements Screen {

    private final EchoesMarsGame game;
    private final SpriteBatch batch;
    private final AssetManager assets;
    private final BitmapFont font;
    private final SaveManager saveManager = new SaveManager();

    // =========================================================
    // CÂMERAS
    // =========================================================

    private final OrthographicCamera camera =
        new OrthographicCamera();

    private final OrthographicCamera hudCamera =
        new OrthographicCamera();

    private final Viewport worldViewport =
        new FitViewport(1280, 720, camera);

    private final Viewport hudViewport =
        new FitViewport(1280, 720, hudCamera);

    private final ShapeRenderer shapeRenderer =
        new ShapeRenderer();

    // =========================================================
    // MUNDO
    // =========================================================

    private static final float WORLD_WIDTH = 3000f;
    private static final float WORLD_HEIGHT = 1900f;

    // =========================================================
    // PLAYER
    // =========================================================

    private final Rectangle player =
        new Rectangle(300f, 300f, 54f, 54f);

    private static final float PLAYER_SPEED = 250f;

    // =========================================================
    // PORTAL
    // =========================================================

    private final Rectangle exitPortal =
        new Rectangle(
            2300f,
            1000f,
            140f,
            140f
        );

    private float portalTime = 0f;

    // =========================================================
    // ALIENS
    // =========================================================

    private static final int ALIEN_COUNT = 5;

    private final Rectangle[] aliens =
        new Rectangle[ALIEN_COUNT];

    private final boolean[] alienAlive =
        new boolean[ALIEN_COUNT];

    private final float[] alienSpeeds =
        new float[ALIEN_COUNT];

    private final int[] alienHealth =
        new int[ALIEN_COUNT];

    // Primeiro alien = BOSS VERMELHO
    private static final int BOSS_INDEX = 0;

    private static final int BOSS_MAX_HEALTH = 100;

    private static final int PLAYER_DAMAGE = 8;

    private static final float LASER_RANGE = 900f;

    private int aliensDefeated = 0;

    private boolean bossDefeated = false;

    // =========================================================
    // TIROS DO PLAYER
    // =========================================================

    private final List<Rectangle> lasers =
        new ArrayList<>();

    private final List<Vector2> laserVelocities =
        new ArrayList<>();

    private float laserCooldown = 0f;

    private static final float LASER_COOLDOWN = 1.2f;
    private static final float LASER_SPEED = 900f;

    // =========================================================
    // TIROS DO BOSS
    // =========================================================

    private final List<Rectangle> enemyProjectiles =
        new ArrayList<>();

    private final List<Vector2> enemyProjectileVelocities =
        new ArrayList<>();

    private float bossAttackCooldown = 0f;

    private static final float BOSS_ATTACK_COOLDOWN = 2.0f;
    private static final float BOSS_ATTACK_RANGE = 750f;
    private static final float BOSS_PROJECTILE_SPEED = 300f;
    private static final float BOSS_DAMAGE = 12f;

    // =========================================================
    // STATUS
    // =========================================================

    private float oxygen = 100f;
    private float energy = 100f;

    private int ice = 0;
    private int water = 0;
    private int fuel = 0;

    // =========================================================
    // GAME
    // =========================================================

    private boolean paused = false;
    private boolean victory = false;
    private boolean gameOver = false;

    // =========================================================
    // MISSÃO
    // =========================================================

    private String missionText =
        "OBJETIVO: Encontre e derrote o alien vermelho.";

    private String message =
        "TITA: algo estranho esta esperando por voce.";

    private float messageTimer = 5f;

    // =========================================================
    // DIÁLOGO
    // =========================================================

    private boolean dialogueActive = false;
    private boolean dialogueFinished = false;
    private int dialogueIndex = 0;

    private final String[] bossDialogue = {

        "Voce chegou ate aqui... entao ainda se lembra.",

        "Eu tambem lembro das coisas do passado.",

        "Marte nao foi o fim. O que aconteceu ainda vive dentro de voce."
    };

    // =========================================================
    // TEXTURAS
    // =========================================================

    private Texture background;
    private Texture playerTexture;
    private Texture alienTexture;

    // =========================================================
    // CONSTRUTORES
    // =========================================================

    public TitanScreen(
        EchoesMarsGame game,
        SpriteBatch batch,
        AssetManager assets
    ) {

        this(
            game,
            batch,
            assets,
            false
        );
    }

    public TitanScreen(
        EchoesMarsGame game,
        SpriteBatch batch,
        AssetManager assets,
        boolean loadSave
    ) {

        this.game = game;
        this.batch = batch;
        this.assets = assets;
        this.font = assets.font;

        loadTextures();
        createAliens();

        hudCamera.position.set(
            640f,
            360f,
            0f
        );

        hudCamera.update();

        camera.position.set(
            640f,
            360f,
            0f
        );

        camera.update();

        if (loadSave) {
            load();
        }
    }

    // =========================================================
    // TEXTURAS
    // =========================================================

    private void loadTextures() {

        background =
            new Texture(
                Gdx.files.internal(
                    "textures/marte_background.png"
                )
            );

        playerTexture =
            new Texture(
                Gdx.files.internal(
                    "textures/lua_astronauta.png"
                )
            );

        alienTexture =
            new Texture(
                Gdx.files.internal(
                    "textures/alien.png"
                )
            );
    }

    // =========================================================
    // ALIENS
    // =========================================================

    private void createAliens() {

        float[][] positions = {
            {800f, 1300f},
            {1200f, 500f},
            {1600f, 1000f},
            {2050f, 1450f},
            {2500f, 600f}
        };

        for (int i = 0; i < ALIEN_COUNT; i++) {

            aliens[i] =
                new Rectangle(
                    positions[i][0],
                    positions[i][1],
                    i == BOSS_INDEX ? 90f : 64f,
                    i == BOSS_INDEX ? 90f : 64f
                );

            alienAlive[i] = true;

            alienSpeeds[i] =
                i == BOSS_INDEX
                    ? 55f
                    : 85f + i * 12f;

            // BOSS = 100 HP
            if (i == BOSS_INDEX) {
                alienHealth[i] =
                    BOSS_MAX_HEALTH;
            } else {
                alienHealth[i] = 24;
            }
        }
    }

    // =========================================================
    // LOAD
    // =========================================================

    private void load() {

        player.setPosition(
            saveManager.getX(),
            saveManager.getY()
        );

        oxygen =
            saveManager.getOxygen();

        energy =
            saveManager.getEnergy();

        ice =
            saveManager.getIce();

        water =
            saveManager.getWater();

        fuel =
            saveManager.getFuel();

        if (
            "TITA".equals(
                saveManager.getPhase()
            )
        ) {

            message =
                "SAVE DE TITA CARREGADO.";

            messageTimer = 4f;
        }
    }

    // =========================================================
    // RENDER
    // =========================================================

    @Override
    public void render(float delta) {

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.ESCAPE
            )
                &&
                !dialogueActive
                &&
                !victory
                &&
                !gameOver
        ) {

            paused = !paused;
        }

        drawWorld();
        drawHud();

        if (paused) {
            drawPause();
            return;
        }

        if (dialogueActive) {
            drawDialogue();
            return;
        }

        if (gameOver) {
            drawGameOver();
            return;
        }

        if (victory) {
            drawVictory();
            return;
        }

        update(delta);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    private void update(float delta) {

        updatePlayer(delta);
        updateOxygen(delta);
        updateAliens(delta);
        updateCombat(delta);
        updateBossAttack(delta);
        updateEnemyProjectiles(delta);
        updatePortal(delta);

        if (laserCooldown > 0f) {

            laserCooldown -= delta;

            if (laserCooldown < 0f) {
                laserCooldown = 0f;
            }
        }

        if (bossAttackCooldown > 0f) {

            bossAttackCooldown -= delta;

            if (bossAttackCooldown < 0f) {
                bossAttackCooldown = 0f;
            }
        }

        if (messageTimer > 0f) {
            messageTimer -= delta;
        }

        if (oxygen <= 0f) {

            oxygen = 0f;
            gameOver = true;
        }
    }

    // =========================================================
    // PLAYER
    // =========================================================

    private void updatePlayer(float delta) {

        float dx = 0f;
        float dy = 0f;

        if (
            Gdx.input.isKeyPressed(Input.Keys.W)
                ||
                Gdx.input.isKeyPressed(Input.Keys.UP)
        ) {
            dy++;
        }

        if (
            Gdx.input.isKeyPressed(Input.Keys.S)
                ||
                Gdx.input.isKeyPressed(Input.Keys.DOWN)
        ) {
            dy--;
        }

        if (
            Gdx.input.isKeyPressed(Input.Keys.A)
                ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT)
        ) {
            dx--;
        }

        if (
            Gdx.input.isKeyPressed(Input.Keys.D)
                ||
                Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        ) {
            dx++;
        }

        if (dx != 0f || dy != 0f) {

            float length =
                (float)
                    Math.sqrt(
                        dx * dx +
                            dy * dy
                    );

            dx /= length;
            dy /= length;

            player.x +=
                dx *
                    PLAYER_SPEED *
                    delta;

            player.y +=
                dy *
                    PLAYER_SPEED *
                    delta;

            energy =
                Math.max(
                    0f,
                    energy - delta * 0.3f
                );
        }

        player.x =
            Math.max(
                0f,
                Math.min(
                    WORLD_WIDTH -
                        player.width,
                    player.x
                )
            );

        player.y =
            Math.max(
                0f,
                Math.min(
                    WORLD_HEIGHT -
                        player.height,
                    player.y
                )
            );
    }

    // =========================================================
    // O2
    // =========================================================

    private void updateOxygen(
        float delta
    ) {

        oxygen =
            Math.max(
                0f,
                oxygen -
                    delta *
                        0.65f
            );
    }

    // =========================================================
    // ALIENS
    // =========================================================

    private void updateAliens(
        float delta
    ) {

        for (int i = 0; i < ALIEN_COUNT; i++) {

            if (!alienAlive[i]) {
                continue;
            }

            Rectangle alien =
                aliens[i];

            float ax =
                alien.x +
                    alien.width / 2f;

            float ay =
                alien.y +
                    alien.height / 2f;

            float px =
                player.x +
                    player.width / 2f;

            float py =
                player.y +
                    player.height / 2f;

            float dx = px - ax;
            float dy = py - ay;

            float distance =
                (float)
                    Math.sqrt(
                        dx * dx +
                            dy * dy
                    );

            if (distance > 1f) {

                dx /= distance;
                dy /= distance;

                alien.x +=
                    dx *
                        alienSpeeds[i] *
                        delta;

                alien.y +=
                    dy *
                        alienSpeeds[i] *
                        delta;
            }

            if (
                player.overlaps(
                    alien
                )
            ) {

                oxygen =
                    Math.max(
                        0f,
                        oxygen -
                            delta * 15f
                    );

                energy =
                    Math.max(
                        0f,
                        energy -
                            delta * 4f
                    );
            }
        }
    }

    // =========================================================
    // COMBATE
    // =========================================================

    private void updateCombat(
        float delta
    ) {

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.SPACE
            )
        ) {

            if (laserCooldown > 0f) {

                showMessage(
                    "LASER EM COOLDOWN!"
                );

            } else {

                Rectangle target =
                    findNearestAlien();

                if (target == null) {

                    showMessage(
                        "NAO HA INIMIGOS."
                    );

                } else {

                    float distance =
                        distanceBetween(
                            player,
                            target
                        );

                    // REGRA VISÍVEL DE ALCANCE
                    if (
                        distance >
                            LASER_RANGE
                    ) {

                        showMessage(
                            "ALVO FORA DO ALCANCE! MAX: 900"
                        );

                    } else {

                        fireLaser(target);

                        laserCooldown =
                            LASER_COOLDOWN;
                    }
                }
            }
        }

        updateLasers(delta);
    }

    // =========================================================
    // TARGET
    // =========================================================

    private Rectangle findNearestAlien() {

        Rectangle result = null;

        float best =
            Float.MAX_VALUE;

        float px =
            player.x +
                player.width / 2f;

        float py =
            player.y +
                player.height / 2f;

        for (int i = 0; i < ALIEN_COUNT; i++) {

            if (!alienAlive[i]) {
                continue;
            }

            float ax =
                aliens[i].x +
                    aliens[i].width / 2f;

            float ay =
                aliens[i].y +
                    aliens[i].height / 2f;

            float dx = ax - px;
            float dy = ay - py;

            float distance =
                (float)
                    Math.sqrt(
                        dx * dx +
                            dy * dy
                    );

            if (distance < best) {

                best = distance;
                result = aliens[i];
            }
        }

        return result;
    }

    // =========================================================
    // FIRE LASER
    // =========================================================

    private void fireLaser(
        Rectangle target
    ) {

        float px =
            player.x +
                player.width / 2f;

        float py =
            player.y +
                player.height / 2f;

        float tx =
            target.x +
                target.width / 2f;

        float ty =
            target.y +
                target.height / 2f;

        float dx = tx - px;
        float dy = ty - py;

        float length =
            (float)
                Math.sqrt(
                    dx * dx +
                        dy * dy
                );

        if (length <= 0f) {
            return;
        }

        dx /= length;
        dy /= length;

        lasers.add(
            new Rectangle(
                px,
                py,
                42f,
                8f
            )
        );

        laserVelocities.add(
            new Vector2(
                dx * LASER_SPEED,
                dy * LASER_SPEED
            )
        );

        showMessage(
            "LASER DISPARADO! DANO: 8"
        );
    }

    // =========================================================
    // UPDATE LASERS
    // =========================================================

    private void updateLasers(
        float delta
    ) {

        for (
            int i = lasers.size() - 1;
            i >= 0;
            i--
        ) {

            Rectangle laser =
                lasers.get(i);

            Vector2 velocity =
                laserVelocities.get(i);

            laser.x +=
                velocity.x * delta;

            laser.y +=
                velocity.y * delta;

            boolean hit = false;

            for (
                int j = 0;
                j < ALIEN_COUNT;
                j++
            ) {

                if (!alienAlive[j]) {
                    continue;
                }

                if (
                    laser.overlaps(
                        aliens[j]
                    )
                ) {

                    // Cada tiro = 8 de dano
                    alienHealth[j] -=
                        PLAYER_DAMAGE;

                    hit = true;

                    if (
                        alienHealth[j] <= 0
                    ) {

                        killAlien(j);

                    } else {

                        if (j == BOSS_INDEX) {

                            showMessage(
                                "BOSS VERMELHO: "
                                    +
                                    alienHealth[j]
                                    +
                                    "/100 HP"
                            );

                        } else {

                            showMessage(
                                "ALIEN ATINGIDO!"
                            );
                        }
                    }

                    break;
                }
            }

            if (
                hit
                    ||
                    laser.x < -100f
                    ||
                    laser.x >
                        WORLD_WIDTH + 100f
                    ||
                    laser.y < -100f
                    ||
                    laser.y >
                        WORLD_HEIGHT + 100f
            ) {

                lasers.remove(i);
                laserVelocities.remove(i);
            }
        }
    }

    // =========================================================
    // MORTE DO ALIEN
    // =========================================================

    private void killAlien(int index) {

        if (!alienAlive[index]) {
            return;
        }

        alienAlive[index] = false;
        aliensDefeated++;

        if (index == BOSS_INDEX) {

            bossDefeated = true;

            // MISSÃO SÓ MUDA AQUI,
            // DEPOIS DA MORTE REAL DO BOSS
            missionText =
                "OBJETIVO: Descubra a saida de Tita.";

            showMessage(
                "ALIEN VERMELHO DERROTADO!"
            );

            // Inicia diálogo
            dialogueActive = true;
            dialogueFinished = false;
            dialogueIndex = 0;

            // Salva a fase TITA
            saveManager.save(
                "TITA",
                player.x,
                player.y,
                oxygen,
                energy,
                ice,
                water,
                fuel,
                true
            );

        } else {

            showMessage(
                "ALIEN DE TITA DESTRUIDO! "
                    +
                    aliensDefeated +
                    "/5"
            );
        }
    }

    // =========================================================
    // ATAQUE RADIAL DO BOSS
    // =========================================================

    private void updateBossAttack(
        float delta
    ) {

        if (!bossDefeated &&
            alienAlive[BOSS_INDEX]) {

            Rectangle boss =
                aliens[BOSS_INDEX];

            float distance =
                distanceBetween(
                    player,
                    boss
                );

            if (
                distance <=
                    BOSS_ATTACK_RANGE
            ) {

                if (
                    bossAttackCooldown <= 0f
                ) {

                    fireBossRadialAttack();

                    bossAttackCooldown =
                        BOSS_ATTACK_COOLDOWN;
                }
            }
        }
    }

    // =========================================================
    // ATAQUE EM 8 DIREÇÕES
    // =========================================================

    private void fireBossRadialAttack() {

        Rectangle boss =
            aliens[BOSS_INDEX];

        float cx =
            boss.x +
                boss.width / 2f;

        float cy =
            boss.y +
                boss.height / 2f;

        for (int i = 0; i < 8; i++) {

            double angle =
                Math.toRadians(
                    i * 45
                );

            float dx =
                (float)
                    Math.cos(angle);

            float dy =
                (float)
                    Math.sin(angle);

            enemyProjectiles.add(
                new Rectangle(
                    cx - 6f,
                    cy - 6f,
                    12f,
                    12f
                )
            );

            enemyProjectileVelocities.add(
                new Vector2(
                    dx *
                        BOSS_PROJECTILE_SPEED,

                    dy *
                        BOSS_PROJECTILE_SPEED
                )
            );
        }

        showMessage(
            "BOSS: ATAQUE RADIAL!"
        );
    }

    // =========================================================
    // PROJÉTEIS DO BOSS
    // =========================================================

    private void updateEnemyProjectiles(
        float delta
    ) {

        for (
            int i =
            enemyProjectiles.size() - 1;
            i >= 0;
            i--
        ) {

            Rectangle projectile =
                enemyProjectiles.get(i);

            Vector2 velocity =
                enemyProjectileVelocities.get(i);

            projectile.x +=
                velocity.x * delta;

            projectile.y +=
                velocity.y * delta;

            if (
                projectile.overlaps(
                    player
                )
            ) {

                oxygen =
                    Math.max(
                        0f,
                        oxygen -
                            BOSS_DAMAGE
                    );

                energy =
                    Math.max(
                        0f,
                        energy -
                            5f
                    );

                enemyProjectiles.remove(i);
                enemyProjectileVelocities.remove(i);

                showMessage(
                    "VOCE FOI ATINGIDO!"
                );

                continue;
            }

            if (
                projectile.x < -100f
                    ||
                    projectile.x >
                        WORLD_WIDTH + 100f
                    ||
                    projectile.y < -100f
                    ||
                    projectile.y >
                        WORLD_HEIGHT + 100f
            ) {

                enemyProjectiles.remove(i);
                enemyProjectileVelocities.remove(i);
            }
        }
    }

    // =========================================================
    // PORTAL
    // =========================================================

    private boolean canOpenPortal() {

        return
            aliensDefeated >= ALIEN_COUNT
                &&
                bossDefeated
                &&
                dialogueFinished;
    }

    private void updatePortal(
        float delta
    ) {

        portalTime += delta;

        // Portal NÃO abre de graça.
        if (!canOpenPortal()) {
            return;
        }

        if (
            player.overlaps(
                exitPortal
            )
        ) {

            showMessage(
                "PORTAL ATIVO! PRESSIONE E."
            );

            if (
                Gdx.input.isKeyJustPressed(
                    Input.Keys.E
                )
            ) {

                victory = true;

                missionText =
                    "OBJETIVO CONCLUIDO: Saia de Tita.";

                saveManager.save(
                    "TITA",
                    player.x,
                    player.y,
                    oxygen,
                    energy,
                    ice,
                    water,
                    fuel,
                    true
                );
            }
        }
    }

    // =========================================================
    // DISTÂNCIA
    // =========================================================

    private float distanceBetween(
        Rectangle a,
        Rectangle b
    ) {

        float ax =
            a.x +
                a.width / 2f;

        float ay =
            a.y +
                a.height / 2f;

        float bx =
            b.x +
                b.width / 2f;

        float by =
            b.y +
                b.height / 2f;

        float dx = bx - ax;
        float dy = by - ay;

        return (float)
            Math.sqrt(
                dx * dx +
                    dy * dy
            );
    }

    // =========================================================
    // WORLD
    // =========================================================

    private void drawWorld() {

        // =====================================================
        // TITÃ TEM COR PRÓPRIA
        // =====================================================

        Gdx.gl.glClearColor(
            0.32f,
            0.20f,
            0.05f,
            1f
        );

        Gdx.gl.glClear(
            GL20.GL_COLOR_BUFFER_BIT
        );

        // =====================================================
        // CÂMERA SEGUE O PERSONAGEM
        // =====================================================

        camera.position.x +=
            (
                player.x +
                    player.width / 2f -
                    camera.position.x
            ) * 0.10f;

        camera.position.y +=
            (
                player.y +
                    player.height / 2f -
                    camera.position.y
            ) * 0.10f;

        camera.position.x =
            Math.max(
                640f,
                Math.min(
                    2360f,
                    camera.position.x
                )
            );

        camera.position.y =
            Math.max(
                360f,
                Math.min(
                    1540f,
                    camera.position.y
                )
            );

        worldViewport.apply();

        camera.update();

        batch.setProjectionMatrix(
            camera.combined
        );

        batch.begin();

        // =====================================================
        // FUNDO
        // =====================================================

        batch.setColor(
            0.70f,
            0.48f,
            0.20f,
            1f
        );

        for (
            int x = 0;
            x < WORLD_WIDTH;
            x += 512
        ) {

            for (
                int y = 0;
                y < WORLD_HEIGHT;
                y += 512
            ) {

                batch.draw(
                    background,
                    x,
                    y,
                    512,
                    512
                );
            }
        }

        batch.setColor(
            Color.WHITE
        );

        // =====================================================
        // ALIENS
        // =====================================================

        for (int i = 0; i < ALIEN_COUNT; i++) {

            if (!alienAlive[i]) {
                continue;
            }

            Rectangle alien =
                aliens[i];

            if (i == BOSS_INDEX) {

                // BOSS VERMELHO
                batch.setColor(
                    1f,
                    0.08f,
                    0.05f,
                    1f
                );

            } else {

                // ALIENS NORMAIS
                batch.setColor(
                    0.20f,
                    1f,
                    0.20f,
                    1f
                );
            }

            batch.draw(
                alienTexture,
                alien.x,
                alien.y,
                alien.width,
                alien.height
            );

            batch.setColor(
                Color.WHITE
            );
        }

        // =====================================================
        // PLAYER
        // =====================================================

        batch.draw(
            playerTexture,
            player.x,
            player.y,
            player.width,
            player.height
        );

        batch.end();

        // =====================================================
        // PROJÉTEIS E PORTAL
        // =====================================================

        shapeRenderer.setProjectionMatrix(
            camera.combined
        );

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        // -----------------------------------------------------
        // TIROS DO PLAYER
        // -----------------------------------------------------

        shapeRenderer.setColor(
            0.2f,
            0.9f,
            1f,
            1f
        );

        for (Rectangle laser : lasers) {

            shapeRenderer.rect(
                laser.x,
                laser.y,
                laser.width,
                laser.height
            );
        }

        // -----------------------------------------------------
        // TIROS DO BOSS
        // -----------------------------------------------------

        shapeRenderer.setColor(
            1f,
            0.15f,
            0.05f,
            1f
        );

        for (
            Rectangle projectile :
            enemyProjectiles
        ) {

            shapeRenderer.circle(
                projectile.x +
                    projectile.width / 2f,
                projectile.y +
                    projectile.height / 2f,
                projectile.width / 2f
            );
        }

        // -----------------------------------------------------
        // VIDA DO BOSS
        // -----------------------------------------------------

        if (
            alienAlive[BOSS_INDEX]
        ) {

            Rectangle boss =
                aliens[BOSS_INDEX];

            float barX =
                boss.x - 10f;

            float barY =
                boss.y +
                    boss.height +
                    15f;

            float barWidth =
                110f;

            float barHeight =
                10f;

            // Fundo
            shapeRenderer.setColor(
                0.1f,
                0.1f,
                0.1f,
                1f
            );

            shapeRenderer.rect(
                barX,
                barY,
                barWidth,
                barHeight
            );

            // Vida
            float healthPercent =
                alienHealth[BOSS_INDEX] /
                    (float) BOSS_MAX_HEALTH;

            shapeRenderer.setColor(
                1f,
                0.1f,
                0.1f,
                1f
            );

            shapeRenderer.rect(
                barX,
                barY,
                barWidth *
                    healthPercent,
                barHeight
            );
        }

        // -----------------------------------------------------
        // PORTAL
        // -----------------------------------------------------

        float pulse =
            (float)
                Math.sin(
                    portalTime * 4f
                );

        float cx =
            exitPortal.x +
                exitPortal.width / 2f;

        float cy =
            exitPortal.y +
                exitPortal.height / 2f;

        if (!canOpenPortal()) {

            // PORTAL BLOQUEADO

            shapeRenderer.setColor(
                0.10f,
                0.08f,
                0.05f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                75f
            );

            shapeRenderer.setColor(
                0.02f,
                0.02f,
                0.01f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                50f
            );

        } else {

            // PORTAL ABERTO

            shapeRenderer.setColor(
                1f,
                0.55f,
                0.10f,
                0.30f
            );

            shapeRenderer.circle(
                cx,
                cy,
                95f +
                    pulse * 8f
            );

            shapeRenderer.setColor(
                1f,
                0.75f,
                0.20f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                70f
            );

            shapeRenderer.setColor(
                0.12f,
                0.06f,
                0.01f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                48f
            );
        }

        shapeRenderer.end();
    }

    // =========================================================
    // HUD
    // =========================================================

    private void drawHud() {

        hudViewport.apply();

        hudCamera.position.set(
            640f,
            360f,
            0f
        );

        hudCamera.update();

        batch.setProjectionMatrix(
            hudCamera.combined
        );

        batch.begin();

        // =====================================================
        // TITULO
        // =====================================================

        font.setColor(
            1f,
            0.70f,
            0.25f,
            1f
        );

        font.getData().setScale(
            1.2f
        );

        font.draw(
            batch,
            "ECHOES // TITA",
            25f,
            690f
        );

        // =====================================================
        // STATUS
        // =====================================================

        font.setColor(
            Color.WHITE
        );

        font.getData().setScale(
            0.9f
        );

        font.draw(
            batch,
            String.format(
                "O2: %.0f  ENERGIA: %.0f",
                oxygen,
                energy
            ),
            25f,
            655f
        );

        font.draw(
            batch,
            "ALIENS: "
                +
                aliensDefeated
                +
                "/5",
            25f,
            625f
        );

        // =====================================================
        // BOSS
        // =====================================================

        if (
            alienAlive[BOSS_INDEX]
        ) {

            font.setColor(
                1f,
                0.20f,
                0.15f,
                1f
            );

            font.draw(
                batch,
                "BOSS VERMELHO: "
                    +
                    alienHealth[BOSS_INDEX]
                    +
                    "/100 HP",
                25f,
                595f
            );

        } else {

            font.setColor(
                0.40f,
                1f,
                0.45f,
                1f
            );

            font.draw(
                batch,
                "BOSS VERMELHO: DERROTADO",
                25f,
                595f
            );
        }

        // =====================================================
        // LASER
        // =====================================================

        if (laserCooldown <= 0f) {

            font.setColor(
                0.4f,
                1f,
                0.5f,
                1f
            );

            font.draw(
                batch,
                "LASER: PRONTO | DANO: 8",
                25f,
                565f
            );

        } else {

            font.setColor(
                1f,
                0.75f,
                0.3f,
                1f
            );

            font.draw(
                batch,
                String.format(
                    "LASER: %.1fs | DANO: 8",
                    laserCooldown
                ),
                25f,
                565f
            );
        }

        // =====================================================
        // REGRA DO TIRO
        // =====================================================

        font.setColor(
            0.55f,
            0.85f,
            1f,
            1f
        );

        font.draw(
            batch,
            "ALCANCE DO LASER: 900",
            25f,
            535f
        );

        // =====================================================
        // MISSÃO
        // =====================================================

        font.setColor(
            1f,
            0.85f,
            0.25f,
            1f
        );

        font.draw(
            batch,
            missionText,
            25f,
            500f
        );

        // =====================================================
        // PORTAL
        // =====================================================

        font.setColor(
            Color.WHITE
        );

        if (!bossDefeated) {

            font.draw(
                batch,
                "PORTAL: BLOQUEADO PELO BOSS",
                25f,
                470f
            );

        } else if (
            !dialogueFinished
        ) {

            font.draw(
                batch,
                "PORTAL: AGUARDE O DIALOGO",
                25f,
                470f
            );

        } else if (
            aliensDefeated < ALIEN_COUNT
        ) {

            font.draw(
                batch,
                "PORTAL: DERROTE OS "
                    +
                    ALIEN_COUNT
                    +
                    " ALIENS",
                25f,
                470f
            );

        } else {

            font.setColor(
                0.5f,
                1f,
                0.6f,
                1f
            );

            font.draw(
                batch,
                "PORTAL: ABERTO | E",
                25f,
                470f
            );
        }

        // =====================================================
        // MENSAGEM
        // =====================================================

        if (messageTimer > 0f) {

            font.setColor(
                0.45f,
                1f,
                0.65f,
                1f
            );

            font.draw(
                batch,
                message,
                25f,
                430f
            );
        }

        // =====================================================
        // CONTROLES
        // =====================================================

        font.setColor(
            0.75f,
            0.80f,
            0.88f,
            1f
        );

        font.getData().setScale(
            0.72f
        );

        font.draw(
            batch,
            "WASD/SETAS MOVER | SPACE LASER | E INTERAGIR | ESC PAUSA",
            25f,
            35f
        );

        font.getData().setScale(
            1f
        );

        batch.end();
    }

    // =========================================================
    // DIÁLOGO
    // =========================================================

    private void drawDialogue() {

        hudViewport.apply();

        hudCamera.position.set(
            640f,
            360f,
            0f
        );

        hudCamera.update();

        batch.setProjectionMatrix(
            hudCamera.combined
        );

        batch.begin();

        font.setColor(
            0f,
            0f,
            0f,
            0.80f
        );

        font.getData().setScale(
            1f
        );

        font.draw(
            batch,
            "ALIEN VERMELHO",
            90f,
            275f
        );

        font.setColor(
            1f,
            0.25f,
            0.20f,
            1f
        );

        font.getData().setScale(
            1.15f
        );

        font.draw(
            batch,
            "ALIEN VERMELHO",
            80f,
            570f
        );

        font.setColor(
            Color.WHITE
        );

        font.getData().setScale(
            1.05f
        );

        font.draw(
            batch,
            bossDialogue[dialogueIndex],
            80f,
            500f,
            1120f,
            1,
            true
        );

        font.setColor(
            0.75f,
            0.80f,
            0.88f,
            1f
        );

        font.getData().setScale(
            0.80f
        );

        font.draw(
            batch,
            "Fala "
                +
                (dialogueIndex + 1)
                +
                "/3   [ ENTER ] continuar",
            80f,
            430f
        );

        font.getData().setScale(
            1f
        );

        batch.end();

        // =====================================================
        // CONTROLE DO DIÁLOGO
        // =====================================================

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.ENTER
            )
        ) {

            dialogueIndex++;

            if (
                dialogueIndex >=
                    bossDialogue.length
            ) {

                dialogueActive = false;
                dialogueFinished = true;

                missionText =
                    "OBJETIVO: Derrote os 4 aliens restantes e va ao portal.";

                showMessage(
                    "DIALOGO CONCLUIDO. PORTAL AINDA BLOQUEADO."
                );

                saveManager.save(
                    "TITA",
                    player.x,
                    player.y,
                    oxygen,
                    energy,
                    ice,
                    water,
                    fuel,
                    true
                );
            }
        }
    }

    // =========================================================
    // PAUSA
    // =========================================================

    private void drawPause() {

        hudViewport.apply();

        hudCamera.position.set(
            640f,
            360f,
            0f
        );

        hudCamera.update();

        batch.setProjectionMatrix(
            hudCamera.combined
        );

        batch.begin();

        font.setColor(
            0.75f,
            0.90f,
            1f,
            1f
        );

        font.getData().setScale(
            2.4f
        );

        font.draw(
            batch,
            "PAUSADO",
            500f,
            520f
        );

        font.setColor(
            Color.WHITE
        );

        font.getData().setScale(
            1.15f
        );

        font.draw(
            batch,
            "[ ENTER ] CONTINUAR",
            455f,
            400f
        );

        font.draw(
            batch,
            "[ S ] SALVAR",
            505f,
            340f
        );

        font.draw(
            batch,
            "[ Q ] SAIR PARA O TITULO",
            425f,
            280f
        );

        font.getData().setScale(
            1f
        );

        batch.end();

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.ENTER
            )
        ) {

            paused = false;
            return;
        }

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.S
            )
        ) {

            saveManager.save(
                "TITA",
                player.x,
                player.y,
                oxygen,
                energy,
                ice,
                water,
                fuel,
                true
            );

            showMessage(
                "JOGO SALVO EM TITA!"
            );

            return;
        }

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.Q
            )
        ) {

            game.setScreen(
                new MenuScreen(
                    game,
                    batch,
                    assets
                )
            );
        }
    }

    // =========================================================
    // VITÓRIA
    // =========================================================

    private void drawVictory() {

        hudViewport.apply();

        hudCamera.position.set(
            640f,
            360f,
            0f
        );

        hudCamera.update();

        batch.setProjectionMatrix(
            hudCamera.combined
        );

        batch.begin();

        font.setColor(
            1f,
            0.8f,
            0.25f,
            1f
        );

        font.getData().setScale(
            2.5f
        );

        font.draw(
            batch,
            "TITA CONCLUIDA!",
            450f,
            450f
        );

        font.setColor(
            Color.WHITE
        );

        font.getData().setScale(
            1.1f
        );

        font.draw(
            batch,
            "Todos os aliens foram derrotados.",
            450f,
            385f
        );

        font.draw(
            batch,
            "O portal foi liberado pelo seu progresso.",
            410f,
            345f
        );

        font.draw(
            batch,
            "[ ENTER ] FINALIZAR",
            500f,
            280f
        );

        font.getData().setScale(
            1f
        );

        batch.end();

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.ENTER
            )
        ) {

            game.setScreen(
                new VictoryScreen(
                    game,
                    batch,
                    assets,
                    0
                )
            );
        }
    }

    // =========================================================
    // GAME OVER
    // =========================================================

    private void drawGameOver() {

        hudViewport.apply();

        hudCamera.position.set(
            640f,
            360f,
            0f
        );

        hudCamera.update();

        batch.setProjectionMatrix(
            hudCamera.combined
        );

        batch.begin();

        font.setColor(
            1f,
            0.2f,
            0.2f,
            1f
        );

        font.getData().setScale(
            2.8f
        );

        font.draw(
            batch,
            "GAME OVER",
            490f,
            450f
        );

        font.setColor(
            Color.WHITE
        );

        font.getData().setScale(
            1.1f
        );

        font.draw(
            batch,
            "O2 acabou.",
            540f,
            380f
        );

        font.draw(
            batch,
            "[ ENTER ] VOLTAR",
            505f,
            300f
        );

        font.getData().setScale(
            1f
        );

        batch.end();

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.ENTER
            )
        ) {

            game.setScreen(
                new MenuScreen(
                    game,
                    batch,
                    assets
                )
            );
        }
    }

    // =========================================================
    // MENSAGEM
    // =========================================================

    private void showMessage(
        String text
    ) {

        message = text;
        messageTimer = 3f;
    }

    // =========================================================
    // RESIZE
    // =========================================================

    @Override
    public void resize(
        int width,
        int height
    ) {

        worldViewport.update(
            width,
            height,
            false
        );

        hudViewport.update(
            width,
            height,
            true
        );
    }

    // =========================================================
    // LIFECYCLE
    // =========================================================

    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

        background.dispose();
        playerTexture.dispose();
        alienTexture.dispose();
        shapeRenderer.dispose();
    }
}

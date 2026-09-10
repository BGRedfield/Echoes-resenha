package screens.lua;

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
import screens.GameScreen;
import screens.MenuScreen;

public class LunarScreen implements Screen {

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
        new Rectangle(180f, 180f, 54f, 54f);

    private static final float PLAYER_SPEED = 250f;

    // =========================================================
    // BASE
    // =========================================================

    private final Rectangle base =
        new Rectangle(380f, 820f, 320f, 190f);

    private final Rectangle processor =
        new Rectangle(485f, 875f, 120f, 90f);

    // =========================================================
    // PORTAL
    // =========================================================

    private final Rectangle portal =
        new Rectangle(2550f, 850f, 130f, 130f);

    private float portalTime = 0f;

    // =========================================================
    // ITENS
    // =========================================================

    private final Rectangle[] oxygenItems = {
        new Rectangle(800f, 350f, 48f, 48f),
        new Rectangle(1050f, 1450f, 48f, 48f)
    };

    private final Rectangle[] foodItems = {
        new Rectangle(1250f, 1200f, 48f, 48f),
        new Rectangle(1650f, 300f, 48f, 48f)
    };

    private final Rectangle[] iceItems = {
        new Rectangle(1900f, 600f, 60f, 60f),
        new Rectangle(2200f, 1300f, 60f, 60f)
    };

    private boolean gotOxygen = false;
    private boolean gotFood = false;
    private boolean gotIce = false;

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

    private int aliensDefeated = 0;

    // =========================================================
    // LASERS
    // =========================================================

    private final List<Rectangle> lasers =
        new ArrayList<>();

    private final List<Vector2> laserVelocities =
        new ArrayList<>();

    private float laserCooldown = 0f;

    private static final float LASER_COOLDOWN =
        2f;

    private static final float LASER_SPEED =
        900f;

    // =========================================================
    // STATUS
    // =========================================================

    private float oxygen = 100f;
    private float energy = 100f;

    private int ice = 0;
    private int water = 0;
    private int fuel = 0;

    private boolean processedIce = false;
    private boolean missionComplete = false;

    // =========================================================
    // GAME
    // =========================================================

    private boolean paused = false;
    private boolean gameOver = false;

    // =========================================================
    // MENSAGEM
    // =========================================================

    private String message =
        "Explore a Lua e complete a missao.";

    private float messageTimer = 5f;

    // =========================================================
    // TEXTURAS
    // =========================================================

    private Texture background;
    private Texture playerTexture;
    private Texture baseTexture;
    private Texture processorTexture;
    private Texture oxygenTexture;
    private Texture foodTexture;
    private Texture iceTexture;
    private Texture alienTexture;

    // =========================================================
    // CONSTRUTORES
    // =========================================================

    public LunarScreen(
        EchoesMarsGame game,
        SpriteBatch batch,
        AssetManager assets
    ) {
        this(game, batch, assets, false);
    }

    public LunarScreen(
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

        hudCamera.position.set(640f, 360f, 0f);
        hudCamera.update();

        if (loadSave) {
            loadGame();
        }
    }

    // =========================================================
    // TEXTURAS
    // =========================================================

    private void loadTextures() {

        background = new Texture(
            Gdx.files.internal("textures/lua_background.png")
        );

        playerTexture = new Texture(
            Gdx.files.internal("textures/lua_astronauta.png")
        );

        baseTexture = new Texture(
            Gdx.files.internal("textures/lua_base.png")
        );

        processorTexture = new Texture(
            Gdx.files.internal("textures/lua_processador.png")
        );

        oxygenTexture = new Texture(
            Gdx.files.internal("textures/lua_oxigenio.png")
        );

        foodTexture = new Texture(
            Gdx.files.internal("textures/lua_comida.png")
        );

        iceTexture = new Texture(
            Gdx.files.internal("textures/lua_gelo.png")
        );

        alienTexture = new Texture(
            Gdx.files.internal("textures/alien.png")
        );
    }

    // =========================================================
    // ALIENS
    // =========================================================

    private void createAliens() {

        float[][] positions = {
            {900f, 1000f},
            {1300f, 1500f},
            {1700f, 700f},
            {2100f, 1100f},
            {2400f, 500f}
        };

        for (int i = 0; i < ALIEN_COUNT; i++) {

            aliens[i] = new Rectangle(
                positions[i][0],
                positions[i][1],
                64f,
                64f
            );

            alienAlive[i] = true;
            alienSpeeds[i] = 75f + i * 10f;
        }
    }

    // =========================================================
    // LOAD
    // =========================================================

    private void loadGame() {

        player.setPosition(
            saveManager.getX(),
            saveManager.getY()
        );

        oxygen = saveManager.getOxygen();
        energy = saveManager.getEnergy();

        ice = saveManager.getIce();
        water = saveManager.getWater();
        fuel = saveManager.getFuel();

        missionComplete =
            saveManager.isMissionReady();

        if (missionComplete) {

            gotOxygen = true;
            gotFood = true;
            gotIce = true;
            processedIce = true;

            for (int i = 0; i < ALIEN_COUNT; i++) {
                alienAlive[i] = false;
            }

            aliensDefeated = ALIEN_COUNT;
        }

        message = "SAVE CARREGADO.";
        messageTimer = 4f;
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
        ) {

            paused = !paused;
        }

        drawWorld();
        drawHud();

        if (paused) {
            drawPause();
            return;
        }

        if (gameOver) {
            drawGameOver();
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
        updateItems();
        updateAliens(delta);
        updateCombat(delta);
        updateMission();
        updatePortal(delta);

        if (messageTimer > 0f) {
            messageTimer -= delta;
        }

        if (laserCooldown > 0f) {
            laserCooldown -= delta;

            if (laserCooldown < 0f) {
                laserCooldown = 0f;
            }
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
                (float) Math.sqrt(
                    dx * dx + dy * dy
                );

            dx /= length;
            dy /= length;

            player.x +=
                dx * PLAYER_SPEED * delta;

            player.y +=
                dy * PLAYER_SPEED * delta;

            energy =
                Math.max(
                    0f,
                    energy - delta * 0.35f
                );
        }

        player.x =
            Math.max(
                0f,
                Math.min(
                    WORLD_WIDTH - player.width,
                    player.x
                )
            );

        player.y =
            Math.max(
                0f,
                Math.min(
                    WORLD_HEIGHT - player.height,
                    player.y
                )
            );
    }

    // =========================================================
    // O2
    // =========================================================

    private void updateOxygen(float delta) {

        if (player.overlaps(base)) {

            oxygen =
                Math.min(
                    100f,
                    oxygen + 20f * delta
                );

        } else {

            oxygen =
                Math.max(
                    0f,
                    oxygen - delta
                );
        }
    }

    // =========================================================
    // ITENS
    // =========================================================

    private void updateItems() {

        if (!gotOxygen) {

            for (Rectangle item : oxygenItems) {

                if (player.overlaps(item)) {

                    gotOxygen = true;

                    oxygen =
                        Math.min(
                            100f,
                            oxygen + 35f
                        );

                    showMessage(
                        "OXIGENIO COLETADO!"
                    );

                    break;
                }
            }
        }

        if (!gotFood) {

            for (Rectangle item : foodItems) {

                if (player.overlaps(item)) {

                    gotFood = true;

                    energy =
                        Math.min(
                            100f,
                            energy + 35f
                        );

                    showMessage(
                        "COMIDA COLETADA!"
                    );

                    break;
                }
            }
        }

        if (!gotIce) {

            for (Rectangle item : iceItems) {

                if (player.overlaps(item)) {

                    gotIce = true;
                    ice++;

                    showMessage(
                        "GELO COLETADO!"
                    );

                    break;
                }
            }
        }

        if (
            (
                player.overlaps(processor)
                    ||
                    player.overlaps(base)
            )
                &&
                Gdx.input.isKeyJustPressed(
                    Input.Keys.E
                )
        ) {

            processIce();
        }
    }

    // =========================================================
    // PROCESSAMENTO
    // =========================================================

    private void processIce() {

        if (ice <= 0) {

            showMessage(
                "VOCE NAO TEM GELO."
            );

            return;
        }

        ice--;
        water++;
        fuel++;

        processedIce = true;

        oxygen =
            Math.min(
                100f,
                oxygen + 25f
            );

        showMessage(
            "GELO PROCESSADO! +AGUA +COMBUSTIVEL"
        );
    }

    // =========================================================
    // ALIENS PERSEGUEM PLAYER
    // =========================================================

    private void updateAliens(float delta) {

        for (int i = 0; i < ALIEN_COUNT; i++) {

            if (!alienAlive[i]) {
                continue;
            }

            Rectangle alien = aliens[i];

            float alienCenterX =
                alien.x + alien.width / 2f;

            float alienCenterY =
                alien.y + alien.height / 2f;

            float playerCenterX =
                player.x + player.width / 2f;

            float playerCenterY =
                player.y + player.height / 2f;

            float dx =
                playerCenterX - alienCenterX;

            float dy =
                playerCenterY - alienCenterY;

            float distance =
                (float) Math.sqrt(
                    dx * dx + dy * dy
                );

            if (distance > 1f) {

                dx /= distance;
                dy /= distance;

                alien.x +=
                    dx * alienSpeeds[i] * delta;

                alien.y +=
                    dy * alienSpeeds[i] * delta;
            }

            // Dano
            if (player.overlaps(alien)) {

                oxygen =
                    Math.max(
                        0f,
                        oxygen - 14f * delta
                    );

                energy =
                    Math.max(
                        0f,
                        energy - 4f * delta
                    );
            }
        }
    }

    // =========================================================
    // COMBATE
    // =========================================================

    private void updateCombat(float delta) {

        if (
            !Gdx.input.isKeyJustPressed(
                Input.Keys.SPACE
            )
        ) {
            updateLasers(delta);
            return;
        }

        if (laserCooldown > 0f) {

            showMessage(
                "LASER EM COOLDOWN!"
            );

            updateLasers(delta);
            return;
        }

        Rectangle target =
            findNearestAlien();

        if (target == null) {

            updateLasers(delta);
            return;
        }

        fireLaser(target);

        laserCooldown =
            LASER_COOLDOWN;

        updateLasers(delta);
    }

    // =========================================================
    // ALIEN MAIS PRÓXIMO
    // =========================================================

    private Rectangle findNearestAlien() {

        Rectangle nearest = null;
        float nearestDistance =
            Float.MAX_VALUE;

        float px =
            player.x + player.width / 2f;

        float py =
            player.y + player.height / 2f;

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
                (float) Math.sqrt(
                    dx * dx + dy * dy
                );

            if (distance < nearestDistance) {

                nearestDistance = distance;
                nearest = aliens[i];
            }
        }

        return nearest;
    }

    // =========================================================
    // DISPARA LASER
    // =========================================================

    private void fireLaser(
        Rectangle target
    ) {

        float px =
            player.x + player.width / 2f;

        float py =
            player.y + player.height / 2f;

        float tx =
            target.x + target.width / 2f;

        float ty =
            target.y + target.height / 2f;

        float dx = tx - px;
        float dy = ty - py;

        float distance =
            (float) Math.sqrt(
                dx * dx + dy * dy
            );

        if (distance <= 0f) {
            return;
        }

        dx /= distance;
        dy /= distance;

        Rectangle laser =
            new Rectangle(
                px,
                py,
                42f,
                8f
            );

        Vector2 velocity =
            new Vector2(
                dx * LASER_SPEED,
                dy * LASER_SPEED
            );

        lasers.add(laser);
        laserVelocities.add(velocity);

        showMessage(
            "LASER DISPARADO!"
        );
    }

    // =========================================================
    // MOVIMENTO DOS LASERS
    // =========================================================

    private void updateLasers(float delta) {

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

            for (int j = 0; j < ALIEN_COUNT; j++) {

                if (!alienAlive[j]) {
                    continue;
                }

                if (laser.overlaps(aliens[j])) {

                    alienAlive[j] = false;

                    aliensDefeated++;

                    hit = true;

                    showMessage(
                        "ALIEN DESTRUIDO! "
                            +
                            aliensDefeated
                            +
                            "/"
                            +
                            ALIEN_COUNT
                    );

                    break;
                }
            }

            if (
                hit
                    ||
                    laser.x < -100f
                    ||
                    laser.x > WORLD_WIDTH + 100f
                    ||
                    laser.y < -100f
                    ||
                    laser.y > WORLD_HEIGHT + 100f
            ) {

                lasers.remove(i);
                laserVelocities.remove(i);
            }
        }
    }

    // =========================================================
    // MISSÃO
    // =========================================================

    private void updateMission() {

        if (
            gotOxygen
                &&
                gotFood
                &&
                gotIce
                &&
                processedIce
                &&
                water > 0
                &&
                fuel > 0
                &&
                aliensDefeated >= ALIEN_COUNT
                &&
                !missionComplete
        ) {

            missionComplete = true;

            showMessage(
                "MISSAO CONCLUIDA! PORTAL PARA MARTE LIBERADO!"
            );
        }
    }

    // =========================================================
    // PORTAL
    // =========================================================

    private void updatePortal(float delta) {

        portalTime += delta;

        if (!missionComplete) {
            return;
        }

        if (
            player.overlaps(portal)
                &&
                (
                    Gdx.input.isKeyJustPressed(
                        Input.Keys.E
                    )
                        ||
                        Gdx.input.isKeyJustPressed(
                            Input.Keys.ENTER
                        )
                )
        ) {

            saveManager.save(
                "MARTE",
                player.x,
                player.y,
                oxygen,
                energy,
                ice,
                water,
                fuel,
                true
            );

            game.setScreen(
                new GameScreen(
                    game,
                    batch,
                    assets,
                    false
                )
            );
        }
    }

    // =========================================================
    // DESENHAR MUNDO
    // =========================================================

    private void drawWorld() {

        Gdx.gl.glClearColor(
            0.035f,
            0.04f,
            0.055f,
            1f
        );

        Gdx.gl.glClear(
            GL20.GL_COLOR_BUFFER_BIT
        );

        camera.position.x +=
            (
                player.x +
                    player.width / 2f -
                    camera.position.x
            ) * 0.1f;

        camera.position.y +=
            (
                player.y +
                    player.height / 2f -
                    camera.position.y
            ) * 0.1f;

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

        // =====================================================
        // SPRITES
        // =====================================================

        batch.begin();

        for (int x = 0; x < WORLD_WIDTH; x += 512) {

            for (int y = 0; y < WORLD_HEIGHT; y += 512) {

                batch.draw(
                    background,
                    x,
                    y,
                    512,
                    512
                );
            }
        }

        batch.draw(
            baseTexture,
            base.x,
            base.y,
            base.width,
            base.height
        );

        batch.draw(
            processorTexture,
            processor.x,
            processor.y,
            processor.width,
            processor.height
        );

        if (!gotOxygen) {

            for (Rectangle item : oxygenItems) {

                batch.draw(
                    oxygenTexture,
                    item.x,
                    item.y,
                    item.width,
                    item.height
                );
            }
        }

        if (!gotFood) {

            for (Rectangle item : foodItems) {

                batch.draw(
                    foodTexture,
                    item.x,
                    item.y,
                    item.width,
                    item.height
                );
            }
        }

        if (!gotIce) {

            for (Rectangle item : iceItems) {

                batch.draw(
                    iceTexture,
                    item.x,
                    item.y,
                    item.width,
                    item.height
                );
            }
        }

        // Aliens verdes
        batch.setColor(
            0.2f,
            1f,
            0.2f,
            1f
        );

        for (int i = 0; i < ALIEN_COUNT; i++) {

            if (!alienAlive[i]) {
                continue;
            }

            Rectangle alien =
                aliens[i];

            batch.draw(
                alienTexture,
                alien.x,
                alien.y,
                alien.width,
                alien.height
            );
        }

        batch.setColor(Color.WHITE);

        batch.draw(
            playerTexture,
            player.x,
            player.y,
            player.width,
            player.height
        );

        batch.end();

        // =====================================================
        // LASERS
        // =====================================================

        shapeRenderer.setProjectionMatrix(
            camera.combined
        );

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

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

        shapeRenderer.end();

        // =====================================================
        // PORTAL
        // =====================================================

        drawPortal();
    }

    // =========================================================
    // PORTAL VISUAL
    // =========================================================

    private void drawPortal() {

        float pulse =
            (float)
                Math.sin(
                    portalTime * 4f
                );

        float cx =
            portal.x + portal.width / 2f;

        float cy =
            portal.y + portal.height / 2f;

        shapeRenderer.setProjectionMatrix(
            camera.combined
        );

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        if (!missionComplete) {

            shapeRenderer.setColor(
                0.12f,
                0.12f,
                0.16f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                82f
            );

            shapeRenderer.setColor(
                0.30f,
                0.30f,
                0.36f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                62f
            );

            shapeRenderer.setColor(
                0.025f,
                0.025f,
                0.04f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                45f
            );

        } else {

            shapeRenderer.setColor(
                0.05f,
                0.35f,
                1f,
                0.25f
            );

            shapeRenderer.circle(
                cx,
                cy,
                92f + pulse * 8f
            );

            shapeRenderer.setColor(
                0.10f,
                0.75f,
                1f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                70f + pulse * 4f
            );

            shapeRenderer.setColor(
                0.01f,
                0.025f,
                0.09f,
                1f
            );

            shapeRenderer.circle(
                cx,
                cy,
                50f
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

        font.setColor(
            0.70f,
            0.90f,
            1f,
            1f
        );

        font.getData().setScale(
            1.2f
        );

        font.draw(
            batch,
            "ECHOES // LUA",
            25f,
            690f
        );

        font.setColor(Color.WHITE);

        font.getData().setScale(
            0.9f
        );

        font.draw(
            batch,
            String.format(
                "O2: %.0f",
                oxygen
            ),
            25f,
            655f
        );

        font.draw(
            batch,
            String.format(
                "ENERGIA: %.0f",
                energy
            ),
            25f,
            625f
        );

        font.draw(
            batch,
            "GELO: " + ice
                +
                "  AGUA: " + water
                +
                "  COMB: " + fuel,
            25f,
            595f
        );

        font.draw(
            batch,
            "ALIENS: "
                +
                aliensDefeated
                +
                "/"
                +
                ALIEN_COUNT,
            25f,
            565f
        );

        if (laserCooldown <= 0f) {

            font.setColor(
                0.4f,
                1f,
                0.5f,
                1f
            );

            font.draw(
                batch,
                "LASER: PRONTO",
                25f,
                535f
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
                    "LASER: %.1fs",
                    laserCooldown
                ),
                25f,
                535f
            );
        }

        font.setColor(
            1f,
            0.85f,
            0.25f,
            1f
        );

        font.draw(
            batch,
            "OBJETIVO:",
            25f,
            495f
        );

        font.setColor(Color.WHITE);

        font.draw(
            batch,
            getObjective(),
            25f,
            468f
        );

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

        font.setColor(
            0.72f,
            0.78f,
            0.86f,
            1f
        );

        font.getData().setScale(
            0.72f
        );

        font.draw(
            batch,
            "WASD/SETAS MOVER",
            25f,
            35f
        );

        font.draw(
            batch,
            "SPACE LASER",
            210f,
            35f
        );

        font.draw(
            batch,
            "E INTERAGIR",
            350f,
            35f
        );

        font.draw(
            batch,
            "ESC PAUSA",
            500f,
            35f
        );

        font.getData().setScale(1f);

        batch.end();
    }

    // =========================================================
    // OBJETIVO
    // =========================================================

    private String getObjective() {

        if (!gotOxygen) {
            return "Colete oxigenio.";
        }

        if (!gotFood) {
            return "Colete comida.";
        }

        if (!gotIce) {
            return "Encontre uma rocha de gelo.";
        }

        if (!processedIce) {
            return "Leve gelo para a base e pressione E.";
        }

        if (aliensDefeated < ALIEN_COUNT) {

            return "Destrua os 5 aliens com o laser.";
        }

        if (!missionComplete) {
            return "Finalize a missao.";
        }

        if (!player.overlaps(portal)) {
            return "Va ate o portal para Marte.";
        }

        return "Pressione E para entrar em Marte.";
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

        font.setColor(Color.WHITE);

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

        font.getData().setScale(1f);

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

            saveGame();
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
    // SAVE
    // =========================================================

    private void saveGame() {

        saveManager.save(
            "LUA",
            player.x,
            player.y,
            oxygen,
            energy,
            ice,
            water,
            fuel,
            missionComplete
        );

        showMessage(
            "JOGO SALVO!"
        );
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

        font.setColor(Color.WHITE);

        font.getData().setScale(
            1.15f
        );

        font.draw(
            batch,
            "O oxigenio acabou.",
            500f,
            380f
        );

        font.draw(
            batch,
            "[ ENTER ] VOLTAR AO TITULO",
            450f,
            300f
        );

        font.getData().setScale(1f);

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

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {

        background.dispose();
        playerTexture.dispose();
        baseTexture.dispose();
        processorTexture.dispose();
        oxygenTexture.dispose();
        foodTexture.dispose();
        iceTexture.dispose();
        alienTexture.dispose();
        shapeRenderer.dispose();
    }
}

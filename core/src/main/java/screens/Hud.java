package screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import entities.Astronauta;
import managers.AssetManager;

public class Hud {

    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;

    public Hud(AssetManager assets) {
        this.font = assets.font;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(SpriteBatch batch, Astronauta astronauta, float cameraX, float cameraY) {
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barX = cameraX - 300;
        float barY = cameraY + 220;
        float barWidth = 220;
        float barHeight = 18;

        //Fundo oxigenio
        shapeRenderer.setColor(0.2f,0.2f,0.2f, 1f);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        //Barra oxigenio
        float oxiPercent = Math.max(0, astronauta.getOxigenio() / 100f);
        if (oxiPercent > 0.5f) shapeRenderer.setColor(0.2f,0.8f,1f,1f);
        else if (oxiPercent > 0.3f) shapeRenderer.setColor(1f,0.8f,0.2f,1f);
        else shapeRenderer.setColor(1f,0.2f,0.2f,1f);
        shapeRenderer.rect(barX,barY, barWidth * oxiPercent, barHeight);

        //Fundo energia
        shapeRenderer.setColor(0.2f,0.2f,0.2f, 1f);
        shapeRenderer.rect(barX, barY - 35, barWidth, barHeight);

        //Barra energia
        float enerPercent = Math.max(0, astronauta.getEnergia() / 100f);
        if (enerPercent > 0.5f) shapeRenderer.setColor(0.2f,0.8f,1f,1f);
        else if (enerPercent > 0.3f) shapeRenderer.setColor(1f,0.8f,0.2f,1f);
        else shapeRenderer.setColor(1f,0.2f,0.2f,1f);
        shapeRenderer.rect(barX, barY - 35, barWidth * enerPercent, barHeight);

        shapeRenderer.end();

        //Textos
        batch.begin();

        float hudX = cameraX - 300; //ajusta para esquerda
        float hudY = cameraY + 280; //Ajuste altura

        font.getData().setScale(1.3f);
        font.setColor(Color.WHITE);

        font.draw(batch, "ECHOES em Marte", hudX, hudY + 30);
        font.draw(batch, "Base Orion", hudX, hudY + 5);

        font.getData().setScale(1.1f);
        font.draw(batch, "02: " + (int) astronauta.getOxigenio() + "%", hudX + 230, barY + 15);
        font.draw(batch, "Energia: " + (int) astronauta.getEnergia() + "%", hudX + 230, barY - 20);

        font.setColor(0.9f,0.9f,0.6f,1f);
        font.draw(batch, "Tempo: " + (int) astronauta.getTempoVivo() + "s", hudX, barY - 70);

        if (astronauta.getOxigenio() < 30) {
            font.setColor(Color.RED);
            font.draw(batch, "OXIGÊNIO CRÍTICO!", hudX, barY - 100);
        }
        if (astronauta.getEnergia() < 30) {
            font.setColor(Color.ORANGE);
            font.draw(batch, "ENERGIA BAIXA!", hudX, barY - 125);
        }

        batch.end();
    }

    public void dispose() { if (shapeRenderer != null) shapeRenderer.dispose();}
}

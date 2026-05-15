package pt.isec.pa.deepsea.ui.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;

public class SuperficieView extends Canvas {
    private DeepSeaManager manager;

    public SuperficieView(DeepSeaManager manager) {
        this.manager = manager;
        this.widthProperty().addListener(e -> desenhar());
        this.heightProperty().addListener(e -> desenhar());

        this.setOnMouseClicked(event -> {
            int linhas = manager.getLinhasSuperficie();
            int colunas = manager.getColunasSuperficie();
            if (linhas == 0 || colunas == 0) return;

            double cellW = getWidth() / colunas;
            double cellH = getHeight() / linhas;

            int colClicada = (int) (event.getX() / cellW);
            int linClicada = (int) (event.getY() / cellH);

            manager.iniciarMergulho(linClicada, colClicada);
        });
    }

    public void desenhar() {
        GraphicsContext gc = getGraphicsContext2D();
        double w = getWidth();
        double h = getHeight();

        // Limpa o canvas
        gc.clearRect(0, 0, w, h);

        // Preenche o fundo do oceano
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, w, h);

        // Obtém as dimensões dinâmicas diretamente da tua classe SuperficieMarinha
        int linhas = manager.getLinhasSuperficie();
        int colunas = manager.getColunasSuperficie();

        // Evita divisão por zero se o jogo não estiver iniciado
        if (linhas == 0 || colunas == 0) return;

        double cellW = w / colunas;
        double cellH = h / linhas;

        // Desenhar o conteúdo das células (Radar de Artefactos)
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                // Se a célula contiver um artefacto no fundo, desenha um indicador visual
                if (manager.contemArtefactoNaSuperficie(i, j)) {
                    gc.setFill(Color.rgb(255, 215, 0, 0.4)); // Círculo dourado semi-transparente
                    gc.fillOval(j * cellW + cellW * 0.2, i * cellH + cellH * 0.2, cellW * 0.6, cellH * 0.6);
                }
            }
        }

        // Desenha as linhas da grelha
        gc.setStroke(Color.DARKBLUE);
        for (int i = 0; i <= linhas; i++) {
            gc.strokeLine(0, i * cellH, w, i * cellH);
        }
        for (int j = 0; j <= colunas; j++) {
            gc.strokeLine(j * cellW, 0, j * cellW, h);
        }

        // Desenha o Navio na sua posição real
        int navioLinha = manager.getLinhaNavio();
        int navioColuna = manager.getColunaNavio();

        gc.setFill(Color.DARKGRAY);
        double margin = 5;
        gc.fillRect(navioColuna * cellW + margin, navioLinha * cellH + margin, cellW - 2 * margin, cellH - 2 * margin);
        
        gc.setFill(Color.WHITE);
        gc.fillText("NAVIO", navioColuna * cellW + cellW / 4, navioLinha * cellH + cellH / 2);
    }
}
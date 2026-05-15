package pt.isec.pa.deepsea.ui.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Drone;

public class FundoMarView extends Canvas {
    private DeepSeaManager manager;

    public FundoMarView(DeepSeaManager manager) {
        this.manager = manager;
        this.widthProperty().addListener(e -> desenhar());
        this.heightProperty().addListener(e -> desenhar());
    }

    public void desenhar() {
        GraphicsContext gc = getGraphicsContext2D();
        double w = getWidth();
        double h = getHeight();

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.NAVY);
        gc.fillRect(0, 0, w, h);

        int linhas = manager.getLinhasFundo();
        int colunas = manager.getColunasFundo();
        if (linhas == 0 || colunas == 0) return;

        double cellW = w / colunas;
        double cellH = h / linhas;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (!manager.isFundoRevelado(i, j)) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(j * cellW, i * cellH, cellW, cellH);
                } else {
                    Object elem = manager.getElemFundo(i, j);
                    if (elem != null) {
                        String tipo = elem.getClass().getSimpleName();
                        if (tipo.equals("Minerio")) gc.setFill(Color.ORANGE);
                        else if (tipo.equals("Monstro")) gc.setFill(Color.RED);
                        else if (tipo.equals("Artefacto")) gc.setFill(Color.GOLD);
                        
                        gc.fillOval(j * cellW + cellW * 0.2, i * cellH + cellH * 0.2, cellW * 0.6, cellH * 0.6);
                    }
                    gc.setStroke(Color.rgb(255, 255, 255, 0.2));
                    gc.strokeRect(j * cellW, i * cellH, cellW, cellH);
                }
            }
        }

        Drone drone = manager.getDrone();
        if (drone != null) {
            int linha = drone.getLinhaPos();
            int coluna = drone.getColunaPos();
            gc.setFill(Color.YELLOW);
            gc.fillOval(coluna * cellW + cellW * 0.1, linha * cellH + cellH * 0.1, cellW * 0.8, cellH * 0.8);
        }
    }
}
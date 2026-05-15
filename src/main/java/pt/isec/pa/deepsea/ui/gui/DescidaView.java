package pt.isec.pa.deepsea.ui.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Drone;

public class DescidaView extends Canvas {
    private DeepSeaManager manager;

    public DescidaView(DeepSeaManager manager) {
        this.manager = manager;
        this.widthProperty().addListener(e -> desenhar());
        this.heightProperty().addListener(e -> desenhar());
    }

    public void desenhar() {
        GraphicsContext gc = getGraphicsContext2D();
        double w = getWidth();
        double h = getHeight();

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, w, h);

        int linhas = manager.getLinhasFosso();
        int colunas = manager.getColunasFosso();
        if (linhas == 0 || colunas == 0) return;

        double cellW = w / colunas;
        double cellH = h / linhas;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Object elem = manager.getElemFosso(i, j);
                if (elem != null) {
                    String tipo = elem.getClass().getSimpleName();
                    if (tipo.equals("Rocha")) gc.setFill(Color.DARKGRAY);
                    else if (tipo.equals("AnimalMarinho")) gc.setFill(Color.RED);
                    else if (tipo.equals("CorrenteMarinha")) gc.setFill(Color.CYAN);
                    else gc.setFill(Color.MAGENTA);
                    
                    gc.fillRect(j * cellW, i * cellH, cellW, cellH);
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
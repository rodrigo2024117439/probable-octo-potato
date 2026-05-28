package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.ui.res.ImageManager;

/**
 * Vista Canvas da superfície (estado SUPERFICIE).
 */
public class SuperficieView extends CanvasPane {
    private final DeepSeaManager manager;

    /**
     * @param manager Facade do modelo
     */
    public SuperficieView(DeepSeaManager manager) {
        this.manager = manager;

        widthProperty().addListener(e -> desenhar());
        heightProperty().addListener(e -> desenhar());

        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::desenhar));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::desenhar));

        this.setOnMouseClicked(event -> {
            int linhas = manager.getLinhasSuperficie();
            int colunas = manager.getColunasSuperficie();

            int[] celula = GridMouseHelper.celulaDoClique(
                    event.getX(), event.getY(), getWidth(), getHeight(), linhas, colunas);

            if (celula != null) {
                manager.iniciarMergulho(celula[0], celula[1]);
            }
        });
    }

    public void desenhar() {
        GraphicsContext gc = getGraphicsContext2D();
        if (gc == null) return;

        double w = getWidth();
        double h = getHeight();
        if (w <= 0 || h <= 0) return;

        gc.clearRect(0, 0, w, h);

        gc.setFill(Color.web("#0e4b75"));
        gc.fillRect(0, 0, w, h);

        int linhas = manager.getLinhasSuperficie();
        int colunas = manager.getColunasSuperficie();
        if (linhas == 0 || colunas == 0) return;

        double cellW = w / colunas;
        double cellH = h / linhas;

        Image imgMar = ImageManager.getImage("mar.png");
        Image imgArtefacto = ImageManager.getImage("artefacto.png");

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {

                if (imgMar != null && !imgMar.isError()) {
                    gc.drawImage(imgMar, j * cellW, i * cellH, cellW, cellH);
                }

                gc.setStroke(Color.web("#1b75b3"));
                gc.setLineWidth(1.0);
                gc.strokeRect(j * cellW, i * cellH, cellW, cellH);

                if (manager.contemArtefactoNaSuperficie(i, j)) {
                    if (imgArtefacto != null && !imgArtefacto.isError()) {
                        gc.drawImage(imgArtefacto, j * cellW + cellW * 0.3, i * cellH + cellH * 0.3, cellW * 0.4, cellH * 0.4);
                    } else {
                        gc.setFill(Color.GOLD);
                        gc.fillOval(j * cellW + cellW * 0.4, i * cellH + cellH * 0.4, cellW * 0.2, cellH * 0.2);
                    }
                }
            }
        }

        int navioLinha = manager.getLinhaNavio();
        int navioColuna = manager.getColunaNavio();

        Image imgNavio = ImageManager.getImage("navioSagres.png");
        if (imgNavio != null && !imgNavio.isError()) {
            gc.drawImage(imgNavio, navioColuna * cellW, navioLinha * cellH, cellW, cellH);
        } else {
            gc.setFill(Color.WHITE);
            gc.fillRoundRect(navioColuna * cellW + cellW * 0.1, navioLinha * cellH + cellH * 0.2, cellW * 0.8, cellH * 0.6, 10, 10);
            gc.setFill(Color.RED);
            gc.fillRect(navioColuna * cellW + cellW * 0.3, navioLinha * cellH + cellH * 0.1, cellW * 0.4, cellH * 0.4);
        }
    }
}
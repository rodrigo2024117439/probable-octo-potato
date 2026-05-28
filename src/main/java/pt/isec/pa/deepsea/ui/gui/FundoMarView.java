package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.ui.res.ImageManager;

/**
 * Vista Canvas do fundo do mar (estado FUNDO_MAR).
 */
public class FundoMarView extends CanvasPane {
    private DeepSeaManager manager;
    private DeepSeaBoard board;

    /**
     * @param manager Facade do modelo
     * @param board Board de referência para obter o tamanho uniforme das células
     */
    public FundoMarView(DeepSeaManager manager, DeepSeaBoard board) {
        this.manager = manager;
        this.board = board;

        widthProperty().addListener(e -> desenhar());
        heightProperty().addListener(e -> desenhar());

        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::desenhar));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::desenhar));

        this.setOnMouseClicked(event -> {
            Drone drone = manager.getDrone();
            if (drone == null) return;

            int linhas = manager.getLinhasFundo();
            int colunas = manager.getColunasFundo();

            int[] celula = GridMouseHelper.celulaDoClique(
                    event.getX(), event.getY(), getWidth(), getHeight(), linhas, colunas);
            if (celula == null) return;

            Direcoes dir = GridMouseHelper.direcaoEntreCelulas(
                    drone.getLinhaPos(), drone.getColunaPos(), celula[0], celula[1]);
            if (dir != null) {
                manager.mover(dir);
            }
        });
    }

    /** Redesenha a grelha do fundo do mar e o drone. */
    public void desenhar() {
        GraphicsContext gc = getGraphicsContext2D();
        if (gc == null) return;

        double w = getWidth();
        double h = getHeight();
        if (w <= 0 || h <= 0) return;

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.web("#03071e"));
        gc.fillRect(0, 0, w, h);

        int linhas = manager.getLinhasFundo();
        int colunas = manager.getColunasFundo();
        if (linhas == 0 || colunas == 0) return;

        // Usa o mesmo cellW/cellH da superfície para uniformizar o tamanho
        double cellW = board != null ? board.getReferenciaCellW() : w / colunas;
        double cellH = board != null ? board.getReferenciaCellH() : h / linhas;

        // Centraliza a grelha se o fundo for menor que a área disponível
        double offsetX = (w - colunas * cellW) / 2;
        double offsetY = (h - linhas * cellH) / 2;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                boolean revelado = manager.isFundoRevelado(i, j);
                double x = offsetX + j * cellW;
                double y = offsetY + i * cellH;

                if (!revelado) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x, y, cellW, cellH);
                    gc.setStroke(Color.web("#131a26"));
                    gc.setLineWidth(0.5);
                    gc.strokeRect(x, y, cellW, cellH);
                } else {
                    String tipo = manager.getElemFundo(i, j);

                    if (tipo != null) {
                        Image img = obterImagemFundo(tipo);

                        if (img != null && !img.isError() && img.getWidth() > 0) {
                            gc.drawImage(img, x, y, cellW, cellH);
                        } else {
                            desenharFormaAlternativaFundo(gc, tipo, x, y, cellW, cellH);
                        }
                    }
                }
            }
        }

        Drone drone = manager.getDrone();
        if (drone != null) {
            int linha = drone.getLinhaPos();
            int coluna = drone.getColunaPos();
            double x = offsetX + coluna * cellW;
            double y = offsetY + linha * cellH;
            Image imgDrone = ImageManager.getImage("drone.png");
            if (imgDrone != null) {
                gc.drawImage(imgDrone, x, y, cellW, cellH);
            } else {
                gc.setFill(Color.YELLOW);
                gc.fillOval(x + cellW * 0.1, y + cellH * 0.1, cellW * 0.8, cellH * 0.8);
            }
        }
    }

    private Image obterImagemFundo(String tipo) {
        if (tipo.equals("Minerio")) {
            return ImageManager.getImage("minerios.png");
        }
        if (tipo.equals("Monstro")) {
            return ImageManager.getImage("monstoMar-removebg-preview.png");
        }
        if (tipo.equals("AnimalMarinho")) {
            return ImageManager.getImage("animal.png");
        }
        if (tipo.equals("Artefacto")) {
            return ImageManager.getImage("artefacto.png");
        }
        return null;
    }

    private void desenharFormaAlternativaFundo(GraphicsContext gc, String tipo, double x, double y, double w, double h) {
        if (tipo.equals("Minerio")) {
            gc.setFill(Color.ORANGE);
        } else if (tipo.equals("Monstro")) {
            gc.setFill(Color.RED);
        } else if (tipo.equals("Artefacto")) {
            gc.setFill(Color.GOLD);
        } else {
            gc.setFill(Color.MAGENTA);
        }
        gc.fillOval(x + w * 0.2, y + h * 0.2, w * 0.6, h * 0.6);
    }
}
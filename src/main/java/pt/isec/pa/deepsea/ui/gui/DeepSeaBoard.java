package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

/**
 * StackPane que organiza as diferentes vistas do jogo consoante o estado.
 */
public class DeepSeaBoard extends StackPane {
    private DeepSeaManager manager;
    private SuperficieView superficieView;
    private DescidaView descidaView;
    private FundoMarView fundoMarView;
    private OficinaView oficinaView;
    private FimJogoView fimJogoView;
    private PuzzleView puzzleView;

    /**
     * @param manager Facade do modelo
     */
    public DeepSeaBoard(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    /**
     * @return Largura de referência para calcular o tamanho uniforme das células
     */
    public double getReferenciaCellW() {
        int colunas = manager.getColunasSuperficie();
        return colunas > 0 ? getWidth() / colunas : 0;
    }

    /**
     * @return Altura de referência para calcular o tamanho uniforme das células
     */
    public double getReferenciaCellH() {
        int linhas = manager.getLinhasSuperficie();
        return linhas > 0 ? getHeight() / linhas : 0;
    }

    private void createViews() {
        this.superficieView = new SuperficieView(manager);

        this.descidaView = new DescidaView(manager);

        this.fundoMarView = new FundoMarView(manager, this);

        this.puzzleView = new PuzzleView(manager);

        this.oficinaView = new OficinaView(manager);
        this.fimJogoView = new FimJogoView(manager);

        this.getChildren().addAll(superficieView, descidaView, fundoMarView, puzzleView, oficinaView, fimJogoView);
        this.setFocusTraversable(true);

        this.widthProperty().addListener((obs, oldVal, newVal) -> update());
        this.heightProperty().addListener((obs, oldVal, newVal) -> update());
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt -> {
            Platform.runLater(this::update);
        });
    }

    private void update() {
        DeepSeaState estado = manager.getState();
        if (estado == null) return;

        configurarCamada(superficieView, false);
        configurarCamada(descidaView, false);
        configurarCamada(fundoMarView, false);
        configurarCamada(puzzleView, false);
        configurarCamada(oficinaView, false);
        configurarCamada(fimJogoView, false);

        switch (estado) {
            case SUPERFICIE -> {
                configurarCamada(superficieView, true);
                if (superficieView != null) superficieView.desenhar();
            }
            case FOSSO -> {
                configurarCamada(descidaView, true);
                if (descidaView != null) descidaView.desenhar();
            }
            case FUNDO_MAR -> {
                configurarCamada(fundoMarView, true);
                if (fundoMarView != null) fundoMarView.desenhar();
            }
            case PUZZLE -> {
                configurarCamada(puzzleView, true);
                if (puzzleView != null) puzzleView.desenhar();
            }
            case OFICINA -> configurarCamada(oficinaView, true);
            case FIM_JOGO -> configurarCamada(fimJogoView, true);
        }
    }

    private void configurarCamada(Node node, boolean ativa) {
        node.setVisible(ativa);
        node.setMouseTransparent(!ativa);
        node.setDisable(!ativa);
        node.setFocusTraversable(ativa);
    }
}
package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.data.Direcoes;

public class DeepSeaRoot extends BorderPane {
    private DeepSeaManager manager;
    private DeepSeaBoard board;
    private DeepSeaHUD hud;

    public DeepSeaRoot(DeepSeaManager manager) {
        this.manager = manager;
        this.board = new DeepSeaBoard(manager);
        this.hud = new DeepSeaHUD(manager);
        criarLayout();
        registarListeners();
        atualizarEcra();
    }

    private void criarLayout() {
        this.setTop(new DeepSeaMenuBar(manager));
        this.setCenter(board);
        this.setBottom(hud);
    }

    private void registarListeners() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt -> {
            Platform.runLater(this::atualizarEcra);
        });

        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> manager.mover(Direcoes.CIMA);
                case DOWN -> manager.mover(Direcoes.BAIXO);
                case LEFT -> manager.mover(Direcoes.ESQUERDA);
                case RIGHT -> manager.mover(Direcoes.DIREITA);
                case SPACE -> manager.avaliarFundo();
                case R -> manager.recolherMinerio();
                case V -> manager.voltarAoNavio();
            }
        });
    }

    private void atualizarEcra() {
        DeepSeaState estado = manager.getState();
        board.atualizar(estado);
    }
}
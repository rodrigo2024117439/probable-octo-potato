package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import pt.isec.pa.deepsea.model.DeepSeaManager;

public class DeepSeaHUD extends HBox {
    private DeepSeaManager manager;
    private Label lblCombustivel;
    private Label lblEstadoDrone;

    public DeepSeaHUD(DeepSeaManager manager) {
        this.manager = manager;
        criarLayout();
        registarListeners();
        atualizar();
    }

    private void criarLayout() {
        this.setPadding(new Insets(10));
        this.setSpacing(30);
        this.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #a0a0a0; -fx-border-width: 2 0 0 0;");

        lblCombustivel = new Label();
        lblEstadoDrone = new Label();

        this.getChildren().addAll(lblCombustivel, lblEstadoDrone);
    }

    private void registarListeners() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt -> {
            Platform.runLater(this::atualizar);
        });
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt -> {
            Platform.runLater(this::atualizar);
        });
    }

    private void atualizar() {
        if (manager.getState() == null) return;
        
        lblCombustivel.setText("Combustível Navio: " + manager.getCombustivelNavio());
        
        if (manager.getDrone() != null) {
            lblEstadoDrone.setText("Combustível Drone: " + manager.getDrone().getCombustivel());
        } else {
            lblEstadoDrone.setText("Drone: Inativo");
        }
    }
}
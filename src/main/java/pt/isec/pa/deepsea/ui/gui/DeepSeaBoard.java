package pt.isec.pa.deepsea.ui.gui;

import javafx.scene.layout.StackPane;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

public class DeepSeaBoard extends StackPane {
    private DeepSeaManager manager;
    private SuperficieView superficieView;
    private DescidaView descidaView;
    private FundoMarView fundoMarView;

    public DeepSeaBoard(DeepSeaManager manager) {
        this.manager = manager;
        
        this.superficieView = new SuperficieView(manager);
        this.superficieView.widthProperty().bind(this.widthProperty());
        this.superficieView.heightProperty().bind(this.heightProperty());

        this.descidaView = new DescidaView(manager);
        this.descidaView.widthProperty().bind(this.widthProperty());
        this.descidaView.heightProperty().bind(this.heightProperty());

        this.fundoMarView = new FundoMarView(manager);
        this.fundoMarView.widthProperty().bind(this.widthProperty());
        this.fundoMarView.heightProperty().bind(this.heightProperty());

        this.getChildren().addAll(superficieView, descidaView, fundoMarView);
    }

    public void atualizar(DeepSeaState estado) {
        if (estado == null) return;

        superficieView.setVisible(false);
        descidaView.setVisible(false);
        fundoMarView.setVisible(false);

        switch (estado) {
            case SUPERFICIE -> {
                superficieView.setVisible(true);
                superficieView.desenhar();
            }
            case DESCIDA, SUBIDA -> {
                descidaView.setVisible(true);
                descidaView.desenhar();
            }
            case FUNDO_MAR, PUZZLE -> {
                fundoMarView.setVisible(true);
                fundoMarView.desenhar();
            }
            default -> {
                // Para estados como FIM_JOGO ou OFICINA, mantemos tudo invisível ou criamos vistas próprias depois.
            }
        }
    }
}
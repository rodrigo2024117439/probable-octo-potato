package pt.isec.pa.deepsea.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import java.util.ArrayList;
import java.util.List;

public class DeepSeaHUD {

    private static final String ESTILO_PAINEL =
            "-fx-background-color: #2c3e50; -fx-border-color: #1a252f;";
    private static final double LARGURA_PAINEL = 240;

    private final DeepSeaManager manager;
    private VBox painelEsquerdo;
    private VBox painelDireito;

    private Label lblEstado;
    private Label lblNavioCombustivel;
    private Label lblNavioRecursos;
    private Label lblNavioPosicao;
    private Label lblDroneAtivo;
    private final List<Label> lblsFrota = new ArrayList<>();
    private VBox boxFrota;
    private Label lblAtalhos;

    public DeepSeaHUD(DeepSeaManager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    public VBox getPainelEsquerdo() { return painelEsquerdo; }
    public VBox getPainelDireito() { return painelDireito; }

    // 1. Método PRIVATE createViews
    private void createViews() {
        // --- PAINEL ESQUERDO ---
        painelEsquerdo = new VBox(12);
        painelEsquerdo.setPadding(new Insets(16));
        painelEsquerdo.setAlignment(Pos.TOP_LEFT);
        painelEsquerdo.setPrefWidth(LARGURA_PAINEL);
        painelEsquerdo.setMinWidth(LARGURA_PAINEL);
        painelEsquerdo.setStyle(ESTILO_PAINEL + " -fx-border-width: 0 2 0 0;");

        Label tituloEsq = criarTitulo("INFORMAÇÃO");
        lblEstado = criarTexto();
        lblNavioCombustivel = criarTexto();
        lblNavioRecursos = criarTexto();
        lblNavioPosicao = criarTexto();

        painelEsquerdo.getChildren().addAll(
                tituloEsq, new Separator(), lblEstado, new Label("— Navio —"),
                lblNavioCombustivel, lblNavioRecursos, lblNavioPosicao
        );

        // --- PAINEL DIREITO ---
        painelDireito = new VBox(12);
        painelDireito.setPadding(new Insets(16));
        painelDireito.setAlignment(Pos.TOP_LEFT);
        painelDireito.setPrefWidth(LARGURA_PAINEL);
        painelDireito.setMinWidth(LARGURA_PAINEL);
        painelDireito.setStyle(ESTILO_PAINEL + " -fx-border-width: 0 0 0 2;");

        Label tituloDir = criarTitulo("DRONES");
        lblDroneAtivo = criarTexto();
        boxFrota = new VBox(6);
        lblAtalhos = criarTexto();
        lblAtalhos.setWrapText(true);
        lblAtalhos.setStyle("-fx-text-fill: #bdc3c7;");

        painelDireito.getChildren().addAll(
                tituloDir, new Separator(), lblDroneAtivo, new Label("— Frota —"),
                boxFrota, new Separator(), new Label("Atalhos"), lblAtalhos
        );
    }

    // 2. Método PRIVATE registerHandlers
    private void registerHandlers() {
        manager.addPropertyChangeListener(DeepSeaManager.PROP_DADOS, evt ->
                Platform.runLater(this::update));
        manager.addPropertyChangeListener(DeepSeaManager.PROP_ESTADO, evt ->
                Platform.runLater(this::update));
    }


    private void update() {
        DeepSeaState estado = manager.getState();
        if (estado == null) return;

        lblEstado.setText("Estado: " + estado.name());
        lblNavioCombustivel.setText("Combustível: " + manager.getCombustivelNavio());
        lblNavioRecursos.setText(String.format("Minérios: %d  |  Artefactos: %d",
                manager.getMineriosNavio(), manager.getArtefactosNavio()));
        lblNavioPosicao.setText(String.format("Posição: [%d, %d]",
                manager.getLinhaNavio(), manager.getColunaNavio()));

        Drone drone = manager.getDrone();
        if (drone != null) {
            int indice = manager.getIndiceDroneSelecionado();
            lblDroneAtivo.setText(String.format("Drone %d — Comb.: %d | Min.: %d | Casco: %d%%",
                    indice > 0 ? indice : 1, drone.getCombustivel(), drone.getMineriosTransp(), drone.getIntegridadeCasco()));
            lblDroneAtivo.setStyle("-fx-text-fill: #f1c40f;");
        } else {
            lblDroneAtivo.setText("Nenhum drone em missão");
            lblDroneAtivo.setStyle("-fx-text-fill: #95a5a6;");
        }

        atualizarFrota();

        // Agora o switch já sabe quem é o "estado"
        String textoAtalhos = "";
        switch (estado) {
            case SUPERFICIE -> textoAtalhos = """
                    Setas / Rato — Mover Navio
                    1, 2, 3 — Selecionar Drone
                    O — Entrar na Oficina
                    Clique na Grelha — Iniciar Mergulho
                    """;
            case OFICINA -> textoAtalhos = """
                    1, 2, 3 — Selecionar Drone
                    Esc / V — Sair da Oficina
                    """;
            case FOSSO -> textoAtalhos = """
                    Setas (Cima/Baixo) — Mover Drone no Fosso
                    Clique na Grelha — Mover Drone
                    """;
            case FUNDO_MAR -> textoAtalhos = """
                    Setas / Rato — Mover Drone
                    Espaço — Avaliar fundo
                    R — Recolher minério
                    V — Voltar ao navio
                    """;
            case PUZZLE -> textoAtalhos = """
                    Setas — Mover a peça
                    """;
            case FIM_JOGO -> textoAtalhos = "Fim da expedição.";
        }

        lblAtalhos.setText(textoAtalhos);
    }

    private void atualizarFrota() {
        boxFrota.getChildren().clear();
        lblsFrota.clear();
        int i = 1;
        for (String linha : manager.getResumoFrotaDrones()) {
            Label lbl = criarTexto();
            lbl.setText(i + ". " + linha);
            lblsFrota.add(lbl);
            boxFrota.getChildren().add(lbl);
            i++;
        }
        if (boxFrota.getChildren().isEmpty()) {
            Label vazio = criarTexto();
            vazio.setText("(sem drones)");
            boxFrota.getChildren().add(vazio);
        }
    }

    private Label criarTitulo(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbl.setStyle("-fx-text-fill: #ecf0f1;");
        return lbl;
    }

    private Label criarTexto() {
        Label lbl = new Label();
        lbl.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        lbl.setStyle("-fx-text-fill: #ecf0f1;");
        lbl.setWrapText(true);
        return lbl;
    }
}
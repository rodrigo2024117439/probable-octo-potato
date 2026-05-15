package pt.isec.pa.deepsea.ui.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.DeepSeaLog;

import java.io.FileWriter;
import java.io.IOException;

public class DeepSeaMenuBar extends MenuBar {
    private DeepSeaManager manager;

    public DeepSeaMenuBar(DeepSeaManager manager) {
        this.manager = manager;
        criarMenus();
    }

    private void criarMenus() {
        Menu menuJogo = new Menu("Jogo");
        
        MenuItem itemNovo = new MenuItem("Novo Jogo");
        itemNovo.setOnAction(e -> manager.iniciarJogo());
        
        MenuItem itemGravar = new MenuItem("Gravar Jogo");
        itemGravar.setOnAction(e -> manager.gravarJogo());
        
        MenuItem itemCarregar = new MenuItem("Carregar Jogo");
        itemCarregar.setOnAction(e -> manager.carregarJogo());

        MenuItem itemVerLogs = new MenuItem("Ver Logs");
        itemVerLogs.setOnAction(e -> mostrarLogs());

        MenuItem itemExportarLogs = new MenuItem("Exportar Logs");
        itemExportarLogs.setOnAction(e -> exportarLogs());

        menuJogo.getItems().addAll(itemNovo, itemGravar, itemCarregar, new SeparatorMenuItem(), itemVerLogs, itemExportarLogs);
        this.getMenus().add(menuJogo);
    }

    private void mostrarLogs() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logs do Jogo");
        alert.setHeaderText("Histórico de Operações");

        TextArea textArea = new TextArea();
        textArea.setText(String.join("\n", DeepSeaLog.getInstance().getLogs()));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 300);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    private void exportarLogs() {
        try (FileWriter writer = new FileWriter("logs_exportados.txt")) {
            for (String log : DeepSeaLog.getInstance().getLogs()) {
                writer.write(log + "\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Logs exportados para logs_exportados.txt");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Não foi possível exportar os logs.");
            alert.showAndWait();
        }
    }
}
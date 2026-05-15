package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

public class EstadoFundoMar extends DeepSeaStateAdapter {

    public EstadoFundoMar(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.FUNDO_MAR;
    }

    @Override
    public boolean mover(Direcoes direcao) {
        Drone drone = context.getDrone();
        if (drone == null) return false;

        int novaLinha = drone.getLinhaPos();
        int novaColuna = drone.getColunaPos();

        switch (direcao) {
            case CIMA -> novaLinha--;
            case BAIXO -> novaLinha++;
            case ESQUERDA -> novaColuna--;
            case DIREITA -> novaColuna++;
        }

        drone.setPosicao(novaLinha, novaColuna);
        drone.consumirCombustivel(1);

        FundoDoMar fundo = modelo.getSuperficie().getCelula(modelo.getNavio().getLinhaPos(), modelo.getNavio().getColunaPos()).getFundoM();
        fundo.revelar(novaLinha, novaColuna);

        ElemFundo elemento = fundo.getConteudo(novaLinha, novaColuna);
        if (elemento != null) {
            elemento.interage(drone);
        }

        // --- VERIFICAÇÃO DE MORTE ---
        if (drone.getIntegridadeCasco() <= 0 || drone.getCombustivel() <= 0) {
            changeState(DeepSeaState.SUPERFICIE);
            return true;
        }

        return true;
    }

    @Override
    public boolean avaliarFundo() {
        Drone drone = context.getDrone();
        if (drone == null) return false;

        FundoDoMar fundo = modelo.getSuperficie().getCelula(modelo.getNavio().getLinhaPos(), modelo.getNavio().getColunaPos()).getFundoM();
        ElemFundo elemento = fundo.getConteudo(drone.getLinhaPos(), drone.getColunaPos());

        if (elemento instanceof Artefacto) {
            changeState(DeepSeaState.PUZZLE);
            return true;
        }
        return false;
    }

    @Override
    public boolean voltarAoNavio() {
        Drone drone = context.getDrone();
        if (drone != null) {
            drone.prepararPosicaoInicial("SUBIDA");
            changeState(DeepSeaState.SUBIDA);
            return true;
        }
        return false;
    }

    @Override
    public boolean recolherMinerio() {
        Drone drone = context.getDrone();
        if (drone == null) return false;

        // Vai buscar a célula onde o drone está
        FundoDoMar fundo = modelo.getSuperficie().getCelula(modelo.getNavio().getLinhaPos(), modelo.getNavio().getColunaPos()).getFundoM();
        ElemFundo elemento = fundo.getConteudo(drone.getLinhaPos(), drone.getColunaPos());

        // Verifica se é minério e se o drone tem pelo menos 1 de combustível para gastar
        if (elemento instanceof Minerio && drone.getCombustivel() >= 1) {
            drone.consumirCombustivel(1); // Gasta combustível pela recolha
            drone.adicionarMinerios(1);
            fundo.removerConteudo(drone.getLinhaPos(), drone.getColunaPos()); // Tira do mapa

            // Verifica morte por combustível
            if (drone.getCombustivel() <= 0) {
                drone.descarregarMinerios();
                drone.descarregarArtefactos();
                changeState(DeepSeaState.SUPERFICIE);
            }
            return true;
        }
        return false;
    }
}
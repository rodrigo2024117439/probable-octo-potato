package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

public class EstadoSubida extends DeepSeaStateAdapter {

    public EstadoSubida(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.SUBIDA;
    }

    @Override
    public boolean mover(Direcoes direcao) {
        Drone drone = context.getDrone();
        if (drone == null) return false;

        if (direcao == Direcoes.CIMA) {
            int novaLinha = drone.getLinhaPos() - 1;

            drone.setPosicao(novaLinha, drone.getColunaPos());
            drone.consumirCombustivel(1);

            // 1. SUCESSO: Chegou ao topo VIVO!
            if (novaLinha <= 0 && drone.getIntegridadeCasco() > 0 && drone.getCombustivel() > 0) {
                // Passa o loot para o navio
                modelo.getNavio().adicionarMinerios(drone.getMineriosTransp());
                modelo.getNavio().recolherArtefactosDoDrone(drone);

                // Limpa os bolsos do drone
                drone.descarregarMinerios();
                drone.descarregarArtefactos();

                changeState(DeepSeaState.SUPERFICIE);
                return true;
            }

            // 2. DERROTA: Morreu na viagem de regresso!
            if (drone.getIntegridadeCasco() <= 0 || drone.getCombustivel() <= 0) {
                // A carga cai toda no fundo do mar! Limpa os bolsos e não entrega ao navio.
                drone.descarregarMinerios();
                drone.descarregarArtefactos();

                changeState(DeepSeaState.SUPERFICIE);
                return true;
            }
            return true;
        }
        return false;
    }
}
package pt.isec.pa.deepsea.model.state.states; // Confirma se o teu package é este!

import pt.isec.pa.deepsea.model.Jogo; // Substitui pelo import do teu modelo, se for diferente
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter; // O import do teu Adapter
import pt.isec.pa.deepsea.model.state.DeepSeaState; // O import do teu Enum
import pt.isec.pa.deepsea.model.state.DeepSeaContext; // O import do teu Context
import pt.isec.pa.deepsea.model.data.Drone;

public class EstadoOficina extends DeepSeaStateAdapter {

    public EstadoOficina(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.OFICINA;
    }

    @Override
    public boolean sairOficina() {
        // Quando sais da oficina, voltas obrigatoriamente para a superfície
        changeState(DeepSeaState.SUPERFICIE);
        return true;
    }

    @Override
    public boolean melhorarCasco() {
        Drone drone = context.getDrone();
        if (drone == null) return false;

        // Regra: Custa 1 minério para reparar 20 de integridade do casco
        int custo = 1;
        int ganhoCasco = 20;

        if (drone.getMinerios() >= custo) {
            drone.gastarMinerios(custo);     // Paga a conta
            // Substitui "repararCasco" pelo nome do teu método no Drone!
            drone.repararCasco(ganhoCasco);  // Recebe o produto
            return true;
        }

        return false; // Retorna falso se não tiver dinheiro suficiente
    }

    @Override
    public boolean comprarCombustivel() {
        Drone drone = context.getDrone();
        if (drone == null) return false;

        // Regra: Custa 1 minério para comprar 10 unidades de combustível
        int custo = 1;
        int ganhoCombustivel = 10;

        if (drone.getMinerios() >= custo) {
            drone.gastarMinerios(custo);              // Paga a conta
            // Substitui "adicionarCombustivel" pelo nome do teu método no Drone!
            drone.adicionarCombustivel(ganhoCombustivel); // Recebe o produto
            return true;
        }

        return false; // Retorna falso se não tiver dinheiro suficiente
    }
}

package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

public class EstadoSuperficie extends DeepSeaStateAdapter {

    public EstadoSuperficie(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.SUPERFICIE;
    }

    @Override
    public boolean entrarOficina() {
        changeState(DeepSeaState.OFICINA);
        return true;
    }

    @Override
    public boolean iniciarMergulho(int linha, int coluna) {
        if (modelo.getSuperficie().getCelula(linha, coluna) == null) {
            return false;
        }

        modelo.getNavio().setPosicao(linha, coluna);

        if (context.getDrone() != null) {
            context.getDrone().prepararPosicaoInicial("DESCIDA");
            changeState(DeepSeaState.DESCIDA);
            return true;
        }
        return false;
    }

    @Override
    public boolean evoluirDrone() {
        return true;
    }
}

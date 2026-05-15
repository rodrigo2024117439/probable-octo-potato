package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.FossoMarinho;
import pt.isec.pa.deepsea.model.data.FundoDoMar;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DeepSeaManager {
    private DeepSeaContext context;
    private PropertyChangeSupport pcs;

    public static final String PROP_ESTADO = "estado";
    public static final String PROP_DADOS = "dados";
    public static final String PROP_LOG = "log";

    public DeepSeaManager() {
        this.context = new DeepSeaContext();
        this.pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public DeepSeaState getState() {
        return context.getState();
    }

    public boolean iniciarJogo() {
        boolean result = context.iniciarJogo();
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        return result;
    }

    public boolean evoluirDrone() {
        boolean result = context.evoluirDrone();
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        pcs.firePropertyChange(PROP_DADOS, null, null);
        return result;
    }

    public boolean iniciarMergulho(int linha, int coluna) {
        boolean result = context.iniciarMergulho(linha, coluna);
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        return result;
    }

    public boolean mover(Direcoes direcao) {
        boolean result = context.mover(direcao);
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        pcs.firePropertyChange(PROP_DADOS, null, null);
        return result;
    }

    public boolean avaliarFundo() {
        boolean result = context.avaliarFundo();
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        pcs.firePropertyChange(PROP_DADOS, null, null);
        return result;
    }

    public boolean recolherMinerio() {
        boolean result = context.recolherMinerio();
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        pcs.firePropertyChange(PROP_DADOS, null, null);
        return result;
    }

    public boolean voltarAoNavio() {
        boolean result = context.voltarAoNavio();
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        return result;
    }

    public boolean jogarPuzzle(Direcoes direcao) {
        boolean result = context.jogarPuzzle(direcao);
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        pcs.firePropertyChange(PROP_DADOS, null, null);
        return result;
    }

    public boolean gravarJogo() {
        boolean result = context.gravarJogo();
        pcs.firePropertyChange(PROP_LOG, null, null);
        return result;
    }

    public boolean carregarJogo() {
        boolean result = context.carregarJogo();
        pcs.firePropertyChange(PROP_ESTADO, null, getState());
        pcs.firePropertyChange(PROP_DADOS, null, null);
        pcs.firePropertyChange(PROP_LOG, null, null);
        return result;
    }

    public Drone getDrone() {
        return context.getDrone();
    }

    public int getCombustivelNavio() {
        return context.getCombustivelNavio();
    }

    public int getLinhaNavio() {
        return context.getLinhaNavio();
    }

    public int getColunaNavio() {
        return context.getColunaNavio();
    }

    public int getMineriosNavio() {
        return context.getMineriosNavio();
    }

    public int getLinhasSuperficie() {
        return context.getModelo().getSuperficie().getLinhas();
    }

    public int getColunasSuperficie() {
        return context.getModelo().getSuperficie().getColunas();
    }

    public boolean contemArtefactoNaSuperficie(int lin, int col) {
        return context.getModelo().getSuperficie().contemArtefacto(lin, col);
    }

    public int getLinhasFosso() { return context.getModelo().getFosso().getAltura(); }
    public int getColunasFosso() { return context.getModelo().getFosso().getLargura(); }
    public Object getElemFosso(int l, int c) { return context.getModelo().getFosso().getCont(l, c); }

    public int getLinhasFundo() { return context.getModelo().getFundo().getAltura(); }
    public int getColunasFundo() { return context.getModelo().getFundo().getLargura(); }
    public Object getElemFundo(int l, int c) { return context.getModelo().getFundo().getConteudo(l, c); }
    public boolean isFundoRevelado(int l, int c) { return context.getModelo().getFundo().estaRevelada(l, c); }
}
package healthwatcher.model.complaint;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.state.FoodComplaintState;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public class FoodComplaint extends Complaint {

    private FoodComplaintState state;

    //construtor vazio
    public FoodComplaint() {
    	super();
    }
    public  FoodComplaint(String solicitante, String descricao,
                           String observacao, String email,
                           Employee atendente, int situacao,
                           Date dataParecer, Date dataQueixa,
                           Address enderecoSolicitante, int qtdeComensais,
                           int qtdeDoentes, int qtdeInternacoes,
                           int qtdeObitos, String localAtendimento,
                           String refeicaoSuspeita, Address enderecoDoente) {

        //inicializar tipo da queixa
        super(solicitante, descricao, observacao,email, atendente, situacao, dataParecer,dataQueixa, enderecoSolicitante);
    }
    public Address getEnderecoDoente() {
        return state.getEnderecoDoente();
    }
    public String getLocalAtendimento() {
        return state.getLocalAtendimento();
    }
    public int getQtdeComensais() {
    	return state.getQtdeComensais();
    }
    public int getQtdeDoentes() {
    	return state.getQtdeDoentes();
    }
    public int getQtdeInternacoes() {
    	return state.getQtdeInternacoes();
    }
    public int getQtdeObitos() {
    	return state.getQtdeObitos();
    }
    public String getRefeicaoSuspeita() {
        return state.getRefeicaoSuspeita();
    }
    public void setEnderecoDoente(Address newEnderecoDoente) {
    	state.setEnderecoDoente(newEnderecoDoente);
    }
    public void setLocalAtendimento(java.lang.String newLocalAtendimento) {
    	state.setLocalAtendimento(newLocalAtendimento);
    }
    public void setQtdeComensais(int newQtdeComensais) {
    	state.setQtdeComensais(newQtdeComensais);
    }
    public void setQtdeDoentes(int newQtdeDoentes) {
    	state.setQtdeDoentes(newQtdeDoentes);
    }
    public void setQtdeInternacoes(int newQtdeInternacoes) {
    	state.setQtdeInternacoes(newQtdeInternacoes);
    }
    public void setQtdeObitos(int newQtdeObitos) {
    	state.setQtdeObitos(newQtdeObitos);
    }
    public void setRefeicaoSuspeita(java.lang.String newRefeicaoSuspeita) {
    	state.setRefeicaoSuspeita(newRefeicaoSuspeita);
    }
    public void setStatus(int newStatus) {
    	super.setSituacao(newStatus);
		state.setStatus(newStatus, this);
	}
    public void setComplaintState(FoodComplaintState _state){
    	state= _state;
    }
}

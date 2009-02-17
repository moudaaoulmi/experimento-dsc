package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.FoodComplaint;
import healthwatcher.model.complaint.Situation;

import java.io.Serializable;


public abstract class FoodComplaintState implements Serializable {
	
	protected int      qtdeComensais;
	protected int      qtdeDoentes;
	protected int      qtdeInternacoes;
	protected int      qtdeObitos;
	protected String   localAtendimento;
	protected String   refeicaoSuspeita;
	protected Address enderecoDoente;

	public FoodComplaintState() {
	}
	
	public FoodComplaintState(int qtdeComensais,
            int qtdeDoentes, int qtdeInternacoes,
            int qtdeObitos, String localAtendimento,
            String refeicaoSuspeita, Address enderecoDoente){
		this.qtdeComensais=qtdeComensais;
        this.qtdeDoentes=qtdeDoentes; 
        this.qtdeInternacoes=qtdeInternacoes;
        this.qtdeObitos=qtdeObitos; 
        this.localAtendimento=localAtendimento;
        this.refeicaoSuspeita=refeicaoSuspeita; 
        this.enderecoDoente=enderecoDoente;
	}
	
	public abstract void setEnderecoDoente(Address newEnderecoDoente);
    public abstract void setLocalAtendimento(java.lang.String newLocalAtendimento);
    public abstract void setQtdeComensais(int newQtdeComensais);
    public abstract void setQtdeDoentes(int newQtdeDoentes);
    public abstract void setQtdeInternacoes(int newQtdeInternacoes);
    public abstract void setQtdeObitos(int newQtdeObitos);
    public abstract void setRefeicaoSuspeita(java.lang.String newRefeicaoSuspeita);
    
    public Address getEnderecoDoente() {
        return this.enderecoDoente;
    }
    public String getLocalAtendimento() {
        return this.localAtendimento;
    }
    public int getQtdeComensais() {
    	return qtdeComensais;
    }
    public int getQtdeDoentes() {
    	return qtdeDoentes;
    }
    public int getQtdeInternacoes() {
    	return qtdeInternacoes;
    }
    public int getQtdeObitos() {
    	return qtdeObitos;
    }
    public String getRefeicaoSuspeita() {
        return this.refeicaoSuspeita;
    }
    public void setStatus(int sit,FoodComplaint complaint) {
    	if(sit!=complaint.getSituacao()){
    		if(sit==Situation.QUEIXA_ABERTA){
    			complaint.setComplaintState(new FoodComplaintStateOpen(qtdeComensais,qtdeDoentes, qtdeInternacoes, qtdeObitos,localAtendimento,
                        refeicaoSuspeita,enderecoDoente));
    		}else if(sit==Situation.QUEIXA_FECHADA){
    			complaint.setComplaintState(new FoodComplaintStateClosed(qtdeComensais,qtdeDoentes, qtdeInternacoes, qtdeObitos,localAtendimento,
                        refeicaoSuspeita,enderecoDoente));
    		}else if(sit==Situation.QUEIXA_SUSPENSA){
    			
    		}
    	}
    }
}

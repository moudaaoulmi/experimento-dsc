package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.complaint.SpecialComplaint;

import java.io.Serializable;


public abstract class SpecialComplaintState implements Serializable {
	
	protected short    idade;
	protected String   instrucao;
	protected String   ocupacao;
	protected Address enderecoOcorrencia;

	public SpecialComplaintState() {
	}
	
	public SpecialComplaintState(short idade,
            String instrucao, String ocupacao,
            Address enderecoOcorrencia){
		this.idade              = idade;
        this.instrucao          = instrucao;
        this.ocupacao           = ocupacao;
        this.enderecoOcorrencia = enderecoOcorrencia;
	}
	public abstract void setEnderecoOcorrencia(Address newEnderecoOcorrencia);
	public abstract void setIdade(short newIdade);
	public abstract void setInstrucao(java.lang.String newInstrucao);
	public abstract void setOcupacao(java.lang.String newOcupacao);
	
	public Address getEnderecoOcorrencia() {
		
        return this.enderecoOcorrencia;
    }
    public short getIdade() {
        return this.idade;
    }
    public String getInstrucao() {
        return this.instrucao;
    }
    public String getOcupacao() {
        return this.ocupacao;
    }
    public void setStatus(int sit,SpecialComplaint complaint) {
    	if(sit!=complaint.getSituacao()){
    		if(sit==Situation.QUEIXA_ABERTA){
    			complaint.setComplaintState(new SpecialComplaintStateOpen(idade, instrucao, ocupacao, enderecoOcorrencia));
    		}else if(sit==Situation.QUEIXA_FECHADA){
    			complaint.setComplaintState(new SpecialComplaintStateClosed(idade, instrucao, ocupacao, enderecoOcorrencia));
    		}else if(sit==Situation.QUEIXA_SUSPENSA){
    			
    		}
    	}
    }
}

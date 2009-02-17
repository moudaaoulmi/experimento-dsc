package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.AnimalComplaint;
import healthwatcher.model.complaint.Situation;

import java.io.Serializable;

import lib.util.Date;




public abstract class AnimalComplaintState implements Serializable {
	
	protected short qtdeAnimais;
	protected Date dataIncomodo;
	protected String animal;
	protected Address enderecoLocalOcorrencia;

	public AnimalComplaintState() {
	}
	
	public AnimalComplaintState(short qtdeAnimais, Date dataIncomodo, String animal, Address endereco){
		this.qtdeAnimais= qtdeAnimais;
		this.dataIncomodo= dataIncomodo;
		this.animal= animal;
		this.enderecoLocalOcorrencia= endereco;
	}
	
	public abstract void setAnimal(String newAnimal);
	public abstract void setDataIncomodo(Date newDataIncomodo);
	public abstract void setEnderecoLocalOcorrencia(Address newEnderecoLocalOcorrencia);
	public abstract void setQtdeAnimais(short newQtdeAnimais);
	public abstract void setTipoAnimal(String newAnimal);
	
	public java.lang.String getAnimal() {
		return animal;
	}
	public Date getDataIncomodo() {
		return this.dataIncomodo;
	}
	public Address getEnderecoLocalOcorrencia() {
		return this.enderecoLocalOcorrencia;
	}
	public short getQtdeAnimais() {
		return this.qtdeAnimais;
	}
	public String getTipoAnimal() {
		return this.animal;
	}
	public void setStatus(int sit,AnimalComplaint complaint) {
    	if(sit!=complaint.getSituacao()){
    		if(sit==Situation.QUEIXA_ABERTA){
    			complaint.setComplaintState(new AnimalComplaintStateOpen(qtdeAnimais, dataIncomodo, animal, enderecoLocalOcorrencia));
    		}else if(sit==Situation.QUEIXA_FECHADA){
    			complaint.setComplaintState(new AnimalComplaintStateClosed(qtdeAnimais, dataIncomodo, animal, enderecoLocalOcorrencia));
    		}else if(sit==Situation.QUEIXA_SUSPENSA){
    			
    		}
    	}
    }

}

package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.AnimalComplaint;
import lib.util.Date;

public abstract class AnimalComplaintState {
	
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
    }
}

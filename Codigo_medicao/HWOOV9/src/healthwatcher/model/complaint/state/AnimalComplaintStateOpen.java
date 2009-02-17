package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import lib.util.Date;


public class AnimalComplaintStateOpen extends AnimalComplaintState {

	public AnimalComplaintStateOpen() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnimalComplaintStateOpen(short qtdeAnimais, Date dataIncomodo,
			String animal, Address endereco) {
		super(qtdeAnimais, dataIncomodo, animal, endereco);
		// TODO Auto-generated constructor stub
	}
	public void setAnimal(String newAnimal) {
		animal = newAnimal;
	}
	public void setDataIncomodo(Date newDataIncomodo) {
		dataIncomodo = newDataIncomodo;
	}
	public void setEnderecoLocalOcorrencia(Address newEnderecoLocalOcorrencia) {
		enderecoLocalOcorrencia = newEnderecoLocalOcorrencia;
	}
	public void setQtdeAnimais(short newQtdeAnimais) {
		qtdeAnimais = newQtdeAnimais;
	}
	public void setTipoAnimal(String newAnimal) {
		animal = newAnimal;
	}
}

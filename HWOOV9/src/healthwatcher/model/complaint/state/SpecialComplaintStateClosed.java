package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;



public class SpecialComplaintStateClosed extends SpecialComplaintState {

	public SpecialComplaintStateClosed() {
		super();
	}

	public SpecialComplaintStateClosed(short idade, String instrucao,
			String ocupacao, Address enderecoOcorrencia) {
		super(idade, instrucao, ocupacao, enderecoOcorrencia);
	}
	public void setEnderecoOcorrencia(Address newEnderecoOcorrencia) {
	}
	public void setIdade(short newIdade) {
	}
	public void setInstrucao(java.lang.String newInstrucao) {
	}
	public void setOcupacao(java.lang.String newOcupacao) {
	}
}

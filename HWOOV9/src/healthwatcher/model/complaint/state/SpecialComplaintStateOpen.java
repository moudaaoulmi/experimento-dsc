package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;



public class SpecialComplaintStateOpen extends SpecialComplaintState {

	public SpecialComplaintStateOpen() {
		super();
	}
	public SpecialComplaintStateOpen(short idade, String instrucao,
			String ocupacao, Address enderecoOcorrencia) {
		super(idade, instrucao, ocupacao, enderecoOcorrencia);
	}
	public void setEnderecoOcorrencia(Address newEnderecoOcorrencia) {
		enderecoOcorrencia = newEnderecoOcorrencia;
	}
	public void setIdade(short newIdade) {
		idade = newIdade;
	}
	public void setInstrucao(java.lang.String newInstrucao) {
		instrucao = newInstrucao;
	}
	public void setOcupacao(java.lang.String newOcupacao) {
		ocupacao = newOcupacao;
	}

}

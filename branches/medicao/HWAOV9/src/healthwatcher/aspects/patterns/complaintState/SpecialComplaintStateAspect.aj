package healthwatcher.aspects.patterns.complaintState;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.complaint.SpecialComplaint;
import healthwatcher.model.complaint.state.SpecialComplaintState;
import healthwatcher.model.complaint.state.SpecialComplaintStateClosed;
import healthwatcher.model.complaint.state.SpecialComplaintStateOpen;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public aspect SpecialComplaintStateAspect {
	
	after(SpecialComplaint specialComplaint): initialization(SpecialComplaint+.new()) && target(specialComplaint) {
		specialComplaint.setComplaintState( new SpecialComplaintStateOpen() );
	}
	
	after(SpecialComplaint specialComplaint,String solicitante, String descricao,
            String observacao, String email, Employee atendente,  int situacao,
            Date dataParecer, Date dataQueixa,  Address enderecoSolicitante, short idade,
            String instrucao, String ocupacao,  Address enderecoOcorrencia) :
            	initialization(SpecialComplaint+.new(..)) && target(specialComplaint) && 
            	args(solicitante, descricao, observacao, email, atendente,
            			situacao, dataParecer, dataQueixa, enderecoSolicitante, idade, instrucao, ocupacao,  enderecoOcorrencia) {
		if (specialComplaint.getSituacao() == Situation.QUEIXA_ABERTA)
			specialComplaint.setComplaintState( new SpecialComplaintStateOpen(idade, instrucao, ocupacao,  enderecoOcorrencia) ) ;
		else if (specialComplaint.getSituacao() == Situation.QUEIXA_FECHADA)
			specialComplaint.setComplaintState( new SpecialComplaintStateClosed(idade, instrucao, ocupacao,  enderecoOcorrencia) );
	}

	after(int status, SpecialComplaint specialComplaint, SpecialComplaintState state) : execution(void SpecialComplaintState+.setStatus(int, SpecialComplaint)) &&
	   args(status, specialComplaint) && target(state) {
    	if( status != specialComplaint.getSituacao()){
    		if( status ==Situation.QUEIXA_ABERTA){
    			specialComplaint.setComplaintState(new SpecialComplaintStateOpen(state.getIdade(), state.getInstrucao(), state.getOcupacao(),  state.getEnderecoOcorrencia()));
    		}else if( status ==Situation.QUEIXA_FECHADA){
    			specialComplaint.setComplaintState(new SpecialComplaintStateClosed(state.getIdade(), state.getInstrucao(), state.getOcupacao(), state.getEnderecoOcorrencia()));
    		}else if(status==Situation.QUEIXA_SUSPENSA){
    			
    		}
    	}

	}

	
}

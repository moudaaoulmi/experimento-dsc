package healthwatcher.aspects.patterns.complaintState;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.AnimalComplaint;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.complaint.state.AnimalComplaintState;
import healthwatcher.model.complaint.state.AnimalComplaintStateClosed;
import healthwatcher.model.complaint.state.AnimalComplaintStateOpen;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public aspect AnimalComplaintStateAspect {
	
	after(AnimalComplaint animalComplaint): initialization(AnimalComplaint+.new()) && target(animalComplaint) {
		animalComplaint.setComplaintState( new AnimalComplaintStateOpen() );
	}
	
	after(AnimalComplaint animalComplaint, String solicitante, String descricao,
			String observacao, String email, Employee atendente,
			int situacao, Date dataParecer,	Date dataQueixa, Address enderecoSolicitante, short qtdeAnimais,
			Date dataIncomodo, String animal, Address endereco) : 
				initialization(AnimalComplaint+.new(..)) && target(animalComplaint) && 
				args(solicitante, descricao, observacao, email, atendente,
            			situacao, dataParecer, dataQueixa, enderecoSolicitante, qtdeAnimais, dataIncomodo, animal, endereco) {
		if (animalComplaint.getSituacao() == Situation.QUEIXA_ABERTA)
			animalComplaint.setComplaintState( new AnimalComplaintStateOpen(qtdeAnimais, dataIncomodo,
					animal, endereco) ) ;
		else if (animalComplaint.getSituacao() == Situation.QUEIXA_FECHADA)
			animalComplaint.setComplaintState( new AnimalComplaintStateClosed(qtdeAnimais, dataIncomodo,
					animal, endereco) );
	}

	after(int status, AnimalComplaint animalComplaint, AnimalComplaintState state) : 
		execution(void AnimalComplaintState+.setStatus(int, AnimalComplaint)) &&
		args(status, animalComplaint) && target(state) {
		
    	if( status != animalComplaint.getSituacao()){
    		if( status ==Situation.QUEIXA_ABERTA){
    			animalComplaint.setComplaintState(new AnimalComplaintStateOpen(state.getQtdeAnimais(), state.getDataIncomodo(), state.getAnimal(), state.getEnderecoLocalOcorrencia()));
    		}else if( status ==Situation.QUEIXA_FECHADA){
    			animalComplaint.setComplaintState(new AnimalComplaintStateClosed(state.getQtdeAnimais(), state.getDataIncomodo(), state.getAnimal(), state.getEnderecoLocalOcorrencia()));
    		}else if(status==Situation.QUEIXA_SUSPENSA){
    			
    		}
    	}

	}

	
}

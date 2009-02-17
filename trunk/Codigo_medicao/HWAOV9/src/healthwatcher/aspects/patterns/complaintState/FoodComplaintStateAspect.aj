package healthwatcher.aspects.patterns.complaintState;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.FoodComplaint;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.complaint.state.FoodComplaintState;
import healthwatcher.model.complaint.state.FoodComplaintStateClosed;
import healthwatcher.model.complaint.state.FoodComplaintStateOpen;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public aspect FoodComplaintStateAspect {
	
	after(FoodComplaint foodComplaint): initialization(FoodComplaint+.new()) && target(foodComplaint) {
		foodComplaint.setComplaintState( new FoodComplaintStateOpen() );
	}
	
	after(FoodComplaint foodComplaint, String solicitante, String descricao, String observacao, String email,
            Employee atendente, int situacao, Date dataParecer, Date dataQueixa,
            Address enderecoSolicitante, int qtdeComensais, int qtdeDoentes, int qtdeInternacoes,
            int qtdeObitos, String localAtendimento,
            String refeicaoSuspeita, Address enderecoDoente) : 
            	initialization(FoodComplaint+.new(..)) && target(foodComplaint) && 
            	args(solicitante, descricao, observacao, email, atendente,
            			situacao, dataParecer, dataQueixa, enderecoSolicitante, qtdeComensais, qtdeDoentes, qtdeInternacoes,
                        qtdeObitos, localAtendimento, refeicaoSuspeita, enderecoDoente) {
		if (foodComplaint.getSituacao() == Situation.QUEIXA_ABERTA)
			foodComplaint.setComplaintState( new FoodComplaintStateOpen(qtdeComensais, qtdeDoentes, qtdeInternacoes,
                    qtdeObitos, localAtendimento, refeicaoSuspeita, enderecoDoente) ) ;
		else if (foodComplaint.getSituacao() == Situation.QUEIXA_FECHADA)
			foodComplaint.setComplaintState( new FoodComplaintStateClosed(qtdeComensais, qtdeDoentes, qtdeInternacoes,
                    qtdeObitos, localAtendimento, refeicaoSuspeita, enderecoDoente) );
	}

	after(int status, FoodComplaint foodComplaint, FoodComplaintState state) : execution(void FoodComplaintState+.setStatus(int, FoodComplaint)) &&
	   args(status, foodComplaint) && target(state) {
    	if( status != foodComplaint.getSituacao()){
    		if( status ==Situation.QUEIXA_ABERTA){
    			foodComplaint.setComplaintState(new FoodComplaintStateOpen(state.getQtdeComensais(), state.getQtdeDoentes(), state.getQtdeInternacoes(),
    					state.getQtdeObitos(), state.getLocalAtendimento(), state.getRefeicaoSuspeita(), state.getEnderecoDoente()));
    		}else if( status ==Situation.QUEIXA_FECHADA){
    			foodComplaint.setComplaintState(new FoodComplaintStateClosed(state.getQtdeComensais(), state.getQtdeDoentes(), state.getQtdeInternacoes(),
    					state.getQtdeObitos(), state.getLocalAtendimento(), state.getRefeicaoSuspeita(), state.getEnderecoDoente()));
    		}else if(status==Situation.QUEIXA_SUSPENSA){
    			
    		}
    	}

	}

	
}

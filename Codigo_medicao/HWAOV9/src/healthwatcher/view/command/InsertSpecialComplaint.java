package healthwatcher.view.command;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.SpecialComplaint;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotValidException;
import lib.patterns.CommandReceiver;
import lib.util.Date;
import lib.util.HTMLCode;

public class InsertSpecialComplaint extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			// Queixa Normal
			String descricaoQueixa = request.getInput("descricaoQueixa");
			String observacaoQueixa = request.getInput("observacaoQueixa");
			String nomeSolicitante = request.getInput("nomeSolicitante");
			String ruaSolicitante = request.getInput("ruaSolicitante");
			String compSolicitante = request.getInput("compSolicitante");
			String bairroSolicitante = request
					.getInput("bairroSolicitante");
			String cidadeSolicitante = request
					.getInput("cidadeSolicitante");
			String ufSolicitante = request.getInput("ufSolicitante");
			String cepSolicitante = request.getInput("cepSolicitante ");
			String telefoneSolicitante = request
					.getInput("telefoneSolicitante");
			Address endSolicitante = new Address(ruaSolicitante,
					compSolicitante, cepSolicitante, ufSolicitante,
					telefoneSolicitante, cidadeSolicitante, bairroSolicitante);
			String emailSolicitante = request.getInput("emailSolicitante");

			// Queixa Diversa
			short idade = Short.parseShort(request.getInput("idade"));
			String instrucao = request.getInput("instrucao");
			String ocupacao = request.getInput("ocupacao");
			String ruaOcorrencia = request.getInput("ruaOcorrencia");
			String compOcorrencia = request.getInput("compOcorrencia");
			String bairroOcorrencia = request.getInput("bairroOcorrencia");
			String cidadeOcorrencia = request.getInput("cidadeOcorrencia");
			String ufOcorrencia = request.getInput("ufOcorrencia");
			String cepOcorrencia = request.getInput("cepOcorrencia ");
			String telefoneOcorrencia = request
					.getInput("telefoneOcorrencia");
			Address endOcorrencia = new Address(ruaOcorrencia, compOcorrencia,
					cepOcorrencia, ufOcorrencia, telefoneOcorrencia,
					cidadeOcorrencia, bairroOcorrencia);
			Calendar agora = Calendar.getInstance();

			Complaint queixa = new SpecialComplaint(nomeSolicitante,
					descricaoQueixa, observacaoQueixa, emailSolicitante, null,
					1, null, new Date(agora.get(Calendar.DAY_OF_MONTH), agora
							.get(Calendar.MONTH), agora.get(Calendar.YEAR)),
					endSolicitante, idade, instrucao, ocupacao, endOcorrencia);

			int codigo = ((IFacade) receiver).insertComplaint(queixa);

			out.println(HTMLCode.htmlPage("Complaint inserted",
					"<p> <h2> Special Complaint inserted</h2> </p>"
							+ "<p> <h2> Save the complaint number: " + codigo
							+ "</h2> </p>"));

		} catch (ObjectAlreadyInsertedException e) {
			out.println(HTMLCode.errorPage("Esta queixa jah existe no BD"));
			e.printStackTrace(out);
		} catch (ObjectNotValidException e) {
			out.println(HTMLCode.errorPage("Erro ao inserir esta queixa"));
			e.printStackTrace(out);
		} catch (Exception e) {
			out.println(lib.util.HTMLCode
					.errorPage("Comunitation error, please try again later."));
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}
}
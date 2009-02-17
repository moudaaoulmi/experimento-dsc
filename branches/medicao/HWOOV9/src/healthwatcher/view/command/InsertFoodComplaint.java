package healthwatcher.view.command;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.FoodComplaint;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import lib.exceptions.CommunicationException;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.exceptions.TransactionException;
import lib.util.Date;
import lib.util.HTMLCode;

public class InsertFoodComplaint extends Command {

	public InsertFoodComplaint(IFacade f) {
		super(f);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
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

			// Queixa Alimentar
			// String nomeVitima = request.getParameter("nomeVitima");
			String ruaVitima = request.getInput("ruaVitima");
			String compVitima = request.getInput("compVitima");
			String bairroVitima = request.getInput("bairroVitima");
			String cidadeVitima = request.getInput("cidadeVitima");
			String ufVitima = request.getInput("ufVitima");
			String cepVitima = request.getInput("cepVitima ");
			String telefoneVitima = request.getInput("telefoneVitima");
			Address endVitima = new Address(ruaVitima, compVitima, cepVitima,
					ufVitima, telefoneVitima, cidadeVitima, bairroVitima);

			short qtdeComensais = Short.parseShort(request
					.getInput("qtdeComensais"));
			short qtdeDoentes = Short.parseShort(request
					.getInput("qtdeDoentes"));
			short qtdeInternacoes = Short.parseShort(request
					.getInput("qtdeInternacoes"));
			short qtdeObitos = Short.parseShort(request
					.getInput("qtdeObitos"));

			String localAtendimento = request.getInput("localAtendimento");
			String refeicaoSuspeita = request.getInput("refeicaoSuspeita");
			Calendar agora = Calendar.getInstance();

			Complaint queixa = new FoodComplaint(nomeSolicitante,
					descricaoQueixa, observacaoQueixa, emailSolicitante, null,
					1, null, new Date(agora.get(Calendar.DAY_OF_MONTH), agora
							.get(Calendar.MONTH), agora.get(Calendar.YEAR)),
					endSolicitante, qtdeComensais, qtdeDoentes,
					qtdeInternacoes, qtdeObitos, localAtendimento,
					refeicaoSuspeita, endVitima);

			int codigo = facade.insertComplaint(queixa);

			out.println(HTMLCode.htmlPage("Complaint inserted",
					"<p> <h2> Food Complaint inserted</h2> </p>"
							+ "<p> <h2> Save the complaint number: " + codigo
							+ "</h2> </p>"));

		} catch (RepositoryException e) {
			out.println(HTMLCode.errorPage("Problemas com o banco de dados"));
			e.printStackTrace(out);
		} catch (TransactionException e) {
			out
					.println(HTMLCode
							.errorPage("Erro no mecanismo de persist?ncia"));
			e.printStackTrace(out);
		} catch (ObjectAlreadyInsertedException e) {
			out.println(HTMLCode.errorPage("Esta queixa jah existe no BD"));
			e.printStackTrace(out);
		} catch (ObjectNotValidException e) {
			out.println(HTMLCode.errorPage("Erro ao inserir esta queixa"));
			e.printStackTrace(out);
		} catch (CommunicationException e) {
			out.println(HTMLCode.errorPage("Erro ao inserir esta queixa"));
			e.printStackTrace(out);
		} catch (Exception e) {
			e.printStackTrace();
			out.println(lib.util.HTMLCode
					.errorPage("Comunitation error, please try again later."));
			e.printStackTrace(out);
		} finally {
			out.println(HTMLCode.close());
			out.close();
		}
	}
}
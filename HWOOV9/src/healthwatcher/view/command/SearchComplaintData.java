package healthwatcher.view.command;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.AnimalComplaint;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.FoodComplaint;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.complaint.SpecialComplaint;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.CommunicationException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.RepositoryException;
import lib.exceptions.TransactionException;
import lib.util.HTMLCode;

public class SearchComplaintData extends Command {

	public SearchComplaintData(IFacade f) {
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

		int codQueixa = Integer.parseInt(request.getInput("codQueixa"));

		try {
			Complaint q = facade.searchComplaint(codQueixa);

			out.println(HTMLCode.open("Queries - Complaints"));

			out.println("<body><h1>Search response<br>Complaint: " + codQueixa
					+ "</h1>");

			out.println("<P>Complaint code: " + q.getCodigo() + "</P>");

			String t = null;

			if (q instanceof SpecialComplaint) {
				t = "Special";
			} else if (q instanceof FoodComplaint) {
				t = "Food";
			} else if (q instanceof AnimalComplaint) {
				t = "Animal";
			}

			out.println("<P>Complaint kind: " + t + "</P>");
			out.println("<P>Complainer: " + q.getSolicitante() + "</P>");
			out.println("<P>E-mail: " + q.getEmail() + "</P>");
			out.println("<P>Complaint's description: " + q.getDescricao()
					+ "</P>");
			out.println("<P>Observation: " + q.getObservacao() + "</P>");

			if (q.getDataQueixa() != null) {
				out.println("<P>Date: "
						+ lib.util.Date.format(q.getDataQueixa(),
								lib.util.Date.FORMATO1) + "</P>");
			}

			String sit;
			if (q.getSituacao() == Situation.QUEIXA_ABERTA) {
				sit = "Open";
			} else if (q.getSituacao() == Situation.QUEIXA_FECHADA) {
				sit = "Closed";
			} else {
				sit = "Suspended";
			}

			out.println("<P>Status: " + sit + "</P>");

			if (q.getSituacao() == Situation.QUEIXA_FECHADA) {
				if (q.getDataParecer() != null) {
					out.println("<P>Observation Date: "
							+ lib.util.Date.format(q.getDataParecer(),
									lib.util.Date.FORMATO1) + "</P>");
				}
			}

			Address end = q.getEnderecoSolicitante();

			if (end != null) {
				out.println("<P>Complainer's address: " + end.getStreet() + ","
						+ end.getComplement() + " Province: "
						+ end.getNeighbourhood() + " </P>");
				out.println("<P>ZIP code: " + end.getZip() + " City: "
						+ end.getCity() + " State: " + end.getState() + "</P>");
				out.println("<P> Phone number: " + end.getPhone() + "</P>");
			}

			if (q instanceof FoodComplaint) {
				out.println("<P>Amount of people that ate the meal: "
						+ ((FoodComplaint) q).getQtdeComensais() + "</P>");
				out.println("<P>Amount of sick people: "
						+ ((FoodComplaint) q).getQtdeDoentes() + "</P>");
				out.println("<P>Amount of people checked into a hospital: "
						+ ((FoodComplaint) q).getQtdeInternacoes() + "</P>");
				out.println("<P>Amount of deaths: "
						+ ((FoodComplaint) q).getQtdeObitos() + "</P>");
				out.println("<P>Place of medical care: "
						+ ((FoodComplaint) q).getLocalAtendimento() + "</P>");
				out.println("<P>Suspicious meal: "
						+ ((FoodComplaint) q).getRefeicaoSuspeita() + "</P>");

				end = ((FoodComplaint) q).getEnderecoDoente();

				if (end != null) {
					out.println("<P>Person sick's address: " + end.getStreet()
							+ "," + end.getComplement() + " Province: "
							+ end.getNeighbourhood() + " </P>");
					out.println("<P>ZIP code: " + end.getZip() + " City: "
							+ end.getCity() + " State: " + end.getState()
							+ "</P>");
					out.println("<P> Phone number: " + end.getPhone() + "</P>");
				}
			}

			if (q instanceof AnimalComplaint) {
				out.println("<P>Animal: " + ((AnimalComplaint) q).getAnimal()
						+ "</P>");
				out.println("<P>Amount of animals: "
						+ ((AnimalComplaint) q).getAnimalQuantity() + "</P>");

				if (((AnimalComplaint) q).getInconvenienceDate() != null) {
					out.println("<P>Date: "
							+ lib.util.Date.format(((AnimalComplaint) q)
									.getInconvenienceDate(),
									lib.util.Date.FORMATO1) + "</P>");
				}

				end = ((AnimalComplaint) q).getOccurenceLocalAddress();

				if (end != null) {
					out.println("<P>Person sick's address: " + end.getStreet()
							+ "," + end.getComplement() + " Province: "
							+ end.getNeighbourhood() + " </P>");
					out.println("<P>ZIP code: " + end.getZip() + " City: "
							+ end.getCity() + " State: " + end.getState()
							+ "</P>");
					out.println("<P> Phone number: " + end.getPhone() + "</P>");
				}
			}

			if (q instanceof SpecialComplaint) {
				out.println("<P>Years old: "
						+ ((SpecialComplaint) q).getIdade() + "</P>");
				out.println("<P>School level: "
						+ ((SpecialComplaint) q).getInstrucao() + "</P>");
				out.println("<P>Ocuppation: "
						+ ((SpecialComplaint) q).getOcupacao() + "</P>");

				end = ((SpecialComplaint) q).getEnderecoOcorrencia();

				if (end != null) {
					out.println("<P>Person sick's address: " + end.getStreet()
							+ "," + end.getComplement() + " Province: "
							+ end.getNeighbourhood() + " </P>");
					out.println("<P>ZIP code: " + end.getZip() + " City: "
							+ end.getCity() + " State: " + end.getState()
							+ "</P>");
					out.println("<P> Phone number: " + end.getPhone() + "</P>");
				}
			}
			out.println(HTMLCode.closeQueries());

		} catch (ObjectNotFoundException e) {
			out.println(lib.util.HTMLCode.errorPageQueries("Complaint "
					+ codQueixa + " not found"));
			// e.printStackTrace(out);
		} catch (RepositoryException e) {
			out.println(lib.util.HTMLCode.errorPageQueries(e.getMessage()));
			e.printStackTrace(out);
		} catch (TransactionException e) {
			out.println(lib.util.HTMLCode.errorPageQueries(e.getMessage()));
			e.printStackTrace(out);
		} catch (CommunicationException e) {
			out.println(lib.util.HTMLCode.errorPageQueries(e.getMessage()));
			e.printStackTrace(out);
		} catch (Exception e) {
			out.println(lib.util.HTMLCode.errorPageQueries(e.getMessage()));
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}
}
package healthwatcher.model.complaint;

import java.util.ArrayList;
import java.util.List;

public class DiseaseType implements java.io.Serializable {

	private int code;

	private String name;

	private String description;

	private String manifestation;

	private String duration;

	private List symptoms;

	public DiseaseType() {
		symptoms = new ArrayList();
	}

	public DiseaseType(String name, String description, String manifestation, String duration,
			List symptoms) {

		this.name = name;
		this.description = description;
		this.manifestation = manifestation;
		this.duration = duration;
		this.symptoms = symptoms;
	}

	public void delete() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int codigo) {
		this.code = codigo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descricao) {
		this.description = descricao;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duracao) {
		this.duration = duracao;
	}

	public String getManifestation() {
		return manifestation;
	}

	public void setManifestation(String manifestacao) {
		this.manifestation = manifestacao;
	}

	public String getName() {
		return name;
	}

	public void setName(String nome) {
		this.name = nome;
	}

	public List getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(List sintomas) {
		this.symptoms = sintomas;
	}

}
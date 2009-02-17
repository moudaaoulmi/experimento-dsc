package healthwatcher.model.complaint;

public class Symptom {

	private int code;

	private String description;

	public Symptom(String descricao) {
		this.description = descricao;
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setCode(int codigo) {
		this.code = codigo;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
package healthwatcher.model.healthguide;

public class MedicalSpeciality {

	private int codigo;

	private String descricao;

	public MedicalSpeciality(String descricao) {
		this.descricao = descricao;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setCodigo(int cod) {
		this.codigo = cod;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String toString() {
		return descricao;
	}
}

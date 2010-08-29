import servicos.ServicosCliente;
import servicos.ServicosUsuario;



public class FachadaServicos {
	
	
	private ServicosUsuario servUsurio;
	private ServicosCliente servCliente;
	
	
	public FachadaServicos() {
		this.servCliente = new ServicosCliente();
		this.servUsurio = new ServicosUsuario();
	}
	
	
	public String mensagemCliente(){
		return this.servCliente.mensagem();
	}
	
	public String mensagemUsuario(){
		return this.servUsurio.mensagem();
	}
	

}

package services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


import exception.ClienteJaexisteException;

import util.UtilData;
import vo.*;
import arquitetura.BancoDeDados;




class ServicosCliente {
	
	
	private BancoDeDados banco;
	
	
	
	public ClienteVO addCliente(ClienteVO item){
		
		String sql = "insert into cliente (nome,tipoPessoa,sexo ,dataNascimento ,dataCadastro ,endereco,bairro ,cidade ,UF ,cep ,cpf_cnpj ,insc_estadual,fone ,contato ,email ,url ,obs,limCredito) values " +
				"'"+item.nome+"','"+item.tipoPessoa+"','"+item.sexo+"','"+UtilData.formatar(item.dataNascimento)+"','"+UtilData.formatar(item.dataCadastro)+"','"+item.endereco+"','"+item.bairro+"','"+item.cidade+"'," +
						"'"+item.UF+"','"+item.cep+"','"+item.cpf_cnpj+"','"+item.insc_estadual+"','"+item.fone+"','"+item.contato+"',"
		 +item.email+"','"+item.url+"','"+item.obs+"','"+item.limCredito+"')";
		
		banco.executarNoQuery(sql);
       //como pegar o ultimo codigo
		
		return item;
		
	}
	
	public boolean removerCliente(ClienteVO item){
		
		String sql = "delete from cliente  where codigo = "+item.codigo;
		
		if(banco.executarNoQuery(sql)==0){
			return false;
		}
		return true;
	}
	
	public Map<Integer, ClienteVO>  pesquisarCliente(String texto,String $coluna){
		String sql = "select *, date_format(datacadastro, '%m/%d/%Y') as dCadastro, date_format(datanascimento, '%m/%d/%Y') as dNascimento from cliente where $coluna like '%$texto%'";
	ResultSet rs =  banco.executar(sql);
	Map<Integer,ClienteVO> clientes = null;
	int i=0;
	try{
	while(rs.next()){
		
		ClienteVO dados_item = new ClienteVO();
		dados_item.codigo = rs.getInt("codigo");
		dados_item.nome = rs.getString("nome");
		dados_item.bairro = rs.getString("bairoo");
		dados_item.cep = rs.getString("cep");
		dados_item.cidade = rs.getString("cidade");
		dados_item.contato = rs.getString("contato");
		dados_item.cpf_cnpj = rs.getString("cpf_cnpj");
		dados_item.dataCadastro = rs.getDate("dataCadastro");
		dados_item.dataNascimento = rs.getDate("dataNascimento");
		dados_item.email = rs.getString("email");
		dados_item.endereco = rs.getString("endereco");
		dados_item.fone = rs.getString("fone");
		dados_item.insc_estadual = rs.getString("insc_estadual");
		dados_item.obs = rs.getString("obs");
		dados_item.sexo = rs.getInt("sexo");
		dados_item.tipoPessoa = rs.getInt("tipoPessoa");
		dados_item.UF = rs.getString("UF");
		dados_item.url = rs.getString("URL");
		dados_item.limCredito = rs.getDouble("limCredito");
		clientes.put(i, dados_item);
	i++;
	}
	}catch (SQLException e) {
		System.out.println("erro");
	}
		return clientes;
	}
	
	public  Map<Integer, ClienteVO> getClientes(){
		
		String sql = "select *,date_format(datacadastro, '%m/%d/%Y') as dCadastro, date_format(datanascimento, '%m/%d/%Y') as dNascimento from cliente";
		Map<Integer,ClienteVO> clientes = null;
		try{
		ResultSet rs =  banco.executar(sql);
		
		int i=0;
		while(rs.next()){
			
			ClienteVO dados_item = new ClienteVO();
			dados_item.codigo = rs.getInt("codigo");
			dados_item.nome = rs.getString("nome");
			dados_item.bairro = rs.getString("bairoo");
			dados_item.cep = rs.getString("cep");
			dados_item.cidade = rs.getString("cidade");
			dados_item.contato = rs.getString("contato");
			dados_item.cpf_cnpj = rs.getString("cpf_cnpj");
			dados_item.dataCadastro = rs.getDate("dataCadastro");
			dados_item.dataNascimento = rs.getDate("dataNascimento");
			dados_item.email = rs.getString("email");
			dados_item.endereco = rs.getString("endereco");
			dados_item.fone = rs.getString("fone");
			dados_item.insc_estadual = rs.getString("insc_estadual");
			dados_item.obs = rs.getString("obs");
			dados_item.sexo = rs.getInt("sexo");
			dados_item.tipoPessoa = rs.getInt("tipoPessoa");
			dados_item.UF = rs.getString("UF");
			dados_item.url = rs.getString("URL");
			dados_item.limCredito = rs.getDouble("limCredito");
			clientes.put(i, dados_item);
		i++;
		}
		
			
		}catch (SQLException e) {
		 System.out.println("erro");
		}
		return clientes;
		}
		

	public ClienteVO  atualizarCliente(ClienteVO cliente) {
		int i=0;
		String sql = "UPDATE cliente SET nome = '"+cliente.nome+"', tipoPessoa = "+cliente.tipoPessoa+", sexo = "+cliente.sexo+", dataNascimento = '"+cliente.dataNascimento+"',dataCadastro = '"+cliente.dataCadastro+"',endereco='"+cliente.endereco+"',bairro='"+cliente.bairro+"',cidade='"+cliente.cidade+"',UF='"+cliente.UF+"',cep='"+cliente.cep+"',"+
		"cpf_cnpj='"+cliente.cpf_cnpj+"',insc_estadual='"+cliente.insc_estadual+"',fone='"+cliente.fone+"',contato='"+cliente.contato+"',email='"+cliente.email+"',url='"+cliente.url+"',obs='"+cliente.obs+"' where codigo="+cliente.codigo+"";
			
		i = this.banco.executarNoQuery(sql);
		 if (i == 0) {
	           	try {
					throw new ClienteJaexisteException();
				} catch (ClienteJaexisteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           }
	           this.banco.close();
		return cliente;	
		
	}
}


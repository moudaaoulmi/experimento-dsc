package services;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import exception.FormadePagtoJaExistenteException;


import vo.FormadePagtoVO;
import arquitetura.BancoDeDados;





class ServicosFormadePagto {
	
	
	private BancoDeDados banco;
	
	public ServicosFormadePagto() {
		
	}
	
	public FormadePagtoVO addFormadePagto(FormadePagtoVO formadepgto){
		
		String sql = "insert into formadepgto (descricao) values ('{formadepgto.descricao}')";
		
		if(banco.executarNoQuery(sql)==0){
			try {
				throw new FormadePagtoJaExistenteException();
			} catch (FormadePagtoJaExistenteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return formadepgto;
		
	}
	
	public boolean removerFormadePagto(FormadePagtoVO formadepgto){
		
		String sql = "delete from formadepgto  where codigo = {formadepgto.codigo}";
		
		if(banco.executarNoQuery(sql)==0){
			return false;
		}
		return true;
	}
	
	public Map<Integer, FormadePagtoVO> pesquisarFormadePagto(String texto,String coluna){
		String sql = "select * from formadepgto where coluna like '%texto%'";
		Map<Integer,FormadePagtoVO> clientes = null;
		try{
			ResultSet rs =  banco.executar(sql);
			
			int i=0;
			while(rs.next()){
				
				FormadePagtoVO dados_item = new FormadePagtoVO();
				dados_item.codigo = rs.getInt("codigo");
				dados_item.descricao = rs.getString("descricao");
				
				clientes.put(i, dados_item);
			i++;
			}
			
				
			}catch (SQLException e) {
			 System.out.println("erro");
			}
			return clientes;
			}

	
	
	public  Map<Integer, FormadePagtoVO> getFormadePagtos(){
		String sql = "select * from formadepgto";
		Map<Integer,FormadePagtoVO> clientes = null;
		try{
			ResultSet rs =  banco.executar(sql);
			
			int i=0;
			while(rs.next()){
				
				FormadePagtoVO dados_item = new FormadePagtoVO();
				dados_item.codigo = rs.getInt("codigo");
				dados_item.descricao = rs.getString("descricao");
				
				clientes.put(i, dados_item);
			i++;
			}
			
				
			}catch (SQLException e) {
			 System.out.println("erro");
			}
			return clientes;
			}
}


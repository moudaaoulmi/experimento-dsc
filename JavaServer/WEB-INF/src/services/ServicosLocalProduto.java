package services;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import exception.UsuarioJaExistenteException;

import vo.LocalProdutoVO;
import arquitetura.BancoDeDados;





class ServicosLocalProduto {
	
	
	private BancoDeDados banco;
	
	public ServicosLocalProduto() {
		
	}
	
	public LocalProdutoVO addLocalProduto(LocalProdutoVO localproduto){
		
		String sql = "insert into localproduto (descricao) values ('{localproduto.descricao}')";
		
		if(banco.executarNoQuery(sql)==0){
			try {
				throw new UsuarioJaExistenteException();
			} catch (UsuarioJaExistenteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return localproduto;
		
	}
	
	public boolean removerLocalProduto(LocalProdutoVO localproduto){
		
		String sql = "delete from localproduto  where codigo = {localproduto.codigo}";
		
		if(banco.executarNoQuery(sql)==0){
			return false;
		}
		return true;
	}
	
	public Map<Integer, LocalProdutoVO> pesquisarLocalProduto(String texto,String coluna){
		String sql = "select * from localproduto where coluna like '%texto%'";
		Map<Integer,LocalProdutoVO> lp = null;
		try{
			ResultSet rs =  banco.executar(sql);
			
			int i=0;
			while(rs.next()){
				
				LocalProdutoVO dados_item = new LocalProdutoVO();
				dados_item.codigo = rs.getInt("codigo");
				dados_item.descricao = rs.getString("descricao");
				
				lp.put(i, dados_item);
			i++;
			}
			
				
			}catch (SQLException e) {
			 System.out.println("erro");
			}
			return lp;
			}

	
	
	public  Map<Integer, LocalProdutoVO> getLocalProdutos(){
		String sql = "select * from localproduto";
		Map<Integer,LocalProdutoVO> lp = null;
		try{
			ResultSet rs =  banco.executar(sql);
			
			int i=0;
			while(rs.next()){
				
				LocalProdutoVO dados_item = new LocalProdutoVO();
				dados_item.codigo = rs.getInt("codigo");
				dados_item.descricao = rs.getString("descricao");
				
				lp.put(i, dados_item);
			i++;
			}
			
				
			}catch (SQLException e) {
			 System.out.println("erro");
			}
			return lp;
			}
}


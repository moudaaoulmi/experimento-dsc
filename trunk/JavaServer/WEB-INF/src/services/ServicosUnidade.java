package services;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import exception.UsuarioJaExistenteException;

import vo.UnidadeVO;
import arquitetura.BancoDeDados;





class ServicosUnidade {
	
	
	private BancoDeDados banco;
	
	public ServicosUnidade() {
		
	}
	
	public UnidadeVO addUnidade(UnidadeVO unidade){
		
		String sql = "insert into unidade (descricao) values ('{unidade.descricao}')";
		
		if(banco.executarNoQuery(sql)==0){
			try {
				throw new UsuarioJaExistenteException();
			} catch (UsuarioJaExistenteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return unidade;
		
	}
	
	public boolean removerUnidade(UnidadeVO unidade){
		
		String sql = "delete from unidade  where codigo = {unidade.codigo}";
		
		if(banco.executarNoQuery(sql)==0){
			return false;
		}
		return true;
	}
	
	public Map<Integer, UnidadeVO> pesquisarUnidade(String texto,String coluna){
		String sql = "select * from unidade where coluna like '%texto%'";
		Map<Integer,UnidadeVO> unidade = null;
		try{
			ResultSet rs =  banco.executar(sql);
			
			int i=0;
			while(rs.next()){
				
				UnidadeVO dados_item = new UnidadeVO();
				dados_item.codigo = rs.getInt("codigo");
				dados_item.descricao = rs.getString("descricao");
				
				unidade.put(i, dados_item);
			i++;
			}
			
				
			}catch (SQLException e) {
			 System.out.println("erro");
			}
			return unidade;
			}

	
	
	public  Map<Integer, UnidadeVO> getUnidades(){
		String sql = "select * from unidade";
		Map<Integer,UnidadeVO> unidade = null;
		try{
			ResultSet rs =  banco.executar(sql);
			
			int i=0;
			while(rs.next()){
				
				UnidadeVO dados_item = new UnidadeVO();
				dados_item.codigo = rs.getInt("codigo");
				dados_item.descricao = rs.getString("descricao");
				
				unidade.put(i, dados_item);
			i++;
			}
			
				
			}catch (SQLException e) {
			 System.out.println("erro");
			}
			return unidade;
			}
}


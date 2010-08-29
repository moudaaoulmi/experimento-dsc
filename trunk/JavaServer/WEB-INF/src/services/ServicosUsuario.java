import arquitetura.BancoDeDados;
import vo.UsuarioVO;


class ServicosUsuario{
	
	
	private BancoDeDados banco;
	
	
	
	public UsuarioVO addUsuario(UsuarioVO usuario){
		
		String sql = "insert into usuario (nome,comissao,senha,permissao,login) values ('"+usuario.nome+"',"+usuario.comissao+",'"+usuario.senha+"',"+usuario.permissao+",'"+usuario.login+"')";
		
		if(banco.executarNoQuery(sql)==0){
		throw new RuntimeException("erro no addUsuario");	
		}
		
		return usuario;
		
	}
	
	public UsuarioVO atualizarUsuario(UsuarioVO usuario){
		String sql = "UPDATE usuario SET nome = '"+usuario.nome+"' , comissao = "+usuario.comissao+" , senha = '"+usuario.senha+"', permissao = "+usuario.permissao+" , login  = '"+usuario.login+"' where codigo = usuario.codigo";
		if(banco.executarNoQuery(sql)==0){
			throw new RuntimeException("erro no addUsuario");	
			}
		return usuario;
		
	}
	
	public UsuarioVO logar(UsuarioVO usuario){
		String sql = "select * from usuario where login = 'usuario.login'";
		
		
		 if(!resultado){
			 throw new Exception(Erros::SQL_ERRO_MENSAGEM,Erros::SQL_ERRO_CODIGO);
	}
		 dados_usuario = NULL;
		 f = fopen('logou.txt','w+');
		 fwrite(f,dados_usuario.login);
		while(registro = resultado.FetchNextObject(){
			dados_usuario = new ();
			dados_usuario.nome = registro.NOME;
			dados_usuario.codigo = registro.CODIGO;
			dados_usuario.comissao = (float)registro.COMISSAO;
			dados_usuario.permissao = registro.PERMISSAO;
			dados_usuario.senha = registro.SENHA;
			dados_usuario.login = registro.LOGIN;
		}
		fwrite(f,dados_usuario.login);
		fwrite(f,dados_usuario.senha);
		if(dados_usuario.senha==usuario.senha){
			fwrite(f,'Entrou');
			return dados_usuario;
		}else{
			 throw new Exception(Erros::AUTENTICACAO_FALHOU_MENSAGEM
			 ,Erros::AUTENTICACAO_FALHOU_CODIGO); 
		}
		
		}
	
	
	public UsuarioVO removerUsuario( usuario){
		
		sql = "delete from usuario  where codigo = "+usuario.codigo+"";
		resultado = this.conn.Execute(sql);
		if(this.conn.Affected_Rows()==0){
			return false;
		}
		return true;
		}
	
	public UsuarioVO pesquisarUsuario(texto,coluna){
		sql = "select * from usuario where coluna like '%texto%'";
		resultado = this.conn.Execute(sql);
		while(registro = resultado.FetchNextObject()){			
			retorna_dados_usuario [] = this.toUsuario(registro);
		}
		return retorna_dados_usuario;
		}
	
	public UsuarioVO getUsuarios(){
		sql = "select * from usuario";
		
		resultado = this.conn.Execute(sql);
		
		while(registro = resultado.FetchNextObject()){			
			retorna_dados_usuario [] = this.toUsuario(registro);
		}
		return retorna_dados_usuario;	
		}
	
	private UsuarioVO toUsuario(registro){
		if(registro.CODIGO != 0){
			dados_usuario = new ();
			dados_usuario.nome = registro.NOME;
			dados_usuario.codigo = registro.CODIGO;
			dados_usuario.comissao = (float)registro.COMISSAO;
			dados_usuario.permissao = registro.PERMISSAO;
			dados_usuario.senha = registro.SENHA;
			dados_usuario.login = registro.LOGIN;
			return dados_usuario;
		}
		}
		}



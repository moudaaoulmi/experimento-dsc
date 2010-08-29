package arquitetura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BancoDeDados {

	private String driver = "com.mysql.jdbc.Driver";
	private String URL = "jdbc:mysql://localhost/caixaEestoque";
	private String USE = "root";
	private String SENHA = "root";
	private Connection conexao;

	private void getConexao() {
		Connection con = null;
		try {

			Class.forName(driver);
			con = DriverManager.getConnection(URL, USE, SENHA);
			this.conexao = con;
		} catch (ClassNotFoundException e) {
			this.erroSQL("Driver não encontrado");

		} catch (SQLException e) {
			this.erroSQL("Falha ao conectar");

		}

	}
	
	private void erroSQL(String sr){
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, sr);
		
	}

	
	public void close(){
		try {
			this.conexao.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.erroSQL(e.getMessage()+"\n"+"Nao foi possivel fechar a conexao");
		}
		
	}
	
	public ResultSet executar(String query) {
		Statement st;
		ResultSet rs;
		this.getConexao();
		try {
			st = this.conexao.createStatement();
			rs = st.executeQuery(query);
			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public int executarNoQuery( String query )  {
	            Statement st;
	           int rs = 0;
	           this.getConexao();
	           
	                try {
						st = this.conexao.createStatement();
						rs = st.executeUpdate(query);
					
	                } catch (SQLException e) {
	                this.erroSQL("Falha ao conectar com o Banco de Dados");
					}
	                
	                return rs;
	            } 

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String url) {
		URL = url;
	}

	public String getUSE() {
		return USE;
	}

	public void setUSE(String use) {
		USE = use;
	}

	public String getSENHA() {
		return SENHA;
	}

	public void setSENHA(String senha) {
		SENHA = senha;
	}

}
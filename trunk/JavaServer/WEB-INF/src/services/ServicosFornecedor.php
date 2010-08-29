<?php
//include 'app/db/BaseDados.php';
include 'app/vo/FornecedorVO.php';

class ServicosFornecedor {
	
	
	private $conn;
	
	function ServicosFornecedor() {
		$db = new BaseDados();
		$this->conn = $db->conn;
	}
	
	public function addFornecedor(FornecedorVO $item){
		
		$sql = "insert into fornecedor (nome,endereco
		 ,bairro ,cidade ,UF ,cep ,cpf_cnpj ,insc_estadual
		 ,fone ,contato ,email ,url ,obs,avaliacao) values ('{$item->nome}'		 
		  ,'{$item->endereco}'
		 ,'{$item->bairro}' ,'{$item->cidade}'
		 ,'{$item->UF}' ,'{$item->cep}'
		 ,'{$item->cpf_cnpj}' ,'{$item->insc_estadual}'
		 ,'{$item->fone}' ,'{$item->contato}'
		 ,'{$item->email}' ,'{$item->url}' ,'{$item->obs}','{$item->avaliacao}')";
		
		$resultado = $this->conn->Execute($sql);
		$item->codigo = $this->conn->insert_Id();
		return $item;
		
	}
	
	public function removerFornecedor(FornecedorVO $item){
		
		$sql = "delete from fornecedor  where codigo = {$item->codigo}";
		$resultado = $this->conn->Execute($sql);
		if($this->conn->Affected_Rows()==0){
			return false;
		}
		return true;
	}
	
	public function pesquisarFornecedor($texto,$coluna){
		$sql = "select * from fornecedor where $coluna like '%$texto%'";
		$resultado = $this->conn->Execute($sql);
		while($registro = $resultado->FetchNextObject()){			
			$dados_item = new FornecedorVO();
			$dados_item->codigo = $registro->CODIGO;
			$dados_item->nome = $registro->NOME;
			$dados_item->bairro = $registro->BAIRRO;
			$dados_item->cep = $registro->CEP;
			$dados_item->cidade = $registro->CIDADE;
			$dados_item->contato = $registro->CONTATO;
			$dados_item->cpf_cnpj = $registro->CPF_CNPJ;
			$dados_item->email = $registro->EMAIL;
			$dados_item->endereco = $registro->ENDERECO;
			$dados_item->fone = $registro->FONE;
			$dados_item->insc_estadual = $registro->INSC_ESTADUAL;
			$dados_item->obs = $registro->OBS;
			$dados_item->UF = $registro->UF;
			$dados_item->url = $registro->URL;
			$dados_item->avaliacao= $registro->AVALIACAO;
			$retorna_dados_item [] = $dados_item;
		}
		return $retorna_dados_item;
	}
	
	public function getFornecedors(){
		$sql = "select * from fornecedor";
		
		$resultado = $this->conn->Execute($sql);
		
		while($registro = $resultado->FetchNextObject()){			
			$dados_item = new FornecedorVO();
			$dados_item->codigo = $registro->CODIGO;
			$dados_item->nome = $registro->NOME;
			$dados_item->bairro = $registro->BAIRRO;
			$dados_item->cep = $registro->CEP;
			$dados_item->cidade = $registro->CIDADE;
			$dados_item->contato = $registro->CONTATO;
			$dados_item->cpf_cnpj = $registro->CPF_CNPJ;
			$dados_item->email = $registro->EMAIL;
			$dados_item->endereco = $registro->ENDERECO;
			$dados_item->fone = $registro->FONE;
			$dados_item->insc_estadual = $registro->INSC_ESTADUAL;
			$dados_item->obs = $registro->OBS;
			$dados_item->UF = $registro->UF;
			$dados_item->url = $registro->URL;
			$dados_item->avaliacao= $registro->AVALIACAO;
			$retorna_dados_item [] = $dados_item;
		}
		return $retorna_dados_item;	
	}


public function atualizarFornecedor(FornecedorVO $fornecedor) {
	
		$sql = "UPDATE fornecedor SET nome = '$fornecedor->nome',endereco='$fornecedor->endereco',bairro='$fornecedor->bairro',cidade='$fornecedor->cidade',UF='$fornecedor->UF',cep='$fornecedor->cep',
		cpf_cnpj='$fornecedor->cpf_cnpj',insc_estadual='$fornecedor->insc_estadual',fone='$fornecedor->fone',contato='$fornecedor->contato',email='$fornecedor->email',url='$fornecedor->url',obs='$fornecedor->obs',avaliacao='$fornecedor->avaliacao' where codigo=$fornecedor->codigo";
		
	$resultado = $this->conn->Execute($sql);
return $fornecedor;	
	
}
}
//$eu = new ServicosFornecedor();
//$c = new FornecedorVO();
//$c->nome = "hitalo";
//$eu->getFornecedors();

?>

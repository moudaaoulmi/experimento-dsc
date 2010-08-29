<?php
//include '/home2/pcmaisma/public_html/admProjetos/ZendAMFAdminProjeto/app/db/BaseDados.php';
//include '/home2/pcmaisma/public_html/admProjetos/ZendAMFAdminProjeto/app/vo/ProjetoVO.php';

//include 'app/db/BaseDados.php';
include 'app/vo/PreVendaVO.php';
include 'app/vo/ItemPrevendaVO.php';

class ServicosPreVenda {
	
	
	private $conn;
	
	
	function ServicosPreVenda() {
		$db = new BaseDados();
		$this->conn = $db->conn;
	}
	
	
	public function abrirPreVenda(PreVendaVO $item){
		$f = fopen('log2.txt',"w+");
		$sql = "insert into prevenda (codUsuario,status,dataAbertura) values ('$item->codUsuario'		 
		 ,'0' ,now())";
		fwrite($f,$sql);
		$this->conn->StartTrans();
		$resultado = $this->conn->Execute($sql);
		$item->codigo = $this->conn->insert_Id();
		
		if($this->conn->HasFailedTrans()){
			throw new Exception("Erro ao abrir pré-Venda",16);
		}	
		
		$this->conn->CompleteTrans();
		$item->status = 0;
		
		return $item;
	}
	
	public function addItemPreVenda(ItemPreVendaVO $item){
			$sql ="select * from produto where codigo = $item->codProduto";
			$this->conn->StartTrans();
			$result = $this->conn->Execute($sql);	
			if(!$result){
				throw new Exception("Produto não existe",4);
			}
			$registro = $result->FetchNextObject();
			if($registro->QTDEMESTOQUE < $item->quantidade){
				throw new Exception("Quantidade de produtos maior que disponível!",15);
			}
			$sql = "insert into itensprevenda (codPrevenda, codProduto, descricao, quantidade,valor) 
			      values ('$item->codigoPrevenda','$item->codProduto','$item->descricao','$item->quantidade','$item->valor')";
			$this->conn->Execute($sql);
			
					
			$sql = "UPDATE produto SET qtdEmEstoque = (qtdEmEstoque - $item->quantidade) where codigo = $item->codProduto";
			
			$result = $this->conn->Execute($sql);
			
			$falhou = false;	
			if($this->conn->HasFailedTrans()){
				$falhou = true;
			}
			
			$item->codigo = $this->conn->insert_Id();
			$this->conn->CompleteTrans();
			
			if($falhou){
				throw new Exception("Erro ao inserir item!",16);
			}
			
			
			return $item;
			
	}
	
	
//	foreach ($item->itemPreVenda as $temp) {
//			$sql = "insert into itensprevenda (codPrevenda, codProduto, quantidade,valor) 
//			      values ('$item->codigo','$temp->codProduto','$temp->quantidade','$temp->valor')";
//			$this->conn->Execute($sql);
//		}
	
	public function fecharPreVenda(PreVendaVO $item){
		$sql = "UPDATE prevenda SET codUsuario = '$item->codUsuario' , status = '1', obs = '$item->obs', valorTotal = '$item->valorTotal' where codigo = $item->codigo";		
		$this->conn->Execute($sql);
		return $item;
	}
	
	public function removerItemPreVenda(ItemPreVendaVO $item){
		
		$sql = "UPDATE produto SET qtdEmEstoque = (qtdEmEstoque + $item->quantidade)  WHERE codigo = $item->codProduto";
		$this->conn->StartTrans();
		$this->conn->Execute($sql);
		$sql = "DELETE FROM itensprevenda WHERE codigo = $item->codigo";
		$this->conn->Execute($sql);
		if($this->conn->HasFailedTrans()){
			$falhou = true;
		}
		$this->conn->CompleteTrans();
		if($falhou){
			throw new Exception("Erro ao remover item pré-venda!",101);
		}
	}
	
	public function cancelarPreVenda(PreVendaVO $item){	
		$sql = "select * FROM itensprevenda WHERE codPrevenda = $item->codigo";
		$this->conn->StartTrans();
		$resultado = $this->conn->Execute($sql);
		while($registro = $resultado->FetchNextObject()){
			$sql = "UPDATE produto SET qtdEmEstoque = (qtdEmEstoque + $registro->QUANTIDADE)  WHERE codigo = $registro->CODPRODUTO";
			$this->conn->Execute($sql);			
		}
		$sql = "DELETE FROM itensprevenda WHERE codPrevenda = $item->codigo";
		$this->conn->Execute($sql);
		$sql = "UPDATE prevenda SET codUsuario = '$item->codUsuario' , status = '2', obs = '$item->obs' where codigo = $item->codigo";
		$this->conn->Execute($sql);
		$falhou = false;
		$falhou = false;	
		if($this->conn->HasFailedTrans()){
			$falhou = true;
		}
		$this->conn->CompleteTrans();
		if($falhou){
			throw new Exception("Erro ao cancelar pré-venda. Pré-venda não cancelada!",100);
		}
		return $item;
	}
	
	
	public function removerItem(PreVendaVO $item){
		
		$sql = "delete from prevenda  where codigo = $item->codigo";
		$resultado = $this->conn->Execute($sql);
		if($this->conn->Affected_Rows()==0){
			return false;
		}
		return true;
	}
	
	public function filtraData($data,$coluna){
		$sql = "select * from prevenda where DATE($coluna) = '$data' and status = 1";
		$resultado = $this->conn->Execute($sql);
		while($registro = $resultado->FetchNextObject()){			
			$retorna_dados_item [] = $this->toPreVenda($registro);
		}
		return $retorna_dados_item;
	}
	
	private function getItensPrevenda($codigo){
		$sql = "select * from itensprevenda where codPrevenda  = $codigo";
		$resultado = $this->conn->Execute($sql);
		$retorna_dados_item = null;
		while($registro = $resultado->FetchNextObject()){
			$dados_item = new ItemPreVendaVO();
			$dados_item->codigo = $registro->CODIGO;
			$dados_item->codigoPrevenda = $registro->CODPREVENDA;
			$dados_item->descricao = $registro->DESCRICAO;
			$dados_item->codProduto = $registro->CODPRODUTO;
			$dados_item->quantidade = $registro->QUANTIDADE;
			$dados_item->valor = $registro->VALOR;
			$retorna_dados_item [] = $dados_item;
		}
		return $retorna_dados_item;
	}
	
	
	
	public function pesquisarItens($texto,$coluna){
		$sql = "select * from prevenda where $coluna like '%$texto%'";
		$resultado = $this->conn->Execute($sql);
		while($registro = $resultado->FetchNextObject()){			
			$retorna_dados_item [] = $this->toPreVenda($registro);
		}
		return $retorna_dados_item;
	}
	
	public function getItens(){
		$f = fopen('log.txt','w+');
		fwrite($f,'Entrou aki');
		$sql = "select * from prevenda";
		fwrite($f,'Entrou aki');
		$resultado = $this->conn->Execute($sql);
		$retorna_dados_item = null;
		while($registro = $resultado->FetchNextObject()){			
			$retorna_dados_item [] = $this->toPreVenda($registro);
		}
		return $retorna_dados_item;	
	}
	
	public function getItensValidos(){
		$sql = "select * from prevenda where status = 1";
		$resultado = $this->conn->Execute($sql);
		$retorna_dados_item = null;
		while($registro = $resultado->FetchNextObject()){			
			$retorna_dados_item [] = $this->toPreVenda($registro);
		}
		return $retorna_dados_item;	
	}
	
	public function pesquisarItensValidos($texto,$coluna){
		$sql = "select * from prevenda where $coluna like '%$texto%' and status = 1";
		$resultado = $this->conn->Execute($sql);
		while($registro = $resultado->FetchNextObject()){			
			$retorna_dados_item [] = $this->toPreVenda($registro);
		}
		return $retorna_dados_item;
	}
	
	
	private function toPreVenda($registro){
		$dados_item = new PreVendaVO();
		$dados_item->codigo = $registro->CODIGO;
		$dados_item->codUsuario = $registro->CODUSUARIO;
		$dados_item->obs = $registro->OBS;
		$dados_item->status = $registro->STATUS;
		$dados_item->dataAbertura = $registro->DATAABERTURA;
		$dados_item->valorTotal = $registro->VALORTOTAL;
		$dados_item->itemPreVenda = $this->getItensPrevenda($registro->CODIGO);
		return $dados_item;
	}
}
$eu = new ServicosPreVenda();
////$c = new PreVendaVo();
////$c->codUsuario = 0;
$eu->getItens();

?>
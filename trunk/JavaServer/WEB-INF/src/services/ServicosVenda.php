<?php

//include 'app/db/BaseDados.php';
include 'app/vo/VendaVO.php';

class ServicosVenda {
		
	function ServicosVenda(){
		$db = new BaseDados();
		$this->conn = $db->conn;
	}
	
	
	public function abrirVenda(VendaVO $item){
		$f = fopen('log.txt','w+');
		fwrite($f,'inicio');
		$sql = "insert into venda (codUsuario,codPreVenda,status,dataVenda) values ('$item->codUsuario', '$item->codPreVenda','0' ,now())";
		fwrite($f,$sql);
		$this->conn->StartTrans();
		$resultado = $this->conn->Execute($sql);
		fwrite($f,$sql);
		$item->codigo = $this->conn->insert_Id();
		
		if($this->conn->HasFailedTrans()){
			throw new Exception("Erro ao abri Venda",16);
		}	
		$this->conn->CompleteTrans();
		$item->status = 0;
		
		return $item;
	}
	
	
	public function fecharVenda(VendaVO $item){
		$f = fopen('lo3.txt','w+');
		$this->conn->StartTrans();
		fwrite($f,$item->codCliente);
		if($item->codCliente != 0){
			foreach ($item->formasPagamento as $v) {
				if($v->codFormaPagamento == 0){
					$sql = "UPDATE cliente SET limCredito = (limCredito - $v->valor) WHERE codigo = $item->codCliente";
					$this->conn->Execute($sql);
				}
			}
		}
		$sql = "UPDATE prevenda SET status = 3 WHERE codigo = $item->codPreVenda"; 
		$this->conn->Execute($sql);
		$sql = "UPDATE venda SET codUsuario = '$item->codUsuario' , codCliente = $item->codCliente, codPreVenda = $item->codPreVenda, status = '1', desconto = $item->desconto, obs = '$item->obs', valorTotal = '$item->valorTotal' where codigo = $item->codigo";
		fwrite($f,$sql);		
		$this->conn->Execute($sql);
		$falhou = false;
		if($this->conn->HasFailedTrans()){
			$falhou = true;
		}
		$this->conn->CompleteTrans();
		if($falhou){
			throw new Exception("Erro ao cancelar venda. Venda não cancelada!",100);
		}
		return $item;
	}
	
		
	public function cancelarVenda(VendaVO $item){	
		$this->conn->StartTrans();
		$sql = "UPDATE venda SET codUsuario = '$item->codUsuario', codPreVenda = null , status = '2', obs = '$item->obs' where codigo = $item->codigo";
		$this->conn->Execute($sql);
		$falhou = false;
		if($this->conn->HasFailedTrans()){
			$falhou = true;
		}
		$this->conn->CompleteTrans();
		if($falhou){
			throw new Exception("Erro ao cancelar venda. Venda não cancelada!",100);
		}
		return $item;
	}
	
	
		
	public function filtraData($data,$coluna){
		$sql = "select * from venda where DATE($coluna) = '$data'";
		$resultado = $this->conn->Execute($sql);
		while($registro = $resultado->FetchNextObject()){			
			$retorna_dados_item [] = $this->toVenda($registro);
		}
		return $retorna_dados_item;
	}
			
	public function pesquisarItens($texto,$coluna){
		$sql = "select * from venda where $coluna like '%$texto%'";
		$resultado = $this->conn->Execute($sql);
		while($registro = $resultado->FetchNextObject()){			
			$retorna_dados_item [] = $this->toVenda($registro);
		}
		return $retorna_dados_item;
	}
	
	public function getItens(){
		$sql = "select * from venda";
		$resultado = $this->conn->Execute($sql);
		while($registro = $resultado->FetchNextObject()){			
			$retorna_dados_item [] = $this->toVenda($registro);
		}
		return $retorna_dados_item;	
	}
	
	
	private function toVenda($registro){
		$dados_item = new VendaVO();
		$dados_item->codigo = $registro->CODIGO;
		$dados_item->codCliente = $registro->CODCLIENTE;
		$dados_item->codUsuario = $registro->CODUSUARIO;
		$dados_item->codPreVenda = $registro->CODPREVENDA;
		$dados_item->obs = $registro->OBS;
		$dados_item->status = $registro->STATUS;
		$dados_item->dataVenda = $registro->DATAVENDA;
		$dados_item->valorTotal = $registro->VALORTOTAL;
		$dados_item->desconto = $regisro->DESCONTO;
		return $dados_item;
	}
}

?>
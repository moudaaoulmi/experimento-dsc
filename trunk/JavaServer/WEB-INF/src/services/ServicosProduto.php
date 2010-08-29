<?php
//include 'app/db/BaseDados.php';
include 'app/vo/ProdutoVO.php';

class ServicosProduto {
	
	
	private $conn;
	
	function ServicosProduto() {
		$db = new BaseDados();
		$this->conn = $db->conn;
	}
	
	public function addProduto(ProdutoVO $item){
		
		$sql = "insert into produto (codBarra,codGrupo
		 ,descricao ,referencia ,codLocal ,codUnidade
		 ,qtdPorUnidade ,qtdEmEstoque ,codFornecedor ,precoCompra ,precoVenda,foto) values ('{$item->codBarra}' ,{$item->codGrupo} ,'{$item->descricao}'
		 ,'{$item->referencia}' ,{$item->codLocal}
		 ,{$item->codUnidade} ,{$item->qtdPorUnidade}
		 ,{$item->qtdEmEstoque} ,{$item->codFornecedor}
		 ,{$item->precoCompra} ,{$item->precoVenda}
		 ,'{$item->foto}')";
		
		$resultado = $this->conn->Execute($sql);
		$item->codigo = $this->conn->insert_Id();
		return $item;
		
	}
	
	public function removerProduto(ProdutoVO $item){
		if($item->codigo==0){
			throw new Exception("Produto não pode ser resolvido!",99);
		}
		$sql = "delete from produto  where codigo = {$item->codigo}";
		$resultado = $this->conn->Execute($sql);
		if($this->conn->Affected_Rows()==0){
			return false;
		}
		return true;
	}
	
	public function pesquisarProduto($texto,$coluna){
		$sql = "select * from produto where $coluna like '%$texto%'";
		$resultado = $this->conn->Execute($sql);
		return $this->toProdutos($resultado);
	}
	
	public function getProduto($texto,$coluna){
		$sql = "select * from produto where $coluna = '$texto'";
		$resultado = $this->conn->Execute($sql);
		$dados_item = null;
		while($registro = $resultado->FetchNextObject()){
			if($registro->CODIGO != 0){			
				$dados_item = new ProdutoVO();
				$dados_item->codigo = $registro->CODIGO;
				$dados_item->codBarra = $registro->CODBARRA;
				$dados_item->codGrupo = $registro->CODGRUPO;
				$dados_item->descricao = $registro->DESCRICAO;
				$dados_item->referencia = $registro->REFERENCIA;
				$dados_item->codLocal = $registro->CODLOCAL;
				$dados_item->codUnidade = $registro->CODUNIDADE;
				$dados_item->qtdPorUnidade = $registro->QTDPORUNIDADE;
				$dados_item->qtdEmEstoque = $registro->QTDEMESTOQUE;
				$dados_item->codFornecedor = $registro->CODFORNECEDOR;
				$dados_item->precoCompra = $registro->PRECOCOMPRA;
				$dados_item->precoVenda = $registro->PRECOVENDA;
				$dados_item->foto = $registro->FOTO;
			}
		}
		return $dados_item;
	}
	
	public function getProdutos(){
		$sql = "select * from produto";
		$resultado = $this->conn->Execute($sql);
		return $this->toProdutos($resultado);
	}
	
	public function filtroProduto($sqlArray){
		$sql = "select * from produto where ";
		$entrou = false;
		foreach($sqlArray as $str) {
			if($str != ""){
				if($entrou){
					$sql = "$sql and $str ";
				}else{
					$sql = "$sql $str ";
					$entrou = true;
				}
			}
		}
		$f = fopen('logo.txt','w+');
		fwrite($f,$sql);
		$resultado = $this->conn->Execute($sql);
		return $this->toProdutos($resultado);	
	}
	
	private function toProdutos($resultado){
		$retorna_dados_item = null;
		$dados_item = null;
		while($registro = $resultado->FetchNextObject()){			
			$dados_item = new ProdutoVO();
			if($registro->CODIGO != 0){
				$dados_item->codigo = $registro->CODIGO;
				$dados_item->codBarra = $registro->CODBARRA;
				$dados_item->codGrupo = $registro->CODGRUPO;
				$dados_item->descricao = $registro->DESCRICAO;
				$dados_item->referencia = $registro->REFERENCIA;
				$dados_item->codLocal = $registro->CODLOCAL;
				$dados_item->codUnidade = $registro->CODUNIDADE;
				$dados_item->qtdPorUnidade = $registro->QTDPORUNIDADE;
				$dados_item->qtdEmEstoque = $registro->QTDEMESTOQUE;
				$dados_item->codFornecedor = $registro->CODFORNECEDOR;
				$dados_item->precoCompra = $registro->PRECOCOMPRA;
				$dados_item->precoVenda = $registro->PRECOVENDA;
				$dados_item->foto = $registro->FOTO;
			$retorna_dados_item [] = $dados_item;
			}
		}
		return $retorna_dados_item;
	}


	public function atualizarProduto(ProdutoVO $produto) {
		if($produto->codigo == 0){
			throw new Exception("Produto não pôde ser atualizado!",100);
		}
		$sql = "UPDATE produto SET codBarra = '$produto->codBarra', codGrupo = $produto->codGrupo, descricao = '$produto->descricao', referencia = '$produto->referencia',codLocal = $produto->codLocal,codUnidade=$produto->codUnidade,qtdPorUnidade=$produto->qtdPorUnidade,qtdEmEstoque=$produto->qtdEmEstoque,codFornecedor=$produto->codFornecedor,precoCompra=$produto->precoCompra,
		precoVenda=$produto->precoVenda,foto='$produto->foto' where codigo=$produto->codigo";
		$resultado = $this->conn->Execute($sql);
		return $produto;	
		
	}
}
//$eu = new ServicosProduto();
//
//$a = array("","","","codFornecedor = '1' ","","codUnidade = '3'");
//
//$eu->filtroProduto($a);

//$c->codBarra = "sdf";
//$c->codFornecedor = 1;
//$c->codGrupo = 1;
//$c->codLocal =1;
//$c->codUnidade = 1;
//$c->descricao = "asdfasdfasdfasdfasdfdafasdfasdfsadf";
//$c->referencia = "asdfkjsaflkasjdfçslkdfjsalçfkj";
//$c->foto = "sdf";
//$c->precoCompra = 1.0;
//$c->precoVenda = 1.0;
//$c->qtdEmEstoque=1;
//$c->qtdPorUnidade = 1;
//$eu->addProduto($c);
//echo $c->codigo;
//$c->codigo = 47;
//$c->dataCadastro = "2010-09-31";
//$eu->getProdutos();

?>

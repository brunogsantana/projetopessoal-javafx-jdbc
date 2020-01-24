package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Despesa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String descricao;
	private Date dataOriginalDespesa;
	private Date dataConcluidaDespesa;
	private String statusDespesa;
	private String meioPagamento;
	private Integer qtdParcela;
	private Double valor;
	private String obs;
	private Double valorParcela;
	private Date dataPagamentoParcela;

	private Conta conta;
	private CategoriaDespesa categoriaDespesaPai;
	private CategoriaDespesaFilho categoriaDespesaFilho;

	public Despesa() {
	}

	public Despesa(Integer id, String descricao, Date dataOriginalDespesa, Date dataConcluidaDespesa,
			String statusDespesa, String meioPagamento, Integer qtdParcela, Double valor, String obs,
			Double valorParcela, Date dataPagamentoParcela, Conta conta, CategoriaDespesa categoriaDespesaPai,
			CategoriaDespesaFilho categoriaDespesaFilho) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.dataOriginalDespesa = dataOriginalDespesa;
		this.dataConcluidaDespesa = dataConcluidaDespesa;
		this.statusDespesa = statusDespesa;
		this.meioPagamento = meioPagamento;
		this.qtdParcela = qtdParcela;
		this.valor = valor;
		this.obs = obs;
		this.valorParcela = valorParcela;
		this.dataPagamentoParcela = dataPagamentoParcela;
		this.conta = conta;
		this.categoriaDespesaPai = categoriaDespesaPai;
		this.categoriaDespesaFilho = categoriaDespesaFilho;
	}

	public String getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(String meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

	public Integer getQtdParcela() {
		return qtdParcela;
	}

	public void setQtdParcela(Integer qtdParcela) {
		this.qtdParcela = qtdParcela;
	}

	public Double getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public Date getDataPagamentoParcela() {
		return dataPagamentoParcela;
	}

	public void setDataPagamentoParcela(Date dataPagamentoParcela) {
		this.dataPagamentoParcela = dataPagamentoParcela;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public CategoriaDespesa getCategoriaDespesaPai() {
		return categoriaDespesaPai;
	}

	public void setCategoriaDespesaPai(CategoriaDespesa categoriaDespesaPai) {
		this.categoriaDespesaPai = categoriaDespesaPai;
	}

	public CategoriaDespesaFilho getCategoriaDespesaFilho() {
		return categoriaDespesaFilho;
	}

	public void setCategoriaDespesaFilho(CategoriaDespesaFilho categoriaDespesaFilho) {
		this.categoriaDespesaFilho = categoriaDespesaFilho;
	}

	public Date getDataOriginalDespesa() {
		return dataOriginalDespesa;
	}

	public void setDataOriginalDespesa(Date dataOriginalDespesa) {
		this.dataOriginalDespesa = dataOriginalDespesa;
	}

	public Date getDataConcluidaDespesa() {
		return dataConcluidaDespesa;
	}

	public void setDataConcluidaDespesa(Date dataConcluidaDespesa) {
		this.dataConcluidaDespesa = dataConcluidaDespesa;
	}

	public String getStatusDespesa() {
		return statusDespesa;
	}

	public void setStatusDespesa(String statusDespesa) {
		this.statusDespesa = statusDespesa;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + ((dataConcluidaDespesa == null) ? 0 : dataConcluidaDespesa.hashCode());
		result = prime * result + ((dataOriginalDespesa == null) ? 0 : dataOriginalDespesa.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((statusDespesa == null) ? 0 : statusDespesa.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Despesa other = (Despesa) obj;
		if (conta == null) {
			if (other.conta != null)
				return false;
		} else if (!conta.equals(other.conta))
			return false;
		if (dataConcluidaDespesa == null) {
			if (other.dataConcluidaDespesa != null)
				return false;
		} else if (!dataConcluidaDespesa.equals(other.dataConcluidaDespesa))
			return false;
		if (dataOriginalDespesa == null) {
			if (other.dataOriginalDespesa != null)
				return false;
		} else if (!dataOriginalDespesa.equals(other.dataOriginalDespesa))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (statusDespesa == null) {
			if (other.statusDespesa != null)
				return false;
		} else if (!statusDespesa.equals(other.statusDespesa))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Despesa [id=" + id + ", descricao=" + descricao + ", dataOriginalDespesa=" + dataOriginalDespesa
				+ ", dataConcluidaDespesa=" + dataConcluidaDespesa + ", statusDespesa=" + statusDespesa
				+ ", meioPagamento=" + meioPagamento + ", qtdParcela=" + qtdParcela + ", valor=" + valor + ", obs="
				+ obs + ", valorParcela=" + valorParcela + ", dataPagamentoParcela=" + dataPagamentoParcela + ", conta="
				+ conta + ", categoriaDespesaPai=" + categoriaDespesaPai + ", categoriaDespesaFilho="
				+ categoriaDespesaFilho + "]";
	}



}

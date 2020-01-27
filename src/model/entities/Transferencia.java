package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Transferencia implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String descricao;
	private Date dataOriginalTransferencia;
	private Date dataConcluidaTransferencia;
	private Double custoTransferencia;
	private Double valor;
	private String obs;

	private Conta contaOrigem;
	private Conta contaDestino;

	public Transferencia() {
	}

	public Transferencia(Integer id, String descricao, Date dataOriginalTransferencia, Date dataConcluidaTransferencia,
			Double custoTransferencia, Double valor, String obs, Conta contaOrigem, Conta contaDestino) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.dataOriginalTransferencia = dataOriginalTransferencia;
		this.dataConcluidaTransferencia = dataConcluidaTransferencia;
		this.custoTransferencia = custoTransferencia;
		this.valor = valor;
		this.obs = obs;
		this.contaOrigem = contaOrigem;
		this.contaDestino = contaDestino;
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

	public Date getDataOriginalTransferencia() {
		return dataOriginalTransferencia;
	}

	public void setDataOriginalTransferencia(Date dataOriginalTransferencia) {
		this.dataOriginalTransferencia = dataOriginalTransferencia;
	}

	public Date getDataConcluidaTransferencia() {
		return dataConcluidaTransferencia;
	}

	public void setDataConcluidaTransferencia(Date dataConcluidaTransferencia) {
		this.dataConcluidaTransferencia = dataConcluidaTransferencia;
	}

	public Double getCustoTransferencia() {
		return custoTransferencia;
	}

	public void setCustoTransferencia(Double custoTransferencia) {
		this.custoTransferencia = custoTransferencia;
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

	public Conta getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(Conta contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public Conta getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(Conta contaDestino) {
		this.contaDestino = contaDestino;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contaDestino == null) ? 0 : contaDestino.hashCode());
		result = prime * result + ((contaOrigem == null) ? 0 : contaOrigem.hashCode());
		result = prime * result + ((dataConcluidaTransferencia == null) ? 0 : dataConcluidaTransferencia.hashCode());
		result = prime * result + ((dataOriginalTransferencia == null) ? 0 : dataOriginalTransferencia.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Transferencia other = (Transferencia) obj;
		if (contaDestino == null) {
			if (other.contaDestino != null)
				return false;
		} else if (!contaDestino.equals(other.contaDestino))
			return false;
		if (contaOrigem == null) {
			if (other.contaOrigem != null)
				return false;
		} else if (!contaOrigem.equals(other.contaOrigem))
			return false;
		if (dataConcluidaTransferencia == null) {
			if (other.dataConcluidaTransferencia != null)
				return false;
		} else if (!dataConcluidaTransferencia.equals(other.dataConcluidaTransferencia))
			return false;
		if (dataOriginalTransferencia == null) {
			if (other.dataOriginalTransferencia != null)
				return false;
		} else if (!dataOriginalTransferencia.equals(other.dataOriginalTransferencia))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		return "Transferencia [id=" + id + ", descricao=" + descricao + ", dataOriginalTransferencia="
				+ dataOriginalTransferencia + ", dataConcluidaTransferencia=" + dataConcluidaTransferencia
				+ ", custoTransferencia=" + custoTransferencia + ", valor=" + valor + ", obs=" + obs + ", contaOrigem="
				+ contaOrigem + ", contaDestino=" + contaDestino + "]";
	}



	

}

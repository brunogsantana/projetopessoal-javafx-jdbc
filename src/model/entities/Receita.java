package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Receita implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String descricao;
	private Date dataOriginalReceita;
	private Date dataConcluidaReceita;
	private String statusReceita;
	private Double valor;
	private String obs;

	private Conta conta;
	private CategoriaReceita categoriaReceita;

	public Receita() {
	}

	public Receita(Integer id, String descricao, CategoriaReceita categoriaReceita, Date dataOriginalReceita,
			Date dataConcluidaReceita, String statusReceita, Double valor, String obs, Conta conta) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.categoriaReceita = categoriaReceita;
		this.dataOriginalReceita = dataOriginalReceita;
		this.dataConcluidaReceita = dataConcluidaReceita;
		this.statusReceita = statusReceita;
		this.valor = valor;
		this.obs = obs;
		this.conta = conta;
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

	public CategoriaReceita getCategoriaReceita() {
		return categoriaReceita;
	}

	public void setCategoriaReceita(CategoriaReceita categoriaReceita) {
		this.categoriaReceita = categoriaReceita;
	}

	public Date getDataOriginalReceita() {
		return dataOriginalReceita;
	}

	public void setDataOriginalReceita(Date dataOriginalReceita) {
		this.dataOriginalReceita = dataOriginalReceita;
	}

	public Date getDataConcluidaReceita() {
		return dataConcluidaReceita;
	}

	public void setDataConcluidaReceita(Date dataConcluidaReceita) {
		this.dataConcluidaReceita = dataConcluidaReceita;
	}

	public String getStatusReceita() {
		return statusReceita;
	}

	public void setStatusReceita(String statusReceita) {
		this.statusReceita = statusReceita;
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
		result = prime * result + ((dataConcluidaReceita == null) ? 0 : dataConcluidaReceita.hashCode());
		result = prime * result + ((dataOriginalReceita == null) ? 0 : dataOriginalReceita.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((statusReceita == null) ? 0 : statusReceita.hashCode());
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
		Receita other = (Receita) obj;
		if (conta == null) {
			if (other.conta != null)
				return false;
		} else if (!conta.equals(other.conta))
			return false;
		if (dataConcluidaReceita == null) {
			if (other.dataConcluidaReceita != null)
				return false;
		} else if (!dataConcluidaReceita.equals(other.dataConcluidaReceita))
			return false;
		if (dataOriginalReceita == null) {
			if (other.dataOriginalReceita != null)
				return false;
		} else if (!dataOriginalReceita.equals(other.dataOriginalReceita))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (statusReceita == null) {
			if (other.statusReceita != null)
				return false;
		} else if (!statusReceita.equals(other.statusReceita))
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
		return "Receita [id=" + id + ", descricao=" + descricao + ", categoriaReceita=" + categoriaReceita
				+ ", dataOriginalReceita=" + dataOriginalReceita + ", dataConcluidaReceita=" + dataConcluidaReceita
				+ ", statusReceita=" + statusReceita + ", valor=" + valor + ", obs=" + obs + ", conta=" + conta + "]";
	}

}

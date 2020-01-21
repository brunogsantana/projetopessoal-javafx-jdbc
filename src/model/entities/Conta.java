package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Conta implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;

	private String cpf;

	private String tipoConta;

	private String banco;

	private Integer numeroBanco;

	private Integer numeroConta;

	private Integer numeroAgencia;

	private Date dataCadastro;

	private Double saldoAtual;

	private Double saldoAnterior;

	private Double saldoInicial;

	private Boolean favorita;

	public Conta() {

	}

	public Conta(Integer id, String name, String cpf, String tipoConta, String banco, Integer numeroBanco,

			Integer numeroConta, Integer numeroAgencia, Date dataCadastro, Double saldoAtual, Double saldoAnterior,

			Double saldoInicial, Boolean favorita) {

		super();

		this.id = id;

		this.name = name;

		this.cpf = cpf;

		this.tipoConta = tipoConta;

		this.banco = banco;

		this.numeroBanco = numeroBanco;

		this.numeroConta = numeroConta;

		this.numeroAgencia = numeroAgencia;

		this.dataCadastro = dataCadastro;

		this.saldoAtual = saldoAtual;

		this.saldoAnterior = saldoAnterior;

		this.saldoInicial = saldoInicial;

		this.favorita = favorita;

	}

	public Integer getId() {

		return id;

	}

	public void setId(Integer id) {

		this.id = id;

	}

	public String getTipoConta() {

		return tipoConta;

	}

	public void setTipoConta(String tipoConta) {

		this.tipoConta = tipoConta;

	}

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public String getCpf() {

		return cpf;

	}

	public void setCpf(String cpf) {

		this.cpf = cpf;

	}

	public String getBanco() {

		return banco;

	}

	public void setBanco(String banco) {

		this.banco = banco;

	}

	public Integer getNumeroBanco() {

		return numeroBanco;

	}

	public void setNumeroBanco(Integer numeroBanco) {

		this.numeroBanco = numeroBanco;

	}

	public Integer getNumeroConta() {

		return numeroConta;

	}

	public void setNumeroConta(Integer numeroConta) {

		this.numeroConta = numeroConta;

	}

	public Integer getNumeroAgencia() {

		return numeroAgencia;

	}

	public void setNumeroAgencia(Integer numeroAgencia) {

		this.numeroAgencia = numeroAgencia;

	}

	public Date getDataCadastro() {

		return dataCadastro;

	}

	public void setDataCadastro(Date dataCadastro) {

		this.dataCadastro = dataCadastro;

	}

	public Double getSaldoAtual() {

		return saldoAtual;

	}

	public void setSaldoAtual(Double saldoAtual) {

		this.saldoAtual = saldoAtual;

	}

	public Double getSaldoAnterior() {

		return saldoAnterior;

	}

	public void setSaldoAnterior(Double saldoAnterior) {

		this.saldoAnterior = saldoAnterior;

	}

	public Double getSaldoInicial() {

		return saldoInicial;

	}

	public void setSaldoInicial(Double saldoInicial) {

		this.saldoInicial = saldoInicial;

	}

	public Boolean getFavorita() {

		return favorita;

	}

	public void setFavorita(Boolean favorita) {

		this.favorita = favorita;

	}

	@Override

	public int hashCode() {

		final int prime = 31;

		int result = 1;

		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());

		result = prime * result + ((favorita == null) ? 0 : favorita.hashCode());

		result = prime * result + ((id == null) ? 0 : id.hashCode());

		result = prime * result + ((name == null) ? 0 : name.hashCode());

		result = prime * result + ((numeroBanco == null) ? 0 : numeroBanco.hashCode());

		result = prime * result + ((numeroConta == null) ? 0 : numeroConta.hashCode());

		result = prime * result + ((saldoAnterior == null) ? 0 : saldoAnterior.hashCode());

		result = prime * result + ((saldoAtual == null) ? 0 : saldoAtual.hashCode());

		result = prime * result + ((saldoInicial == null) ? 0 : saldoInicial.hashCode());

		result = prime * result + ((tipoConta == null) ? 0 : tipoConta.hashCode());

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

		Conta other = (Conta) obj;

		if (cpf == null) {

			if (other.cpf != null)

				return false;

		} else if (!cpf.equals(other.cpf))

			return false;

		if (favorita == null) {

			if (other.favorita != null)

				return false;

		} else if (!favorita.equals(other.favorita))

			return false;

		if (id == null) {

			if (other.id != null)

				return false;

		} else if (!id.equals(other.id))

			return false;

		if (name == null) {

			if (other.name != null)

				return false;

		} else if (!name.equals(other.name))

			return false;

		if (numeroBanco == null) {

			if (other.numeroBanco != null)

				return false;

		} else if (!numeroBanco.equals(other.numeroBanco))

			return false;

		if (numeroConta == null) {

			if (other.numeroConta != null)

				return false;

		} else if (!numeroConta.equals(other.numeroConta))

			return false;

		if (saldoAnterior == null) {

			if (other.saldoAnterior != null)

				return false;

		} else if (!saldoAnterior.equals(other.saldoAnterior))

			return false;

		if (saldoAtual == null) {

			if (other.saldoAtual != null)

				return false;

		} else if (!saldoAtual.equals(other.saldoAtual))

			return false;

		if (saldoInicial == null) {

			if (other.saldoInicial != null)

				return false;

		} else if (!saldoInicial.equals(other.saldoInicial))

			return false;

		if (tipoConta != other.tipoConta)

			return false;

		return true;

	}


	@Override

	public String toString() {

		return "Conta [id=" + id + ", name=" + name + ", cpf=" + cpf + ", tipoConta=" + tipoConta + ", banco=" + banco

				+ ", numeroBanco=" + numeroBanco + ", numeroConta=" + numeroConta + ", numeroAgencia=" + numeroAgencia

				+ ", dataCadastro=" + dataCadastro + ", saldoAtual=" + saldoAtual + ", saldoAnterior=" + saldoAnterior

				+ ", saldoInicial=" + saldoInicial + ", favorita=" + favorita + "]";

	}

}
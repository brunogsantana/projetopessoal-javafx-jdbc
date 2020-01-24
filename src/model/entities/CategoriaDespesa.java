package model.entities;

import java.io.Serializable;

public class CategoriaDespesa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String descricao;
	private String catPaiDespesa;
	
	private CategoriaDespesaFilho catFilhoDespesa;
	
	public CategoriaDespesa() {
	}


	public CategoriaDespesa(Integer id, String descricao, String catPaiDespesa, CategoriaDespesaFilho catFilhoDespesa) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.catPaiDespesa = catPaiDespesa;
		this.catFilhoDespesa = catFilhoDespesa;
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


	public String getCatPaiDespesa() {
		return catPaiDespesa;
	}


	public void setCatPaiDespesa(String catPaiDespesa) {
		this.catPaiDespesa = catPaiDespesa;
	}


	public CategoriaDespesaFilho getCatFilhoDespesa() {
		return catFilhoDespesa;
	}

	public void setCatFilhoDespesa(CategoriaDespesaFilho catFilhoDespesa) {
		this.catFilhoDespesa = catFilhoDespesa;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catFilhoDespesa == null) ? 0 : catFilhoDespesa.hashCode());
		result = prime * result + ((catPaiDespesa == null) ? 0 : catPaiDespesa.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CategoriaDespesa other = (CategoriaDespesa) obj;
		if (catFilhoDespesa == null) {
			if (other.catFilhoDespesa != null)
				return false;
		} else if (!catFilhoDespesa.equals(other.catFilhoDespesa))
			return false;
		if (catPaiDespesa == null) {
			if (other.catPaiDespesa != null)
				return false;
		} else if (!catPaiDespesa.equals(other.catPaiDespesa))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "CategoriaDespesa [id=" + id + ", descricao=" + descricao + ", catPaiDespesa=" + catPaiDespesa
				+ ", catFilhoDespesa=" + catFilhoDespesa + "]";
	}


}
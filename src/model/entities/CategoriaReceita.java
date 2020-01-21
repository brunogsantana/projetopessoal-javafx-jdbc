package model.entities;

import java.io.Serializable;

public class CategoriaReceita implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String descricao;

	private String catReceita;

	public CategoriaReceita() {

	}

	public CategoriaReceita(Integer id, String descricao, String catReceita) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.catReceita = catReceita;
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

	public String getCatReceita() {
		return catReceita;
	}

	public void setCatReceita(String catReceita) {
		this.catReceita = catReceita;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catReceita == null) ? 0 : catReceita.hashCode());
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
		CategoriaReceita other = (CategoriaReceita) obj;
		if (catReceita == null) {
			if (other.catReceita != null)
				return false;
		} else if (!catReceita.equals(other.catReceita))
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
		return "CategoriaReceita [id=" + id + ", descricao=" + descricao + ", catReceita=" + catReceita + "]";
	}

}
package model.entities;

import java.io.Serializable;

public class CategoriaDespesaFilho implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String categoriaFilhoDespesa;
	
	public CategoriaDespesaFilho() {
	}
	
	public CategoriaDespesaFilho(Integer id, String categoriaFilhoDespesa) {
		super();
		this.id = id;
		this.categoriaFilhoDespesa = categoriaFilhoDespesa;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategoriaFilhoDespesa() {
		return categoriaFilhoDespesa;
	}

	public void setCategoriaFilhoDespesa(String categoriaFilhoDespesa) {
		this.categoriaFilhoDespesa = categoriaFilhoDespesa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoriaFilhoDespesa == null) ? 0 : categoriaFilhoDespesa.hashCode());
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
		CategoriaDespesaFilho other = (CategoriaDespesaFilho) obj;
		if (categoriaFilhoDespesa == null) {
			if (other.categoriaFilhoDespesa != null)
				return false;
		} else if (!categoriaFilhoDespesa.equals(other.categoriaFilhoDespesa))
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
		return "CategoriaDespesaFilho [id=" + id + ", categoriaFilhoDespesa=" + categoriaFilhoDespesa + "]";
	}

	

}
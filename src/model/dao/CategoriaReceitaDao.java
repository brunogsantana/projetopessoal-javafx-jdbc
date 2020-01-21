package model.dao;

import java.util.List;

import model.entities.CategoriaReceita;

public interface CategoriaReceitaDao {

	void insert(CategoriaReceita obj);
	void update(CategoriaReceita obj);
	void deleteById(Integer id);
	CategoriaReceita findById(Integer id);
	List<CategoriaReceita> findAll();
}

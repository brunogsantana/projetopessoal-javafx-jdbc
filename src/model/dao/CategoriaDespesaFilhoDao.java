package model.dao;

import java.util.List;

import model.entities.CategoriaDespesaFilho;

public interface CategoriaDespesaFilhoDao {

	void insert(CategoriaDespesaFilho obj);
	void update(CategoriaDespesaFilho obj);
	void deleteById(Integer id);
	CategoriaDespesaFilho findById(Integer id);
	List<CategoriaDespesaFilho> findAll();

}

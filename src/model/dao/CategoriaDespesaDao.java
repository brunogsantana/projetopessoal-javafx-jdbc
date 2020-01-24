package model.dao;

import java.util.List;

import model.entities.CategoriaDespesa;

public interface CategoriaDespesaDao {

	void insert(CategoriaDespesa obj);
	void update(CategoriaDespesa obj);
	void deleteById(Integer id);
	CategoriaDespesa findById(Integer id);
	List<CategoriaDespesa> findAll();
	List<CategoriaDespesa> findCatFilhos(String catPai);
}

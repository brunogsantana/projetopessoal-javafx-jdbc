package model.dao;

import java.util.List;

import model.entities.CategoriaDespesa;
import model.entities.Conta;
import model.entities.Despesa;

public interface DespesaDao {

	void insert(Despesa obj);
	void update(Despesa obj);
	void deleteById(Integer id);
	Despesa findById(Integer id);
	List<Despesa> findAll();
	List<Despesa> findByConta(Conta conta);
	List<Despesa> findByCategoriaDespesa(CategoriaDespesa categoriaDespesa);
}

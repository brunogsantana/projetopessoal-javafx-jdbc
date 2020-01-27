package model.dao;

import java.util.List;

import model.entities.Conta;
import model.entities.Transferencia;

public interface TransferenciaDao {

	void insert(Transferencia obj);
	void update(Transferencia obj);
	void deleteById(Integer id);
	Transferencia findById(Integer id);
	List<Transferencia> findAll();
	List<Transferencia> findByConta(Conta conta);

}

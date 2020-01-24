package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DespesaDao;
import model.entities.Conta;
import model.entities.Despesa;

public class DespesaService {
	
	private DespesaDao dao = DaoFactory.createDespesaDao();
	
	public List<Despesa> findAll(){
		return dao.findAll();
	}
	
	public List<Despesa> findByConta(Conta obj){
		return dao.findByConta(obj);
	}
	
	public void saveOrUpdate (Despesa obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Despesa obj) {
		dao.deleteById(obj.getId());
	}
}

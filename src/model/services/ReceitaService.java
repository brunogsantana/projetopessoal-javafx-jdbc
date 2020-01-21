package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ReceitaDao;
import model.entities.Conta;
import model.entities.Receita;

public class ReceitaService {
	
	private ReceitaDao dao = DaoFactory.createReceitaDao();
	
	public List<Receita> findAll(){
		return dao.findAll();
	}
	
	public List<Receita> findByConta(Conta obj){
		return dao.findByConta(obj);
	}
	
	public void saveOrUpdate (Receita obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Receita obj) {
		dao.deleteById(obj.getId());
	}
}

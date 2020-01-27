package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TransferenciaDao;
import model.entities.Conta;
import model.entities.Transferencia;

public class TransferenciaService {
	
	private TransferenciaDao dao = DaoFactory.createTransferenciaDao();
	
	public List<Transferencia> findAll(){
		return dao.findAll();
	}
	
	public List<Transferencia> findByConta(Conta obj){
		return dao.findByConta(obj);
	}
	
	public void saveOrUpdate (Transferencia obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Transferencia obj) {
		dao.deleteById(obj.getId());
	}
}

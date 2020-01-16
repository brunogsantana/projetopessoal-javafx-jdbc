package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.LancamentoDao;
import model.entities.Lancamento;

public class LancamentoService {
	
	private LancamentoDao dao = DaoFactory.createLancamentoDao();
	
	public List<Lancamento> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate (Lancamento obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Lancamento obj) {
		dao.deleteById(obj.getId());
	}
}

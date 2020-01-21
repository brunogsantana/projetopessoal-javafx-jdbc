package model.services;

import java.util.List;

import model.dao.ContaDao;
import model.dao.DaoFactory;
import model.entities.Conta;

public class ContaService {

	private ContaDao dao = DaoFactory.createContaDao();

	public List<Conta> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Conta obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Conta obj) {
		dao.deleteById(obj.getId());
	}
}

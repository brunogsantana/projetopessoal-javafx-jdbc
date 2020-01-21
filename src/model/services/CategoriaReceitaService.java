package model.services;

import java.util.List;

import model.dao.CategoriaReceitaDao;
import model.dao.DaoFactory;
import model.entities.CategoriaReceita;

public class CategoriaReceitaService {

	private CategoriaReceitaDao dao = DaoFactory.createCategoriaReceitaDao();

	public List<CategoriaReceita> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(CategoriaReceita obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(CategoriaReceita obj) {
		dao.deleteById(obj.getId());
	}
}

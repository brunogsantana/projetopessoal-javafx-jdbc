package model.services;

import java.util.List;

import model.dao.CategoriaDespesaFilhoDao;
import model.dao.DaoFactory;
import model.entities.CategoriaDespesaFilho;

public class CategoriaDespesaFilhoService {

	private CategoriaDespesaFilhoDao dao = DaoFactory.createCategoriaDespesaFilhoDao();

	public List<CategoriaDespesaFilho> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(CategoriaDespesaFilho obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(CategoriaDespesaFilho obj) {
		dao.deleteById(obj.getId());
	}
}

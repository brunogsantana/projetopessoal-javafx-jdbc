package model.services;

import java.util.List;

import model.dao.CategoriaDespesaDao;
import model.dao.DaoFactory;
import model.entities.CategoriaDespesa;

public class CategoriaDespesaService {

	private CategoriaDespesaDao dao = DaoFactory.createCategoriaDespesaDao();

	public List<CategoriaDespesa> findAll() {
		return dao.findAll();
	}
	
	public List<CategoriaDespesa> findCatFilhos(String catPai) {
		return dao.findCatFilhos(catPai);
	}

	public void saveOrUpdate(CategoriaDespesa obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(CategoriaDespesa obj) {
		dao.deleteById(obj.getId());
	}
}

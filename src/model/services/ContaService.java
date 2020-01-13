package model.services;

import java.util.List;

import model.dao.ContaDao;
import model.dao.DaoFactory;
import model.entities.Conta;

public class ContaService {
	
	private ContaDao dao = DaoFactory.createContaDao();
	
	public List<Conta> findAll(){
		return dao.findAll();
	}
}

package model.dao;

import db.DB;
import model.dao.impl.ContaDaoJDBC;
import model.dao.impl.LancamentoDaoJDBC;

public class DaoFactory {

	public static LancamentoDao createLancamentoDao() {
		return new LancamentoDaoJDBC(DB.getConnection());
	}
	
	public static ContaDao createContaDao(){
		return new ContaDaoJDBC(DB.getConnection());
	}
}

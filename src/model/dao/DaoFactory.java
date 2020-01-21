package model.dao;

import db.DB;
import model.dao.impl.CategoriaReceitaDaoJDBC;
import model.dao.impl.ContaDaoJDBC;
import model.dao.impl.ReceitaDaoJDBC;

public class DaoFactory {

	public static ReceitaDao createReceitaDao() {
		return new ReceitaDaoJDBC(DB.getConnection());
	}
	
	public static ContaDao createContaDao(){
		return new ContaDaoJDBC(DB.getConnection());
	}
	
	public static CategoriaReceitaDao createCategoriaReceitaDao(){
		return new CategoriaReceitaDaoJDBC(DB.getConnection());
	}
}

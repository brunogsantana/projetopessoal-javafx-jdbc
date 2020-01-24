package model.dao;

import db.DB;
import model.dao.impl.CategoriaDespesaDaoJDBC;
import model.dao.impl.CategoriaDespesaFilhoDaoJDBC;
import model.dao.impl.CategoriaReceitaDaoJDBC;
import model.dao.impl.ContaDaoJDBC;
import model.dao.impl.DespesaDaoJDBC;
import model.dao.impl.ReceitaDaoJDBC;

public class DaoFactory {

	public static ReceitaDao createReceitaDao() {
		return new ReceitaDaoJDBC(DB.getConnection());
	}

	public static DespesaDao createDespesaDao() {
		return new DespesaDaoJDBC(DB.getConnection());
	}

	public static ContaDao createContaDao() {
		return new ContaDaoJDBC(DB.getConnection());
	}

	public static CategoriaDespesaDao createCategoriaDespesaDao() {
		return new CategoriaDespesaDaoJDBC(DB.getConnection());
	}
	
	public static CategoriaDespesaFilhoDao createCategoriaDespesaFilhoDao() {
		return new CategoriaDespesaFilhoDaoJDBC(DB.getConnection());
	}

	public static CategoriaReceitaDao createCategoriaReceitaDao() {
		return new CategoriaReceitaDaoJDBC(DB.getConnection());
	}
}

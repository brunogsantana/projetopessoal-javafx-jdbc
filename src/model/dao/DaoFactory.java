package model.dao;

import db.DB;
import model.dao.impl.ContaDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static ContaDao createContaDao(){
		return new ContaDaoJDBC(DB.getConnection());
	}
}

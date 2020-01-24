package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.CategoriaDespesaFilhoDao;
import model.entities.CategoriaDespesaFilho;

public class CategoriaDespesaFilhoDaoJDBC implements CategoriaDespesaFilhoDao {

	private Connection conn;

	public CategoriaDespesaFilhoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public CategoriaDespesaFilho findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM categoriaDespesaFilho WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				CategoriaDespesaFilho obj = new CategoriaDespesaFilho();

				obj.setId(rs.getInt("Id"));
				obj.setCategoriaFilhoDespesa(rs.getString("CategoriaFilhoDespesa"));
				
				return obj;

			}

			return null;

		} catch (SQLException e) {

			throw new DbException(e.getMessage());

		} finally {

			DB.closeStatement(st);

			DB.closeResultSet(rs);

		}

	}

	@Override

	public List<CategoriaDespesaFilho> findAll() {

		PreparedStatement st = null;

		ResultSet rs = null;

		try {

			st = conn.prepareStatement("SELECT * FROM CategoriaDespesaFilho ORDER BY Id");

			rs = st.executeQuery();

			List<CategoriaDespesaFilho> list = new ArrayList<>();

			while (rs.next()) {

				CategoriaDespesaFilho obj = new CategoriaDespesaFilho();

				obj.setId(rs.getInt("Id"));
				obj.setCategoriaFilhoDespesa(rs.getString("CategoriaFilhoDespesa"));

				list.add(obj);

			}

			return list;

		} catch (SQLException e) {

			throw new DbException(e.getMessage());

		} finally {

			DB.closeStatement(st);

			DB.closeResultSet(rs);

		}

	}

	@Override

	public void insert(CategoriaDespesaFilho obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("INSERT INTO categoriaDespesaFilho "

					+ "(CategoriaFilhoDespesa)"

					+ "VALUES " + "(?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getCategoriaFilhoDespesa());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {

				ResultSet rs = st.getGeneratedKeys();

				if (rs.next()) {

					int id = rs.getInt(1);

					obj.setId(id);

				}

				DB.closeResultSet(rs);

			} else {

				throw new DbException("Unexpected error! No rows affected!");

			}

		} catch (SQLException e) {

			throw new DbException(e.getMessage());

		} finally {

			DB.closeStatement(st);

		}

	}

	@Override

	public void update(CategoriaDespesaFilho obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("UPDATE categoriaDespesaFilho "

					+ "SET CategoriaFilhoDespesa = ?"

					+ "WHERE Id = ?");

			st.setString(1, obj.getCategoriaFilhoDespesa());
			st.setInt(2, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {

			throw new DbException(e.getMessage());

		} finally {

			DB.closeStatement(st);

		}

	}

	@Override

	public void deleteById(Integer id) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("DELETE FROM CategoriaDespesaFilho WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {

			throw new DbIntegrityException(e.getMessage());

		} finally {

			DB.closeStatement(st);

		}

	}

}
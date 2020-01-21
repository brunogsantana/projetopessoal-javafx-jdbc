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
import model.dao.CategoriaReceitaDao;
import model.entities.CategoriaReceita;

public class CategoriaReceitaDaoJDBC implements CategoriaReceitaDao {

	private Connection conn;

	public CategoriaReceitaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public CategoriaReceita findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM CategoriaReceita WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				CategoriaReceita obj = new CategoriaReceita();

				obj.setId(rs.getInt("Id"));

				obj.setDescricao(rs.getString("Descricao"));

				obj.setCatReceita(rs.getString("CategoriaReceita"));

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

	public List<CategoriaReceita> findAll() {

		PreparedStatement st = null;

		ResultSet rs = null;

		try {

			st = conn.prepareStatement("SELECT * FROM CategoriaReceita ORDER BY Descricao");

			rs = st.executeQuery();

			List<CategoriaReceita> list = new ArrayList<>();

			while (rs.next()) {

				CategoriaReceita obj = new CategoriaReceita();

				obj.setId(rs.getInt("Id"));

				obj.setDescricao(rs.getString("Descricao"));

				obj.setCatReceita(rs.getString("CategoriaReceita"));

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

	public void insert(CategoriaReceita obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("INSERT INTO CategoriaReceita "

					+ "(Descricao, CategoriaReceita)"

					+ "VALUES " + "(?,?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescricao());

			st.setString(2, obj.getCatReceita());

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

	public void update(CategoriaReceita obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("UPDATE CategoriaReceita "

					+ "SET Descricao = ?, CategoriaReceita = ?"

					+ "WHERE Id = ?");

			st.setString(1, obj.getDescricao());

			st.setString(2, obj.getCatReceita());

			st.setInt(3, obj.getId());

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

			st = conn.prepareStatement("DELETE FROM CategoriaReceita WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {

			throw new DbIntegrityException(e.getMessage());

		} finally {

			DB.closeStatement(st);

		}

	}

}
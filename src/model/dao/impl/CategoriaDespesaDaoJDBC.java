package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.CategoriaDespesaDao;
import model.entities.CategoriaDespesa;
import model.entities.CategoriaDespesaFilho;
import model.entities.Receita;

public class CategoriaDespesaDaoJDBC implements CategoriaDespesaDao {

	private Connection conn;

	public CategoriaDespesaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public CategoriaDespesa findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn
					.prepareStatement("SELECT categoriaDespesa.*,categoriaDespesa.CategoriaFilhoDespesa as DepCatFilho "
							+ "FROM categoriaDespesa INNER JOIN categoriaDespesaFilho "
							+ "ON categoriaDespesa.CategoriaFilhoDespesaId  = categoriaDespesaFilho.Id "
							+ "WHERE categoriaDespesa.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				CategoriaDespesaFilho dep = instantiateCategoriaDespesaFilho(rs);
				CategoriaDespesa obj = instantiateCategoriaDespesa(rs, dep);
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

	private CategoriaDespesa instantiateCategoriaDespesa(ResultSet rs, CategoriaDespesaFilho dep) throws SQLException {
		CategoriaDespesa obj = new CategoriaDespesa();
		obj.setId(rs.getInt("Id"));
		obj.setDescricao(rs.getString("Descricao"));
		obj.setCatPaiDespesa(rs.getString("CategoriaPaiDespesa"));
		obj.setCatFilhoDespesa(dep);
		return obj;
	}

	private CategoriaDespesaFilho instantiateCategoriaDespesaFilho(ResultSet rs) throws SQLException {
		CategoriaDespesaFilho dep = new CategoriaDespesaFilho();
		dep.setId(rs.getInt("CategoriaFilhoDespesaId"));
		dep.setCategoriaFilhoDespesa(rs.getString("DepCatFilho"));
		return dep;
	}

	@Override
	public List<CategoriaDespesa> findAll() {

		PreparedStatement st = null;

		ResultSet rs = null;

		try {

			st = conn.prepareStatement(
					"SELECT categoriaDespesa.*, categoriaDespesa.CategoriaFilhoDespesa as DepCatFilho "
							+ "FROM categoriaDespesa INNER JOIN categoriaDespesaFilho "
							+ "ON categoriaDespesa.CategoriaFilhoDespesaId  = categoriaDespesaFilho.Id "
							+ "ORDER BY Descricao");

			rs = st.executeQuery();

			List<CategoriaDespesa> list = new ArrayList<>();
			Map<Integer, CategoriaDespesaFilho> map = new HashMap<>();
			while (rs.next()) {
				CategoriaDespesaFilho dep = map.get(rs.getInt("CategoriaFilhoDespesaId"));

				if (dep == null) {
					dep = instantiateCategoriaDespesaFilho(rs);
					map.put(rs.getInt("CategoriaFilhoDespesaId"), dep);
				}
				CategoriaDespesa obj = instantiateCategoriaDespesa(rs, dep);
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

	public void insert(CategoriaDespesa obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("INSERT INTO categoriaDespesa "

					+ "(Descricao, CategoriaPaiDespesa, CategoriaFilhoDespesa, CategoriaFilhoDespesaId)"

					+ "VALUES " + "(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescricao());
			st.setString(2, obj.getCatPaiDespesa());
			st.setString(3, obj.getCatFilhoDespesa().getCategoriaFilhoDespesa());
			st.setInt(4, obj.getCatFilhoDespesa().getId());

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

	public void update(CategoriaDespesa obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("UPDATE categoriaDespesa "

					+ "SET Descricao = ?, CategoriaPaiDespesa = ?, CategoriaFilhoDespesa = ?, CategoriaFilhoDespesaId = ? "

					+ "WHERE Id = ?");

			st.setString(1, obj.getDescricao());
			st.setString(2, obj.getCatPaiDespesa());
			st.setString(3, obj.getCatFilhoDespesa().getCategoriaFilhoDespesa());
			st.setInt(4, obj.getCatFilhoDespesa().getId());
			st.setInt(5, obj.getId());

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

			st = conn.prepareStatement("DELETE FROM categoriaDespesa WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {

			throw new DbIntegrityException(e.getMessage());

		} finally {

			DB.closeStatement(st);

		}

	}

	@Override
	public List<CategoriaDespesa> findCatFilhos(String catPai) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT categoriaDespesa.*, categoriaDespesa.CategoriaFilhoDespesa as DepCatFilho "
							+ "FROM categoriaDespesa INNER JOIN categoriaDespesaFilho "
							+ "ON categoriaDespesa.CategoriaFilhoDespesaId  = categoriaDespesaFilho.Id "
							+ "WHERE categoriaDespesa.CategoriaPaiDespesa = ? " + "ORDER BY Descricao");
			st.setString(1, catPai);
			rs = st.executeQuery();

			List<CategoriaDespesa> list = new ArrayList<>();
			Map<Integer, CategoriaDespesaFilho> map = new HashMap<>();
			while (rs.next()) {
				CategoriaDespesaFilho dep = map.get(rs.getInt("CategoriaFilhoDespesaId"));

				if (dep == null) {
					dep = instantiateCategoriaDespesaFilho(rs);
					map.put(rs.getInt("CategoriaFilhoDespesaId"), dep);
				}
				CategoriaDespesa obj = instantiateCategoriaDespesa(rs, dep);
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

}
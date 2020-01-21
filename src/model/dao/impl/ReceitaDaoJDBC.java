package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.ReceitaDao;
import model.entities.CategoriaReceita;
import model.entities.Conta;
import model.entities.Receita;

public class ReceitaDaoJDBC implements ReceitaDao {

	private Connection conn;

	public ReceitaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Receita obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO receita "
					+ "(DataOriginalReceita, DataConcluidaReceita, Descricao, CategoriaReceita, StatusReceita, Valor, Obs, ContaId, CategoriaId) "
					+ "VALUES " + "( ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataOriginalReceita().getTime()));
			st.setDate(2, new java.sql.Date(obj.getDataConcluidaReceita().getTime()));
			st.setString(3, obj.getDescricao());
			st.setString(4, obj.getCategoriaReceita().getCatReceita());
			st.setString(5, obj.getStatusReceita());
			st.setDouble(6, obj.getValor());
			st.setString(7, obj.getObs());
			st.setInt(8, obj.getConta().getId());
			st.setInt(9, obj.getCategoriaReceita().getId());

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
	public void update(Receita obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE receita "
					+ "SET DataOriginalReceita = ?, DataConcluidaReceita = ?, Descricao = ?, CategoriaReceita = ?, StatusReceita = ?, Valor = ?, Obs = ?, ContaId = ?, CategoriaId = ? "
					+ "WHERE Id = ?");

			st.setDate(1, new java.sql.Date(obj.getDataOriginalReceita().getTime()));
			st.setDate(2, new java.sql.Date(obj.getDataConcluidaReceita().getTime()));
			st.setString(3, obj.getDescricao());
			st.setString(4, obj.getCategoriaReceita().getCatReceita());
			st.setString(5, obj.getStatusReceita());
			st.setDouble(6, obj.getValor());
			st.setString(7, obj.getObs());
			st.setInt(8, obj.getConta().getId());
			st.setInt(9, obj.getCategoriaReceita().getId());
			st.setInt(10, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM receita WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Receita findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT receita.*,conta.Name as DepName " + "FROM receita INNER JOIN conta "
					+ "ON receita.ContaId = conta.Id " + "WHERE receita.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Conta dep = instantiateConta(rs);
				CategoriaReceita dep1 = instantiateCategoriaReceita(rs);
				Receita obj = instantiateReceita(rs, dep, dep1);
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

	private Receita instantiateReceita(ResultSet rs, Conta dep, CategoriaReceita dep1) throws SQLException {
		Receita obj = new Receita();
		obj.setId(rs.getInt("Id"));
		obj.setDataOriginalReceita(new java.util.Date(rs.getTimestamp("DataOriginalReceita").getTime()));
		obj.setDataConcluidaReceita(new java.util.Date(rs.getTimestamp("DataConcluidaReceita").getTime()));
		obj.setDescricao(rs.getString("Descricao"));
		obj.setCategoriaReceita(dep1);
		obj.setStatusReceita(rs.getString("StatusReceita"));
		obj.setValor(rs.getDouble("Valor"));
		obj.setObs(rs.getString("Obs"));
		obj.setConta(dep);
		return obj;
	}

	private Conta instantiateConta(ResultSet rs) throws SQLException {
		Conta dep = new Conta();
		dep.setId(rs.getInt("ContaId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	private CategoriaReceita instantiateCategoriaReceita(ResultSet rs) throws SQLException {
		CategoriaReceita dep1 = new CategoriaReceita();
		dep1.setId(rs.getInt("CategoriaId"));
		dep1.setCatReceita(rs.getString("CategoriaReceita"));
		return dep1;
	}

	@Override
	public List<Receita> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("" + "SELECT receita.*, conta.name as DepName "
					+ "FROM receita INNER JOIN conta " + "ON receita.ContaId = conta.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Receita> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			Map<Integer, CategoriaReceita> map1 = new HashMap<>();

			while (rs.next()) {

				Conta dep = map.get(rs.getInt("ContaId"));
				CategoriaReceita dep1 = map1.get(rs.getInt("CategoriaId"));

				if (dep == null) {
					dep = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), dep);
				}

				if (dep1 == null) {
					dep1 = instantiateCategoriaReceita(rs);
					map1.put(rs.getInt("CategoriaId"), dep1);
				}

				Receita obj = instantiateReceita(rs, dep, dep1);
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
	public List<Receita> findByConta(Conta conta) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT receita.*,conta.Name as DepName " + "FROM receita INNER JOIN conta "

					+ "ON receita.ContaId = conta.Id " + "WHERE ContaId = ? " + "ORDER BY Name");

			st.setInt(1, conta.getId());

			rs = st.executeQuery();

			List<Receita> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			Map<Integer, CategoriaReceita> map1 = new HashMap<>();

			while (rs.next()) {

				Conta dep = map.get(rs.getInt("ContaId"));
				CategoriaReceita dep1 = map1.get(rs.getInt("CategoriaId"));

				if (dep == null) {
					dep = instantiateConta(rs);
					dep1 = instantiateCategoriaReceita(rs);
					map.put(rs.getInt("ContaId"), dep);
				}

				if (dep1 == null) {
					dep1 = instantiateCategoriaReceita(rs);
					map1.put(rs.getInt("CategoriaId"), dep1);
				}
				
				Receita obj = instantiateReceita(rs, dep, dep1);
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
	public List<Receita> findByCategoriaReceita(CategoriaReceita categoriaReceita) {
		// TODO Auto-generated method stub
		return null;
	}
}

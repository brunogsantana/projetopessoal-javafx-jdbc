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
					+ "(DataOriginalReceita, DataConcluidaReceita, Descricao, CodigoCategoriaReceita, CategoriaReceita, StatusReceita, Valor, Obs, ContaId) "
					+ "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataOriginalReceita().getTime()));
			st.setDate(2, new java.sql.Date(obj.getDataConcluidaReceita().getTime()));
			st.setString(3, obj.getDescricao());
			st.setInt(4, obj.getCodigoCategoriaReceita());
			st.setString(5, obj.getCategoriaReceita());
			st.setString(6, obj.getStatusReceita());
			st.setDouble(7, obj.getValor());
			st.setString(8, obj.getObs());
			st.setInt(9, obj.getConta().getId());

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
					+ "SET DataOriginalReceita = ?, DataConcluidaReceita = ?, Descricao = ?, CodigoCategoriaReceita = ?, CategoriaReceita = ?, StatusReceita = ?, Valor = ?, Obs = ?, ContaId = ? "
					+ "WHERE Id = ?");

			st.setDate(1, new java.sql.Date(obj.getDataOriginalReceita().getTime()));
			st.setDate(2, new java.sql.Date(obj.getDataConcluidaReceita().getTime()));
			st.setString(3, obj.getDescricao());
			st.setInt(4, obj.getCodigoCategoriaReceita());
			st.setString(5, obj.getCategoriaReceita());
			st.setString(6, obj.getStatusReceita());
			st.setDouble(7, obj.getValor());
			st.setString(8, obj.getObs());
			st.setInt(9, obj.getConta().getId());
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
				Receita obj = instantiateReceita(rs, dep);
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

	private Receita instantiateReceita(ResultSet rs, Conta dep) throws SQLException {
		Receita obj = new Receita();
		obj.setId(rs.getInt("Id"));
//		obj.setDataOriginalReceita(rs.getDate("DataOriginalReceita"));
//		obj.setDataConcluidaReceita(rs.getDate("DataConcluidaReceita"));
		obj.setDataOriginalReceita(new java.util.Date(rs.getTimestamp("DataOriginalReceita").getTime()));
		obj.setDataConcluidaReceita(new java.util.Date(rs.getTimestamp("DataConcluidaReceita").getTime()));
		obj.setDescricao(rs.getString("Descricao"));
		obj.setCodigoCategoriaReceita(rs.getInt("CodigoCategoriaReceita"));
		obj.setCategoriaReceita(rs.getString("CategoriaReceita"));
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

	@Override
	public List<Receita> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT receita.*,conta.Name as DepName " + "FROM receita INNER JOIN conta "
					+ "ON receita.ContaId = conta.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Receita> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();

			while (rs.next()) {

				Conta dep = map.get(rs.getInt("ContaId"));

				if (dep == null) {
					dep = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), dep);
				}

				Receita obj = instantiateReceita(rs, dep);
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

			while (rs.next()) {

				Conta dep = map.get(rs.getInt("ContaId"));

				if (dep == null) {
					dep = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), dep);
				}

				Receita obj = instantiateReceita(rs, dep);
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

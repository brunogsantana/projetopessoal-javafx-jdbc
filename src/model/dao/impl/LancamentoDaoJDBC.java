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
import model.dao.LancamentoDao;
import model.entities.Conta;
import model.entities.Lancamento;

public class LancamentoDaoJDBC implements LancamentoDao {

	private Connection conn;
	
	public LancamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Lancamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, ContaId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getConta().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Lancamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, ContaId = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getConta().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Lancamento findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,conta.Name as DepName "
					+ "FROM seller INNER JOIN conta "
					+ "ON seller.ContaId = conta.Id "
					+ "WHERE seller.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Conta dep = instantiateConta(rs);
				Lancamento obj = instantiateLancamento(rs, dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Lancamento instantiateLancamento(ResultSet rs, Conta dep) throws SQLException {
		Lancamento obj = new Lancamento();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
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
	public List<Lancamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,conta.Name as DepName "
					+ "FROM seller INNER JOIN conta "
					+ "ON seller.ContaId = conta.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Lancamento> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			
			while (rs.next()) {
				
				Conta dep = map.get(rs.getInt("ContaId"));
				
				if (dep == null) {
					dep = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), dep);
				}
				
				Lancamento obj = instantiateLancamento(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Lancamento> findByConta(Conta conta) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,conta.Name as DepName "
					+ "FROM seller INNER JOIN conta "
					+ "ON seller.ContaId = conta.Id "
					+ "WHERE ContaId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, conta.getId());
			
			rs = st.executeQuery();
			
			List<Lancamento> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			
			while (rs.next()) {
				
				Conta dep = map.get(rs.getInt("ContaId"));
				
				if (dep == null) {
					dep = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), dep);
				}
				
				Lancamento obj = instantiateLancamento(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}

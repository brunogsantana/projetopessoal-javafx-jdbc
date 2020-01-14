package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ContaDao;
import model.entities.Conta;
import model.entities.enums.TipoConta;

public class ContaDaoJDBC implements ContaDao {

	private Connection conn;
	
	public ContaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Conta findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Conta WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Conta obj = new Conta();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setCpf(rs.getString("CPF"));
				obj.setTipoConta(rs.getString("TipoConta"));
				obj.setBanco(rs.getString("Banco"));
				obj.setNumeroBanco(rs.getInt("NumeroBanco"));
				obj.setNumeroAgencia(rs.getInt("NumeroAgencia"));
				obj.setNumeroConta(rs.getInt("NumeroConta"));
				obj.setDataCadastro(rs.getDate("DataCadastro"));
				obj.setSaldoAtual(rs.getDouble("SaldoAtual"));
				obj.setFavorita(rs.getBoolean("Favorita"));
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

	@Override
	public List<Conta> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Conta ORDER BY Name");
			rs = st.executeQuery();

			List<Conta> list = new ArrayList<>();

			while (rs.next()) {
				Conta obj = new Conta();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setCpf(rs.getString("CPF"));
				obj.setTipoConta(rs.getString("TipoConta"));
				obj.setBanco(rs.getString("Banco"));
				obj.setNumeroBanco(rs.getInt("NumeroBanco"));
				obj.setNumeroAgencia(rs.getInt("NumeroAgencia"));
				obj.setNumeroConta(rs.getInt("NumeroConta"));
				obj.setDataCadastro(rs.getDate("DataCadastro"));
				obj.setSaldoAtual(rs.getDouble("SaldoAtual"));
				obj.setFavorita(rs.getBoolean("Favorita"));
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
	public void insert(Conta obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO Conta " +
				"(Name) " +
				"VALUES " +
				"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
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
	public void update(Conta obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE Conta " +
				"WHERE Id = ?" +
								
				"SET Name = ? " +
				"SET CPF = ? " +
				"SET Banco = ? " +

				
				"WHERE Id = ?");

			st.setInt(1, obj.getId());
			st.setString(2, obj.getName());
			st.setString(3, obj.getCpf());
			st.setString(4,obj.getTipoConta());
			st.setString(5, obj.getBanco());
			st.setInt(6,obj.getNumeroBanco());
			st.setInt(7,obj.getNumeroAgencia());
			st.setInt(8, obj.getNumeroConta());
			st.setDate(9, (Date) obj.getDataCadastro());
			st.setDouble(10,obj.getSaldoAtual());
			st.setBoolean(11, obj.getFavorita());

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
			st = conn.prepareStatement(
				"DELETE FROM Conta WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
}

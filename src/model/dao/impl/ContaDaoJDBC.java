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
import model.dao.ContaDao;
import model.entities.Conta;

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
			st = conn.prepareStatement("SELECT * FROM Conta WHERE Id = ?");
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
				obj.setSaldoInicial(rs.getDouble("SaldoInicial"));
				obj.setSaldoAtual(rs.getDouble("SaldoAtual"));
				obj.setFavorita(rs.getBoolean("Favorita"));
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
	public List<Conta> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM Conta ORDER BY Name");
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
				obj.setSaldoInicial(rs.getDouble("SaldoInicial"));
				obj.setSaldoAtual(rs.getDouble("SaldoAtual"));
				obj.setFavorita(rs.getBoolean("Favorita"));
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
	public void insert(Conta obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO Conta "
					+ "(Name, CPF, TipoConta, Banco, NumeroBanco, NumeroAgencia, NumeroConta, DataCadastro, SaldoInicial, Favorita)"
					+ "VALUES " + "(?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getCpf());
			st.setString(3, obj.getTipoConta());
			st.setString(4, obj.getBanco());
			st.setInt(5, obj.getNumeroBanco());
			st.setInt(6, obj.getNumeroAgencia());
			st.setInt(7, obj.getNumeroConta());
			st.setDate(8, new java.sql.Date(obj.getDataCadastro().getTime()));
			st.setDouble(9, obj.getSaldoInicial());
//			st.setDouble(10, obj.getSaldoAtual());
			st.setBoolean(10, obj.getFavorita());

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
	public void update(Conta obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Conta "
					+ "SET Name = ?, CPF = ?, TipoConta = ?, Banco = ?, NumeroBanco = ?, NumeroAgencia = ?, NumeroConta = ?, DataCadastro = ?, SaldoInicial = ?, SaldoAtual = ?, Favorita = ? "
					+ "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getCpf());
			st.setString(3, obj.getTipoConta());
			st.setString(4, obj.getBanco());
			st.setInt(5, obj.getNumeroBanco());
			st.setInt(6, obj.getNumeroAgencia());
			st.setInt(7, obj.getNumeroConta());
			st.setDate(8, new java.sql.Date(obj.getDataCadastro().getTime()));
			st.setDouble(9, obj.getSaldoInicial());
			st.setDouble(10, obj.getSaldoAtual());
			st.setBoolean(11, obj.getFavorita());
			st.setInt(12, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM Conta WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}
}

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
import model.dao.TransferenciaDao;
import model.entities.Conta;
import model.entities.Transferencia;

public class TransferenciaDaoJDBC implements TransferenciaDao {

	private Connection conn;

	public TransferenciaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Transferencia obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO transferencia "
					+ "(DataOriginalTransferencia, DataConcluidaTransferencia, Descricao, ContaOrigemId, ContaDestinoId, Valor, CustoTransferencia, Obs) "
					+ "VALUES " + "( ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataOriginalTransferencia().getTime()));
			st.setDate(2, new java.sql.Date(obj.getDataConcluidaTransferencia().getTime()));
			st.setString(3, obj.getDescricao());
			st.setInt(4, obj.getContaOrigem().getId());
			st.setInt(5, obj.getContaDestino().getId());
			st.setDouble(6, obj.getValor());
			st.setDouble(7, obj.getCustoTransferencia());
			st.setString(8, obj.getObs());

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
	public void update(Transferencia obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE transferencia "
					+ "SET DataOriginalTransferencia = ?, DataConcluidaTransferencia = ?, Descricao = ?, ContaOrigem = ?, ContaDestino = ?, Valor = ?, CustoTransferencia = ?, Obs = ? "
					+ "WHERE Id = ?");

			st.setDate(1, new java.sql.Date(obj.getDataOriginalTransferencia().getTime()));
			st.setDate(2, new java.sql.Date(obj.getDataConcluidaTransferencia().getTime()));
			st.setString(3, obj.getDescricao());
			st.setInt(4, obj.getContaOrigem().getId());
			st.setInt(5, obj.getContaDestino().getId());
			st.setDouble(6, obj.getValor());
			st.setDouble(7, obj.getCustoTransferencia());
			st.setString(8, obj.getObs());
			st.setInt(9, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM transferencia WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Transferencia findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT transferencia.*,conta.Name as DepName " + "FROM transferencia INNER JOIN conta "
							+ "ON transferencia.ContaOrigemId = conta.Id " + "WHERE transferencia.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Conta dep = instantiateContaOrigem(rs);
				Conta dep1 = instantiateContaDestino(rs);
				Transferencia obj = instantiateTransferencia(rs, dep,dep1);
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

	private Transferencia instantiateTransferencia(ResultSet rs, Conta dep, Conta dep1)
			throws SQLException {
		Transferencia obj = new Transferencia();
		obj.setId(rs.getInt("Id"));
		obj.setDataOriginalTransferencia(new java.util.Date(rs.getTimestamp("DataOriginalTransferencia").getTime()));
		obj.setDataConcluidaTransferencia(new java.util.Date(rs.getTimestamp("DataConcluidaTransferencia").getTime()));
		obj.setDescricao(rs.getString("Descricao"));
		obj.setContaOrigem(dep);
		obj.setContaDestino(dep1);
		obj.setValor(rs.getDouble("Valor"));
		obj.setCustoTransferencia(rs.getDouble("CustoTransferencia"));
		obj.setObs(rs.getString("Obs"));
		return obj;
	}

	private Conta instantiateContaOrigem(ResultSet rs) throws SQLException {
		Conta dep = new Conta();
		dep.setId(rs.getInt("ContaOrigemId"));
		dep.setName(rs.getString("DepOrigemName"));
		return dep;
	}
	
	private Conta instantiateContaDestino(ResultSet rs) throws SQLException {
		Conta dep1 = new Conta();
		dep1.setId(rs.getInt("ContaDestinoId"));
	//	dep1.setName(rs.getString("Name"));
		return dep1;
	}

	@Override
	public List<Transferencia> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"" + "SELECT transferencia.*, conta.name as DepOrigemName " + "FROM transferencia INNER JOIN conta "
							+ "ON transferencia.ContaOrigemId = conta.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Transferencia> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			Map<Integer, Conta> map1 = new HashMap<>();

			while (rs.next()) {

				Conta dep = map.get(rs.getInt("ContaOrigemId"));
				Conta dep1 = map1.get(rs.getInt("ContaDestinoId"));
				
				if (dep == null) {
					dep = instantiateContaOrigem(rs);
					map.put(rs.getInt("ContaOrigemId"), dep);
				}
		
				if (dep1 == null) {
					dep1 = instantiateContaDestino(rs);
					map1.put(rs.getInt("ContaDestinoId"), dep1);
				}
				
				Transferencia obj = instantiateTransferencia(rs, dep, dep1);
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
	public List<Transferencia> findByConta(Conta conta) {
		return null;
//		PreparedStatement st = null;
//		ResultSet rs = null;
//		try {
//			st = conn.prepareStatement(
//					"SELECT transferencia.*,conta.Name as DepName " + "FROM transferencia INNER JOIN conta "
//
//							+ "ON transferencia.ContaId = conta.Id " + "WHERE ContaId = ? " + "ORDER BY Name");
//
//			st.setInt(1, conta.getId());
//
//			rs = st.executeQuery();
//
//			List<Transferencia> list = new ArrayList<>();
//			Map<Integer, Conta> map = new HashMap<>();
//
//			while (rs.next()) {
//
//				Conta dep = map.get(rs.getInt("ContaId"));
//	
//				if (dep == null) {
//					dep = instantiateConta(rs);
//					map.put(rs.getInt("ContaId"), dep);
//				}
//
//				
//				Transferencia obj = instantiateTransferencia(rs, dep);
//				list.add(obj);
//			}
//			return list;
//		} catch (SQLException e) {
//			throw new DbException(e.getMessage());
//		} finally {
//			DB.closeStatement(st);
//			DB.closeResultSet(rs);
//		}
	}


}

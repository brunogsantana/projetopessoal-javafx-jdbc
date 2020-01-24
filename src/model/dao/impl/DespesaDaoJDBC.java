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
import model.dao.DespesaDao;
import model.entities.CategoriaDespesa;
import model.entities.CategoriaDespesaFilho;
import model.entities.Conta;
import model.entities.Despesa;

public class DespesaDaoJDBC implements DespesaDao {

	private Connection conn;

	public DespesaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Despesa obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO despesa "
					+ "(DataOriginalDespesa, DataConcluidaDespesa, Descricao, CategoriaPaiDespesa, CategoriaFilhoDespesa, StatusDespesa, MeioPagamento, QtdParcela, ValorParcela, DataPagamentoParcela, Valor, Obs, ContaId, CategoriaId, CategoriaFilhoDespesaId) "
					+ "VALUES " + "( ?, ?,?,?,?,?, ?, ?, ?,?, ?, ?, ?,?,?)", Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataOriginalDespesa().getTime()));
			st.setDate(2, new java.sql.Date(obj.getDataConcluidaDespesa().getTime()));
			st.setString(3, obj.getDescricao());
			st.setString(4, obj.getCategoriaDespesaPai().getCatPaiDespesa());
			st.setString(5, obj.getCategoriaDespesaPai().getCatFilhoDespesa().getCategoriaFilhoDespesa());
			st.setString(6, obj.getStatusDespesa());
			st.setString(7, obj.getMeioPagamento());
			st.setInt(8, obj.getQtdParcela());
			st.setDouble(9, obj.getValorParcela());
			st.setDate(10, new java.sql.Date(obj.getDataPagamentoParcela().getTime()));
			st.setDouble(11, obj.getValor());
			st.setString(12, obj.getObs());
			st.setInt(13, obj.getConta().getId());
			st.setInt(14, obj.getCategoriaDespesaPai().getId());
			st.setInt(15, obj.getCategoriaDespesaFilho().getId());

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
	public void update(Despesa obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE despesa "
					+ "SET DataOriginalDespesa = ?, DataConcluidaDespesa = ?, Descricao = ?, CategoriaPaiDespesa = ?, CategoriaFilhoDespesa = ?, StatusDespesa = ?, MeioPagamento = ?, QtdParcela = ?, ValorParcela = ?, DataPagamentoParcela = ?, Valor = ?, Obs = ?, ContaId = ?, CategoriaId = ?, CategoriaFilhoDespesaId = ? "
					+ "WHERE Id = ?");

			st.setDate(1, new java.sql.Date(obj.getDataOriginalDespesa().getTime()));
			st.setDate(2, new java.sql.Date(obj.getDataConcluidaDespesa().getTime()));
			st.setString(3, obj.getDescricao());
			st.setString(4, obj.getCategoriaDespesaPai().getCatPaiDespesa());
			st.setString(5, obj.getCategoriaDespesaFilho().getCategoriaFilhoDespesa());
			st.setString(6, obj.getStatusDespesa());
			st.setString(7, obj.getMeioPagamento());
			st.setInt(8, obj.getQtdParcela());
			st.setDouble(9, obj.getValorParcela());
			st.setDate(10, new java.sql.Date(obj.getDataPagamentoParcela().getTime()));
			st.setDouble(11, obj.getValor());
			st.setString(12, obj.getObs());
			st.setInt(13, obj.getConta().getId());
			st.setInt(14, obj.getCategoriaDespesaPai().getId());
			st.setInt(15, obj.getCategoriaDespesaFilho().getId());
			st.setInt(16, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM despesa WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Despesa findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT despesa.*,conta.Name as DepName " + "FROM despesa INNER JOIN conta "
					+ "ON despesa.ContaId = conta.Id " + "WHERE despesa.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Conta dep = instantiateConta(rs);
				CategoriaDespesa dep1 = instantiateCategoriaDespesa(rs);
				CategoriaDespesaFilho dep2 = instantiateCategoriaDespesaFilho(rs);
				Despesa obj = instantiateDespesa(rs, dep, dep1,dep2);
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

	private Despesa instantiateDespesa(ResultSet rs, Conta dep, CategoriaDespesa dep1, CategoriaDespesaFilho dep2) throws SQLException {
		Despesa obj = new Despesa();
		obj.setId(rs.getInt("Id"));
		obj.setDataOriginalDespesa(new java.util.Date(rs.getTimestamp("DataOriginalDespesa").getTime()));
		obj.setDataConcluidaDespesa(new java.util.Date(rs.getTimestamp("DataConcluidaDespesa").getTime()));
		obj.setDescricao(rs.getString("Descricao"));
		obj.setCategoriaDespesaPai(dep1);
		obj.setCategoriaDespesaFilho(dep2);
		obj.setStatusDespesa(rs.getString("StatusDespesa"));
		obj.setMeioPagamento(rs.getString("MeioPagamento"));
		obj.setQtdParcela(rs.getInt("QtdParcela"));
		obj.setValorParcela(rs.getDouble("ValorParcela"));
		obj.setDataPagamentoParcela(new java.util.Date(rs.getTimestamp("DataPagamentoParcela").getTime()));
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

	private CategoriaDespesa instantiateCategoriaDespesa(ResultSet rs) throws SQLException {
		CategoriaDespesa dep1 = new CategoriaDespesa();
		dep1.setId(rs.getInt("CategoriaId"));
		dep1.setCatPaiDespesa(rs.getString("CategoriaPaiDespesa"));
		return dep1;
	}
	
	private CategoriaDespesaFilho instantiateCategoriaDespesaFilho(ResultSet rs) throws SQLException {
		CategoriaDespesaFilho dep2 = new CategoriaDespesaFilho();
		dep2.setId(rs.getInt("CategoriaFilhoDespesaId"));
		dep2.setCategoriaFilhoDespesa(rs.getString("CategoriaFilhoDespesa"));
		return dep2;
	}

	@Override
	public List<Despesa> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("" + "SELECT despesa.*, conta.name as DepName "
					+ "FROM despesa INNER JOIN conta " + "ON despesa.ContaId = conta.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Despesa> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			Map<Integer, CategoriaDespesa> map1 = new HashMap<>();
			Map<Integer, CategoriaDespesaFilho> map2 = new HashMap<>();

			while (rs.next()) {

				Conta dep = map.get(rs.getInt("ContaId"));
				CategoriaDespesa dep1 = map1.get(rs.getInt("CategoriaId"));
				CategoriaDespesaFilho dep2 = map2.get(rs.getInt("CategoriaFilhoDespesaId"));

				if (dep == null) {
					dep = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), dep);
				}

				if (dep1 == null) {
					dep1 = instantiateCategoriaDespesa(rs);
					map1.put(rs.getInt("CategoriaId"), dep1);
				}

				if (dep2 == null) {
					dep2 = instantiateCategoriaDespesaFilho(rs);
					map2.put(rs.getInt("CategoriaFilhoDespesaId"), dep2);
				}

				Despesa obj = instantiateDespesa(rs, dep, dep1, dep2);
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
	public List<Despesa> findByConta(Conta conta) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT despesa.*,conta.Name as DepName " + "FROM despesa INNER JOIN conta "

					+ "ON despesa.ContaId = conta.Id " + "WHERE ContaId = ? " + "ORDER BY Name");

			st.setInt(1, conta.getId());

			rs = st.executeQuery();

			List<Despesa> list = new ArrayList<>();
			Map<Integer, Conta> map = new HashMap<>();
			Map<Integer, CategoriaDespesa> map1 = new HashMap<>();
			Map<Integer, CategoriaDespesaFilho> map2 = new HashMap<>();

			while (rs.next()) {

				Conta dep = map.get(rs.getInt("ContaId"));
				CategoriaDespesa dep1 = map1.get(rs.getInt("CategoriaId"));
				CategoriaDespesaFilho dep2 = map2.get(rs.getInt("CategoriaFilhoDespesaId"));

				if (dep == null) {
					dep = instantiateConta(rs);
					map.put(rs.getInt("ContaId"), dep);
				}

				if (dep1 == null) {
					dep1 = instantiateCategoriaDespesa(rs);
					map1.put(rs.getInt("CategoriaId"), dep1);
				}

				if (dep2 == null) {
					dep2 = instantiateCategoriaDespesaFilho(rs);
					map2.put(rs.getInt("CategoriaFilhoDespesaId"), dep2);
				}

				Despesa obj = instantiateDespesa(rs, dep, dep1, dep2);
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
	public List<Despesa> findByCategoriaDespesa(CategoriaDespesa categoriaDespesa) {
		// TODO Auto-generated method stub
		return null;
	}
}

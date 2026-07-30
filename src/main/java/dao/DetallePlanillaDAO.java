package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import entidad.DetallePlanilla;
import interfaces.IDetallePlanillaDAO;
import util.ConexionBD;

public class DetallePlanillaDAO implements IDetallePlanillaDAO {

	@Override
	public void insertar(DetallePlanilla dp)
	{
		String sql = "INSERT INTO DetallePlanilla(IDPlanilla, IDPersonal, diasTrabajados, diasTardanza, diasFalta, montoDescuento, montoTotal) VALUES(?,?,?,?,?,?,?)";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, dp.getIdPlanilla());
			ps.setInt(2, dp.getIdPersonal());
			ps.setInt(3, dp.getDiasTrabajados());
			ps.setInt(4, dp.getDiasTardanza());
			ps.setInt(5, dp.getDiasFalta());
			ps.setBigDecimal(6, dp.getMontoDescuento());
			ps.setBigDecimal(7, dp.getMontoTotal());
			ps.executeUpdate();
		}
		catch (SQLException e) {e.printStackTrace();}
	}

	
	
	@Override
	public DetallePlanilla buscarPorId(int idDetalle)
	{
		DetallePlanilla dp = null;
		String sql = "SELECT * FROM DetallePlanilla WHERE IDDetalle=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery())
		{
			if(rs.next())
			{
				dp = new DetallePlanilla();
				dp.setIdDetalle(rs.getInt("IDDetalle"));
				dp.setIdPlanilla(rs.getInt("IDPlanilla"));
				dp.setIdPersonal(rs.getInt("IDPersonal"));
				dp.setDiasTrabajados(rs.getInt("diasTrabajados"));
				dp.setDiasTardanza(rs.getInt("diasTardanza"));
				dp.setDiasFalta(rs.getInt("diasFalta"));
				dp.setMontoDescuento(rs.getBigDecimal("montoDescuento"));
				dp.setMontoTotal(rs.getBigDecimal("montoTotal"));
			}
		}
		
		catch (SQLException e) {e.printStackTrace();}
		
		return dp;
	}

	
	
	@Override
	public List<DetallePlanilla> listar()
	{
		List<DetallePlanilla> lista = new ArrayList<>();
		String sql = "SELECT * FROM DetallePlanilla";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql);
		     ResultSet  rs = ps.executeQuery())
		{
			while(rs.next())
			{
				DetallePlanilla dp = new DetallePlanilla();
				dp.setIdDetalle(rs.getInt("IDDetalle"));
				dp.setIdPlanilla(rs.getInt("IDPlanilla"));
				dp.setIdPersonal(rs.getInt("IDPersonal"));
				dp.setDiasTrabajados(rs.getInt("diasTrabajados"));
				dp.setDiasTardanza(rs.getInt("diasTardanza"));
				dp.setDiasFalta(rs.getInt("diasFalta"));
				dp.setMontoDescuento(rs.getBigDecimal("montoDescuento"));
				dp.setMontoTotal(rs.getBigDecimal("montoTotal"));
				lista.add(dp);
			}
		}
		
		catch (SQLException e) {e.printStackTrace();}
		
		return lista;
	}

	
	
	@Override
	public void actualizar(DetallePlanilla dp)
	{
		String sql = "UPDATE DetallePlanilla SET IDPlanilla=?, IDPersonal=?, diasTrabajados=?, diasTardanza=?, diasFalta=?, montoDescuento=?, montoTotal=? WHERE IDDetalle=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, dp.getIdPlanilla());
			ps.setInt(2, dp.getIdPersonal());
			ps.setInt(3, dp.getDiasTrabajados());
			ps.setInt(4, dp.getDiasTardanza());
			ps.setInt(5, dp.getDiasFalta());
			ps.setBigDecimal(6, dp.getMontoDescuento());
			ps.setBigDecimal(7, dp.getMontoTotal());
			ps.setInt(8, dp.getIdDetalle());
			ps.executeUpdate();
		}
		
		catch (SQLException e) {e.printStackTrace();}	
	}

	
	
	@Override
	public void eliminar(int idDetalle)
	{
		String sql = "DELETE FROM DetallePlanilla WHERE IDDetalle=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{	
			ps.setInt(1, idDetalle);
			ps.executeUpdate();
		}
		
		catch (SQLException e) {e.printStackTrace();}
	}

}

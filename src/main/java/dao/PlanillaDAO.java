package dao;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;

import entidad.Planilla;
import interfaces.IPlanillaDAO;
import util.ConexionBD;

public class PlanillaDAO implements IPlanillaDAO {

	@Override
	public void insertar(Planilla pl)
	{
		String sql = "INSERT INTO Planilla(mes, anio) VALUES(?,?)";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, pl.getMes());
			ps.setInt(2, pl.getAnio());
			ps.executeUpdate();
		}
		
		catch (SQLException e) {e.printStackTrace();}
	}

	
	
	@Override
	public Planilla buscarPorId(int idPlanilla)
	{
		Planilla pl = null;
		String sql = "SELECT * FROM Planilla WHERE IDPlanilla=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, idPlanilla);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				pl = new Planilla();
				pl.setIdPlanilla(rs.getInt("IDPlanilla"));
				pl.setMes(rs.getInt("mes"));
				pl.setAnio(rs.getInt("anio"));
			}
		}
		
		catch (SQLException e) {e.printStackTrace();}
		
		return pl;
	}

	
	
	@Override
	public List<Planilla> listar()
	{
		List<Planilla> lista = new ArrayList<>();
		String sql = "SELECT * FROM Planilla";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql);
		     ResultSet rs = ps.executeQuery())
		{
			while(rs.next())
			{
				Planilla pl = new Planilla();
				pl.setIdPlanilla(rs.getInt("IDPlanilla"));
				pl.setMes(rs.getInt("mes"));
				pl.setAnio(rs.getInt("anio"));
				lista.add(pl);
			}
		}
		
		catch (SQLException e) {e.printStackTrace();}
		
		return lista;
	}

	
	
	@Override
	public void actualizar(Planilla pl)
	{		
		String sql = "UPDATE Planilla SET mes=?, anio=? WHERE IDPlanilla=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{	
			ps.setInt(1, pl.getMes());
			ps.setInt(2, pl.getAnio());
			ps.setInt(3, pl.getIdPlanilla());
			ps.executeUpdate();
		}
		
		catch (SQLException e) {e.printStackTrace();}
	}

	
	
	@Override
	public void eliminar(int idPlanilla)
	{
		String sql = "DELETE FROM Planilla WHERE IDPlanilla=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, idPlanilla);
			ps.executeUpdate();
		}
		
		catch (SQLException e) {e.printStackTrace();}
	}

}

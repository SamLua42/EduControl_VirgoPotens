package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entidad.Asistencia;
import interfaces.IAsistenciaDAO;
import util.ConexionBD;


public class AsistenciaDAO implements IAsistenciaDAO{

	@Override
	public void insertar(Asistencia a)
	{	
		String sql = "INSERT INTO Asistencia(IDPersonal, fecha, horaMarcada, clasificacion) VALUES(?,?,?,?)";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{	
			ps.setInt(1, a.getIdPersonal());
			ps.setDate(2, a.getFecha());
			ps.setTime(3, a.getHoraMarcada());
			ps.setString(4, a.getClasificacion());
			ps.executeUpdate();
		}
		
		catch (SQLException e) { e.printStackTrace(); }
	}

	
	
	@Override
	public Asistencia buscarPorId(int idAsistencia)
	{
		Asistencia a = null;
		String sql = "SELECT * FROM Asistencia WHERE IDAsistencia=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1,idAsistencia);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				a = new Asistencia();
				a.setIdAsistencia(rs.getInt("IDAsistencia"));
				a.setIdPersonal(rs.getInt("IDPersonal"));
				a.setFecha(rs.getDate("fecha"));
				a.setHoraMarcada(rs.getTime("horaMarcada"));
				a.setClasificacion(rs.getString("clasificacion"));
			}
		}
		
		catch (SQLException e) { e.printStackTrace(); }
		
		return a;
	}

	
	
	@Override
	public List<Asistencia> listar()
	{
		List<Asistencia> lista = new ArrayList<>();
		String sql = "SELECT * FROM Asistencia";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql);
		     ResultSet rs = ps.executeQuery())
		{
			while(rs.next())
			{
				Asistencia a = new Asistencia();
				a.setIdAsistencia(rs.getInt("IDAsistencia"));
				a.setIdPersonal(rs.getInt("IDPersonal"));
				a.setFecha(rs.getDate("fecha"));
				a.setHoraMarcada(rs.getTime("horaMarcada"));
				a.setClasificacion(rs.getString("clasificacion"));
				lista.add(a);
			}
		}
		
		catch (SQLException e) { e.printStackTrace(); }
		
		return lista;
	}

	
	
	@Override
	public void actualizar(Asistencia a)
	{
		String sql = "UPDATE Asistencia SET IDPersonal=?, fecha=?, horaMarcada=?, clasificacion=? WHERE IDAsistencia=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			
			ps.setInt(1, a.getIdPersonal());
			ps.setDate(2, a.getFecha());
			ps.setTime(3, a.getHoraMarcada());
			ps.setString(4, a.getClasificacion());
			ps.setInt(5, a.getIdAsistencia());
			ps.executeUpdate();
		}
		
		catch (SQLException e) { e.printStackTrace(); }
	}

	
	
	@Override
	public void eliminar(int idAsistencia)
	{
		String sql = "DELETE FROM Asistencia WHERE IDAsistencia=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, idAsistencia);
			ps.executeUpdate();
		}
		
		catch (SQLException e) { e.printStackTrace(); }
	}
	
}

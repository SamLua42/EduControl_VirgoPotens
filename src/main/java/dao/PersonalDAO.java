package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entidad.Personal;
import interfaces.IPersonalDAO;
import util.ConexionBD;


public class PersonalDAO implements IPersonalDAO {

	@Override
	public void insertar(Personal p) 
	{
		String sql = "INSERT INTO Personal (nombre,apellido,dni) VALUES (?,?,?)";
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{	
			 ps.setString(1, p.getNombre());
			 ps.setString(2, p.getApellido());
			 ps.setString(3, p.getDni());
			 ps.executeUpdate();
		}
		
		catch (SQLException e) {e.printStackTrace();}
	}
	

	
	@Override
	public Personal buscarPorId(int idPersonal) 
	{
		Personal p = null;
		String sql = "SELECT * FROM Personal WHERE idPersonal = ?";
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			 ps.setInt(1, idPersonal);
			 ResultSet rs = ps.executeQuery();
			 
			 if(rs.next())
			 {
				 p = new Personal();
				 p.setIdPersonal(rs.getInt("IDPersonal"));
				 p.setNombre(rs.getString("nombre"));
				 p.setApellido(rs.getString("apellido"));
				 p.setDni(rs.getString("dni"));
			 }	
		}
	
		catch (SQLException e) {e.printStackTrace();}
		
		return p;
	}

	
	
	@Override
	public List<Personal> listar() 
	{
		List<Personal> lista = new ArrayList<>();
		String sql = "SELECT * FROM Personal";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery())
		{
			while(rs.next())
			{
				Personal p = new Personal();
				p.setIdPersonal(rs.getInt("idPersonal"));
				p.setNombre(rs.getString("nombre"));
				p.setApellido(rs.getString("apellido"));
				p.setDni(rs.getString("dni"));
				lista.add(p);
			}
		}
		
		catch (SQLException e) { e.printStackTrace(); }
		
		return lista;
	}
	
	

	@Override
	public void actualizar(Personal p) 
	{	
		String sql = "UPDATE Personal SET nombre=?, apellido=?, dni=? WHERE IDPersonal=?";
		
		try (Connection con  = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, p.getNombre());
			ps.setString(2, p.getApellido());
			ps.setString(3, p.getDni());
			ps.setInt(4, p.getIdPersonal());
			ps.executeUpdate();
		}
		
		catch (SQLException e) { e.printStackTrace(); }
	}

	
	
	@Override
	public void eliminar(int idPersonal) 
	{
		String sql = "DELETE FROM Personal WHERE IDPersonal=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, idPersonal);
			ps.executeUpdate();
		}
		
		catch (SQLException e) { e.printStackTrace(); }		
	}
	
}

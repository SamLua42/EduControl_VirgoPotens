package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import interfaces.IConceptoPagoDAO;
import entidad.ConceptoPago;
import util.ConexionBD;


public class ConceptoPagoDAO implements IConceptoPagoDAO {

	@Override
	public int insertar(ConceptoPago c)
	{
		int resultado = 0;
		String sql = "INSERT INTO ConceptoPago(tipoPersonal, tarifaDiaria, descuentoTardanza, descuentoFalta) VALUES(?,?,?,?)";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, c.getTipoPersonal());
			ps.setBigDecimal(2, c.getTarifaDiaria());
			ps.setBigDecimal(3, c.getDescuentoTardanza());
			ps.setBigDecimal(4, c.getDescuentoFalta());
			resultado = ps.executeUpdate();
		}
		
		catch (SQLException e) { e.printStackTrace(); }
		
		return resultado;
	}

	
	
	@Override
	public ConceptoPago buscarPorId(int idConcepto)
	{
		ConceptoPago c = null;
		String sql = "SELECT * FROM ConceptoPago WHERE IDConcepto=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, idConcepto);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				c = new ConceptoPago();
				c.setIdConcepto(rs.getInt("IDConcepto"));
				c.setTipoPersonal(rs.getString("tipoPersonal"));
				c.setTarifaDiaria(rs.getBigDecimal("tarifaDiaria"));
				c.setDescuentoTardanza(rs.getBigDecimal("descuentoTardanza"));
				c.setDescuentoFalta(rs.getBigDecimal("descuentoFalta"));
			}
		}
		
		catch (SQLException e) { e.printStackTrace();}
		
		return c;
	}

	
	
	@Override
	public List<ConceptoPago> listar()
	{
		List<ConceptoPago> lista = new ArrayList<>();
		String sql = "SELECT * FROM ConceptoPago";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql);
		     ResultSet rs = ps.executeQuery())
		{
			while(rs.next())
			{
				ConceptoPago c = new ConceptoPago();
				c.setIdConcepto(rs.getInt("IDConcepto"));
				c.setTipoPersonal(rs.getString("tipoPersonal"));
				c.setTarifaDiaria(rs.getBigDecimal("tarifaDiaria"));
				c.setDescuentoTardanza(rs.getBigDecimal("descuentoTardanza"));
				c.setDescuentoFalta(rs.getBigDecimal("descuentoFalta"));
				lista.add(c);
			}
		}
		
		catch (SQLException e) {e.printStackTrace();}

		return lista;
	}

	
	
	@Override
	public int actualizar(ConceptoPago c) 
	{
		int resultado = 0;
		String sql = "UPDATE ConceptoPago SET tipoPersonal=?, tarifaDiaria=?, descuentoTardanza=?, descuentoFalta=? WHERE IDConcepto=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setString(1, c.getTipoPersonal());
			ps.setBigDecimal(2, c.getTarifaDiaria());
			ps.setBigDecimal(3, c.getDescuentoTardanza());
			ps.setBigDecimal(4, c.getDescuentoFalta());
			ps.setInt(5, c.getIdConcepto());
			resultado = ps.executeUpdate();
		}
		
		catch (SQLException e) {e.printStackTrace();}
		
		return resultado;
	}

	
	
	@Override
	public int eliminar(int idConcepto)
	{
		int resultado = 0;
		String sql = "DELETE FROM ConceptoPago WHERE IDConcepto=?";
		
		try (Connection con = ConexionBD.miConexion();
			 PreparedStatement ps = con.prepareStatement(sql))
		{
			ps.setInt(1, idConcepto);
			resultado = ps.executeUpdate();
		}
		
		catch (SQLException e) {e.printStackTrace();}
		
		return resultado;
	}

}

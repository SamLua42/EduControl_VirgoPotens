package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entidad.ConfiguracionInstitucion;
import util.ConexionBD;

public class ConfiguracionInstitucionDAO {

    public ConfiguracionInstitucion obtener()
    {
        ConfiguracionInstitucion c = null;
        String sql = "SELECT * FROM ConfiguracionInstitucion WHERE ID = 1";

        try (Connection con = ConexionBD.miConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {
            if (rs.next())
            {
                c = new ConfiguracionInstitucion();
                c.setId(rs.getInt("ID"));
                c.setLogoRuta(rs.getString("logoRuta"));
                c.setDependencia(rs.getString("dependencia"));
                c.setTelefono(rs.getString("telefono"));
                c.setPaginaWeb(rs.getString("paginaWeb"));
                c.setForma(rs.getString("forma"));
                c.setDirector(rs.getString("directora"));
                c.setNivelModalidad(rs.getString("nivelModalidad"));
                c.setGenero(rs.getString("genero"));
                c.setTurno(rs.getString("turno"));
                c.setToleranciaTardanzaMinutos(rs.getInt("toleranciaTardanzaMinutos"));
                c.setPorcentajeOnp(rs.getBigDecimal("porcentajeOnp"));
                c.setPorcentajeAfp(rs.getBigDecimal("porcentajeAfp"));
                c.setPorcentajeEssalud(rs.getBigDecimal("porcentajeEssalud"));
            }
        }
        catch (SQLException e) { e.printStackTrace(); }

        if (c == null)
        {
            c = new ConfiguracionInstitucion();
            c.setId(1);
            c.setToleranciaTardanzaMinutos(10);
            c.setPorcentajeOnp(new BigDecimal("13.00"));
            c.setPorcentajeAfp(new BigDecimal("11.37"));
            c.setPorcentajeEssalud(new BigDecimal("9.00"));
        }

        return c;
    }


    public int actualizar(ConfiguracionInstitucion c)
    {
        int resultado = 0;
        String sql = "UPDATE ConfiguracionInstitucion SET logoRuta=?, dependencia=?, telefono=?, paginaWeb=?, forma=?, directora=?, nivelModalidad=?, genero=?, turno=?, toleranciaTardanzaMinutos=?, porcentajeOnp=?, porcentajeAfp=?, porcentajeEssalud=? WHERE ID=1";

        try (Connection con = ConexionBD.miConexion();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, c.getLogoRuta());
            ps.setString(2, c.getDependencia());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getPaginaWeb());
            ps.setString(5, c.getForma());
            ps.setString(6, c.getDirector());
            ps.setString(7, c.getNivelModalidad());
            ps.setString(8, c.getGenero());
            ps.setString(9, c.getTurno());
            ps.setInt(10, c.getToleranciaTardanzaMinutos());
            ps.setBigDecimal(11, c.getPorcentajeOnp());
            ps.setBigDecimal(12, c.getPorcentajeAfp());
            ps.setBigDecimal(13, c.getPorcentajeEssalud());
            resultado = ps.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return resultado;
    }
}
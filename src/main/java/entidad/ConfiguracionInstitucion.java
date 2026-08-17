package entidad;

public class ConfiguracionInstitucion
{
    private int id;
    private String logoRuta;
    private String dependencia;
    private String telefono;
    private String paginaWeb;
    private String forma;
    private String director;
    private String nivelModalidad;
    private String genero;
    private String turno;
    private int toleranciaTardanzaMinutos;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLogoRuta() { return logoRuta; }
    public void setLogoRuta(String logoRuta) { this.logoRuta = logoRuta; }

    public String getDependencia() { return dependencia; }
    public void setDependencia(String dependencia) { this.dependencia = dependencia; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPaginaWeb() { return paginaWeb; }
    public void setPaginaWeb(String paginaWeb) { this.paginaWeb = paginaWeb; }

    public String getForma() { return forma; }
    public void setForma(String forma) { this.forma = forma; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getNivelModalidad() { return nivelModalidad; }
    public void setNivelModalidad(String nivelModalidad) { this.nivelModalidad = nivelModalidad; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public int getToleranciaTardanzaMinutos() { return toleranciaTardanzaMinutos; }
    public void setToleranciaTardanzaMinutos(int toleranciaTardanzaMinutos) { this.toleranciaTardanzaMinutos = toleranciaTardanzaMinutos; }
}
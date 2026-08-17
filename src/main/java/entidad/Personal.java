package entidad;

import java.sql.Time;
import java.sql.Timestamp;

public class Personal {

	//ATRIBUTOS==============================================================================================================================
	private int idPersonal;
	private String nombre;
	private String apellido;
	private String dni;
	private String cargo;
	private String tipoPersonal;
	private String sistemaPension;
	private Time horaEntradaEsperada;
	private String usuario;
	private String contrasena;
	private String rol;
	private boolean estado;
	private Timestamp fechaRegistro;


	//CONSTRUCTORES=========================================================================================================================
	public Personal()
	{
	}

	public Personal(int idPersonal, String nombre, String apellido, String dni, String cargo, String tipoPersonal, String sistemaPension, Time horaEntradaEsperada,
			        String usuario, String contrasena, String rol, boolean estado, Timestamp fechaRegistro )
	{
		this.idPersonal = idPersonal;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.cargo = cargo;
		this.tipoPersonal = tipoPersonal;
		this.sistemaPension = sistemaPension;
		this.horaEntradaEsperada = horaEntradaEsperada;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.rol = rol;
		this.estado = estado;
		this.fechaRegistro = fechaRegistro;
	}


	//GETTERS Y SETTERS======================================================================================================================
	public int getIdPersonal() {return idPersonal;}
	public void setIdPersonal(int idPersonal) {this.idPersonal = idPersonal;}

	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}

	public String getApellido() {return apellido;}
	public void setApellido(String apellido) {this.apellido = apellido;}

	public String getDni() {return dni;}
	public void setDni(String dni) {this.dni = dni;}

	public String getCargo() {return cargo;}
	public void setCargo(String cargo) {this.cargo = cargo;}

	public String getTipoPersonal() {return tipoPersonal;}
	public void setTipoPersonal(String tipoPersonal) {this.tipoPersonal = tipoPersonal;}

	public String getSistemaPension() {return sistemaPension;}
	public void setSistemaPension(String sistemaPension) {this.sistemaPension = sistemaPension;}

	public Time getHoraEntradaEsperada() {return horaEntradaEsperada;}
	public void setHoraEntradaEsperada(Time horaEntradaEsperada) {this.horaEntradaEsperada = horaEntradaEsperada;}

	public String getUsuario() {return usuario;}
	public void setUsuario(String usuario) {this.usuario = usuario;}

	public String getContrasena() {return contrasena;}
	public void setContrasena(String contrasena) {this.contrasena = contrasena;}

	public String getRol() {return rol;}
	public void setRol(String rol) {this.rol = rol;}

	public boolean isEstado() {return estado;}
	public void setEstado(boolean estado) {this.estado = estado;}

	public Timestamp getFechaRegistro() {return fechaRegistro;}
	public void setFechaRegistro(Timestamp fechaRegistro) {this.fechaRegistro = fechaRegistro;}

}
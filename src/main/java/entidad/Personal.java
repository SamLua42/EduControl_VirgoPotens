package entidad;

import java.sql.Time;
import java.sql.Timestamp;

public class Personal {
	
	//ATRIBUTOS==============================================================================================================================
	private int IDPersonal;
	private String nombre;
	private String apellido;
	private String dni;
	private String cargo;
	private String tipoPersonal;
	private Time horaEntradaEsperada;
	private String usuario;
	private String contraseña;
	private String rol;
	private boolean estado;
	private Timestamp fechaRegistro;
	
	
	//CONSTRUCTORES=========================================================================================================================
	public Personal()
	{
	}
	
	public Personal(int IDPersonal, String nombre, String apellido, String dni, String cargo, String tipoPersonal, Time horaEntradaEsperada,
			        String usuario, String contraseña, String rol, boolean estado, Timestamp fechaRegistro )
	{
		this.IDPersonal = IDPersonal;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.cargo = cargo;
		this.tipoPersonal = tipoPersonal;
		this.horaEntradaEsperada = horaEntradaEsperada;
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.rol = rol;
		this.estado = estado;
		this.fechaRegistro = fechaRegistro;
	}

	
	//GETTERS Y SETTERS======================================================================================================================
	public int getIDPersonal() {return IDPersonal;}
	public void setIDPersonal(int iDPersonal) {IDPersonal = iDPersonal;}

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

	public Time getHoraEntradaEsperada() {return horaEntradaEsperada;}
	public void setHoraEntradaEsperada(Time horaEntradaEsperada) {this.horaEntradaEsperada = horaEntradaEsperada;}

	public String getUsuario() {return usuario;}
	public void setUsuario(String usuario) {this.usuario = usuario;}

	public String getContraseña() {return contraseña;}
	public void setContraseña(String contraseña) {this.contraseña = contraseña;}

	public String getRol() {return rol;}
	public void setRol(String rol) {this.rol = rol;}

	public boolean isEstado() {return estado;}
	public void setEstado(boolean estado) {this.estado = estado;}

	public Timestamp getFechaRegistro() {return fechaRegistro;}
	public void setFechaRegistro(Timestamp fechaRegistro) {this.fechaRegistro = fechaRegistro;}
	
}

package entidad;

import java.sql.Timestamp;

public class Planilla {
	
	//ATRIBUTOS==============================================================================================================================
	private int idPlanilla;
	private int mes;
	private int anio;
	private String estado;
	private Timestamp fechaProcesado;

	
	
	//CONSTRUCTORES=========================================================================================================================
	public Planilla()
	{
	}
	
	public Planilla(int idPlanilla, int mes, int anio, String estado, Timestamp fechaProcesado)
	{
		this.idPlanilla = idPlanilla;
		this.mes = mes;
		this.anio = anio;
		this.estado = estado;
		this.fechaProcesado = fechaProcesado;
	}

	
	//GETTERS Y SETTERS======================================================================================================================
	public int getIdPlanilla() {return idPlanilla;}
	public void setIdPlanilla(int idPlanilla) {this.idPlanilla = idPlanilla;}

	public int getMes() {return mes;}
	public void setMes(int mes) {this.mes = mes;}

	public int getAnio() {return anio;}
	public void setAnio(int anio) {this.anio = anio;}

	public String getEstado() {return estado;}
	public void setEstado(String estado) {this.estado = estado;}

	public Timestamp getFechaProcesado() {return fechaProcesado;}
	public void setFechaProcesado(Timestamp fechaProcesado) {this.fechaProcesado = fechaProcesado;}

}

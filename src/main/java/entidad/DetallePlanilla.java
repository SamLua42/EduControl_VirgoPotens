package entidad;

import java.math.BigDecimal;

public class DetallePlanilla {
	
	//ATRIBUTOS==============================================================================================================================
	private int idDetalle;
	private int idPlanilla;
	private int idPersonal;
	private int diasTrabajados;
	private int diasTardanza;
	private int diasFalta;
	private BigDecimal montoDescuento;
	private BigDecimal montoTotal;

		
	//CONSTRUCTORES=========================================================================================================================
	public DetallePlanilla()
	{
	}
	
	public DetallePlanilla(int idDetalle, int idPlanilla, int idPersonal, int diasTrabajados, int diasTardanza, int diasFalta, BigDecimal montoDescuento, BigDecimal montoTotal)
	{
		this.idDetalle = idDetalle;
		this.idPlanilla = idPlanilla;
		this.idPersonal = idPersonal;
		this.diasTrabajados = diasTrabajados;
		this.diasTardanza = diasTardanza;
		this.diasFalta = diasFalta;
		this.montoDescuento = montoDescuento;
		this.montoTotal = montoTotal;
	}

	
	//GETTERS Y SETTERS======================================================================================================================
	public int getIdDetalle() {return idDetalle;}
	public void setIdDetalle(int idDetalle) {this.idDetalle = idDetalle;}

	public int getIdPlanilla() {return idPlanilla;}
	public void setIdPlanilla(int idPlanilla) {this.idPlanilla = idPlanilla;}
	
	public int getIdPersonal() {return idPersonal;}
	public void setIdPersonal(int idPersonal) {this.idPersonal = idPersonal;}

	public int getDiasTrabajados() {return diasTrabajados;}
	public void setDiasTrabajados(int diasTrabajados) {this.diasTrabajados = diasTrabajados;}

	public int getDiasTardanza() {return diasTardanza;}
	public void setDiasTardanza(int diasTardanza) {this.diasTardanza = diasTardanza;}

	public int getDiasFalta() {return diasFalta;}
	public void setDiasFalta(int diasFalta) {this.diasFalta = diasFalta;}

	public BigDecimal getMontoDescuento() {return montoDescuento;}
	public void setMontoDescuento(BigDecimal montoDescuento) {this.montoDescuento = montoDescuento;}

	public BigDecimal getMontoTotal() {return montoTotal;}
	public void setMontoTotal(BigDecimal montoTotal) {this.montoTotal = montoTotal;}
	
}

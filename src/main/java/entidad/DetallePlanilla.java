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
	private BigDecimal montoBruto;
	private BigDecimal montoDescuento;
	private BigDecimal montoDescuentoPension;
	private BigDecimal montoEssalud;
	private BigDecimal montoTotal;
	private BigDecimal montoNeto;


	//CONSTRUCTORES=========================================================================================================================
	public DetallePlanilla()
	{
	}

	public DetallePlanilla(int idDetalle, int idPlanilla, int idPersonal, int diasTrabajados, int diasTardanza, int diasFalta,
			BigDecimal montoBruto, BigDecimal montoDescuento, BigDecimal montoDescuentoPension, BigDecimal montoEssalud,
			BigDecimal montoTotal, BigDecimal montoNeto)
	{
		this.idDetalle = idDetalle;
		this.idPlanilla = idPlanilla;
		this.idPersonal = idPersonal;
		this.diasTrabajados = diasTrabajados;
		this.diasTardanza = diasTardanza;
		this.diasFalta = diasFalta;
		this.montoBruto = montoBruto;
		this.montoDescuento = montoDescuento;
		this.montoDescuentoPension = montoDescuentoPension;
		this.montoEssalud = montoEssalud;
		this.montoTotal = montoTotal;
		this.montoNeto = montoNeto;
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

	public BigDecimal getMontoBruto() {return montoBruto;}
	public void setMontoBruto(BigDecimal montoBruto) {this.montoBruto = montoBruto;}

	public BigDecimal getMontoDescuento() {return montoDescuento;}
	public void setMontoDescuento(BigDecimal montoDescuento) {this.montoDescuento = montoDescuento;}

	public BigDecimal getMontoDescuentoPension() {return montoDescuentoPension;}
	public void setMontoDescuentoPension(BigDecimal montoDescuentoPension) {this.montoDescuentoPension = montoDescuentoPension;}

	public BigDecimal getMontoEssalud() {return montoEssalud;}
	public void setMontoEssalud(BigDecimal montoEssalud) {this.montoEssalud = montoEssalud;}

	public BigDecimal getMontoTotal() {return montoTotal;}
	public void setMontoTotal(BigDecimal montoTotal) {this.montoTotal = montoTotal;}

	public BigDecimal getMontoNeto() {return montoNeto;}
	public void setMontoNeto(BigDecimal montoNeto) {this.montoNeto = montoNeto;}

}
package entidad;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class ConceptoPago {

		//ATRIBUTOS==============================================================================================================================
		private int idConcepto;
		private String tipoPersonal;
		private BigDecimal tarifaDiaria;
		private BigDecimal descuentoTardanza;
		private BigDecimal descuentoFalta;
		private Timestamp fechaActualizacion;
		
		
		//CONSTRUCTORES=========================================================================================================================
		public ConceptoPago()
		{
		}
		
		public ConceptoPago(int idConcepto, String tipoPersonal, BigDecimal tarifaDiaria, BigDecimal descuentoTardanza, BigDecimal descuentoFalta, Timestamp fechaActualizacion)
		{
			this.idConcepto = idConcepto;
			this.tipoPersonal = tipoPersonal;
			this.tarifaDiaria = tarifaDiaria;
			this.descuentoTardanza = descuentoTardanza;
			this.descuentoFalta = descuentoFalta;
			this.fechaActualizacion = fechaActualizacion;
		}

		
		//GETTERS Y SETTERS======================================================================================================================
		public int getIdConcepto() {return idConcepto;}
		public void setIdConcepto(int idConcepto) {this.idConcepto = idConcepto;}		
		
		public String getTipoPersonal() {return tipoPersonal;}
		public void setTipoPersonal(String tipoPersonal) {this.tipoPersonal = tipoPersonal;}

		public BigDecimal getTarifaDiaria() {return tarifaDiaria;}
		public void setTarifaDiaria(BigDecimal tarifaDiaria) {this.tarifaDiaria = tarifaDiaria;}

		public BigDecimal getDescuentoTardanza() {return descuentoTardanza;}
		public void setDescuentoTardanza(BigDecimal descuentoTardanza) {this.descuentoTardanza = descuentoTardanza;}

		public BigDecimal getDescuentoFalta() {return descuentoFalta;}
		public void setDescuentoFalta(BigDecimal descuentoFalta) {this.descuentoFalta = descuentoFalta;}

		public Timestamp getFechaActualizacion() {return fechaActualizacion;}
		public void setFechaActualizacion(Timestamp fechaActualizacion) {this.fechaActualizacion = fechaActualizacion;}

}

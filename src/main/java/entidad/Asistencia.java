package entidad;

import java.sql.Time;
import java.sql.Date;

public class Asistencia {
		
		//ATRIBUTOS==============================================================================================================================
		private int idAsistencia;
		private int idPersonal;
		private Date fecha;
		private Time horaMarcada;
		private String clasificacion;
		
		
		//CONSTRUCTORES=========================================================================================================================
		public Asistencia()
		{
		}
		
		public Asistencia(int idAsistencia, int idPersonal, Date fecha, Time horaMarcada, String clasificacion)
		{
			this.idAsistencia = idAsistencia;
			this.idPersonal = idPersonal;
			this.fecha = fecha;
			this.horaMarcada = horaMarcada;
			this.clasificacion = clasificacion;
		}

		
		//GETTERS Y SETTERS======================================================================================================================
		public int getIdAsistencia() {return idAsistencia;}
		public void setIdAsistencia(int idAsistencia) {this.idAsistencia = idAsistencia;}		
		
		public int getIdPersonal() {return idPersonal;}
		public void setIdPersonal(int idPersonal) {this.idPersonal = idPersonal;}

		public Date getFecha() {return fecha;}
		public void setFecha(Date fecha) {this.fecha = fecha;}

		public Time getHoraMarcada() {return horaMarcada;}
		public void setHoraMarcada(Time horaMarcada) {this.horaMarcada = horaMarcada;}

		public String getClasificacion() {return clasificacion;}
		public void setClasificacion(String clasificacion) {this.clasificacion = clasificacion;}

}

package util;

import java.sql.Date;
import java.sql.Time;

import dao.AsistenciaDAO;
import entidad.Asistencia;

public class PruebaDatosReporte {

    public static void main(String[] args) {

        AsistenciaDAO dao = new AsistenciaDAO();

        Asistencia a1 = new Asistencia();
        a1.setIdPersonal(1);
        a1.setFecha(Date.valueOf("2026-08-10"));
        a1.setHoraMarcada(Time.valueOf("07:28:00"));
        a1.setClasificacion("Puntual");
        dao.insertar(a1);

        Asistencia a2 = new Asistencia();
        a2.setIdPersonal(1);
        a2.setFecha(Date.valueOf("2026-08-11"));
        a2.setHoraMarcada(Time.valueOf("07:46:00"));
        a2.setClasificacion("Tardanza");
        dao.insertar(a2);

        Asistencia a3 = new Asistencia();
        a3.setIdPersonal(1);
        a3.setFecha(Date.valueOf("2026-08-12"));
        a3.setHoraMarcada(null);
        a3.setClasificacion("Falta");
        dao.insertar(a3);

        System.out.println("Datos de prueba insertados.");
    }
}
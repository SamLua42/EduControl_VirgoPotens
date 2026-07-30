package util;

import java.sql.Time;
import java.util.List;

import dao.PersonalDAO;
import entidad.Personal;

public class PruebaPersonalDAO {

    public static void main(String[] args) {

        PersonalDAO dao = new PersonalDAO();

        // 1. Crear un Personal de prueba
        Personal nuevo = new Personal();
        nuevo.setNombre("Juana");
        nuevo.setApellido("Perez");
        nuevo.setDni("87654321");
        nuevo.setCargo("Docente de Aula");
        nuevo.setTipoPersonal("Docente");
        nuevo.setHoraEntradaEsperada(Time.valueOf("08:00:00"));
        nuevo.setUsuario("jperez");
        nuevo.setContrasena("temporal123");
        nuevo.setRol("Trabajador");

        // 2. Insertar
        dao.insertar(nuevo);
        System.out.println("Personal insertado.");

        // 3. Listar todos para verificar
        List<Personal> lista = dao.listar();
        System.out.println("Total de personal registrado: " + lista.size());

        for (Personal p : lista) {
            System.out.println(p.getIdPersonal() + " - " + p.getNombre() + " " + p.getApellido() + " - " + p.getTipoPersonal());
        }
    }
}

package interfaces;

import java.util.List;
import entidad.Asistencia;

public interface IAsistenciaDAO {

	void insertar(Asistencia a);
	Asistencia buscarPorId(int idAsistencia);
	List<Asistencia> listar();
	void actualizar(Asistencia a);
	void eliminar(int idAsistencia);
}

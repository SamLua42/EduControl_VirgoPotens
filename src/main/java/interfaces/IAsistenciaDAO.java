package interfaces;

import java.util.List;
import entidad.Asistencia;

public interface IAsistenciaDAO {

	int insertar(Asistencia a);
	Asistencia buscarPorId(int idAsistencia);
	List<Asistencia> listar();
	int actualizar(Asistencia a);
	int eliminar(int idAsistencia);
}

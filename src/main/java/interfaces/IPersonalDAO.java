package interfaces;

import java.util.List;
import entidad.Personal;

public interface IPersonalDAO {
	int insertar(Personal p);
	Personal buscarPorId(int idPersonal);
	Personal buscarPorUsuario(String usuario);
	List<Personal> listar();
	int actualizar(Personal p);
	int eliminar(int idPersonal);

}


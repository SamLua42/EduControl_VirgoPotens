package interfaces;

import java.util.List;
import entidad.Personal;

public interface IPersonalDAO {
	void insertar(Personal p);
	Personal buscarPorId(int idPersonal);
	List<Personal> listar();
	void actualizar(Personal p);
	void eliminar(int idPersonal);

}


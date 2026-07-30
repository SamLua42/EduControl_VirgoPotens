package interfaces;

import java.util.List;
import entidad.DetallePlanilla;

public interface IDetallePlanillaDAO {
	
	void insertar(DetallePlanilla dp);
	DetallePlanilla buscarPorId(int idDetalle);
	List<DetallePlanilla> listar();
	void actualizar(DetallePlanilla dp);
	void eliminar(int idDetalle);
}

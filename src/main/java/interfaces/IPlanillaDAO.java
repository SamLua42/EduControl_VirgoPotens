package interfaces;

import java.util.List;
import entidad.Planilla;

public interface IPlanillaDAO {
	
	int insertar(Planilla pl);
	Planilla buscarPorId(int idPlanilla);
	List<Planilla> listar();
	int actualizar(Planilla pl);
	int eliminar(int idPlanilla);
}

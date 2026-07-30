package interfaces;

import java.util.List;
import entidad.Planilla;

public interface IPlanillaDAO {
	
	void insertar(Planilla pl);
	Planilla buscarPorId(int idPlanilla);
	List<Planilla> listar();
	void actualizar(Planilla pl);
	void eliminar(int idPlanilla);
}

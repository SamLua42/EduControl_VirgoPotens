package interfaces;

import java.util.List;
import entidad.ConceptoPago;

public interface IConceptoPagoDAO {
	
	int insertar(ConceptoPago c);
	ConceptoPago buscarPorId(int idConcepto);
	List<ConceptoPago> listar();
	int actualizar(ConceptoPago c);
	int eliminar(int idConcepto);

}

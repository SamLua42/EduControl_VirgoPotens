package interfaces;

import java.util.List;
import entidad.ConceptoPago;

public interface IConceptoPagoDAO {
	
	void insertar(ConceptoPago c);
	ConceptoPago buscarPorId(int idConcepto);
	List<ConceptoPago> listar();
	void actualizar(ConceptoPago c);
	void eliminar(int idConcepto);

}

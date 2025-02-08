package persistencia;

import modelo.ContactoIndividual;

public interface IAdaptadorContactoIndividualDAO {
	
	public void registrarContacto(ContactoIndividual c);
	
	public ContactoIndividual recuperarContacto(int tel);
	
	public void modificarContacto(ContactoIndividual c);
}

package persistencia;

import modelo.ContactoIndividual;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorContactoIndividual {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorContactoIndividual unicaInstancia = null;
	
	public static AdaptadorContactoIndividual getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorContactoIndividual();
		else
			return unicaInstancia;
	}

	private AdaptadorContactoIndividual() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public void registrarContacto(ContactoIndividual c) {
		// TODO Auto-generated method stub
		
		
	}

}

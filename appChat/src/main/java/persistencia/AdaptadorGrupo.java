package persistencia;

import modelo.Grupo;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorGrupo {
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorGrupo unicaInstancia = null;

    public static AdaptadorGrupo getUnicaInstancia() { // patron singleton
        if (unicaInstancia == null)
            return new  AdaptadorGrupo();
        else
            return unicaInstancia;
    }

    private  AdaptadorGrupo() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    public void registrarGrupo(Grupo c) {

    }

    public void recuperarGrupo(Grupo c) {

    }

    public static void modificarGrupo(Grupo contacto) {

    }
}
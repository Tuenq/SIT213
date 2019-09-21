package transmetteurs;

import filtres.FiltreInterface;
import information.Information;
import information.InformationNonConforme;

public class Emetteur<E,R> extends Transmetteur<E,R> implements FiltreInterface {
    @Override
    public void recevoir(Information<E> information) throws InformationNonConforme {

    }

    @Override
    public void emettre() throws InformationNonConforme {

    }
}

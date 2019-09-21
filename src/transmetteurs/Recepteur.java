package transmetteurs;

import filtres.FiltreInterface;
import information.Information;
import information.InformationNonConforme;

public class Recepteur<R,E> extends Transmetteur<R,E> {
    @Override
    public void recevoir(Information<R> information) throws InformationNonConforme {

    }

    @Override
    public void emettre() throws InformationNonConforme {

    }
}

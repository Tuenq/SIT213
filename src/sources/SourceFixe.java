package sources;

import information.Information;
import information.InformationNonConforme;

/**
 * SourceFixe génére permet de définir la variable informationGeneree par rapport à un message fixe
 * passé en paramétre du constructeur, si ce message ne comporte pas strictement des boolean la méthode
 * emettre léve l'exception InformationNonComforme
 * @author Mathieu
 */
public class SourceFixe extends Source<Boolean> {

    public SourceFixe(Information<Boolean> messageFixe){
        super();

        informationGeneree = messageFixe;
    }
}
package transmetteurs;

import javax.xml.transform.Source;

import destinations.DestinationInterface;
import filtres.Filtre;
import filtres.FiltreNRZ;
import filtres.FiltreNRZT;
import filtres.FiltreRZ;
import information.Information;
import information.InformationNonConforme;
import sources.SourceFixe;
import visualisations.Sonde;
import visualisations.SondeAnalogique;

public class Emetteur extends Transmetteur<Boolean,Float> {

    public Information<Float> informationCodee=new Information<Float>();
    String forme;
    int nbEch;
    float amplMin;
    float amplMax;
    
    @Override
    public void recevoir(Information<Boolean> information) throws InformationNonConforme {
        informationRecue = information;
        forme=informationRecue.forme;
        nbEch=informationRecue.nbEch;
        amplMin=informationRecue.amplMin;
        amplMax=informationRecue.amplMax;

        if (forme.equals("NRZ")) {
        	FiltreNRZ codeur=new FiltreNRZ();
            informationEmise = codeur.codageNRZ(information, nbEch);
        }
        else if (forme.equals("NRZT")) {
        	FiltreNRZT codeur=new FiltreNRZT();
            informationEmise = codeur.codageNRZT(information, nbEch);
        }
        else { //Par defaut on effectue un codage RZ du signal numérique
        FiltreRZ codeur=new FiltreRZ();
        informationEmise = codeur.codageRZ(information, nbEch, amplMin, amplMax);
        emettre();
        }
    }


    @Override
    public void emettre() throws InformationNonConforme {
        //informationEmise
        //Pour chaque destination connectÃ©e on envoie les informations Ã  Ã©mettre
        for (DestinationInterface<Float> destination : destinationsConnectees) {
        	destination.recevoir(informationEmise);
        	
        }

    }
}

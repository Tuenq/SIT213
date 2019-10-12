package convertisseurs;

import communs.Outils;
import destinations.DestinationInterface;
import filtres.FiltreAdapte;
import filtres.FiltreMiseEnForme;
import information.Information;
import information.InformationNonConforme;
import transmetteurs.Transmetteur;
import visualisations.SondeAnalogique;

public class Recepteur extends Transmetteur<Float,Boolean> {
    private FiltreAdapte filtreAdapte;

    int[] retardCanal;
    Float [] attenuationCanal;
    int nbTrajetIndirect = 0;
	public Recepteur(FiltreMiseEnForme filtre) {
	    filtreAdapte = new FiltreAdapte(filtre);
	}
   public Recepteur(int nombreEchantillon, Float [] attenuationCanal, int [] retardCanal){
	    this.nombreEchantillon = nombreEchantillon;
	    this.attenuationCanal = attenuationCanal;
	    this.retardCanal = retardCanal;
	    nbTrajetIndirect = retardCanal.length;
    }

    @Override
    public void recevoir(Information<Float> information) throws InformationNonConforme {
	    informationRecue = information;
	    if(nbTrajetIndirect != 0) 
	    	filtreTrajetMultiple();
        calculAmplBruite();
        decodage();
        emettre();
    }
 

    private void decodage(){
	    informationEmise = new Information<>();
	    Float[] dataIn = informationRecue.getArray();
	    double[] signal = Outils.convertToDoubleArray(dataIn);

	    Boolean[] dataOut = filtreAdapte.appliquer(signal);

	    informationEmise = new Information<>(dataOut);
    }
    
    public void filtreTrajetMultiple() {
    	// recupérer le nbElement d'origine
    	int nbTrajetIndirect = retardCanal.length;
    	int maxRetard = retardCanal[nbTrajetIndirect - 1];
    	Float[] infoMelangee = informationRecue.getArray();
    	Float[] infoDetectee = new Float[infoMelangee.length - maxRetard];
    		
		//Suppression du premier trajet indirect
    	int debut = 0;
    	int fin = retardCanal[0];
		for (int j = debut; j<fin; j++) 
			infoDetectee[j] = infoMelangee[j];
	
		debut = fin;
		fin = (nbTrajetIndirect>1)? retardCanal[1] : infoDetectee.length; 
		for (int j = debut; j<fin; j++) 
			infoDetectee[j] = infoMelangee[j] - (attenuationCanal[0]* infoDetectee[j-retardCanal[0]]);
		
		//Suppression du second trajet multiple
		if(nbTrajetIndirect>=2) {
			debut = fin;
			fin = infoDetectee.length; 
			for (int j = debut; j<fin; j++) 
				infoDetectee[j] = infoMelangee[j] - (attenuationCanal[1] * infoDetectee[j-retardCanal[1]]);
		}
		//TODO : renvoyer une donnée au lieu de modifier informationRecue
		informationRecue = new Information<Float>(infoDetectee); 
    }

    @Override
    public void emettre() throws InformationNonConforme {
        for (DestinationInterface<Boolean> destination : destinationsConnectees) {
            destination.recevoir(informationEmise);
        }
    }
}



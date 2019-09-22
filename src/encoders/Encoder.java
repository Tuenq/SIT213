package filtres;

import information.Information;

public abstract class Filtre implements FiltreInterface {

	public enum availableEncoder {
		RZ, NRZ, NRZT;
	}

	Information<Float> informationCodee=new Information<Float>();
	Float[] infoRecuperee;
	Information <Boolean> infoATransmettre=new Information <Boolean>();
	private float amplMax=1;
	private float amplMin=0;


	public void echantilloneSymbole(float echantillon, float incrementation, float pasEch, float debut, float limite){
		float x=debut;
		while(x<limite || x==1) {
			informationCodee.add(echantillon);
			x+=pasEch;
			echantillon+=incrementation;
		}
	}

	public Information<Boolean> decodageBinaire(Information<Float> info, int nbEch){
		int nbSymbole = info.nbElements()/nbEch;
		amplMax=info.amplMax;
		amplMin=info.amplMin;

		for(int i=0;i<nbSymbole;i++) {
			// On récupère l'échantillon situé à la position t + 1/3T :
			//ce qui correspond au premier échantillon codé à 1 si le symbole est true
			float valeurRetournee = Math.max(info.iemeElement((nbEch*i)+(nbEch/3)+1), info.iemeElement((nbEch*i)+(nbEch/3)));
			infoRecuperee[i]=valeurRetournee;
			if((valeurRetournee == amplMax) && (amplMax!=amplMin))
				infoATransmettre.add(true);
			else
				infoATransmettre.add(false);
			}

		return infoATransmettre;

	}
}


package encoders;

import information.Information;

public abstract class Encoder implements EncoderInterface {

	public enum encoders {
		RZ, NRZ, NRZT;
	}

	Information<Float> informationCodee=new Information<Float>();
	Information <Boolean> infoATransmettre=new Information <Boolean>();
	float amplMax;
	float amplMin;


	public void echantilloneSymbole(float echantillon, float incrementation, float pasEch, float debut, float limite){
		float x=debut;
		while(x<limite || x==1) {
			informationCodee.add(echantillon);
			x+=pasEch;
			echantillon+=incrementation;
		}
	}

	public Information<Boolean> decodageBinaire(Information<Float> info, int nbEch, float amplMin, float amplMax){
		int nbSymbole = info.nbElements()/nbEch;
		this.amplMax=amplMax;
		this.amplMin=amplMin;

		for(int i=0;i<nbSymbole;i++) {
			// On récupère l'échantillon situé à la position t + 1/3T :
			//ce qui correspond au premier échantillon codé à 1 si le symbole est true
			float valeurRetournee = Math.max(info.iemeElement((nbEch*i)+(nbEch/3)+1), info.iemeElement((nbEch*i)+(nbEch/3)));
			if((valeurRetournee == amplMax) && (amplMax!=amplMin))
				infoATransmettre.add(true);
			else
				infoATransmettre.add(false);
			}

		return infoATransmettre;

	}
}


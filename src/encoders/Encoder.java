package encoders;

import information.Information;

public abstract class Encoder implements EncoderInterface {

	public enum encoders {
		RZ, NRZ, NRZT;
	}

	int nbEch;
	private float amplMax;
	private float amplMin;

	Encoder(int nbEch, float amplMin, float amplMax) {
		this.nbEch = nbEch;
		this.amplMin = amplMin;
		this.amplMax = amplMax;
	}

	Information<Float> echantilloneSymbole(Information<Boolean> data) {
		Information<Float> dataOut = new Information<>();
		for (Boolean datum : data) {
				for (int i = 0; i < nbEch; i++) {
					dataOut.add(datum ? amplMax : amplMin);
				}
		}
		return dataOut;
	}

	public Information<Boolean> decodage(Information<Float> info){
		Information<Boolean> dataOut = new Information<>();
		int nbSymbole = info.nbElements()/nbEch;
		for(int i=0;i<nbSymbole;i++) {
			// On récupère l'échantillon situé à la position t + 1/3T :
			//ce qui correspond au premier échantillon codé à 1 si le symbole est true
			float valeurRetournee = Math.max(info.iemeElement((nbEch*i)+(nbEch/3)+1), info.iemeElement((nbEch*i)+(nbEch/3)));
			if((valeurRetournee == amplMax) && (amplMax!=amplMin))
				dataOut.add(true);
			else
				dataOut.add(false);
			}
		return dataOut;
	}
}


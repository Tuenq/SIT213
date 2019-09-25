package encoders;

import information.Information;

public abstract class Encoder implements EncoderInterface {

	int nbEch;
	float amplMax;
	float amplMin;
	public enum encoders {
		RZ, NRZ, NRZT;
	}

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

	public Information<Boolean> decodage(Information<Float> data){
		Information<Boolean> dataOut = new Information<>();
		int compteur = 0;
		float sum = 0;

		for (Float datum: data) {
			sum += datum;
			if (compteur == nbEch) {
				dataOut.add(booleanDistance(sum/nbEch));
				sum = compteur = 0;
			}


			compteur = ++compteur % nbEch;
		}
		return dataOut;
	}

	/**
	 * Permet de déterminer, à partir des amplitudes théoriques, le symbole en fonction de la moyenne passé.
	 *
	 * @param mean Valeur moyenne du signal.
	 * @return Symbole décidé selon la distance entre les amplitudes maximum et minimum.<br>
	 * Dans le cas où la valeur moyenne est à équidistance des limites, la valeur false est retournée.
	 */
	boolean booleanDistance(float mean) {
		float maxDistance = Math.abs(mean - amplMax);
		float minDistance = Math.abs(mean - amplMin);

		return maxDistance < minDistance;
	}
}


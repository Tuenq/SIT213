package filtres;

import information.Information;
import information.InformationBooleen;

import java.util.Arrays;

public abstract class FiltreMiseEnForme {

	public enum forme {
		RZ, NRZ, NRZT;
	}

	int nbEch;
	private float amplMax;
	private float amplMin;
	float[] donneeFiltre;
	protected float puissanceFiltre = 0;
	private float puissanceMoyenneSortieFiltre = 0;


	FiltreMiseEnForme(int nbEch, float amplMin, float amplMax) {
		this.nbEch = nbEch;
		this.amplMin = amplMin;
		this.amplMax = amplMax;

		initialisationFiltre();
	}

	abstract void initialisationFiltre();

	public float[] echantillonage(Information<Boolean> data) {
		int cpt = 0;
		float[] dataOut = new float[data.nbElements() * nbEch];
		for (Boolean datum : data) {
			final float value_ech = datum ? amplMax : amplMin;
			puissanceMoyenneSortieFiltre += Math.abs(puissanceFiltre * Math.pow(value_ech, 2));
			final int index_start = cpt++ * nbEch;
			final int index_stop = index_start + nbEch;

			Arrays.fill(dataOut, index_start, index_stop, value_ech);
		}
		puissanceMoyenneSortieFiltre = puissanceMoyenneSortieFiltre / data.nbElements();
		return dataOut;
	}

	public void appliquer(float[] data) {
		for (int ech = 0; ech < data.length; ech++) {
			data[ech] *= donneeFiltre[ech % nbEch];
		}
	}

	float[] genererSymbole(boolean valeur) throws SymboleNulException {
		if (amplMax == 0f && valeur)  // Cas où la limite haute est nulle
			throw new SymboleNulException("Symbole positif généré d'amplitude nulle !");
		else if (amplMin == 0f && !valeur)
			throw new SymboleNulException("Symbole négatif généré d'amplitude nulle");

		Information<Boolean> valeur_symbole = new InformationBooleen(valeur);
		float[] symbole = echantillonage(valeur_symbole);
		appliquer(symbole);
		return symbole;
	}
	
    /**
     * Pour recup�rer la puissance moyenne en sortie du filtre de mise en forme
     * @return
     */
    public float getPuissanceMoyenneSortieFiltre() {
    	return puissanceMoyenneSortieFiltre;
    }
    
    
}


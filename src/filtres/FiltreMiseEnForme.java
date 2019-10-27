package filtres;

import information.Information;
import information.InformationBooleen;

import java.util.Arrays;

/**
 * Classe abstraite pour l'implémentation des filtres de mise en forme.
 */
public abstract class FiltreMiseEnForme {

	/**
	 * Liste les différentes mise en forme implémentées.
	 */
	public enum forme {
		RZ, NRZ, NRZT;
	}

	int nbEch;
	float amplMax;
	float amplMin;
	float[] donneeFiltre;
	protected float puissanceFiltre = 0;
	private float puissanceMoyenneSortieFiltre = 0;


	FiltreMiseEnForme(int nbEch, float amplMin, float amplMax) {
		this.nbEch = nbEch;
		this.amplMin = amplMin;
		this.amplMax = amplMax;

		initialisationFiltre();
	}

	/**
	 * Permet d'initialiser les données d'un filtre en fonction du nombre d'échantillon pour un symbole unitaire.
	 */
	abstract void initialisationFiltre();

	/**
	 * Génère l'équivalent d'une valeur booléenne en une liste de valeur flottante.
	 * @param data Liste d'information booléenne.
	 * @return Liste d'information analogique.
	 */
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
    
    /**
     * Pour recup�rer la puissance moyenne en sortie du filtre de mise en forme
     * @return
     */
    public float getPuissanceMoyenneSortieFiltre() {
    	return puissanceMoyenneSortieFiltre;
    }
}


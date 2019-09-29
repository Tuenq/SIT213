package visualisations;
	
import communs.Outils;
import information.Information;

/** 
 * Classe réalisant l'affichage de l'histogramme des valeurs d'une information
 * composée d'éléments de type réel (float)
 */
public class SondeHistogramme extends SondeAnalogique {

	private Integer nbPlage;

    /**
     * pour construire une sonde puissance
	 * @param nombrePlage  le nombre d'intervales à utiliser pour l'histogramme
     * @param nom le nom de la fenêtre d'affichage
     */
    public SondeHistogramme(int nombrePlage, String nom) {
		super(nom);
		nbPlage = nombrePlage;
    }

    private Float[] calculHistogramme() {
    	Float[] ampl = Outils.calculAmplitude(informationRecue);
		float amplMax = ampl[Outils.amplIndex.MAX.ordinal()];
		float amplMin = ampl[Outils.amplIndex.MIN.ordinal()];

		float pas = (amplMax - amplMin) / nbPlage;

		Float[] histo = new Float[nbPlage];

		for (int plage = 0; plage < nbPlage; plage++) {
			float limitLow = amplMin + pas*plage;
			float limitHigh = amplMin + pas*(plage+1);

			histo[plage] = 0f;

			for (Float datum: informationRecue) {
				if (limitLow <= datum && datum < limitHigh)
					histo[plage]++;
			}

//			System.out.println("plage " + plage + " : ["+limitLow+";"+limitHigh+"] = " + histo[plage]);
		}

		return histo;
	}

	@Override
    public void recevoir(Information <Float> information) {
		informationRecue = information;
		Float[] histo = calculHistogramme();
		super.recevoir(new Information<>(histo));
    }
}

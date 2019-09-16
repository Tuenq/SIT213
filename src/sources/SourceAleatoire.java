package sources;

import information.Information;

import java.lang.Boolean;
import java.util.*;



/**
 * SourceAleatoire
 */
public class SourceAleatoire extends Source<Boolean> {

    int taille;

    public SourceAleatoire(int nbBit) {
        taille = nbBit;
        informationGeneree = new Information<Boolean>();

        for (int j=0; j<= taille; j++) {
            Random rnd = new Random();
            boolean value = rnd.nextBoolean();
            informationGeneree.add(value);
        }
    }

    public SourceAleatoire(int nbBit, long seed) {
        taille = nbBit;
        informationGeneree = new Information<Boolean>();

        for (int j = 0; j <= taille; j++) {
            Random rnd = new Random(seed);
            boolean value = rnd.nextBoolean();
            informationGeneree.add(value);
        }

    }

    public Information<Boolean> getInformationEmise(){
        informationEmise= informationGeneree;
        return informationEmise;
    }

    public static void main (String[] args){
        Source S = new SourceAleatoire(10);
        System.out.println(S.getInformationEmise());

        Source S2 = new SourceAleatoire(10, -10); //false pour seed <0 et true pour seed > 0
        System.out.println(S2.getInformationEmise());

    }
}


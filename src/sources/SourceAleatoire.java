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

        for (int j=0; j<= taille; j++) {
            informationGeneree = new Information<Boolean>();
            Random rnd = new Random();
            boolean value = rnd.nextBoolean();
            informationGeneree.add(value);
        }
    }

    public SourceAleatoire(int nbBit, long seed) {
        taille = nbBit;

        for (int j = 0; j <= taille; j++) {
            informationGeneree = new Information<Boolean>();
            Random rnd = new Random(seed);
            boolean value = rnd.nextBoolean();
            informationGeneree.add(value);
        }

    }

    public static String toString(){
        System.out.println();
    }
}


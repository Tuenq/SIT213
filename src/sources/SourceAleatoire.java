package sources;

import information.Information;

import java.lang.Boolean;
import java.util.*;



/**
 * SourceAleatoire génére une suite aléatoire de Boolean avec ou sans seed pour une quantité préconfiguré d'item
 * @author Ludovic
 */
public class SourceAleatoire extends Source<Boolean> {

    public SourceAleatoire(int nbBit, boolean utilisationGerme, long seed) {
        informationGeneree = new Information<Boolean>();
        Random random;
        if (utilisationGerme)
            random = new Random(seed);
        else
            random = new Random();

        boolean value;
        for (int j = 0 ; j < nbBit ; j++) {
            value = random.nextBoolean();
            informationGeneree.add(value);
        }

    }

    public static void main (String[] args){
        Source instance1 = new SourceAleatoire(6, true, 6364664);
        try {
            instance1.emettre();
        }
        catch (Exception exception){
            System.out.println(exception);
        }
        System.out.println(instance1.getInformationEmise());

    }
}


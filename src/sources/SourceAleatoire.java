package sources;

import information.Information;

import java.lang.Boolean;
import java.util.*;

/**
 * SourceAleatoire génére une suite aléatoire de Boolean avec ou sans seed pour une quantité préconfigurée d'item
 *
 * @author Ludovic
 */
public class SourceAleatoire extends Source<Boolean> {
    /**
     * Determine une chaine aleatoire d'un certain nombre de bits
     * @param nbBit taille du message en bits
     */
    public SourceAleatoire(int nbBit) {
        informationGeneree = new Information<>();
        Random random = new Random();

        boolean value;
        for (int j = 0; j < nbBit; j++) {
            value = random.nextBoolean();
            informationGeneree.add(value);
        }

    }

    /**
     * Determine une chaine aleatoire correspondant a la seed d'un certain nombre de bits
     * @param nbBit taille du message en bits
     * @param seed correspondant a une sequence aleatoire precise
     */
    public SourceAleatoire(int nbBit, Integer seed) {
        informationGeneree = new Information<>();
        Random random = new Random(seed);  // FIXME: Call super and extract Random

        boolean value;
        for (int j = 0; j < nbBit; j++) {
            value = random.nextBoolean();
            informationGeneree.add(value);
        }
    }

    public static void main(String[] args) {
        Source instance1 = new SourceAleatoire(6, 6364664);
        try {
            instance1.emettre();
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
        System.out.println(instance1.getInformationEmise());
    }
}


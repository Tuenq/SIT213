package bruit;

import information.Information;
import visualisations.Sonde;
import visualisations.SondeAnalogique;
import visualisations.SondeHistogramme;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

/**
 * La classe BruitGaussien permet de:
 *
 * - Specifier les différents paramétres que la loi gaussienne doit suivre
 * - Générer un nombre specifique de valeurs aléatoire suivant cette loi
 * - Récupérer les valeurs aléatoire générées 
 */
public class BruitGaussien {
    private Random uniformLaw;
    private Float[] donneeBruit;

    /**
     * Création de la loi de génération du bruit gaussien sans seed
     */
    public BruitGaussien() {
        uniformLaw = new Random();
    }

    /**
     * Création de la loi de génération du bruit gaussien avec seed
     * @param seed specifie la seed de génération des valeurs aléatoire du bruit
     */
    public BruitGaussien(int seed) {
        uniformLaw = new Random(seed);
    }

    /**
     * Génération d'une valeur aléatoire, multiplier par l'écart type de notre gaussienne
     * @param ecartType valeur de l'écart type
     * @return la valeur aléatoire du bruit générer
     */
    private float genererEchantillon(float ecartType) {
        return (float) (uniformLaw.nextGaussian()*ecartType);
    }

    /**
     * Génere un nombre de valeurs spécifié de bruit pour un écart type spécifié
     * @param ecartType specifie l'écart type de notre loi gaussienne
     * @param size specifie le nombre de valeur aléatoire à générer
     */
    public void initialiser(float ecartType, int size) {
        donneeBruit = new Float[size];

        for (int i = 0; i < size; i++) {
            donneeBruit[i] = genererEchantillon(ecartType);
        }
    }

    /**
     * Renvoi les valeurs de bruit présente dans la variable donneeBruit
     * @return les valeurs de bruit
     */
    public Information<Float> recuperationBruit() {
        return new Information<>(donneeBruit);
    }

    /**
     * Permet d'appliquer un bruit gaussien sur une sequence de bits donnée
     * @param dataIn information en entree
     * @param dataIn_len taille de l'information recue
     * @return
     */
    public Float[] appliquer(Information<Float> dataIn, int dataIn_len) {
        Float[] dataOut =  new Float[dataIn_len];
        Iterator<Float> dataIn_Iterator = dataIn.iterator();

        for (int index = 0; index < dataIn_len; index++) {
            dataOut[index] = donneeBruit[index]  + dataIn_Iterator.next();
        }

        return dataOut;
    }

    public static void main(String[] args) {
        BruitGaussien bruit = new BruitGaussien();
        bruit.initialiser(1f, 1000000);
        Information<Float> information = bruit.recuperationBruit();

        for (int i = 0 ; i < information.nbElements() ; i++){
            try {
                FileWriter fileWriter = new FileWriter("BruitGaussien.csv", true);
                fileWriter.write( information.iemeElement(i) + "\n");
                fileWriter.close();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}

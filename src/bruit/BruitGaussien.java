package bruit;

import information.Information;
import visualisations.Sonde;
import visualisations.SondeAnalogique;
import visualisations.SondeHistogramme;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class BruitGaussien {
    private Random uniformLaw;
    private Float[] donneeBruit;

    public BruitGaussien() {
        uniformLaw = new Random();
    }

    public BruitGaussien(int seed) {
        uniformLaw = new Random(seed);
    }

    private float genererEchantillon(float ecartType) {
        return (float) (uniformLaw.nextGaussian()*ecartType);
    }

    public void initialiser(float ecartType, int size) {
        donneeBruit = new Float[size];

        for (int i = 0; i < size; i++) {
            donneeBruit[i] = genererEchantillon(ecartType);
        }
    }

    public Information<Float> recuperationBruit() {
        return new Information<>(donneeBruit);
    }

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

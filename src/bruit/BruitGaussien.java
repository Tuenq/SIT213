package bruit;

import information.Information;

import java.util.Iterator;
import java.util.Random;

public class BruitGaussien {
    private Random a1;
    private Random a2;
    private Float[] donneeBruit;

    public BruitGaussien() {
        a1 = new Random();
        a2 = new Random();
    }

    public BruitGaussien(int seed) {
        a1 = new Random(seed);
        a2 = new Random(++seed);
    }

    private float genererEchantillon(float ecartType) {
        double pa1 = -2.0f * Math.log(1.0f - a1.nextFloat());
        double pa2 = Math.cos(2.0f * Math.PI * a2.nextFloat());

        double data = ecartType * Math.sqrt(pa1) * pa2;
        return (float) data;
    }

    public void initialiser(float ecartType, int size) {
        donneeBruit = new Float[size];

        for (int i = 0; i < size; i++) {
            donneeBruit[i] = genererEchantillon(ecartType);
        }
    }

    public Float[] appliquer(Information<Float> dataIn, int dataIn_len) {
        Float[] dataOut =  new Float[dataIn_len];
        Iterator<Float> dataIn_Iterator = dataIn.iterator();

        for (int index = 0; index < dataIn_len; index++) {
            dataOut[index] = donneeBruit[index]  + dataIn_Iterator.next();
        }

        return dataOut;
    }
}

package information;

public class InformationAnalogique extends Information<Float> {

    public InformationAnalogique(float[] data) {
        super();
        for (float datum:data)
            this.add(datum);
    }
}

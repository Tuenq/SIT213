package canaux;

import information.Information;

public class CanalIndirect {

    private final int distanceTemporelle;
    private final float amplitudeRelative;

    public CanalIndirect(int distanceTemporelle, float amplitudeRelative) {
        this.distanceTemporelle = distanceTemporelle;
        this.amplitudeRelative = amplitudeRelative;
    }

    public Float[] simulationTrajet(Float[] data) {
        appliquerAttenuation(data);
        appliquerRetard(data);
        return data;
    }

    private void appliquerAttenuation(Float[] data) {

    }

    private void appliquerRetard(Float[] data) {

    }
}

package encoders;

import information.Information;

public interface EncoderInterface {
    Information<Float> codage(Information<Boolean> data);
}

package information;

public class InformationBooleen extends Information<Boolean> {
    public InformationBooleen(String booleanString) throws InformationNonConforme {
        super();

        int len = booleanString.length();
        for (int index = 0; index < len; index++) {
            char caractere = booleanString.charAt(index);
            switch (caractere) {
                case '1':
                    this.add(true);
                    break;
                case '0':
                    this.add(false);
                    break;
                default:
                    throw new InformationNonConforme("Le caractère [" + caractere + "] ne peut être cnvertie en booléen !");
            }
        }
    }
}

package information;

/**
 * La classe InformationBooleen hérite de la classe Information et stock des données de type Boolean
 */
public class InformationBooleen extends Information<Boolean> {
    public InformationBooleen(boolean valeur) {
        super();
        this.add(valeur);
    }

    /**
     * Constructeur de la classe, permet de cloner les donnée présente dans la chaine de string et de les stocker
     * dans le nouvel object InformationBooleen
     * @param booleanString les data à cloner
     */
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

package sources;

import information.InformationNonConforme;

/**
 * SourceFixe
 */
public class SourceFixe extends Source<Boolean> {

    public SourceFixe(){
        super();
    }

    public void emettre(String messagefixe) throws InformationNonConforme {
        for(int i=0;i<messagefixe.length();i++) {
            if(messagefixe.charAt(i) == '1')
                informationGeneree.add(true);
            else if(messagefixe.charAt(i) == '0')
                informationGeneree.add(false);
            else {
                throw new InformationNonConforme("un ou plusieurs caractères ne sont pas de type booleen");
            }
        }
        super.emettre();
    }
}
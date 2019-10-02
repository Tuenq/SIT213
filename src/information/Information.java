package information;

import java.util.*;

/**
 * @author prou
 */
public class Information<T> implements Iterable<T> {

	private LinkedList<T> content;

    /**
     * pour construire une information vide
     */
    public Information() {
        this.content = new LinkedList<>();
    }

    /**
     * pour construire à partir d'un tableau de T une information
     *
     * @param content le tableau d'éléments pour initialiser l'information construite
     */
    public Information(T[] content) {
        this.content = new LinkedList<T>();
        for (T t : content) {
            this.content.addLast(t);
        }
    }

    public Information(Information<T> information) {
        this.content = new LinkedList<>();
        Iterator<T> contentIterator = information.iterator();
        contentIterator.forEachRemaining(this.content::add);
    }

    public static Information<Boolean> stringToBoolean(String messageString)
            throws InformationNonConforme {
        Information<Boolean> message = new Information<>();

        for (int i = 0; i < messageString.length(); i++) {
            switch (messageString.charAt(i)) {
                case '1':
                    message.add(true);
                    break;
                case '0':
                    message.add(false);
                    break;
                default:
                    throw new InformationNonConforme("un ou plusieurs caractères ne sont pas de type booleen");
            }
        }
        return message;
    }

    /**
     * pour connaître le nombre d'éléments d'une information
     *
     * @return le nombre d'éléments de l'information
     */
    public int nbElements() {
        return this.content.size();
    }

    /**
     * pour renvoyer un élément d'une information
     * @param i Valeur de l'index de l'élément
     * @return le ieme élément de l'information
     */
    public T iemeElement(int i) {
        return this.content.get(i);
    }

    /**
     * pour modifier le ième élément d'une information
     * @param i Valeur de l'index de l'élément
     * @param v Valeur de l'élément
     */
    public void setIemeElement(int i, T v) {
        this.content.set(i, v);
    }

    /**
     * pour ajouter un élément à la fin de l'information
     *
     * @param valeur l'élément à rajouter
     */
    public void add(T valeur) {
        this.content.add(valeur);
    }

    /**
     * pour comparer l'information courante avec une autre information
     *
     * @param o l'information  avec laquelle se comparer
     * @return "true" si les 2 informations contiennent les mêmes
     * éléments aux mêmes places; "false" dans les autres cas
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (!(o instanceof Information))
            return false;
        Information<T> information = (Information<T>) o;
        if (this.nbElements() != information.nbElements())
            return false;
        for (int i = 0; i < this.nbElements(); i++) {
            if (!this.iemeElement(i).equals(information.iemeElement(i)))
                return false;
        }
        return true;
    }

    /**
     * pour afficher une information
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.nbElements(); i++) {
            s.append(" ").append(this.iemeElement(i));
        }
        return s.toString();
    }

    /**
     * Pour utilisation du "for each"
     */
    public Iterator<T> iterator() {
        return content.iterator();
    }
    
    //FIX ME A ameliorer
    public Information<Float> amplifierAttenuer(Information<Float> dataIn, float ampl) {
    	Information<Float> dataOut = new Information<Float>();
    	for(float datum: dataIn) {
    		dataOut.add(datum*ampl);
    	}
		return dataOut;	
    }
    
    public Information<Float> additioner(Information<Float> dataIn1, Information<Float> dataIn2) throws InformationNonConforme {

    	//FIX ME en attendant que les informations soient des tableaux
    	//float[] tabDataIn1 =  dataIn1.getArray();
    	Information<Float> dataOut = new Information<Float>();
    	for(int i=0; i<dataIn1.nbElements(); i++) {
    		dataOut.add(dataIn1.iemeElement(i)+dataIn2.iemeElement(i));
    	}
		return dataOut;	
    }
    
}

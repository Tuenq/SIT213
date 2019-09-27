package destinations;

import information.*;


/** 
 * Interface d'un composant ayant le comportement d'une destination
 * d'informations dont les éléments sont de type T
 * @author prou
 */
public  interface DestinationInterface <T>  {   
   
    /**
     * pour obtenir la dernière information reçue par une destination.
     * @return une information   
     */  
    public Information <T>  getInformationRecue(); 
   	 
    /**
     * Pour recevoir une information de la source qui nous est
     * connectée
     * @throws InformationNonConforme Cas de l'information est invalide
     * @param information  l'information  à recevoir
     */
    public void recevoir(Information <T> information) throws InformationNonConforme;
}

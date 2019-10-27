package information;

/**
 * Exception InformationNonComforme héritant de Exception
 */
public class InformationNonConforme extends Exception {
   
    private static final long serialVersionUID = 1917L;
    
    public InformationNonConforme() {
	super();
    }
   
    public InformationNonConforme(String motif) {
	super(motif);
    }
}

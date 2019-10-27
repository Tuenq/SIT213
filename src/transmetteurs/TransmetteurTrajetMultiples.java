		package transmetteurs;
		
		import destinations.DestinationInterface;
		import information.Information;
		import information.InformationNonConforme;
		import visualisations.SondeAnalogique;

		/**
		 * La classe TransmetteurTrajetMultiples héritant de la classe Transmetteur permet d'ajouter un élément dans
		 * la chaine de transmission, générant un ensemble de trajet indirect en fonction des paramétre du transmetteur.
		 */
		public class TransmetteurTrajetMultiples extends Transmetteur<Float,Float>{

			private int[] retard;
			private int nombreTrajet;
			private Float[] amplitude;
			private boolean afficherTrajetsIndirects = false;

			/**
			 * Permet de prendre en compte le nombre de trajets supplementaires et leur retard et amplitude respectifs
			 * @param retard defini l'ensemble des retards concernant les trajets mutliples ajoutes
			 * @param amplitude defini respectivement les ammplitudes concernant les trajets multiples ajoutes
			 */
			public TransmetteurTrajetMultiples(int[] retard, Float[] amplitude) {
				nombreTrajet=amplitude.length;
				this.amplitude=amplitude;
				this.retard=retard;
			}
			/**
			 * Permet de recuperer l'information, applique le multi trajets et retransmet l'information aux destinations connectees
			 * @param information  l'information  reçue
			 * @throws InformationNonConforme Cas d'erreur remonté par l'information
			 */
			@Override
			public void recevoir(Information<Float> information) throws InformationNonConforme {
			    informationRecue = information;
		        genererSignalTrajetMultiple();
		        emettre();
			}
			/**
			 * Permet l'émission des données vers les destinations connectées.
			 * @throws InformationNonConforme Cas où l'information est invalide
			 */
			@Override
			public void emettre() throws InformationNonConforme {
				   for (DestinationInterface<Float> destination : destinationsConnectees) {
		               destination.recevoir(informationEmise);
		       }
				
			}

			/**
			 * Ajoute au signal direct l'ensemble des trajets multiplles selon leurs caracteristiques
			 */
			private void genererSignalTrajetMultiple(){

				Float[] signalTrajetDirect = informationRecue.getArray();
				Float[] signalPrecedent = signalTrajetDirect;
				//TODO : recupérer l'ampl max et min du signal à partir de là avec un array list 

			

				for(int i=0; i<nombreTrajet; i++) {
			
					Float[] signalGenere = new Float[signalTrajetDirect.length + retard[i]];
					Float[] signalAttenueRetarde = new Float[signalGenere.length];
					
					for(int j=0; j<retard[i];j++) 
						signalAttenueRetarde[j]=0f;
					
					for(int j=retard[i]; j<signalTrajetDirect.length + retard[i]; j++) 
						signalAttenueRetarde[j]=(amplitude[i]*signalTrajetDirect[j-retard[i]]);
					
					for(int j=0; j<signalPrecedent.length; j++) 
						signalGenere[j] = signalPrecedent[j] + signalAttenueRetarde[j];

					for(int j=signalPrecedent.length; j<signalAttenueRetarde.length; j++) 
						signalGenere[j] = signalAttenueRetarde[j];
					
					signalPrecedent=signalGenere;

					if(afficherTrajetsIndirects) {
						SondeAnalogique sonde=new SondeAnalogique(" signal trajet indirect " + (i+1));
						sonde.recevoir(new Information<>(signalAttenueRetarde));
					}
				}
			
				informationEmise=new Information<>(signalPrecedent);
				informationEmise.setPuissanceMoyenne(informationRecue.getPuissaanceMoyenne());
			}
		
			/**
			 * Affichage des signaux de trajets indirects
			 */
			public void afficher() {
				afficherTrajetsIndirects = true;
			}

		}
		

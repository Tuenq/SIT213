		package transmetteurs;
		
		import destinations.DestinationInterface;
		import information.Information;
		import information.InformationNonConforme;
		import visualisations.SondeAnalogique;
		
		public class TransmetteurTrajetMultiples extends Transmetteur<Float,Float>{

			private int[] retard;
			private int nombreTrajet;
			private Float[] amplitude;
			private boolean afficherTrajetsIndirects = false;

			/**
			 *
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
			
			private void genererSignalTrajetMultiple(){

				Float[] signalTrajetDirect = informationRecue.getArray();
				Float[] signalPrecedent = signalTrajetDirect;

				int longueur = retard.length;
				int tampon = 0;
				boolean permut;

				do {
					// hypothèse : le tableau est trié
					permut = false;
					for (int i = 0; i < longueur - 1; i++) {
						// Teste si 2 éléments successifs sont dans le bon ordre ou non
						if (retard[i] > retard[i + 1]) {
							// s'ils ne le sont pas, on échange leurs positions
							tampon = retard[i];
							retard[i] = retard[i + 1];
							retard[i + 1] = tampon;
							permut = true;
						}
					}
				} while (permut);

				for(int i=0; i<nombreTrajet; i++) {
			
					Float[] signalGenere = new Float[signalTrajetDirect.length + retard[i]];
					Float[] signalAttenueRetarde = new Float[signalGenere.length];
					
					for(int j=0; j<retard[i];j++) 
						signalAttenueRetarde[j]=0f;
					
					for(int j=retard[i]; j<signalTrajetDirect.length + retard[i]; j++) 
						signalAttenueRetarde[j]=(amplitude[i]*signalTrajetDirect[j-retard[i]]);
					
					for(int j=0; j<signalPrecedent.length; j++) 
						signalGenere[j] = signalPrecedent[j] + signalAttenueRetarde[j];

					if (signalAttenueRetarde.length - signalPrecedent.length >= 0)
						System.arraycopy(signalAttenueRetarde, signalPrecedent.length, signalGenere, signalPrecedent.length, signalAttenueRetarde.length - signalPrecedent.length);
					
					signalPrecedent=signalGenere;

					if(afficherTrajetsIndirects) {
						SondeAnalogique sonde=new SondeAnalogique(" signal trajet indirect " + (i+1));
						sonde.recevoir(new Information<>(signalAttenueRetarde));
					}
				}
			
				informationEmise=new Information<>(signalPrecedent);
			}
		
			/**
			 * Affichage des signaux de trajets indirects
			 */
			public void afficher() {
				afficherTrajetsIndirects = true;
			}

		}
		

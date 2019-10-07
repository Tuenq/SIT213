		package transmetteurs;
		
		import destinations.DestinationInterface;
		import information.Information;
		import information.InformationNonConforme;
		import visualisations.SondeAnalogique;
		
		public class TransmetteurTrajetMultiples extends Transmetteur<Float,Float>{
		
			int nombreTrajet=0;
			Float[] amplitude;
			int[] retard;
			Boolean afficherTrajetsIndirects = false;
			
			public TransmetteurTrajetMultiples(Float[] amplitude, int[] retard) {
				nombreTrajet=amplitude.length;
				this.amplitude=amplitude;
				this.retard=retard;
			}
		
			@Override
			public void recevoir(Information<Float> information) throws InformationNonConforme {
			    informationRecue = information;
		        genererSignalTrajetMultiple();
		        emettre();
			}
		
			@Override
			public void emettre() throws InformationNonConforme {
				   for (DestinationInterface<Float> destination : destinationsConnectees) {
		               destination.recevoir(informationEmise);
		       }
				
			}
			
			private Information<Float> genererSignalTrajetMultiple(){

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
						
					for(int j=signalPrecedent.length; j<signalAttenueRetarde.length; j++) 
						signalGenere[j] = signalAttenueRetarde[j];
					
					signalPrecedent=signalGenere;
					System.out.println(afficherTrajetsIndirects);
					if(afficherTrajetsIndirects) {
					SondeAnalogique sonde=new SondeAnalogique(" signal trajet indirect " + (i+1));
					sonde.recevoir(new Information<>(signalAttenueRetarde));
					}
				}
			
				informationEmise=new Information<>(signalPrecedent);
				return informationEmise;
			}
		
			/**
			 * Activer l'affichage des signaux à trajets indirects
			 */
			public void afficherTrajetsIndirects() {
				afficherTrajetsIndirects = true ; 
			}

		}
		

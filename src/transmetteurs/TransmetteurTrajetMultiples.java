		package transmetteurs;
		
		import destinations.DestinationInterface;
		import information.Information;
		import information.InformationNonConforme;
		import visualisations.SondeAnalogique;
		
		public class TransmetteurTrajetMultiples extends Transmetteur<Float,Float>{
		
			int nombreTrajet=0;
			Float[] amplitude;
			int[] retard;
			Information<Information<Float>> infoTrajetsIndirects=new Information<Information<Float>>();
			
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
						
					for(int j=signalPrecedent.length; j<signalTrajetDirect.length + retard[i];j++) 
						signalGenere[j] = signalAttenueRetarde[j-retard[i]];
					
					infoTrajetsIndirects.add(new Information<>(signalAttenueRetarde));
					signalPrecedent=signalGenere;
		
				}
			
				informationEmise=new Information<>(signalPrecedent);
				return informationEmise;
			}
		
			/**
			 * Affichage des signaux de trajets indirects
			 */
			public void afficherTrajetsIndirects() {

				int numeroTrajet=1;
				for(Information<Float> trajetIndirect : infoTrajetsIndirects) {
				SondeAnalogique sonde=new SondeAnalogique(" signal du trajet " +  numeroTrajet++);
				sonde.recevoir(trajetIndirect);
				}
			}

		}
		

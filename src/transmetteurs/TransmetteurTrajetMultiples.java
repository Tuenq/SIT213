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


			public TransmetteurTrajetMultiples(int[] retard, Float[] amplitude) {
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
			
			private void genererSignalTrajetMultiple(){

				Float[] signalTrajetDirect = informationRecue.getArray();
				Float[] signalPrecedent = signalTrajetDirect;

			

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
		

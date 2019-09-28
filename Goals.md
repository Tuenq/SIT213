# Simulation d'un système de transmission

> SIT210 - Atelier logiciel - Année scolaire 2019-202

## Objectifs principaux

Réalisation d'une maquette logicielle en Java simulant un système de transmission numérique élémentaire.

L'évaluation portera sur la qualité technique de la réalisation, notamment :

* Qualité de la documentation de la maquette logicielle (notamment la javadoc)
* Effort de validation des résultats de simulation produits par la maquette
* Maîtrise du processus de travail
  * Gestion des versions successives de la maquette logicielle et du dossier technique
  * Démarche qualité (Respect des exigences de mise en forme du livrable)
  * Synergie de l'équipe

## Livrables

Pour chaque itération :

- Livraison la veille de la séance suivante avant 23:55 sur [Moodle](https://moodle.imt-atlantique.fr/course/view.php?id=580).
- Le **rapport** (compte-rendu) livré sous forme de `pdf`  comportant :
  - Une nouvelle section concernant la nouvelle étape
  - Nommé `Bartoli-Dumestre-Francis-Hugdelarauze-X` avec `X` le numéro d'itération
  - Actualisé donnant les faits marquants et les observations faites sur la base du simulateur.
  - Il devra confronter les **résultats observés** aux prévisions initiales (explicitant la **liste des paramètres pertinents** de la simulation et les **résultats attendus**)
- Le **simulateur** (logiciel) livré dans une archive `tar.gz` formatté de la façon suivante :
  - Nommé `Bartoli-Dumestre-Francis-Hugdelarauze.tar.gz`
  - Contenant les dossiers :
    - `src` : Code JAVA
    - `bin` : (Généré à la demande) Fichiers .class
    - `docs`  : (Généré à la demande) Fichiers javadoc
  - Contenant les scripts :
    - `compile`
    - `genDoc`
    - `cleanAll`
    - `simulateur`  : Spécifié dans le document `commande unique`
    - `runTests` : Indique l'état des auto-test
  - Un fichier `readme`  (description et utilisation)

### Définition des objectifs des séances

* Étape 0 : *Déploiement de logiciel*
* Étape 1 : *Transmission élémentaire "back-to-back"*
* Étape 2 : *Transmission non bruitée d'un signal analogique*
* Étape 3 : *Transmission non-idéale avec canal bruité de type « gaussien »*
* Étape 4 : 
  * A) *Transmission non-idéale avec divers bruit « réels »*
  * B) *Transmission non-idéale avec divers bruit « réels » + le bruit « gaussien »*
* Étape 5 : 
  * A) *Codage de canal*
  * B) *Codage de canal avec bruit de type « gaussien »*
* Étape 6 : Simulation avec une étude de cas soumise par le client
* Étape 7 : Livraison du logiciel - Dédiée à la restitution du travail :
  * Présentation (10min) du bilan organisationnel et technique du logiciel (avec démonstration possible).
  * Questions/Réponses (5min)


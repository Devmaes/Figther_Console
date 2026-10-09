# Plan de validation

## Contrôles effectués hors Maven

- Compilation Java 17 des sources : réussie avec `javac --release 17`.
- Vérifications du domaine et des contre-attaques : exécutées par un harnais Java autonome.
- Cinquante simulations d'IA contre IA sur des seeds fixes : toutes terminées par K.O. ou double K.O., sans boucle de 500 échanges.
- Combat humain scripté en mode Duel : un joueur remporte la partie quand l'adversaire atteint 0 vie.

## Vérifications automatiques dans GitHub Actions

`mvn clean verify` exécute JUnit 5 et l'intégration SQLite puis produit un JAR exécutable. Les résultats CI doivent être vérifiés avant de qualifier une version de release.

## Test manuel conseillé

Jouer une partie en SOLO avec la musique activée et désactivée ; essayer une attaque de puissance 5 face à blocage 5 ; tester une contre-attaque 4 vs 9 ; vérifier que l'IA réagit et que le classement conserve les scores entre deux lancements.

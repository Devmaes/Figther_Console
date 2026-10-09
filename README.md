# ⚔ Fighter Console — édition stratégique 2.0

> **Un combat au tour par tour où chaque attaque attend une réaction avant de produire ses effets.**
>
> **Developed by Devmaes** · Java 17+ · Console ANSI · SQLite locale · IA tactique · Ambiance sonore originale

## Aperçu

```text
╔═══════════════════════════════════════════════════════════════════════╗
║           F I G H T E R   C O N S O L E   //   V 2 . 0               ║
║             STRATÉGIE • CONTRE-ATTAQUE • SURVIE | DEVMAES            ║
╚═══════════════════════════════════════════════════════════════════════╝

 [1] ⚔ SOLO         Affronter une IA tactique
 [2] ⚔ DUEL         Deux joueurs sur le même terminal
 [3] ★ TOP 5        Classement local des meilleurs scores
 [4] ? RÈGLES       Comprendre les échanges et les points
 [5] ♪ MUSIQUE      activée
 [0] ⏻ QUITTER
```

Cette version est une refonte de l'expérience Java initiale publiée en 2024. Le dépôt préserve son historique Git ; le moteur est désormais isolé de la console, des données, du son et de l'IA.

## Le principe original

Un joueur déclare une attaque, **l'adversaire choisit de bloquer, de contre-attaquer ou d'encaisser**, puis seulement après cette réaction, le moteur résout l'échange et met à jour les points. Le combat s'arrête lorsqu'un combattant tombe à **0 point de vie**.

Les valeurs initiales (3 vies, 15 points d'attaque, 7 points de défense), la récupération via blocage et l'idée d'attaques par puissance viennent du prototype. La différence de puissance entre contre-attaques, le repos, les plafonds et les règles de classement sont des précisions ajoutées pour rendre le nouveau moteur déterministe et jouable. Elles sont documentées et modifiables.

Voir les [règles exhaustives](docs/REGLES.md).

## Fonctionnalités

- Interface en console avec panneaux Unicode, couleurs ANSI optionnelles, barres de ressources et messages de combat.
- Deux modes : duel local et solo contre cinq adversaires aux caractéristiques différenciées.
- IA à **recherche tactique sur un échange** : simule les réactions légales, évalue dégâts et ressources, puis sélectionne ses actions selon l'état du combat.
- Musique d'ambiance arcade créée par synthèse audio **originale en Java** ; démarre avec le combat, boucle, puis s'arrête à la fin. Se désactive si aucun périphérique sonore n'est disponible.
- Base **SQLite locale** pour les parties gagnées et affichage des **5 meilleurs scores**. Pas de cloud, pas de compte utilisateur.
- Entrées de console validées, calcul des règles purement fonctionnel, tests unitaires et intégration SQLite.
- CI GitHub Actions avec Maven.

## Technologies et architecture

| Composant | Choix |
| --- | --- |
| Langage | Java 17+ |
| Build / dépendances | Maven |
| Interface | Terminal Unicode, ANSI facultatif |
| Logique du jeu | `Combat` (état immuable, action/réaction/résolution) |
| IA | `CombatAI` (évaluation tactique à un échange) |
| Son | Java Sound API (boucle procédurale, sans contenu tiers) |
| Persistance | SQLite + JDBC, uniquement en local |
| Tests | JUnit 5, GitHub Actions |

## Installer et démarrer

Pré-requis : **JDK 17+** et **Maven 3.8+**.

```bash
git clone https://github.com/Devmaes/Figther_Console.git
cd Figther_Console
mvn clean package
java -jar target/fighter-console-2.0.0.jar
```

Version silencieuse et sans couleur :

```bash
java -jar target/fighter-console-2.0.0.jar --no-sound --no-color
```

Sur un ordinateur sans Maven, compiler le **moteur pur** avec `javac` est possible, mais le classement SQLite nécessite le pilote JDBC fourni par le build Maven.

Par défaut, la base est créée sous `~/.fighter-console/scores.db`. On peut définir `FIGHTER_DB_PATH` pour utiliser un autre emplacement et `FIGHTER_NO_SOUND=1` pour couper le son.

## Vérifier

```bash
mvn test
mvn clean verify
```

Les tests couvrent les réactions différées, l'annulation de dégâts, les contre-attaques, les limites de ressources, les coups illégaux, le calcul de score, la persistance et le classement top 5.

## Respect du projet initial

Le dépôt conserve l'historique des fichiers de l'ancienne version (`App.java`, `cm/Fighterconsole/Player.java`). La nouvelle application se trouve sous `src/main/java/cm/fighterconsole/` et utilise une architecture Maven.

## Documentation

- [Règles de combat](docs/REGLES.md)
- [Architecture et décisions techniques](docs/ARCHITECTURE.md)
- [Formule du classement](docs/CLASSEMENT.md)
- [Limites et suite du développement](docs/ROADMAP.md)

---

**Developed by Devmaes** · Jeu personnel de stratégie en console Java.

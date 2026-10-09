# Architecture

- `Combat` : règles du domaine, objets immuables (`record Fighter`), validations, calcul d'échanges. Aucune dépendance I/O.
- `CombatAI` : évalue les actions légales et recherche le meilleur échange pour l'ordinateur. Pas d'accès direct au terminal.
- `Terminal` : entrées contrôlées, panneaux console et règles expliquées.
- `App` : orchestration des tours, choix du mode, statistiques par combat, état de la session.
- `Scores` : stockage local via JDBC/SQLite, SQL paramétré, classement limité à cinq résultats.
- `Soundtrack` : synthèse d'un thème musical original dans la mémoire, boucle sur `Clip` et repli silencieux sans périphérique.
- `src/test` : tests JUnit 5 du domaine, de l'IA et de SQLite.

## Séparation des responsabilités

Les règles ne dépendent ni d'un `Scanner`, ni d'OpenGL/GUI, ni d'une base de données. Cette séparation permet de développer plus tard une interface JavaFX, une version Web ou des agents d'IA plus évolués sans remplacer le moteur.

## Sécurité et vie privée

Le jeu ne stocke que le pseudo fourni et le résultat des combats. SQLite est un fichier local non synchronisé. Les requêtes utilisent des `PreparedStatement`. Les pseudos sont bornés et les contrôles de terminal sont filtrés. Ne publiez pas votre fichier personnel `scores.db`.

## Limites

Le moteur IA ne fait pas de deep learning : c'est une recherche heuristique à un échange. Le son nécessite une sortie audio fonctionnelle et peut être désactivé. Les performances varient selon le terminal et la JVM.

# Contribution

Le moteur de règles est indépendant des entrées console. Pour proposer un ajustement, modifier d'abord `docs/REGLES.md`, puis les tests `CombatTest` et le code `Combat`.

```bash
mvn clean verify
```

Messages de commits recommandés : `feat:`, `fix:`, `test:`, `docs:`, `refactor:`.

Éviter de versionner `scores.db` ou des fichiers de données personnels.

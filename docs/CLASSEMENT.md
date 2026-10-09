# Le classement local (Top 5)

Après chaque victoire, la base `~/.fighter-console/scores.db` conserve une ligne avec le pseudo du gagnant, son adversaire, le mode, les tours, la date et le score.

Le classement présente les **cinq meilleures performances individuelles** ; un même pseudo peut apparaître plusieurs fois. Les égalités sont départagées par la partie remportée en moins de tours, puis par l'ordre d'enregistrement. Les défaites ne créent aucune ligne de classement.

Formule :

```text
score = 100
      + (30 × vies restantes)
      + (4 × attaque restante)
      + (3 × défense restante)
      + (10 × attaques réussies)
      + (5 × blocages intégraux)
      + max(0, 60 - nombre d'échanges)
```

Cela récompense la victoire, la rapidité et l'économie des ressources. Le calcul est local ; aucune connexion Internet n'est nécessaire.

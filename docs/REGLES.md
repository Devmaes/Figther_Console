# Règles de Fighter Console v2

## Valeurs de départ

Chaque combattant commence avec **3 points de vie**, **15 points d'attaque** et **7 points de défense**. Limites : vie ≤ 3, attaque ≤ 20, défense ≤ 12. Les cinq bots ont des bonus de départ modestes selon leur identité.

## Le tour est une promesse, pas un effet immédiat

1. **Annonce** : le joueur actif déclare la puissance de son attaque (de 1 à son capital d'attaque). Il peut aussi *reprendre son souffle* (+3 points d'attaque et +1 de défense, sans dépasser les limites), ce qui passe la main sans provoquer de réaction.
2. **Réaction** : l'adversaire choisit entre bloquer, contre-attaquer et encaisser. **Aucun point n'a changé** durant cette annonce et ce choix.
3. **Résolution simultanée de l'échange** : le moteur calcule les deux résultats, consomme les ressources correspondantes, puis affiche un nouveau tableau de bord.
4. **Changement de main** : si les deux joueurs ont encore des points de vie, l'autre joueur peut annoncer la prochaine action.

## Attaquer

Une attaque dépense exactement les points annoncés du capital d'attaque. Une puissance initiale égale ou supérieure à **4** peut faire perdre **1 vie** si elle n'est pas suffisamment bloquée ou contrée.

## Bloquer

Le défenseur choisit une force comprise entre 1 et ses points de défense. La force bloquée est soustraite de la force attaquante. Si la force **résiduelle ≥ 4**, le défenseur perd une vie. Le blocage coûte les points de défense déclarés et récupère **1 point d'attaque** (plafonné à 20). Si le blocage est intégral, d'au moins 5 points, le défenseur récupère également **1 point de vie** (plafonné à 3). Un blocage intégral compte dans le bonus final.

## Contre-attaquer

Le défenseur choisit une puissance de contre-attaque comprise entre 1 et son attaque disponible. Les deux combattants dépensent leur puissance de frappe. Si une puissance dépasse l'autre d'au moins **4**, le plus faible perd **1 vie**. En revanche, si l'écart est inférieur à 4, **les deux coups sont échangés** : chaque combattant perd 1 vie si la frappe adverse a une puissance d'au moins 4. Exemple : attaque 5 et contre 7 → différence 2 : chaque joueur perd 1 vie. Attaque 4 et contre 9 → différence 5 : seul l'attaquant perd 1 vie. C'est une interprétation d'équilibrage du prototype ; elle pourra être modifiée après tes tests.

## Encaisser

Sans riposte, le défenseur perd des points de défense égaux à la puissance de l'attaque (plancher 0) et perd également 1 point d'attaque (plancher 0). Si l'attaque annoncée est au moins égale à 4, il perd 1 vie. Un combattant sans attaque **et** sans défense est vulnérable à une attaque de n'importe quelle puissance positive.

## Fin de la partie

**Dès qu'un des combattants tombe à 0 vie après la résolution complète d'un échange**, le combat s'arrête et l'autre gagne. Si les deux arrivent simultanément à zéro, c'est un double K.O. sans vainqueur ni score. Le score du gagnant est enregistré localement. Le repos ne provoque pas de K.O. et ne rend pas de points de vie. Aucun compte à rebours caché n'interrompt le combat.

## À propos des précisions

Le prototype Java d'origine annonce parfois un seuil de 2 dans le texte, mais compare à 4 dans sa méthode `attaque()`. **Cette version adopte explicitement 4**, comme l'implémentation d'origine. Les règles de contre-attaque, de récupération et de classement ont été précisées pour éviter les comportements contradictoires. Elles pourront être réajustées après tests de jeu.

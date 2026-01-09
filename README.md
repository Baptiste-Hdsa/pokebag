# Pokebag

Petit jeu de combat au tour par tour (inspiré Pokémon) en Java/Maven.

Le jeu lit les monstres et attaques depuis `Monsters.txt` et `Attacks.txt` à la racine du projet.

## Commande pour compiler:

```
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out fr.bapti.esiea.Main
 ```

## Jouer

- Au démarrage, choisissez le mode :
  - 1 = Solo (contre un robot)
  - 2 = Multi (deux joueurs en local, même clavier)
- Pour chaque joueur, constituez votre équipe (max 3 monstres) et choisissez jusqu’à 4 attaques par monstre.

### Contrôles (entrées clavier)
- Menus principaux/combat : entrez le numéro affiché et validez par Entrée.
- Action par tour :
  - 1 = Attaquer (puis choisir l’attaque par son index)
  - 2 = Objet (puis choisir l’objet par son index)
  - 3 = Changer de monstre (puis choisir un monstre vivant)
- Si votre monstre est KO pendant un tour, vous devez immédiatement en choisir un autre vivant. Le monstre KO est retiré de votre poche.

### Rappels de règles
- Les types et avantages/faiblesses influencent les dégâts.
- Type « nature » regroupe Plante (GRASS) et Insecte (INSECT). Sur terrain inondé, les monstres de type nature régénèrent un peu de vie en fin de tour.
- Eau (WATER) peut inonder le terrain (1 à 3 tours). Les non‑eau peuvent glisser et rater leur action.
- Feu (FIRE) peut brûler. Plante (GRASS) peut se soigner (~20%). Insecte (INSECT) empoisonne 1 attaque sur 3.
- Électrique (ELECTRIC) peut paralyser (récupération progressive).
- Sous terre (terre) double la défense pendant quelques tours.



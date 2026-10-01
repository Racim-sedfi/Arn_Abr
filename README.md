# Arn_Abr — Arbres binaires de recherche et arbres rouge-noir en Java

Implémentation en Java de deux structures de données génériques — l'**arbre binaire de recherche
(ABR)** et l'**arbre rouge-noir (ARN)** — sous forme de collections compatibles avec l'API
`java.util.Collection`, accompagnée d'une étude expérimentale comparant leurs performances
en construction et en recherche.

## Technologies

- Java 17
- Maven
- JUnit 5 (tests unitaires)
- OpenCSV (export des résultats de l'étude)

## Fonctionnalités principales

- `ABR<E>` et `ARN<E>` héritent de `AbstractCollection<E>` : ajout, recherche (`contains`),
  suppression, `clear`, itérateur en ordre croissant (avec `remove`) et `toString`.
- Ordre naturel (`Comparable`) ou `Comparator` fourni à la construction ; constructeur par recopie.
- ARN : rééquilibrage par recoloration et rotations gauche/droite, nœud sentinelle.
- `EtudeExp` : mesure du temps de construction et de recherche pour des tailles de 100 à
  100 000 clés, insérées dans un ordre **aléatoire** ou **croissant** ; résultats affichés
  dans la console et exportés en CSV.

## Résultats de l'étude

Les fichiers `etude*.csv` à la racine contiennent les mesures obtenues (en secondes).
Extrait pour 100 000 clés :

| Scénario              | ABR       | ARN      |
|-----------------------|-----------|----------|
| Insertion aléatoire   | 0,034 s   | 0,041 s  |
| Insertion croissante  | 10,684 s  | 0,018 s  |
| Recherche aléatoire   | 0,026 s   | 0,032 s  |
| Recherche croissante  | 29,513 s  | 0,009 s  |

Avec des clés insérées dans l'ordre croissant, l'ABR dégénère en liste chaînée (coût linéaire),
alors que l'ARN reste équilibré (coût logarithmique). Les valeurs dépendent de la machine utilisée.

## Structure du projet

```
├── pom.xml
├── etude*.csv                      # Résultats de l'étude expérimentale
└── src
    ├── main/java/etude
    │   ├── EtudeExp.java           # Programme de l'étude comparative
    │   ├── abr/ABR.java            # Arbre binaire de recherche
    │   └── arn/ARN.java            # Arbre rouge-noir
    └── test/java
        ├── abr/ABRTest.java
        └── arn/ARNTest.java
```

## Compilation et exécution

Prérequis : JDK 17 et Maven.

```bash
mvn compile
mvn exec:java -Dexec.mainClass=etude.EtudeExp   # lance l'étude (régénère les fichiers CSV)
```

## Tests

```bash
mvn test
```

30 tests unitaires JUnit 5 (16 pour l'ABR, 14 pour l'ARN) : constructeurs, ajout, recherche,
suppression (feuille, nœud à un ou deux enfants, racine), itérateur et affichage.

## Auteurs

Racim Sedfi et Mohand-Said Mane (travail en binôme)

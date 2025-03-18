/**
 * Ce code a été réaliser en binome par 
 * @autheur mohand-said Mane
 * @author racim Sedfi
 */
package etude;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.opencsv.CSVWriter;

import etude.abr.ABR;
import etude.arn.ARN;

public class EtudeExp {

    public static void main(String[] args) {
        int[] tailles = {100, 500, 1_000, 5_000, 10_000, 50_000, 100_000};
        final int TAILLE = tailles.length;

        // Temps pour les insertions
        double[] tempsABRInsertionAleatoire = new double[TAILLE];
        double[] tempsARNInsertionAleatoire = new double[TAILLE];
        double[] tempsABRInsertionCroissant = new double[TAILLE];
        double[] tempsARNInsertionCroissant = new double[TAILLE];

        // Temps pour les recherches
        double[] tempsABRRechercheAleatoire = new double[TAILLE];
        double[] tempsARNRechercheAleatoire = new double[TAILLE];
        double[] tempsABRRechercheCroissant = new double[TAILLE];
        double[] tempsARNRechercheCroissant = new double[TAILLE];

        for (int i = 0; i < TAILLE; i++) {
            int taille = tailles[i];

            // Clés aléatoires
            List<Integer> clesAleatoires = genererClesAleatoires(taille);

            // Clés dans un ordre croissant
            List<Integer> clesCroissantes = genererClesCroissantes(taille);

            // Tester les performances pour les clés aléatoires
            tempsABRInsertionAleatoire[i] = testerPerformanceConstruction(new ABR<>(), clesAleatoires);
            tempsARNInsertionAleatoire[i] = testerPerformanceConstruction(new ARN<>(), clesAleatoires);
            tempsABRRechercheAleatoire[i] = testerPerformanceRecherche(new ABR<>(clesAleatoires), clesAleatoires);
            tempsARNRechercheAleatoire[i] = testerPerformanceRecherche(new ARN<>(clesAleatoires), clesAleatoires);

            // Tester les performances pour les clés croissantes
            tempsABRInsertionCroissant[i] = testerPerformanceConstruction(new ABR<>(), clesCroissantes);
            tempsARNInsertionCroissant[i] = testerPerformanceConstruction(new ARN<>(), clesCroissantes);
            tempsABRRechercheCroissant[i] = testerPerformanceRecherche(new ABR<>(clesCroissantes), clesCroissantes);
            tempsARNRechercheCroissant[i] = testerPerformanceRecherche(new ARN<>(clesCroissantes), clesCroissantes);
        }
        
	     // Afficher les résultats dans la console
	        afficherResultats("Insertion Aléatoire", tailles, tempsABRInsertionAleatoire, tempsARNInsertionAleatoire);
	        afficherResultats("Insertion Croissante", tailles, tempsABRInsertionCroissant, tempsARNInsertionCroissant);
	        afficherResultats("Recherche Aléatoire", tailles, tempsABRRechercheAleatoire, tempsARNRechercheAleatoire);
	        afficherResultats("Recherche Croissante", tailles, tempsABRRechercheCroissant, tempsARNRechercheCroissant);
	    

        // Sauvegarder les résultats dans des fichiers CSV
        sauvegarderResultats("etudeAleatoireConstruction.csv", tailles, tempsABRInsertionAleatoire, tempsARNInsertionAleatoire, "Insertion Aléatoire");
        sauvegarderResultats("etudeCroissantConstruction.csv", tailles, tempsABRInsertionCroissant, tempsARNInsertionCroissant, "Insertion Croissante");
        sauvegarderResultats("etudeAleatoireRecherche.csv", tailles, tempsABRRechercheAleatoire, tempsARNRechercheAleatoire, "Recherche Aléatoire");
        sauvegarderResultats("etudeCroissantRecherche.csv", tailles, tempsABRRechercheCroissant, tempsARNRechercheCroissant, "Recherche Croissante");
    }
    
    private static void afficherResultats(String type, int[] tailles, double[] tempsABR, double[] tempsARN) {
        System.out.println("\n=== Résultats pour " + type + " ===");
        System.out.printf("%-10s %-20s %-20s%n", "Taille", "Temps ABR (s)", "Temps ARN (s)");
        System.out.println("---------------------------------------------");
        for (int i = 0; i < tailles.length; i++) {
            System.out.printf("%-10d %-20.6f %-20.6f%n", tailles[i], tempsABR[i], tempsARN[i]);
        }
        System.out.println("---------------------------------------------\n");
    }

    private static List<Integer> genererClesAleatoires(int taille) {
        List<Integer> cles = new ArrayList<>(taille);
        for (int i = 0; i < taille; i++) {
            cles.add(i);
        }
        Collections.shuffle(cles, new Random());
        return cles;
    }

    private static List<Integer> genererClesCroissantes(int taille) {
        List<Integer> cles = new ArrayList<>(taille);
        for (int i = 0; i < taille; i++) {
            cles.add(i);
        }
        return cles; // Les clés sont déjà triées dans l'ordre croissant
    }

    private static <T extends Comparable<T>> double testerPerformanceConstruction(ABR<T> structure, List<T> cles) {
        long debut = System.nanoTime();
        for (T cle : cles) {
            structure.add(cle);
        }
        long fin = System.nanoTime();
        return (fin - debut) / 1_000_000_000.0; // Conversion en secondes
    }

    private static <T extends Comparable<T>> double testerPerformanceRecherche(ABR<T> structure, List<T> cles) {
        long debut = System.nanoTime();
        for (T cle : cles) {
            structure.contains(cle);
        }
        long fin = System.nanoTime();
        return (fin - debut) / 1_000_000_000.0; // Conversion en secondes
    }


    private static <T extends Comparable<T>> double testerPerformanceConstruction(ARN<T> structure, List<T> cles) {
        long debut = System.nanoTime();
        for (T cle : cles) {
            structure.add(cle);
        }
        long fin = System.nanoTime();
        return (fin - debut) / 1_000_000_000.0; // Conversion en secondes
    }

    private static <T extends Comparable<T>> double testerPerformanceRecherche(ARN<T> structure, List<T> cles) {
        long debut = System.nanoTime();
        for (T cle : cles) {
            structure.contains(cle);
        }
        long fin = System.nanoTime();
        return (fin - debut) / 1_000_000_000.0; // Conversion en secondes
    }
    private static void sauvegarderResultats(String nomFichier, int[] tailles, double[] tempsABR, double[] tempsARN, String type) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(nomFichier))) {
            // Écrire l'en-tête
            String[] header = new String[tailles.length + 1];
            header[0] = "Taille";
            for (int i = 0; i < tailles.length; i++) {
                header[i + 1] = Integer.toString(tailles[i]);
            }
            writer.writeNext(header);

            // Écrire les temps pour ARN
            String[] ligneARN = new String[tailles.length + 1];
            ligneARN[0] = "Temps ARN (" + type + ")";
            for (int i = 0; i < tempsARN.length; i++) {
                ligneARN[i + 1] = String.format("%.6f", tempsARN[i]);
            }
            writer.writeNext(ligneARN);

            // Écrire les temps pour ABR
            String[] ligneABR = new String[tailles.length + 1];
            ligneABR[0] = "Temps ABR (" + type + ")";
            for (int i = 0; i < tempsABR.length; i++) {
                ligneABR[i + 1] = String.format("%.6f", tempsABR[i]);
            }
            writer.writeNext(ligneABR);

            System.out.println("Fichier CSV créé avec succès : " + nomFichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier : " + e.getMessage());
        }
    }
}


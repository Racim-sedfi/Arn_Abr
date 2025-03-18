/**
 * Ce code a été réaliser en binome par 
 * @autheur mohand-said Mane
 * @author racim Sedfi
 */
package abr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import etude.abr.ABR;

public class ABRTest {

    // --- Test Constructeurs ---
    @Test
    public void testConstructeurVide() {
        ABR<Integer> tree = new ABR<>();
        assertTrue(tree.isEmpty(), "L'arbre doit être vide à l'initialisation.");
        assertEquals(0, tree.size(), "La taille doit être 0.");
    }

    @Test
    public void testConstructeurAvecComparateur() {
        ABR<Integer> tree = new ABR<>((a, b) -> b - a); // Ordre décroissant
        tree.add(10);
        tree.add(20);
        tree.add(5);
        Iterator<Integer> it = tree.iterator();
        assertEquals(Integer.valueOf(20), it.next(), "Le plus grand élément doit venir en premier.");
    }

    @Test
    public void testConstructeurParRecopie() {
        Collection<Integer> data = Arrays.asList(10, 5, 15, 20);
        ABR<Integer> tree = new ABR<>(data);
        assertEquals(4, tree.size(), "La taille de l'arbre doit correspondre à celle de la collection.");
        for (int val : data) {
            assertTrue(tree.contains(val), "L'arbre doit contenir les éléments initialement insérés.");
        }
    }

    // --- Test Méthode add ---
    @Test
    public void testAddEtSize() {
        ABR<Integer> tree = new ABR<>();
        assertTrue(tree.add(10), "L'ajout d'un élément doit réussir.");
        assertTrue(tree.contains(10), "L'arbre doit contenir l'élément ajouté.");
        assertFalse(tree.add(10), "L'ajout d'un doublon doit échouer.");
        
        assertEquals(1, tree.size());
    }

    @Test
    public void testAddAvecComparateur() {
        ABR<Integer> tree = new ABR<>((a, b) -> b - a); // Ordre décroissant
        tree.add(10);
        tree.add(20);
        tree.add(5);
        assertEquals(3, tree.size(), "La taille doit refléter les ajouts.");
        Iterator<Integer> it = tree.iterator();
        assertEquals(Integer.valueOf(20), it.next(), "L'ordre doit respecter le comparateur.");
        assertEquals(Integer.valueOf(10), it.next(), "L'ordre doit respecter le comparateur.");
    }

    // --- Test Méthode contains ---
    @Test
    public void testContains() {
        ABR<Integer> tree = new ABR<>();
        tree.add(10);
        tree.add(20);
        assertTrue(tree.contains(10), "L'élément doit être trouvé.");
        assertFalse(tree.contains(5), "Un élément absent ne doit pas être trouvé.");
    }

    @Test
    public void testContainsArbreVide() {
        ABR<Integer> tree = new ABR<>();
        assertFalse(tree.contains(10), "Un arbre vide ne doit pas contenir d'éléments.");
    }

    // --- Test Méthode remove ---
    @Test
    public void testRemoveFeuille() {
        ABR<Integer> tree = new ABR<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        assertTrue(tree.remove(5), "La suppression d'une feuille doit réussir.");
        assertFalse(tree.contains(5), "L'élément supprimé ne doit plus être présent.");
    }

    @Test
    public void testRemoveAvecUnEnfant() {
        ABR<Integer> tree = new ABR<>();
        tree.add(10);
        tree.add(5);
        tree.add(3);
        assertTrue(tree.remove(5), "La suppression d'un nœud avec un enfant doit réussir.");
        assertFalse(tree.contains(5), "Le nœud supprimé ne doit plus être présent.");
        assertTrue(tree.contains(3), "L'enfant du nœud supprimé doit être toujours présent.");
    }

    @Test
    public void testRemoveAvecDeuxEnfants() {
        ABR<Integer> tree = new ABR<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(3);
        tree.add(7);
        assertTrue(tree.remove(5), "La suppression d'un nœud avec deux enfants doit réussir.");
        assertFalse(tree.contains(5), "Le nœud supprimé ne doit plus être présent.");
        assertTrue(tree.contains(7), "Le successeur doit remplacer le nœud supprimé.");
    }

    @Test
    public void testRemoveRacine() {
        ABR<Integer> tree = new ABR<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        assertTrue(tree.remove(10), "La suppression de la racine doit réussir.");
        assertFalse(tree.contains(10), "La racine supprimée ne doit plus être présente.");
    }

    // --- Test Méthode clear ---
    @Test
    public void testClear() {
        ABR<Integer> tree = new ABR<>();
        tree.add(10);
        tree.add(20);
        tree.clear();
        assertTrue(tree.isEmpty(), "L'arbre doit être vide après un clear.");
        assertEquals(0, tree.size(), "La taille de l'arbre doit être 0 après un clear.");
    }

    // --- Test Méthode iterator ---
    @Test
    public void testIterator() {
        ABR<Integer> tree = new ABR<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        Iterator<Integer> it = tree.iterator();
        assertTrue(it.hasNext(), "L'itérateur doit avoir des éléments.");
        assertEquals(Integer.valueOf(5), it.next(), "Le premier élément doit être le minimum.");
        assertEquals(Integer.valueOf(10), it.next(), "Le deuxième élément doit être dans l'ordre croissant.");
        assertEquals(Integer.valueOf(15), it.next(), "Le dernier élément doit être le maximum.");
        assertFalse(it.hasNext(), "L'itérateur ne doit plus avoir d'éléments.");
    }

    @Test
    public void testIteratorArbreVide() {
        ABR<Integer> tree = new ABR<>();
        Iterator<Integer> it = tree.iterator();
        assertFalse(it.hasNext(), "L'itérateur d'un arbre vide ne doit pas avoir d'éléments.");
        assertThrows(NoSuchElementException.class, it::next, "L'appel de next() doit lever une exception.");
    }

    @Test
    public void testIteratorRemove() {
        ABR<Integer> tree = new ABR<>();
        tree.add(10);
        tree.add(5);
        Iterator<Integer> it = tree.iterator();
        it.next();
        it.remove();
        assertFalse(tree.contains(5), "L'élément doit être supprimé via l'itérateur.");
        assertEquals(1, tree.size(), "La taille doit être mise à jour après suppression via l'itérateur.");
    }

    // --- Test Méthode toString ---
    @Test
    public void testToString() {
        ABR<Integer> tree = new ABR<>();
        tree.add(2);
        tree.add(3);
        tree.add(1);

        String expectedOutput =
            "       +-- 3\n" +
            "-- 2 --|\n"+
            "       +-- 1\n";

        assertEquals(expectedOutput, tree.toString());
    }

}

/**
 * Ce code a été réaliser en binome par 
 * @autheur mohand-said Mane
 * @author racim Sedfi
 */
package arn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import etude.arn.ARN;

public class ARNTest {

    @Test
    public void testConstructorNaturalOrder() {
        ARN<Integer> arn = new ARN<>();
        assertTrue(arn.isEmpty(), "Arbre rouge-noir devrait être vide après construction.");
    }

    @Test
    public void testConstructorWithComparator() {
        ARN<Integer> arn = new ARN<>(Comparator.reverseOrder());
        arn.add(10);
        arn.add(5);
        arn.add(15);

        Iterator<Integer> it = arn.iterator();
        assertEquals(15, it.next(), "L'ordre des éléments devrait être inversé.");
        assertEquals(10, it.next());
        assertEquals(5, it.next());
    }

    @Test
    public void testConstructorWithCollection() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);

        ARN<Integer> arn = new ARN<>(list);
        assertEquals(3, arn.size(), "L'arbre devrait contenir les mêmes éléments que la collection.");
        assertTrue(arn.contains(10), "L'arbre devrait contenir 10.");
        assertTrue(arn.contains(20), "L'arbre devrait contenir 20.");
        assertTrue(arn.contains(30), "L'arbre devrait contenir 30.");
    }

    @Test
    public void testAddElements() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(20);
        arn.add(5);

        assertEquals(3, arn.size(), "La taille de l'arbre devrait être 3.");
        assertTrue(arn.contains(10), "L'arbre devrait contenir 10.");
        assertTrue(arn.contains(5), "L'arbre devrait contenir 5.");
        assertTrue(arn.contains(20), "L'arbre devrait contenir 20.");
    }

    @Test
    public void testRemoveElements() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(20);
        arn.add(5);

        assertTrue(arn.remove(10), "La suppression de 10 devrait réussir.");
        assertFalse(arn.contains(10), "10 ne devrait plus être dans l'arbre.");
        assertEquals(2, arn.size(), "La taille de l'arbre devrait être 2.");

        assertTrue(arn.remove(5), "La suppression de 5 devrait réussir.");
        assertTrue(arn.remove(20), "La suppression de 20 devrait réussir.");
        assertTrue(arn.isEmpty(), "L'arbre devrait être vide après suppression de tous les éléments.");
    }

    @Test
    public void testRemoveNonExistentElement() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        assertThrows(NullPointerException.class, () -> arn.remove(20), "La suppression d'un élément inexistant devrait lever une exception.");
    }

    @Test
    public void testClear() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(20);
        arn.clear();
        assertTrue(arn.isEmpty(), "L'arbre devrait être vide après appel à clear().");
    }

    @Test
    public void testContains() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(20);

        assertTrue(arn.contains(10), "L'arbre devrait contenir 10.");
        assertFalse(arn.contains(5), "L'arbre ne devrait pas contenir 5.");
    }

    @Test
    public void testIterator() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(20);
        arn.add(5);

        Iterator<Integer> it = arn.iterator();
        assertTrue(it.hasNext(), "L'itérateur devrait avoir des éléments.");
        assertEquals(5, it.next(), "L'itérateur devrait renvoyer les éléments dans l'ordre.");
        assertEquals(10, it.next());
        assertEquals(20, it.next());
        assertFalse(it.hasNext(), "L'itérateur ne devrait plus avoir d'éléments.");
    }

    @Test
    public void testIteratorRemove() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(20);
        arn.add(5);

        Iterator<Integer> it = arn.iterator();
        while (it.hasNext()) {
            int val = it.next();
            if (val == 10) {
                it.remove();
            }
        }

        assertFalse(arn.contains(10), "10 devrait avoir été supprimé par l'itérateur.");
        assertEquals(2, arn.size(), "La taille de l'arbre devrait être 2 après suppression.");
    }

    @Test
    public void testEmptyTree() {
        ARN<Integer> arn = new ARN<>();
        assertFalse(arn.iterator().hasNext(), "Un itérateur sur un arbre vide ne devrait pas avoir d'éléments.");
        assertThrows(NoSuchElementException.class, () -> arn.iterator().next(), "next() sur un arbre vide devrait lever une exception.");
    }

    @Test
    public void testDuplicateElements() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(10);

        assertEquals(2, arn.size(), "L'arbre devrait contenir les doublons si la logique les autorise.");
        assertTrue(arn.contains(10), "L'arbre devrait contenir 10.");
    }

    @Test
    public void testOrderPreservation() {
        ARN<Integer> arn = new ARN<>();
        arn.add(10);
        arn.add(5);
        arn.add(20);

        Iterator<Integer> it = arn.iterator();
        assertEquals(5, it.next(), "Les éléments devraient être dans l'ordre croissant.");
        assertEquals(10, it.next());
        assertEquals(20, it.next());
    }
    
    // --- Test Méthode toString ---
    @Test
    public void testToString() {
        ARN<Integer> tree = new ARN<>();
        tree.add(2);
        tree.add(3);
        tree.add(1);

        String expectedOutput =
            "       +--- 3:R\n" +
            "--- 2:N ---|\n"+
            "       +--- 1:R\n";

        assertEquals(expectedOutput, tree.toString());
    }
}

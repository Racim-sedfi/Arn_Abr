/**
 * Ce code a été réaliser en binome par 
 * @autheur mohand-said Mane
 * @author racim Sedfi
 */
package etude.arn;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;



/**
 * <p>
 * Implantation de l'interface Collection basée sur les arbres binaires de
 * recherche. Les éléments sont ordonnés soit en utilisant l'ordre naturel (cf
 * Comparable) soit avec un Comparator fourni à la création.
 * </p>
 *
 * <p>
 * Certaines méthodes de AbstractCollection doivent être surchargées pour plus
 * d'efficacité.
 * </p>
 *
 * @param <E> le type des clés stockées dans l'arbre
 */
public class ARN<E extends Comparable<? super E>> extends AbstractCollection<E> {
	private Noeud racine;

	private Noeud sentinelle = new Noeud(null);

	private int taille;

	private Comparator<? super E> cmp;

	private enum couleur {
		N, R // N: Noire, R: Rouge
	}

	private class Noeud {
		E cle;
		Noeud gauche;
		Noeud droit;
		Noeud pere;
		public int numGauche = 0;
		public int numDroit = 0;
		couleur couleur;

		Noeud(E cle) {
			// TODO
			couleur = ARN.couleur.N;
			numGauche = 0;
			numDroit = 0;
			this.cle = cle;
			gauche = droit = pere = sentinelle;
		}
		/**
		 * Renvoie le noeud contenant la clé minimale du sous-arbre enraciné dans ce
		 * noeud
		 *
		 * @return le noeud contenant la clé minimale du sous-arbre enraciné dans ce
		 *         noeud
		 */
		Noeud minimum() {
			// TODO
			Noeud courant = this;
			while (courant.gauche != sentinelle)
				courant = courant.gauche;
			return courant;

		}

		/**
		 * Renvoie le successeur de ce noeud
		 *
		 * @return le noeud contenant la clé qui suit la clé de ce noeud dans l'ordre
		 *         des clés, null si c'es le noeud contenant la plus grande clé
		 */
		Noeud suivant() {
			// TODO
			if (droit != sentinelle)
				return droit.minimum();

			Noeud courant = this;
			Noeud parent = pere;
			while (parent != sentinelle && courant == parent.droit) {
				courant = parent;
				parent = parent.pere;
			}
			return parent;
		}
	}

	// Consructeurs

	/**
	 * Crée un arbre vide. Les éléments sont ordonnés selon l'ordre naturel
	 */
	public ARN() {
		// TODO
		taille = 0;
		racine = sentinelle;
		this.cmp = Comparator.naturalOrder();
	}

	/**
	 * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par le
	 * comparateur
	 *
	 * @param cmp le comparateur utilisé pour définir l'ordre des éléments
	 */
	public ARN(Comparator<? super E> cmp) {
		// TODO
		taille = 0;
		racine = sentinelle;
		this.cmp = cmp;
	}

	/**
	 * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments que
	 * c. L'ordre des éléments est l'ordre naturel.
	 *
	 * @param c la collection à copier
	 */
	public ARN(Collection<? extends E> c) {
		// TODO
		this();
		addAll(c);
	}

	@Override
	public Iterator<E> iterator() {
		return new ARNIterator();
	}

	@Override
	public int size() {
		return taille;
	}

	@Override
	public boolean add(E e) {
		Noeud noeud = new Noeud(e);
		ajouter(noeud);
		taille++;
		return true;
	}

	private void ajouter(Noeud z) {
		Noeud y = sentinelle;
		Noeud x = racine;
		while (x != sentinelle) {
			y = x;
			x = cmp.compare(z.cle, x.cle) < 0 ? x.gauche : x.droit;
		}
		z.pere = y;
		if (y == sentinelle) // arbre vide
			racine = z;
		else if (cmp.compare(z.cle, y.cle) < 0)
			y.gauche = z;
		else
			y.droit = z;

		z.gauche = z.droit = sentinelle;
		z.couleur = couleur.R;
		ajouterCorrection(z);
	}

	private void ajouterCorrection(Noeud z) {
		Noeud y;
		while (z.pere != sentinelle && z.pere.couleur == couleur.R) {
			// (*) La seule propriété RN violée est (4) : z et z.pere sont rouges
			if (z.pere == z.pere.pere.gauche) {
				y = z.pere.pere.droit; // l'oncle de z
				if (y != sentinelle && y.couleur == couleur.R) {
					// cas 1
					z.pere.couleur = couleur.N;
					y.couleur = couleur.N;
					z.pere.pere.couleur = couleur.R;
					z = z.pere.pere;
				} else {
					if (z == z.pere.droit) {
						// cas 2
						z = z.pere;
						rotationGauche(z);
					}
					// cas 3
					z.pere.couleur = couleur.N;
					z.pere.pere.couleur = couleur.R;
					rotationDroite(z.pere.pere);
				}
			} else {
				// idem en miroir, gauche <-> droite
				// cas 1', 2', 3'
				y = z.pere.pere.gauche; // l'oncle de z
				if (y != sentinelle && y.couleur == couleur.R) {
					// cas 1
					z.pere.couleur = couleur.N;
					y.couleur = couleur.N;
					z.pere.pere.couleur = couleur.R;
					z = z.pere.pere;
				} else {
					if (z == z.pere.gauche) {
						// cas 2
						z = z.pere;
						rotationDroite(z);
					}
					// cas 3
					z.pere.couleur = couleur.N;
					z.pere.pere.couleur = couleur.R;
					rotationGauche(z.pere.pere);
				}
			}
		}
		// (**) La seule propriété (potentiellement) violée est (2)
		// if (racine != null)
		racine.couleur = couleur.N;

	}

	private void rotationGauche(Noeud x) {
		// Effectue la rotation à gauche comme décrit dans l'algorithme dans le texte du cours.
		Noeud y;
		y = x.droit;
		x.droit = y.gauche;

		// Vérifiez l'existence de y.left et apportez des modifications au pointeur
		if (y.gauche != sentinelle)
			y.gauche.pere = x;
		y.pere = x.pere;

		// le parent de x est nul
		if (x.pere == sentinelle)
			racine = y;


		// x est le fils gauche de son parent
		else if (x.pere.gauche == x)
			x.pere.gauche = y;

		// x est le fils droit de son parent.
		else
			x.pere.droit = y;

		// Fin de la rotation gauche
		y.gauche = x;
		x.pere = y;
	}

	private void rotationDroite(Noeud y) {

		// Effectue la rotation comme décrit dans le texte du cours.
		Noeud x = y.gauche;
		if (x != sentinelle) {
			y.gauche = x.droit;

			if (x.droit != sentinelle)
				x.droit.pere = y;
			x.pere = y.pere;

			// Vérifie l'existence de x.droit
			if (x.droit != sentinelle)
				x.droit.pere = y;
			x.pere = y.pere;

			// y.pere est nul
			if (y.pere == sentinelle)
				racine = x;

			// y est fils droit de son pere
			else if (y.pere.droit == y)
				y.pere.droit = x;

			// y est fils gauche de son pere
			else
				y.pere.gauche = x;
			x.droit = y;
		}

		y.pere = x;
	}

	// Quelques méthodes utiles

	/**
	 * Recherche une clé. Cette méthode peut être utilisée par
	 * {@link #contains(Object)} et {@link #remove(Object)}
	 *
	 * @param o la clé à chercher
	 * @return le noeud qui contient la clé ou null si la clé n'est pas trouvée.
	 */
	private Noeud rechercher(Object o) {

		return rechercher(racine, o);
	}

	private Noeud rechercher(Noeud racine, Object o) {
		if (racine == sentinelle || racine.cle == o)
			return racine;
		if (cmp.compare((E) o, racine.cle) < 0) {
			return rechercher(racine.gauche, o);
		} else {
			return rechercher(racine.droit, o);
		}
	}

	/**
	 * Supprime le noeud z. Cette méthode peut être utilisée dans
	 * {@link #remove(Object)} et {@link Iterator#remove()}
	 *
	 * @param z le noeud à supprimer
	 * @return le noeud contenant la clé qui suit celle de z dans l'ordre des clés.
	 *         Cette valeur de retour peut être utile dans {@link Iterator#remove()}
	 */
	 private Noeud supprimer(Noeud z) {
     Noeud y, x = sentinelle;

     // Trouver le nœud à supprimer (y)
     if (z.gauche == sentinelle || z.droit == sentinelle)
         y = z;
     else
         y = z.suivant(); // Trouver le successeur

     // x sera le fils unique de y ou sentinelle si y n'a pas de fils
     if (y.gauche != sentinelle)
         x = y.gauche;
     else
         x = y.droit;

     // Lien entre x et le parent de y
     if (x != sentinelle)
         x.pere = y.pere;

     // Mise à jour des liens du parent de y
     if (y.pere == sentinelle) {
         racine = x; // Suppression de la racine
     } else {
         if (y == y.pere.gauche)
             y.pere.gauche = x;
         else
             y.pere.droit = x;
     }

     // Si on supprime un nœud noir, on doit corriger l'arbre
     if (y.couleur == couleur.N) {
         // Appeler la méthode de correction après suppression
         supprimerCorrection(x);
     }

     // Remplacer la clé de z par celle de y si y n'est pas z
     if (y != z)
         z.cle = y.cle;
     
     return x;
 }

 private void supprimerCorrection(Noeud x) {
     // Cette méthode corrige les propriétés de l'arbre rouge-noir après suppression
     while (x != racine && x.couleur == couleur.N) {
         if (x == x.pere.gauche) {
             Noeud w = x.pere.droit; // Frère de x

             // Cas 1 : Frère rouge
             if (w.couleur == couleur.R) {
                 w.couleur = couleur.N;
                 x.pere.couleur = couleur.R;
                 rotationGauche(x.pere);
                 w = x.pere.droit; // Mettre à jour w
             }

             // Cas 2 : Frère noir avec deux enfants noirs
             if (w.gauche.couleur == couleur.N && w.droit.couleur == couleur.N) {
                 w.couleur = couleur.R;
                 x = x.pere; // Remonter
             } else {
                 // Cas 3 : Frère noir avec enfant gauche noir et enfant droit rouge
                 if (w.droit.couleur == couleur.N) {
                     w.gauche.couleur = couleur.N;
                     w.couleur = couleur.R;
                     rotationDroite(w);
                     w = x.pere.droit; // Mettre à jour w
                 }

                 // Cas 4 : Frère noir avec enfant droit rouge
                 w.couleur = x.pere.couleur;
                 x.pere.couleur = couleur.N;
                 w.droit.couleur = couleur.N;
                 rotationGauche(x.pere);
                 x = racine; // Terminer la boucle
             }
         } else {
             // Répéter en miroir pour le cas où x est le fils droit
             Noeud w = x.pere.gauche;

             if (w.couleur == couleur.R) {
                 w.couleur = couleur.N;
                 x.pere.couleur = couleur.R;
                 rotationDroite(x.pere);
                 w = x.pere.gauche;
             }

             if (w.droit.couleur == couleur.N && w.gauche.couleur == couleur.N) {
                 w.couleur = couleur.R;
                 x = x.pere;
             } else {
                 if (w.gauche.couleur == couleur.N) {
                     w.droit.couleur = couleur.N;
                     w.couleur = couleur.R;
                     rotationGauche(w);
                     w = x.pere.gauche;
                 }
                 w.couleur = x.pere.couleur;
                 x.pere.couleur = couleur.N;
                 w.gauche.couleur = couleur.N;
                 rotationDroite(x.pere);
                 x = racine;
             }
         }
     }
     x.couleur = couleur.N; // Assurer que la racine soit noire
 }

	/**
	 * Les itérateurs doivent parcourir les éléments dans l'ordre ! Ceci peut se
	 * faire facilement en utilisant {@link Noeud#minimum()} et
	 * {@link Noeud#suivant()}
	 */
	private class ARNIterator implements Iterator<E> {
		private Noeud noeudSuivant;
		private Noeud courant;

		public ARNIterator() {
			noeudSuivant = racine != sentinelle ? racine.minimum() : sentinelle;
			courant = sentinelle;
		}

		public boolean hasNext() {
			// TODO
			return noeudSuivant != sentinelle;
		}

		public E next() {
			// TODO
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			courant = noeudSuivant;
			noeudSuivant = noeudSuivant.suivant();

			return courant.cle;
		}

		public void remove() {
			// TODO
			if (courant == sentinelle)
				throw new IllegalStateException();
			 
			// Implémentation de la logique de suppression
			supprimer(courant);
			 taille--;
			courant = sentinelle;
		}
	}

	// Pour un "joli" affichage

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		toString(racine, buf, "", maxStrLen(racine));
		return buf.toString();
	}

	private void toString(Noeud x, StringBuffer buf, String path, int len) {
		if (x == sentinelle)
			return;
		toString(x.droit, buf, path + "D", len);
		for (int i = 0; i < path.length(); i++) {
			for (int j = 0; j < len + 6; j++)
				buf.append(' ');
			char c = ' ';
			if (i == path.length() - 1)
				c = '+';
			else if (path.charAt(i) != path.charAt(i + 1))
				c = '|';
			buf.append(c);
		}
		buf.append("--- " + x.cle.toString() + ":" + x.couleur);
		if (x.gauche != sentinelle || x.droit != sentinelle) {
			buf.append(" ---");
			for (int j = x.cle.toString().length(); j < len; j++)
				buf.append('-');
			buf.append('|');
		}
		buf.append("\n");
		toString(x.gauche, buf, path + "G", len);
	}

	private int maxStrLen(Noeud x) {
		return x == sentinelle ? 0 : Math.max(x.cle.toString().length(),
				Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
	}

	// TODO : voir quelles autres méthodes il faut surcharger
	@Override
	public boolean remove(Object o) {
		Noeud noeud = rechercher(o);
		if (noeud == sentinelle) {
	        throw new NullPointerException("L'objet à supprimer ne peut pas être null ou ne pas exister dans l'arbre");
	    }
		supprimer(noeud);
		taille--;
		noeud = sentinelle ;
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return (rechercher(o) != sentinelle);
	}

 

	@Override
	public void clear() {
	    racine = sentinelle;
	    taille = 0;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
	    for (Object element : c) {
	        return (remove(element));
	    }
	    return true;
	}
}

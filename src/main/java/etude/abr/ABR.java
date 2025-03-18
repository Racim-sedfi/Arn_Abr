/**
 * Ce code a été réaliser en binome par 
 * @autheur mohand-said Mane
 * @author racim Sedfi
 */

package etude.abr;

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
 * @param <E>
 *            le type des clés stockées dans l'arbre
 */
public class ABR<E> extends AbstractCollection<E> {
	private Noeud racine;
	private int taille;
	private Comparator<? super E> cmp;

	private class Noeud {
		E cle;
		Noeud gauche;
		Noeud droit;
		Noeud pere;

		Noeud(E cle) {
			this.cle = cle;
		}

		/**
		 * Renvoie le noeud contenant la clé minimale du sous-arbre enraciné
		 * dans ce noeud
		 * 
		 * @return le noeud contenant la clé minimale du sous-arbre enraciné
		 *         dans ce noeud
		 */
		Noeud minimum() {
			Noeud courant = this;
			while(courant.gauche != null) courant = courant.gauche;
			return courant;
		}

		/**
		 * Renvoie le successeur de ce noeud
		 * 
		 * @return le noeud contenant la clé qui suit la clé de ce noeud dans
		 *         l'ordre des clés, null si c'es le noeud contenant la plus
		 *         grande clé
		 */
		Noeud suivant() {
			if(this.droit != null) {
				return this.droit.minimum();
			}
			
			Noeud courant = this;
			Noeud parent = courant.pere;
			while(parent != null && courant == parent.droit) {
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
	public ABR() {
		this.taille = 0;
		this.racine = null;
		this.cmp = null;
	}

	/**
	 * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par
	 * le comparateur
	 * 
	 * @param cmp
	 *            le comparateur utilisé pour définir l'ordre des éléments
	 */
	public ABR(Comparator<? super E> cmp) {
		this.cmp = cmp;
		this.racine = null;
		this.taille = 0;
	}

	/**
	 * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments
	 * que c. L'ordre des éléments est l'ordre naturel.
	 * 
	 * @param c
	 *            la collection à copier
	 */
	public ABR(Collection<? extends E> c) {
		this();
		addAll(c);
	}

	@Override
	public Iterator<E> iterator() {
		return new ABRIterator();
	}

	@Override
	public int size() {
		return taille;
	}

	// Quelques méthodes utiles
	
	/**
	 * Comparer deux element entre eux 
	 * @param a, b
	 * @return un entier qui indique si les deux objets sont egaux 
	 */
	
	private int comparer(E a, E b) {
		if(cmp != null) {
			return cmp.compare(a,  b);
		}else {
			return ((Comparable<? super E>) a).compareTo(b);
		}
	}
	/**
	 * Recherche une clé. Cette méthode peut être utilisée par
	 * {@link #contains(Object)} et {@link #remove(Object)}
	 * 
	 * @param o
	 *            la clé à chercher
	 * @return le noeud qui contient la clé ou null si la clé n'est pas trouvée.
	 */
	private Noeud rechercher(Object o) {
		E elemnt = (E) o;
		Noeud courant = racine;
		while(courant != null) {
			int cmpResultat = comparer(elemnt, courant.cle);
			if(cmpResultat == 0) {
				return courant;
			}else if (cmpResultat < 0) {
				courant = courant.gauche;
			}else {
				courant = courant.droit;
			}
		}
		return null;
	}
	

	/**
	 * Supprime le noeud z. Cette méthode peut être utilisée dans
	 * {@link #remove(Object)} et {@link Iterator#remove()}
	 * 
	 * @param z
	 *            le noeud à supprimer
	 * @return le noeud contenant la clé qui suit celle de z dans l'ordre des
	 *         clés. Cette valeur de retour peut être utile dans
	 *         {@link Iterator#remove()}
	 */
	private Noeud supprimer(Noeud z) {
		if (z == null) return null;

        Noeud y;

        if (z.gauche== null && z.droit == null) { //Cas : pas d'enfants
            detacher(z);
            y = null;
            taille--;
        } else if (z.gauche == null || z.droit == null) { //Cas : 1 seul enfant
            y = (z.gauche != null) ? z.gauche : z.droit;
            remplacer(z, y);
            taille--;
        } else { //Cas: 2 enfants
            y = z.droit.minimum();
            z.cle = y.cle;
            supprimer(y);
        }

        return y;
	}

    /**
     * Détache le noeud z de son noeud parent.
     * @param z : noeud qu'on veut détacher
     */
    private void detacher(Noeud z) {
        if (z.pere == null) {
            racine = null;
        } else if (z == z.pere.gauche) {
            z.pere.gauche = null;
        } else {
            z.pere.droit = null;
        }
    }
    /**
     * rempacer un noeud u par un autre noeud v
     * @param u le Noeud qui doit etre remplacer
     * @param v Le noeud qui remplace u
     */
    private void remplacer(Noeud u, Noeud v) {
        if (u.pere == null) {
            racine = v; // u était la racine, maintenant v devient la racine
        } else if (u == u.pere.gauche) {
            u.pere.gauche = v;
        } else {
            u.pere.droit = v;
        }

        if (v != null) {
            v.pere = u.pere; // MAJ de la reference parent de v
        }
    }
	/**
	 * Les itérateurs doivent parcourir les éléments dans l'ordre ! Ceci peut se
	 * faire facilement en utilisant {@link Noeud#minimum()} et
	 * {@link Noeud#suivant()}
	 */
	private class ABRIterator implements Iterator<E> {
		private Noeud courant;
		private Noeud dernierRetourner;
	    ABRIterator() {
	        courant = (racine == null) ? null : racine.minimum();
	    }
		public boolean hasNext() {
			return courant != null;
		}

		public E next() {
			if(courant == null) {
				throw new NoSuchElementException();
			}
			dernierRetourner = courant;
			E value = courant.cle;
			courant = courant.suivant();
			return value;
		}

		public void remove() {
			if(dernierRetourner == null) {
				throw new IllegalStateException("next() doit être appelée avant remove() ou remove() a déjà été appelée après le dernier next()");
			}
			
			supprimer(dernierRetourner);
			dernierRetourner = null;
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
		if (x == null)
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
		buf.append("-- " + x.cle.toString());
		if (x.gauche != null || x.droit != null) {
			buf.append(" --");
			for (int j = x.cle.toString().length(); j < len; j++)
				buf.append('-');
			buf.append('|');
		}
		buf.append("\n");
		toString(x.gauche, buf, path + "G", len);
	}

	private int maxStrLen(Noeud x) {
		return x == null ? 0 : Math.max(x.cle.toString().length(),
				Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
	}

	// TODO : voir quelles autres méthodes il faut surcharger
	public boolean add(E element) {
        if (racine == null) {
            racine = new Noeud(element);
            taille++;
            return true;
        } else {
            return inserer(racine, element);
        }
    }

    /**
     * Insère un élément
     * @param node
     * @param element
     * @return
     */
	private boolean inserer(Noeud node, E element) {
	    Noeud parent = null;
	    int cmpResult = 0;
	    
	    // Parcourir l'arbre pour trouver l'emplacement d'insertion
	    while (node != null) {
	        cmpResult = comparer(element, node.cle);
	        if (cmpResult == 0) {
	            return false; // Élement déjà existant
	        } else {
	            parent = node; // Mémoriser le parent
	            if (cmpResult < 0) {
	                node = node.gauche; // Continuer à gauche
	            } else {
	                node = node.droit; // Continuer à droite
	            }
	        }
	    }

	    // Créer un nouveau noeud et le relier à son parent
	    Noeud nouveauNoeud = new Noeud(element);
	    if (cmpResult < 0) {
	        parent.gauche = nouveauNoeud; // Insertion à gauche
	    } else {
	        parent.droit = nouveauNoeud; // Insertion à droite
	    }
	    nouveauNoeud.pere = parent; // Définir le parent du nouveau noeud
	    taille++;
	    return true;
	}

    @Override
    public boolean contains(Object o) {
        return rechercher(o) != null;
    }
	@Override
	public boolean remove(Object o) {
		Noeud noeud = rechercher(o);
		if (noeud == null) {
	        throw new NullPointerException("L'objet à supprimer ne peut pas être null ou ne pas exister dans l'arbre");
	    }
		supprimer(noeud);
		taille--;
		noeud = null;
		return true;
	}
 
	
	@Override
	public void clear() {
	    racine = null;
	    taille = 0;
	}
}


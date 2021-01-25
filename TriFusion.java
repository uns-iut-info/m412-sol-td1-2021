/*
 * M412 2020-2021: distributed programming
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TriFusion {

	private static final Random RAND = new Random(System.currentTimeMillis());

	private TriFusion() {
		// Exists only to defeat instantiation.
	}

	/**
	 * Crée un tableau de la taille donnée et le remplit avec des entiers
	 * positifs aléatoires
	 */
	static long[] creeTableauAleatoire(int taille, int max) {
		long[] tab = new long[taille];
		for (int i = 0; i < tab.length; i++) {
			tab[i] = RAND.nextInt(max);
		}
		return tab;
	}

	/**
	 * Fusionne 2 à 2 les intervalles déjà triés spécifiés par les bornes de
	 * intervalle
	 */
	private static List<Integer> merge(List<Integer> intervalle, long[] tab, long[] tabRes) {
		/* copie de la liste intervalle pour retourner une liste à jour */
		List<Integer> resteIntervalle = new ArrayList<>(intervalle);

		int i = 0, j = 0;
		int debut1, fin1, debut2, fin2 = 0;
		while (i < intervalle.size() - 2) {
			/*
			 * Il reste au moins deux intervalles à fusionner : le premier de
			 * debut1 à fin1 et le second de debut2 à fin2
			 */
			debut1 = intervalle.get(i);
			fin1 = intervalle.get(i + 1);
			debut2 = fin1;
			fin2 = intervalle.get(i + 2);
			/*
			 * Fusion
			 */
			int i1 = debut1;
			int i2 = debut2;
			Debugger.log("Merge de " + Arrays.toString(Arrays.copyOfRange(tab, debut1, fin1)) + " et de "
					+ Arrays.toString(Arrays.copyOfRange(tab, debut2, fin2)));
			for (int k = debut1; k < fin2; k++) {
				if (i2 >= fin2 || (i1 < fin1 && tab[i1] < tab[i2])) {
					tabRes[k] = tab[i1++];
				} else {
					tabRes[k] = tab[i2++];
				}
			}
			/*
			 * le nouvel intervalle (résultat de la fusion) est [debut1 fin2[
			 */
			resteIntervalle.remove(j + 1);
			i += 2;
			j++;
		}
		/* copie dans tabRes du dernier intervalle si besoin (cas impair) */
		if (tab.length - fin2 >= 0) System.arraycopy(tab, fin2, tabRes, fin2, tab.length - fin2);
		return resteIntervalle;
	}

	/**
	 * tri parallèle d'un tableau d'entiers. Le tableau est découpé en morceaux
	 * de tailles égales p (argument de la ligne de commande)
	 */
	static long[] tri(long[] tab, int p) {
		assert (p > 0) : "le nombre de threads doit être > 0";
		// et p pas trop grand, sinon ça marche quand même
		if (tab.length < 2) { // rien à trier
			return tab;
		}
		if (p > (tab.length / 2)) {
			// ou beaucoup moins: pas besoin de multithread
			p = tab.length / 2;
		}
		TriIntervalle[] triIntervalle = new TriIntervalle[p];
		Thread[] threads = new Thread[p];

		/*
		 * on enregistre le début de chaque intervalle dans une liste pour la
		 * fusion à suivre
		 */
		List<Integer> intervalle = new ArrayList<>();

		int tailleIntervalle = tab.length / p;
		int r = tab.length % p; // le reste

		int debut = 0;
		int fin = 0;
		for (int i = 0; i < p; i++) {
			fin = debut + tailleIntervalle;
			intervalle.add(debut);
			triIntervalle[i] = new TriIntervalle(tab, debut, fin);
			threads[i] = new Thread(triIntervalle[i]);
			Debugger.log(String.format("On lance le thread triIntervalle(tab, %3d, %3d)", debut, fin));
			threads[i].start();
			debut += tailleIntervalle;
			if (i == p - r - 1) {
				/*
				 * les r derniers intervalles sont plus grands de 1 élément,
				 * dans SommeTab on avait choisi les p-1 premiers, et le dernier
				 * était plus petit.
				 */
				tailleIntervalle++;
			}
		}
		intervalle.add(fin);
		Debugger.log("Liste des intervalles : " + Arrays.toString(intervalle.toArray()));
		/*
		 * puis on attend la terminaison de tous les threads
		 */
		try {
			for (Thread t : threads) {
				t.join();
			}
		} catch (InterruptedException ie) {
			System.err.println("Caught InterruptedException: " + ie.getMessage());
		}

		/*
		 * Fusion des intervalles triés
		 */
		long[] tabRes = new long[tab.length];
		while (intervalle.size() > 2) {
			intervalle = merge(intervalle, tab, tabRes);
			Debugger.log("Liste des intervalles : " + Arrays.toString(intervalle.toArray()));
			tab = Arrays.copyOf(tabRes, tab.length);
		}
		return tab;
	}

	/**
	 * Classe pour trier les éléments de l'intervalle [debut, fin[ sur un
	 * tableau d'entiers.
	 */
	static class TriIntervalle implements Runnable {
		// https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html
		private long[] tab;
		private int debut;
		private int fin;

		TriIntervalle(long[] tab, int debut, int fin) {
			this.tab = tab;
			this.debut = debut;
			this.fin = fin;
		}

		/**
		 * méthode run() exécutée quand on démarre un thread TriIntervalle
		 */
		public void run() {
			Arrays.sort(tab, debut, fin);
		}
	}

	public static void main(String[] args) {
		/*
		 * p : nombre de threads = le nombre de coeurs disponibles par défaut
		 */
		int p = Runtime.getRuntime().availableProcessors();
		if (args.length > 1) {
			try {
				p = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Argument entier coeurs attendu!");
				System.exit(1);
			}
		}

		Debugger.enabled = true;

		int taille = 23;
		long[] tab1 = creeTableauAleatoire(taille, 100);
		System.out.println("tab1 non trié : " + Arrays.toString(tab1));
		tab1 = tri(tab1, p);
		System.out.println("tab1 trié     : " + Arrays.toString(tab1));

		long[] tab2 = { 45, 20, 80, 72, 9, 86, 5, 38, 77, 76, 24, 62, 96, 46, 45, 78, 43, 71, 1, 27, 4, 60, 8 };
		System.out.println("tab2 non trié : " + Arrays.toString(tab2));
		tab2 = tri(tab2, 4);
		System.out.println("tab2 trié     : " + Arrays.toString(tab2));

		long[] tab3 = creeTableauAleatoire(127, 100);
		System.out.println("tab3 non trié : " + Arrays.toString(tab3));
		tab3 = tri(tab3, 7);
		System.out.println("tab3 trié     : " + Arrays.toString(tab3));

		long[] tab4 = creeTableauAleatoire(7, 100);
		System.out.println("tab4 non trié : " + Arrays.toString(tab4));
		tab4 = tri(tab4, 3);
		System.out.println("tab4 trié     : " + Arrays.toString(tab4));
	}
}

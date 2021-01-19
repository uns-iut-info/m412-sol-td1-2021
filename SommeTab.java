/*
 * M412 2019-2020: distributed programming
 */

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Random;

/**
 * Somme parallèle des éléments d'un tableau
 */
public class SommeTab {

	private static final Random RAND = new Random(System.currentTimeMillis());

	/**
	 * Crée un tableau de la taille donnée et le remplit avec des entiers
	 * positifs aléatoires
	 */
	public static long[] creeTableauAleatoire(int taille) {
		long[] tab = new long[taille];
		for (int i = 0; i < tab.length; i++) {
			tab[i] = RAND.nextInt(100);
		}
		return tab;
	}

	/**
	 * Crée un tableau de la taille donnée et le remplit avec des entiers
	 * positifs connus
	 */
	private static long[] creeTableauConnu(int taille, int nombre) {
		long[] tab = new long[taille];
		Arrays.fill(tab, nombre);
		return tab;
	}

	/**
	 * Retourne la somme des valeurs des éléments d'un tableau d'entiers. Le
	 * tableau est découpé en morceaux de tailles égales p (argument de la ligne
	 * de commande)
	 */
	private static long somme(long[] tab, int p) throws InterruptedException {

		// assert tab.length > p(p-1)
		int intervalle = (tab.length % p == 0) ? tab.length / p : tab.length / p + 1;
		SommeIntervalle[] sommeIntervalle = new SommeIntervalle[p];
		Thread[] threads = new Thread[p];

		for (int i = 0; i < p; i++) { // p - 1 // crée p instances différentes
										// pour
			sommeIntervalle[i] = new SommeIntervalle(tab, i * intervalle, Math.min((i + 1) * intervalle, tab.length));
			threads[i] = new Thread(sommeIntervalle[i]);
			threads[i].start();
		}
		// sommeIntervalle[p - 1] = new SommeIntervalle(tab, (p - 1) *
		// intervalle, tab.length);
		// threads[p - 1] = new Thread(sommeIntervalle[p - 1]);
		// threads[p - 1].start();
		for (Thread t : threads) {
			t.join();
		}
		long total = 0;
		for (SommeIntervalle s : sommeIntervalle) {
			total += s.getResultat();
		}
		return total;
	}

	/**
	 * Retourne la somme des valeurs des éléments d'un tableau d'entiers. Le
	 * tableau est transformé en IntStream sur lequel on applique une réduction
	 * de type somme (Java 1.8)
	 */
	private static long sommeStreamSeq(long[] tabTest) {
		return Arrays.stream(tabTest).sum();
	}

	/**
	 * Retourne la somme des valeurs des éléments d'un tableau d'entiers. Le
	 * tableau est transformé en IntStream parallèle sur lequel on applique une
	 * réduction de type somme (Java 1.8) - Voir comment connaître / paramétrer
	 * le nombre de threads utilisés
	 * 
	 * http://stackoverflow.com/questions/21163108/custom-thread-pool-in-java-8-
	 * parallel-stream
	 */
	private static long sommeStreamPar(long[] tab) {
		return Arrays.stream(tab).parallel().sum();
	}

	/**
	 * Quelques essais de somme pour voir.
	 */
	public static void main(String[] args) throws Exception {

		int taille = 1024;
		/*
		 * http://stackoverflow.com/questions/3038392/do-java-arrays-have-a-
		 * maximum-size
		 */
		int repetition = 100;
		if (args.length > 1) {
			try {
				taille = Integer.parseInt(args[0]);
				repetition = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Arguments entiers attendus!");
				System.exit(1);
			}
		} else {
			System.err.println("arguments taille et repetition attendus!");
			System.exit(1);
		}
		// p : nombre de threads = le nombre de coeurs disponibles par défaut
		int p = Runtime.getRuntime().availableProcessors();
		if (args.length > 2) {
			try {
				p = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				System.err.println("Argument entier coeurs attendu!");
				System.exit(1);
			}
		}

		// long[] tabTest = creeTableauAleatoire(taille);
		long[] tabTest = creeTableauConnu(taille, 10);

		// mesure du temps d'exécution (répété n fois)

		System.out.println();
		p = p / 4;
		// calcul parallèle
		long totalPar = 0;
		for (int cpu = 0; cpu < 6; cpu++) {
			long tempsDebutPar = System.currentTimeMillis();
			totalPar = 0;
			for (int j = 1; j <= repetition; j++) {
				totalPar = somme(tabTest, p);

			}
			long tempsFinPar = System.currentTimeMillis();
			System.out.printf("Calcul parallèle  (%3d) pour %10d éléments (répété %d fois) =>  %6d ms ; %20d %n", p,
					taille, repetition, tempsFinPar - tempsDebutPar, totalPar);
			p *= 2;
		}

		// calcul séquentiel
		long totalSeq = 0;
		long tempsDebutSeq = System.currentTimeMillis();
		for (int j = 1; j <= repetition; j++) {
			totalSeq = somme(tabTest, 1);
		}
		long tempsFinSeq = System.currentTimeMillis();

		System.out.printf("Calcul séquentiel (  1) pour %10d éléments (répété %d fois) =>  %6d ms ; %20d %n", taille,
				repetition, tempsFinSeq - tempsDebutSeq, totalSeq);

		// calcul avec un stream Java 1.8
		long totalStreamSeq = 0;
		long tempsDebutStreamSeq = System.currentTimeMillis();
		for (int j = 1; j <= repetition; j++) {
			totalStreamSeq = sommeStreamSeq(tabTest);
		}
		long tempsFinStreamSeq = System.currentTimeMillis();
		System.out.printf("Calcul streamSeq  (  1) pour %10d éléments (répété %d fois) =>  %6d ms ; %20d %n", taille,
				repetition, tempsFinStreamSeq - tempsDebutStreamSeq, totalStreamSeq);

		// calcul avec un stream Java 1.8
		long totalStreamPar = 0;
		long tempsDebutStreamPar = System.currentTimeMillis();
		for (int j = 1; j <= repetition; j++) {
			totalStreamPar = sommeStreamPar(tabTest);
		}
		long tempsFinStreamPar = System.currentTimeMillis();

		System.out.printf("Calcul streamPar  (  ?) pour %10d éléments (répété %d fois) =>  %6d ms ; %20d %n", taille,
				repetition, tempsFinStreamPar - tempsDebutStreamPar, totalStreamPar);
		// vérification des résultats
		if (totalPar != totalSeq) {
			throw new RuntimeException("Somme fausse (Par/Seq) : " + totalPar + " vs. " + totalSeq);
		}
		if (totalStreamSeq != totalSeq) {
			throw new RuntimeException("Somme fausse (StreamSeq/Seq): " + totalStreamSeq + " vs. " + totalSeq);
		}
		if (totalStreamPar != totalSeq) {
			throw new RuntimeException("Somme fausse (StreamPar/Seq): " + totalStreamPar + " vs. " + totalSeq);
		}

		long gcCount = 0;
		long gcTimeMs = 0;
		for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
			gcCount += gc.getCollectionCount();
			gcTimeMs += gc.getCollectionTime();
		}

		long usedMem = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
		System.out.println("gcCount : " + gcCount);
		System.out.println("gcTimeMs : " + gcTimeMs);
		System.out.println("usedMem : " + usedMem);
	}
}
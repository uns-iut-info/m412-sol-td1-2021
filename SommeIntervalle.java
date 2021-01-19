/*
 * M412 2019-2020: distributed programming
 */

/**
 * Thread qui calcule la somme des éléments de l'intervalle [debut, fin[ sur
 * un tableau d'entiers. Un intervalle vide renvoie 0.
 */
public class SommeIntervalle implements Runnable {
	private long[] tab;
	private int debut;
	private int fin;
	private long resultat = 0;

	SommeIntervalle(long[] tab, int debut, int fin) {
		this.tab = tab;
		this.debut = debut;
		this.fin = fin;
	}

	long getResultat() {
		return resultat;
	}

	/**
	 * méthode run() exécutée quand on démarre un thread SommeIntervalle
	 */
	public void run() {
		//System.out.println("Somme de " + debut + " à " + fin);
		for (int i = debut; i < fin; i++) {
			resultat += tab[i];
		}
	}
}

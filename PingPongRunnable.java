/*
 * M412 2019-2020: distributed programming
 */

/*
 * PingPongThread : 
 * crée un thread de nom nom et affiche nb fois ce nom
 * Thread.sleep permet d'observer l'entrelacement des thread
 * 
 */

public class PingPongRunnable implements Runnable {
	private String nom;
	private int nb;

	private PingPongRunnable(String nom, int nb) {
		this.nom = nom;
		this.nb = nb;
	}

//	public String getName() {
//		return nom;
//	}

	public void run() {
		for (int count = 0; count < nb; count++) {
			System.out.println(Thread.currentThread().getName() + ":" + this.nom + "!");
			try {
				Thread.sleep((long) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		(new Thread(new PingPongRunnable("ping", 1000))).start();
		(new Thread(new PingPongRunnable("pong", 1000))).start();

		// ajouter getter setters pour modifier le nom
//		PingPongRunnable h = new PingPongRunnable("poum", 1000);
//		Thread t1 = new Thread(h);
//		t1.start();
//		Thread t2 = new Thread(h);
//		t2.start();
	}

}
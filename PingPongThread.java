/*
 * M412 2019-2020: distributed programming
 */

/*
 * PingPongThread : 
 * crée un thread de nom nom et affiche nb fois ce nom
 * Thread.sleep permet d'observer l'entrelacement des threads
 * 
 */

public class PingPongThread extends Thread {
	private String nom;
	private int nb;

	private PingPongThread(String type, int nb) {
		this.nom = type;
		this.nb = nb;
	}

	public void run() {
		for (int count = 0; count < nb; count++) {
			System.out.println(this.nom + "!");
			try {
				Thread.sleep((long) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		(new PingPongThread("ping", 1000)).start();
		//(new PingPongThread("pong", 1000)).start();
		PingPongThread pong = new PingPongThread("pong", 1000);
		pong.start();
	}

}
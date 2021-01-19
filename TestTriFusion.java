/*
 * M412 2019-2020: distributed programming
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class TestTriFusion {

	@Test
	void test() {
		Debugger.enabled = false;
		int[] taille = { 0, 7, 11, 103, 1789, 2048, 1048577 };
		int[] thread = { 1, 2, 3, 4, 8, 13, 64 };
		for (int t : taille) {
			for (int p : thread) {
				long[] tab = TriFusion.creeTableauAleatoire(t, 1000);
				tab = TriFusion.tri(tab, p);

				for (int i = 0; i < tab.length - 1; i++) {
					if (tab[i] > tab[i + 1])
						fail("tri incorrect");
				}
			}
		}
	}

}

/*
 * M412 2019-2020: distributed programming
 */

class Debugger {
	static boolean enabled;

	private static boolean isEnabled() {
		return enabled;
	}

	static void log(Object o) {
		if (Debugger.isEnabled()) {
			System.out.println(o.toString());
		}
	}
}
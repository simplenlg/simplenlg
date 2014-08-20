package simplenlg.morphology.english;

/**
 * This class is used to parse numbers that are passed as figures, to determine
 * whether they should take "a" or "an" as determiner.
 * 
 * @author bertugatt
 * 
 */
public class DeterminerAgrHelper {
	/*
	 * An array of strings which are exceptions to the rule that "an" comes
	 * before vowels
	 */
	private static final String[] AN_EXCEPTIONS = { "one", "180", "110" };

	/*
	 * Start of string involving vowels, for use of "an"
	 */
	private static final String AN_AGREEMENT = "\\A(a|e|i|o|u).*";

	/*
	 * Start of string involving numbers, for use of "an" -- courtesy of Chris
	 * Howell, Agfa healthcare corporation
	 */
	// private static final String AN_NUMERAL_AGREEMENT =
	// "^(((8((\\d+)|(\\d+(\\.|,)\\d+))?).*)|((11|18)(\\d{3,}|\\D)).*)$";

	/**
	 * Check whether this string starts with a number that needs "an" (e.g.
	 * "an 18% increase")
	 * 
	 * @param string
	 *            the string
	 * @return <code>true</code> if this string starts with 11, 18, or 8,
	 *         excluding strings that start with 180 or 110
	 */
	public static boolean requiresAn(String string) {
		boolean req = false;
		
		String lowercaseInput = string.toLowerCase();

		if (lowercaseInput.matches(AN_AGREEMENT) && !isAnException(lowercaseInput)) {
			req = true;

		} else {
			String numPref = getNumericPrefix(lowercaseInput);

			if (numPref != null && numPref.length() > 0
					&& numPref.matches("^(8|11|18).*$")) {
				Integer num = Integer.parseInt(numPref);
				req = checkNum(num);
			}
		}

		return req;
	}

	/*
	 * check whether a string beginning with a vowel is an exception and doesn't
	 * take "an" (e.g. "a one percent change")
	 * 
	 * @return
	 */
	private static boolean isAnException(String string) {
		for (String ex : AN_EXCEPTIONS) {
			if (string.matches("^" + ex + ".*")) {
				// if (string.equalsIgnoreCase(ex)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Returns <code>true</code> if the number starts with 8, 11 or 18 and is
	 * either less than 100 or greater than 1000, but excluding 180,000 etc.
	 */
	private static boolean checkNum(int num) {
		boolean needsAn = false;

		// eight, eleven, eighty and eighteen
		if (num == 11 || num == 18 || num == 8 || (num >= 80 && num < 90)) {
			needsAn = true;

		} else if (num > 1000) {
			num = Math.round(num / 1000);
			needsAn = checkNum(num);
		}

		return needsAn;
	}

	/*
	 * Retrieve the numeral prefix of a string.
	 */
	private static String getNumericPrefix(String string) {
		StringBuffer numeric = new StringBuffer();

		if (string != null) {
			string = string.trim();

			if (string.length() > 0) {

				StringBuffer buffer = new StringBuffer(string);
				char first = buffer.charAt(0);

				if (Character.isDigit(first)) {
					numeric.append(first);

					for (int i = 1; i < buffer.length(); i++) {
						Character next = buffer.charAt(i);

						if (Character.isDigit(next)) {
							numeric.append(next);

							// skip commas within numbers
						} else if (next.equals(',')) {
							continue;

						} else {
							break;
						}
					}
				}
			}
		}

		return numeric.length() == 0 ? null : numeric.toString();
	}

}

package org.fingies;

// We probably won't need to use this class for a while... but it's here now in case we do.

/**
 * An InputCheck can be used to check whether a string satisfies a certain condition or not.
 * This could be especially useful when getting input from the user, such as for requiring
 * their input to meet certain conditions before allowing them to enter it.
 * 
 * Other classes can implement this interface in order to override the check() method,
 * which is what gets called to check the string.
 * 
 * It will probably be easier to just create anonymous inner functions with this class, as
 * detailed below.
 * 
 * @author Lincoln Craddock
 */
public interface InputCheck {
	
	/**
	 * An InputCheck that returns an empty string for every input string.
	 * 
	 * e.i. Every string statisfies this check.
	 */
	public static final InputCheck acceptAll = new InputCheck() {

		@Override
		public String check(String t) {
			return "";
		}
	};
	
	/**
	 * Checks whether the given string meets certain requirements. If it doesn't, a message
	 * explaining which requirements were not met should be returned. Otherwise, if the string
	 * is acceptable, the empty string ("") should be returned.
	 * 
	 * The easiest way to override this method will probably be to create an anonymous inner
	 * class that implements InputCheck, like this:
	 * 
	 * <pre><code>
	 * 
	 * InputCheck ex = new InputCheck() {
	 *     @Override
	 *     public String check(String t) {
	 *         // code that returns "" if t satisfies the check, and a message if it doesn't.
	 *     }
	 * };
	 * 
	 * </code></pre>
	 * 
	 * @param t The input string to check.
	 * @return An empty string if the input meets the requirements; otherwise, a message
	 *         explaining which requirements were not met
	 */
	public String check(String t);

}

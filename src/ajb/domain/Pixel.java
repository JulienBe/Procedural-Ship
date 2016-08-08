package ajb.domain;

public class Pixel {

	public enum State {
        EMPTY(0),
        /**
         * Greyish one
         */
        FILLED(1),
        /**
         * Black one
         */
        BORDER(2),
        /**
         * A secondary pixel has the secondary color, that is to say, the bright color
         */
        SECONDARY(3),
        FILL_STRUCTURE(4);

        int value;

        State(int i) { value = i; }
    }

    /**
     * Default is {@link State.EMPTY}
     */
	public State value = State.EMPTY;
	public int depth = 0;

}

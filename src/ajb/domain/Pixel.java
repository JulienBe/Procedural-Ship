package ajb.domain;

public class Pixel {

	public enum State {
        EMPTY(0),
        FILLED(1),
        BORDER(2),
        /**
         * A secondary pixel has the secondary color, that is to say, the bright color
         */
        SECONDARY(3);

        int value;

        State(int i) { value = i; }
    }

	public State value = State.EMPTY;
	public int depth = 0;

}

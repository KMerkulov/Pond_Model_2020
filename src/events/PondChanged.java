package events;

import java.util.EventObject;

public class PondChanged extends EventObject{
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public PondChanged(Object source) {
        super(source);
    }
}


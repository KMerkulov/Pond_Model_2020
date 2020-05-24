package events;

import java.util.EventListener;

public interface PondChangedListener extends EventListener {
    public void pondChangedEventOccured(PondChanged evt);
}

package lib.sharedcollections.objects.actions;

import lib.sharedcollections.util.serial.Serializable;

// Actions must be fully atomic - failure to apply should mean the prior state is maintained. This is crucial for
// effective client-side prediction (Dead Reckoning)
public interface Action extends Serializable {
    boolean apply();

    void revert();
}

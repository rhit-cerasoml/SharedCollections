package lib.sharedcollections.objects.sharedhashmap;

import lib.sharedcollections.util.serial.Serializable;

public interface TargetedAction<V> extends Serializable {
    boolean apply(V target);
    boolean revert(V target);
}

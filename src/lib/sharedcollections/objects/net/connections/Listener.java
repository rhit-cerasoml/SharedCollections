package lib.sharedcollections.objects.net.connections;

import lib.sharedcollections.objects.net.connections.id.Source;
import lib.sharedcollections.util.serial.SerializingInputStream;

public interface Listener {
    void accept(byte[] data, Source source) throws SerializingInputStream.InvalidStreamLengthException;
}

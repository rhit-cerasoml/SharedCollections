package lib.sharedcollections.objects.net.connections;

import lib.sharedcollections.objects.net.connections.id.Destination;
import lib.sharedcollections.objects.net.connections.id.Source;

import java.io.IOException;

public interface Connection {
    void send(byte[] data, Destination destination) throws IOException;
    void setListener(Listener listener);
    void close() throws IOException;
    boolean isClosed();
    Source getSource();
}

package lib.sharedcollections.objects.net.host;

import lib.sharedcollections.objects.net.connections.Connection;

public interface ConnectionAcceptor {
    void acceptConnection(Connection c);
}

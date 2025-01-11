package lib.sharedcollections.objects.net.connections;

import lib.sharedcollections.objects.net.connections.id.Destination;
import lib.sharedcollections.objects.net.connections.id.Source;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectionGroup implements Connection {
    private ArrayList<Connection> connections = new ArrayList<>();
    private Listener listener = (data, source) -> {};
    private boolean open = true;

    public void addConnection(Connection connection){
        if(!open) return;
        connection.setListener(listener);
        connections.add(connection);
    }

    public void send(byte[] data, Destination destination) throws IOException {
        if(!open) throw new IOException("Connection Closed");
        if(destination.policy == Destination.Policy.ALL) {
            for (int i = connections.size() - 1; i >= 0; i--) {
                if (connections.get(i).isClosed()) {
                    connections.remove(i);
                    continue;
                }
                connections.get(i).send(data, new Destination(Destination.Policy.ALL));
            }
        }else if(destination.policy == Destination.Policy.ALL_EXCEPT){
            for (int i = connections.size() - 1; i >= 0; i--) {
                if(connections.get(i).getSource() != destination.source) {
                    if (connections.get(i).isClosed()) {
                        connections.remove(i);
                        continue;
                    }
                    connections.get(i).send(data, new Destination(Destination.Policy.ALL));
                }
            }
        }else if(destination.policy == Destination.Policy.ONLY){
            for (int i = connections.size() - 1; i >= 0; i--) {
                if(connections.get(i).getSource() == destination.source) {
                    if (connections.get(i).isClosed()) {
                        connections.remove(i);
                        break;
                    }
                    connections.get(i).send(data, new Destination(Destination.Policy.ALL));
                    break;
                }
            }
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        for(Connection connection : connections){
            connection.setListener(listener);
        }
    }

    public void close() throws IOException {
        open = false;
        for(Connection connection : connections){
            connection.close();
        }
    }

    public boolean isClosed() {
        return !open;
    }

    @Override
    public Source getSource() {
        throw new RuntimeException("Source requested from connection group. Did you add a group to another group?");
    }
}

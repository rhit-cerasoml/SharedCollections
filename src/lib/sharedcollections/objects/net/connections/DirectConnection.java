package lib.sharedcollections.objects.net.connections;

import lib.sharedcollections.objects.net.connections.id.Destination;
import lib.sharedcollections.objects.net.connections.id.Source;
import lib.sharedcollections.util.serial.SerializingInputStream;

import java.io.IOException;

public class DirectConnection implements Connection {
    private Listener listener = new NullListener();
    private DirectConnection endpoint;
    private boolean open = true;
    private final Source source = new Source();

    public DirectConnection(){
        this.endpoint = new DirectConnection(this);
    }

    private DirectConnection(DirectConnection endpoint){
        this.endpoint = endpoint;
    }

    public DirectConnection getEndpoint() {
        return endpoint;
    }

    @Override
    public void send(byte[] data, Destination destination) throws IOException {
        if(!open) throw new IOException("Connection Closed");
        if(      destination.policy == Destination.Policy.ALL ||
                (destination.policy == Destination.Policy.ONLY          && destination.source == source) ||
                (destination.policy == Destination.Policy.ALL_EXCEPT    && destination.source != source)) {
            try {
                this.endpoint.recv(data);
            }catch (Exception e){
                throw new IOException("Direct Receive Failed");
            }
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void close() {
        if(open) {
            endpoint.endpoint = null;
            endpoint.open = false;
            endpoint = null;
            open = false;
        }
    }

    @Override
    public boolean isClosed() {
        return !open;
    }

    @Override
    public Source getSource() {
        return source;
    }

    private void recv(byte[] data) throws SerializingInputStream.InvalidStreamLengthException, IOException {
        listener.accept(data, source);
    }
}

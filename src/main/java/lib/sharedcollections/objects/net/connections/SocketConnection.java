package lib.sharedcollections.objects.net.connections;

import lib.sharedcollections.objects.net.connections.id.Destination;
import lib.sharedcollections.objects.net.connections.id.Source;
import lib.sharedcollections.util.serial.SerializingInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketConnection extends Thread implements Connection {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Listener listener = new NullListener();
    private final Source source = new Source();
    private volatile boolean open = true; // no lock needed since we assign this value prior to the closing of socket

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public SocketConnection(Socket socket, boolean start) throws IOException {
        this(socket);
        if(start){
            start();
        }
    }

    @Override
    public void send(byte[] data, Destination destination) throws IOException {
        if(open){
            if(      destination.policy == Destination.Policy.ALL ||
                    (destination.policy == Destination.Policy.ONLY          && destination.source == source) ||
                    (destination.policy == Destination.Policy.ALL_EXCEPT    && destination.source != source)) {
                byte[] buf = new byte[4];
                int val = data.length;

                for (int i = 4; i != 0; i--) {
                    buf[i - 1] = (byte) (val & 0xFF);
                    val >>= 8;
                }
                outputStream.write(buf);
                outputStream.write(data);
                outputStream.flush();
            }
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void close() throws IOException {
        open = false;
        socket.close();
    }

    @Override
    public boolean isClosed() {
        return !open;
    }

    @Override
    public Source getSource() {
        return source;
    }

    @Override
    public void run() {
        super.run();
        while(open){
            try {
                byte[] binlen = new byte[4];
                int result = inputStream.read(binlen);
                if(result != 4) {
                    System.out.println("malformed packet size: " + result);
                    close();
                }
                int len = 0;
                for(int i = 0; i < 4; i++) {
                    len <<= 8;
                    len |= Byte.toUnsignedInt(binlen[i]);
                }
                byte[] data = new byte[len];
                result = inputStream.read(data);
                if(result != len) {
                    System.out.println("malformed packet");
                    close();
                }
                listener.accept(data, source);
            } catch (IOException e) {
                e.printStackTrace();
                open = false;
            } catch (SerializingInputStream.InvalidStreamLengthException ignored) {
            }
        }
    }
}

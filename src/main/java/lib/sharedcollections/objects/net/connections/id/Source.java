package lib.sharedcollections.objects.net.connections.id;

public class Source {
    private static int idGen = 0;
    public final int ID;
    public Source(){
        ID = idGen;
        idGen++;
    }
}

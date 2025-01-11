package lib.sharedcollections.objects.net.connections.id;

public class Destination {
    public enum Policy {
        ALL,
        ALL_EXCEPT,
        ONLY
    }
    public final Policy policy;
    public final Source source;
    public Destination(Policy policy, Source source){
        this.policy = policy;
        this.source = source;
    }
    public Destination(Policy policy){
        this.policy = policy;
        this.source = null;
        if(policy != Policy.ALL){
            throw new RuntimeException("Policy not valid without destination");
        }
    }
}

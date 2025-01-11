package lib.sharedcollections.util.serial;

public class Optional<T extends Serializable> {
    public T content = null;
    public boolean valid = false;
    private Deserializer<T> deserializer;
    public Optional(Deserializer<T> deserializer){
        this.deserializer = deserializer;
    }
    public void set(T content){
        this.content = content;
        this.valid = true;
    }
    public void invalidate(){
        this.content = null;
        this.valid = false;
    }
    public void write(SerializingOutputStream out){
        if(valid){
            out.writeBoolean(true);
            this.content.serialize(out);
        }else{
            out.writeBoolean(false);
        }
    }
    public void read(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.valid = in.readBoolean();
        if(this.valid){
            this.content = deserializer.deserialize(in);
        }
    }

    @Override
    public String toString() {
        return content.toString();
    }
}

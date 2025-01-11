package lib.sharedcollections.objects.actions;

import lib.sharedcollections.util.serial.Serializable;
import lib.sharedcollections.util.serial.SerializingInputStream;
import lib.sharedcollections.util.serial.SerializingOutputStream;

public class ActionResponse implements Serializable {
    boolean success;
    public ActionResponse(boolean success){
        this.success = success;
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeBoolean(success);
    }

    public ActionResponse(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.success = in.readBoolean();
    }
}

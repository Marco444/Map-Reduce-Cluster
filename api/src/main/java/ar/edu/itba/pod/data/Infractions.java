package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Infractions implements DataSerializable {
    String code;
    String description;

    public Infractions() {
    }

    private Infractions(
            String code, String description
    ) {
        this.code = code;
        this.description = description;
    }

    public static Infractions of(String code, String description) {
        return new Infractions(code, description);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(code);
        out.writeUTF(description);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.code = in.readUTF();
        this.description = in.readUTF();
    }
}

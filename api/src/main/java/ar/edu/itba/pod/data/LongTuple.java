package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class LongTuple implements DataSerializable {
    private long key;
    private long value;

    public LongTuple() {

    }

    private LongTuple(long key, long value) {
        this.key = key;
        this.value = value;
    }

    public static LongTuple of(long key, long value) {
        return new LongTuple(key, value);
    }

    public long getKey() {
        return key;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongTuple that = (LongTuple) o;
        return key == that.key && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(key);
        out.writeLong(value);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        key = in.readLong();
        value = in.readLong();
    }
}

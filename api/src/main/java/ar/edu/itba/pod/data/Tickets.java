package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Tickets implements DataSerializable {
    private String licensePlate;
    private String issueDate;
    private long infractionCode;
    private double fineAmount;
    private String countyName;
    private String issuingAgency;

    public Tickets() {

    }

    private Tickets(
            String licensePlate,
            String issueDate,
            int infractionCode,
            double fineAmount,
            String countyName,
            String issuingAgency
    ) {
        this.licensePlate = licensePlate;
        this.issueDate = issueDate;
        this.infractionCode = infractionCode;
        this.fineAmount = fineAmount;
        this.countyName = countyName;
        this.issuingAgency = issuingAgency;
    }

    public static Tickets of(
            String licensePlate,
            String issueDate,
            int infractionCode,
            double fineAmount,
            String countyName,
            String issuingAgency
    ) {
        return new Tickets(
                licensePlate,
                issueDate,
                infractionCode,
                fineAmount,
                countyName,
                issuingAgency
        );
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public long getInfractionCode() {
        return infractionCode;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(licensePlate);
        out.writeUTF(issueDate);
        out.writeLong(infractionCode);
        out.writeDouble(fineAmount);
        out.writeUTF(countyName);
        out.writeUTF(issuingAgency);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        licensePlate = in.readUTF();
        issueDate = in.readUTF();
        infractionCode = in.readLong();
        fineAmount = in.readDouble();
        countyName = in.readUTF();
        issuingAgency = in.readUTF();
    }
}


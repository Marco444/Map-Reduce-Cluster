package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class TicketsNYC implements DataSerializable {
    private String licensePlate;
    private String issueDate;
    private int infractionCode;
    private double fineAmount;
    private String countyName;
    private String issuingAgency;

    public TicketsNYC() {

    }

    private TicketsNYC(
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

    public static TicketsNYC of(
            String licensePlate,
            String issueDate,
            int infractionCode,
            double fineAmount,
            String countyName,
            String issuingAgency
    ) {
        return new TicketsNYC(
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

    public int getInfractionCode() {
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
        out.writeInt(infractionCode);
        out.writeDouble(fineAmount);
        out.writeUTF(countyName);
        out.writeUTF(issuingAgency);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        licensePlate = in.readUTF();
        issueDate = in.readUTF();
        infractionCode = in.readInt();
        fineAmount = in.readDouble();
        countyName = in.readUTF();
        issuingAgency = in.readUTF();
    }
}


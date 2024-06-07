package ar.edu.itba.pod.data;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ticket implements DataSerializable {
    private String licensePlate;
    private LocalDate issueDate;
    private String infractionCode;
    private double fineAmount;
    private String countyName;
    private String issuingAgency;

    public Ticket() {
    }

    private Ticket(String licensePlate, LocalDate issueDate, String infractionCode, double fineAmount, String countyName, String issuingAgency) {
        this.licensePlate = licensePlate;
        this.issueDate = issueDate;
        this.infractionCode = infractionCode;
        this.fineAmount = fineAmount;
        this.countyName = countyName;
        this.issuingAgency = issuingAgency;
    }

    public static Ticket of(String licensePlate, String issueDate, String infractionCode, double fineAmount, String countyName, String issuingAgency) {
        return new Ticket(licensePlate, LocalDate.parse(issueDate), infractionCode, fineAmount, countyName, issuingAgency);
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getInfractionCode() {
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
        out.writeUTF(issueDate.format(DateTimeFormatter.ISO_DATE));
        out.writeUTF(infractionCode);
        out.writeDouble(fineAmount);
        out.writeUTF(countyName);
        out.writeUTF(issuingAgency);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        licensePlate = in.readUTF();
        issueDate = LocalDate.parse(in.readUTF(), DateTimeFormatter.ISO_DATE);
        infractionCode = in.readUTF();
        fineAmount = in.readDouble();
        countyName = in.readUTF();
        issuingAgency = in.readUTF();
    }
}

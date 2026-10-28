package restaurantrush;

public class VIPCustomer extends Customer {
    public VIPCustomer(String id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public int getSatisfactionLossPerTurn() {
        return 5;
    }

    // Bill calculation defers to Customer to read the locked price recorded at order time
    @Override
    public int calculateBill() {
        return super.calculateBill();
    }

    @Override
    public String getTypeLabel() {
        return "VIP";
    }
}
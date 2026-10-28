package restaurantrush;

public class VIPCustomer extends Customer {
    public VIPCustomer(String id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    // VIPs are more patient and lose only 5 satisfaction points per turn
    @Override
    public int getSatisfactionLossPerTurn() {
        return 5;
    }

    // VIPs receive a 10% discount on the base item price
    @Override
    public int calculateBill() {
        int basePrice = super.calculateBill();
        return (int) Math.round(basePrice * 0.90);
    }

    @Override
    public String getTypeLabel() {
        return "VIP";
    }
}
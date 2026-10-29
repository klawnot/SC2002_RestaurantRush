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

    @Override
    public int calculateItemPrice(MenuItem item, boolean happyHourActive) {
        int price = (int) Math.round(item.getPriceCents() * 0.90);
        if (happyHourActive) {
            price = (int) Math.round(price * 0.80);
        }
        return price;
    }
}
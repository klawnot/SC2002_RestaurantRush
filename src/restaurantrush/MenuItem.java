package restaurantrush;

public class MenuItem {
    private final String name;
    private final int priceCents;
    private final int prepUnitsRequired;

    public MenuItem(String name, int priceCents, int prepUnitsRequired) {
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnitsRequired = prepUnitsRequired;
    }

    public String getName() {
        return name;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public int getPrepUnitsRequired() {
        return prepUnitsRequired;
    }

    @Override
    public String toString() {
        return String.format("%s ($%.2f, %d prep unit%s)", 
            name, priceCents / 100.0, prepUnitsRequired, prepUnitsRequired > 1 ? "s" : "");
    }
}
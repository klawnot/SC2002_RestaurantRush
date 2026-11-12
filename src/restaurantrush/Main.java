package restaurantrush;

import java.util.Random;

public class Main {
    // Default seed keeps customer food choices reproducible when no seed is given
    private static final long DEFAULT_SEED = 2002L;

    public static void main(String[] args) {
        long seed = DEFAULT_SEED;
        if (args.length > 0) {
            try {
                seed = Long.parseLong(args[0].trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Invalid seed '" + args[0] + "'. Using default seed " + DEFAULT_SEED + ".");
            }
        }

        Menu menu = new Menu(new Random(seed));
        RestaurantState state = new RestaurantState();
        RestaurantView view = new RestaurantView();
        InputHandler input = new InputHandler();

        System.out.println("==================================================");
        System.out.println("         WELCOME TO RESTAURANT RUSH               ");
        System.out.println("==================================================");
        System.out.println("Seed: " + seed);

        TurnController controller = new TurnController(state, view, input, menu);
        controller.runGame();
    }
}
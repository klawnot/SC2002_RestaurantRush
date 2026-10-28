package restaurantrush;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu(new Random());
        RestaurantState state = new RestaurantState();
        RestaurantView view = new RestaurantView();
        InputHandler input = new InputHandler();

        System.out.println("==================================================");
        System.out.println("         WELCOME TO RESTAURANT RUSH               ");
        System.out.println("==================================================");

        TurnController controller = new TurnController(state, view, input, menu);
        controller.runGame();
    }
}
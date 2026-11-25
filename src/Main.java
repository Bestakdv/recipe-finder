import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int TOP_MEALS = 5;
    private static final String FAVORITES_FILE = "favorites.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            run(scanner);
        // Catching ConnectException first because it means that there is no internet and it needs its own message
        } catch (ConnectException e) {
            System.out.println("Could not connect to TheMealDB. Check your internet connection and try again.");
        } catch (IOException e) {
            System.out.println("Something went wrong with TheMealDB: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("The request was interrupted. Please try again.");
        } finally {
            scanner.close();
        }
    }

    private static void run(Scanner scanner) throws IOException, InterruptedException {
        Favorites favorites = new Favorites(FAVORITES_FILE);
        showFavorites(favorites);

        List<String> ingredients = readIngredients(scanner);
        ApiClient api = new ApiClient();
        RecipeFinder finder = new RecipeFinder(api);
        List<Meal> topMeals = finder.findTopMeals(ingredients, TOP_MEALS);

        if (topMeals.isEmpty()) {
            System.out.println();
            System.out.println("No meals found with those ingredients. Try different ingredients.");
            return;
        }

        System.out.println();
        System.out.println("Top meals:");
        for (int i = 0; i < topMeals.size(); i++) {
            Meal meal = topMeals.get(i);
            System.out.println((i + 1) + ". " + meal.getName()
                    + " (uses " + meal.getMatchCount() + " of your " + ingredients.size() + " ingredients)");
        }

        System.out.println();
        int choice = readChoice(scanner, topMeals.size());

        Recipe recipe = api.getRecipe(topMeals.get(choice - 1).getId());
        List<String> missing = finder.findMissingIngredients(recipe, ingredients);
        printRecipe(recipe, missing);
        askToSave(scanner, favorites, recipe.getName());
    }

    private static List<String> readIngredients(Scanner scanner) {
        List<String> ingredients = new ArrayList<>();
        while (ingredients.isEmpty()) {
            System.out.print("Enter your ingredients, separated by commas: ");
            ingredients = cleanIngredients(scanner.nextLine());
            if (ingredients.isEmpty()) {
                System.out.println("Please enter at least one ingredient.");
            }
        }
        return ingredients;
    }

    private static int readChoice(Scanner scanner, int maxChoice) {
        while (true) {
            System.out.print("Pick a meal (1-" + maxChoice + "): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                // Any non number becomes an invalid choice and it asks again instead of crashing
                choice = 0;
            }

            if (choice >= 1 && choice <= maxChoice) {
                return choice;
            }
            System.out.println("Please enter a number from 1 to " + maxChoice + ".");
        }
    }
    
    /* Cleaning the input to make sure extra spaces and commas don't affect searches 
       and repeated ingredients aren't counted twice when doing the ranking */
    private static List<String> cleanIngredients(String input) {
        String[] parts = input.split(",");
        List<String> ingredients = new ArrayList<>();

        for (int i = 0; i < parts.length; i++) {
            String ingredient = parts[i].trim().toLowerCase();
            if (!ingredient.isEmpty() && !ingredients.contains(ingredient)) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    private static void printRecipe(Recipe recipe, List<String> missing) {
        List<String> ingredients = recipe.getIngredients();
        List<String> measures = recipe.getMeasures();

        System.out.println();
        System.out.println(recipe.getName());
        System.out.println();
        System.out.println("Ingredients:");
        for (int i = 0; i < ingredients.size(); i++) {
            String line = "  - " + (measures.get(i) + " " + ingredients.get(i)).trim();
            if (missing.contains(ingredients.get(i))) {
                line += " (missing)";
            }
            System.out.println(line);
        }

        System.out.println();
        System.out.println("You're missing " + missing.size() + " of " + ingredients.size() + " ingredients.");

        System.out.println();
        System.out.println("Instructions:");
        System.out.println(recipe.getInstructions());
    }

    private static void showFavorites(Favorites favorites) {
        try {
            List<String> names = favorites.load();
            if (!names.isEmpty()) {
                System.out.println("Your favorites:");
                for (int i = 0; i < names.size(); i++) {
                    System.out.println("  - " + names.get(i));
                }
                System.out.println();
            }
        } catch (IOException e) {
            // A problem with the favorites file shouldn't stop the user from searching for meals
            System.out.println("Could not read your favorites.");
            System.out.println();
        }
    }

    private static void askToSave(Scanner scanner, Favorites favorites, String mealName) {
        System.out.println();
        System.out.print("Save this recipe to your favorites? (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        if (!answer.equals("y") && !answer.equals("yes")) {
            return;
        }

        // Error is caught here instead of in main because main's error messages are about TheMealDB and this is a file problem
        try {
            if (favorites.save(mealName)) {
                System.out.println("Saved to favorites.");
            } else {
                System.out.println("This recipe is already in your favorites.");
            }
        } catch (IOException e) {
            System.out.println("Could not save to favorites.");
        }
    }
}
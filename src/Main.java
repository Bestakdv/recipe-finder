import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int TOP_MEALS = 5;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your ingredients, separated by commas: ");
        String input = scanner.nextLine();
        scanner.close();

        List<String> ingredients = cleanIngredients(input);
        RecipeFinder finder = new RecipeFinder(new ApiClient());
        List<Meal> topMeals = finder.findTopMeals(ingredients, TOP_MEALS);

        System.out.println();
        System.out.println("Top meals:");
        for (int i = 0; i < topMeals.size(); i++) {
            Meal meal = topMeals.get(i);
            System.out.println((i + 1) + ". " + meal.getName()
                    + " (uses " + meal.getMatchCount() + " of your " + ingredients.size() + " ingredients)");
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
}
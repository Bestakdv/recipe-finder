import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your ingredients, separated by commas: ");
        String input = scanner.nextLine();
        scanner.close();

        List<String> ingredients = cleanIngredients(input);
        ApiClient api = new ApiClient();

        for (int i = 0; i < ingredients.size(); i++) {
            String ingredient = ingredients.get(i);
            List<Meal> meals = api.searchIngredient(ingredient);

            System.out.println();
            System.out.println(ingredient + ": " + meals.size() + " meals found");
            for (int j = 0; j < meals.size(); j++) {
                System.out.println("  " + meals.get(j).getName());
            }
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
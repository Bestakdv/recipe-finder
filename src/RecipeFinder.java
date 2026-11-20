import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeFinder {
    private final ApiClient api;

    public RecipeFinder(ApiClient api) {
        this.api = api;
    }

    public List<Meal> findTopMeals(List<String> ingredients, int limit) throws IOException, InterruptedException {
        // Using the meal ID as the key as IDs are unique and the same meal from different searches only gets counted as one meal
        Map<String, Meal> mealsID = new HashMap<>();

        for (int i = 0; i < ingredients.size(); i++) {
            List<Meal> meals = api.searchIngredient(ingredients.get(i));

            for (int j = 0; j < meals.size(); j++) {
                Meal meal = meals.get(j);
                mealsID.putIfAbsent(meal.getId(), meal);
                mealsID.get(meal.getId()).addMatch();
            }
        }

        List<Meal> rankedMeals = new ArrayList<>(mealsID.values());

        // Sorting by name because HashMap doesn't keep an order so this makes the same input prints the same order
        Collections.sort(rankedMeals, (a, b) -> {
            if (a.getMatchCount() != b.getMatchCount()) {
                return b.getMatchCount() - a.getMatchCount();
            }
            return a.getName().compareTo(b.getName());
        });

        // Using Math.min as there can be less than 5 meals found and this prevents crashing
        return new ArrayList<>(rankedMeals.subList(0, Math.min(limit, rankedMeals.size())));
    }

    // Comparing in lowercase as the users ingredients are already lowercase and names like Chicken Breast still match
    public List<String> findMissingIngredients(Recipe recipe, List<String> userIngredients) {
        List<String> recipeIngredients = recipe.getIngredients();
        List<String> missing = new ArrayList<>();

        for (int i = 0; i < recipeIngredients.size(); i++) {
            String ingredient = recipeIngredients.get(i);
            if (!userIngredients.contains(ingredient.toLowerCase())) {
                missing.add(ingredient);
            }
        }
        return missing;
    }
}
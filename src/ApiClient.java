import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {
    // TheMealDB's free public test key uses 1 which is why it's at the end and doesn't require anyone signing up
    private static final String URL = "https://www.themealdb.com/api/json/v1/1/";

    // Reusing the line for all requests because the program will make one request for each ingredient
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Meal> searchIngredient(String ingredient) throws IOException, InterruptedException {
        // TheMealDB uses underscores for ingredient names with spaces and encoding with UTF_8 prevents special characters from breaking the URL
        String formatted = URLEncoder.encode(ingredient.replace(" ", "_"), StandardCharsets.UTF_8);
        MealResponse result = gson.fromJson(sendRequest("filter.php?i=" + formatted), MealResponse.class);

        // TheMealDB returns null when it can't find any meals so this prevents any crashes
        if (result.meals == null) {
            return new ArrayList<>();
        }
        return result.meals;
    }

    public Recipe getRecipe(String mealId) throws IOException, InterruptedException {
        JsonObject meal = JsonParser.parseString(sendRequest("lookup.php?i=" + mealId))
                .getAsJsonObject()
                .getAsJsonArray("meals")
                .get(0)
                .getAsJsonObject();

        List<String> ingredients = new ArrayList<>();
        List<String> measures = new ArrayList<>();

        // The API has up to 20 ingredients as separate fields instead of a list and this loopsthrough all of them and skips any empty ones
        for (int i = 1; i <= 20; i++) {
            String ingredient = getText(meal, "strIngredient" + i);
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
                measures.add(getText(meal, "strMeasure" + i));
            }
        }
        return new Recipe(getText(meal, "strMeal"), ingredients, measures, getText(meal, "strInstructions"));
    }

    // Searching and looking up recipes send the same request and this keeps it in one place
    private String sendRequest(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + path))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Unused ingredient fields can be empty or null so this turns both into an empty string to prevent crashing
    private String getText(JsonObject meal, String key) {
        JsonElement value = meal.get(key);
        if (value == null || value.isJsonNull()) {
            return "";
        }
        return value.getAsString().trim();
    }

    // The API puts the list it returns inside objects so this class is needed to reach the list
    private static class MealResponse {
        List<Meal> meals;
    }
}
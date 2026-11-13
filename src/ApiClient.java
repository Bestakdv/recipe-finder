import com.google.gson.Gson;
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

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "filter.php?i=" + formatted))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        MealResponse result = gson.fromJson(response.body(), MealResponse.class);

        // TheMealDB returns null when it can't find any meals so this prevents any crashes
        if (result.meals == null) {
            return new ArrayList<>();
        }
        return result.meals;
    }

    // The API puts the list it returns inside objects so this class is needed to reach the list
    private static class MealResponse {
        List<Meal> meals;
    }
}
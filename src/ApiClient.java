import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    // TheMealDB's free public test key uses 1 which is why it's at the end and doesn't require anyone signing up
    private static final String URL = "https://www.themealdb.com/api/json/v1/1/";

    // Reusing the line for all requests because the program will make one request for each ingredient
    private final HttpClient client = HttpClient.newHttpClient();

    public String searchIngredient(String ingredient) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "filter.php?i=" + ingredient))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
import com.google.gson.annotations.SerializedName;

public class Meal {
    // Improving readability by using better names for the JSON values
    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    // RecipeFinder fills it in when ranking the meals as it's not in the JSON
    private int matchCount;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void addMatch() {
        matchCount++;
    }
}
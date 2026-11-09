import com.google.gson.annotations.SerializedName;

public class Meal {
    // Improving readability by using better names for the JSON values
    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
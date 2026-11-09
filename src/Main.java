import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ApiClient api = new ApiClient();
        List<Meal> meals = api.searchIngredient("chicken_breast");
        for (int i = 0; i < meals.size(); i++) {
            System.out.println(meals.get(i).getName());
        }
    }
}
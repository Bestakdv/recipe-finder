import java.util.List;

public class Recipe {
    private final String name;
    // Keeping measures separate from ingredients to make sure the ingredient names can be compared on their own
    private final List<String> ingredients;
    private final List<String> measures;
    private final String instructions;

    public Recipe(String name, List<String> ingredients, List<String> measures, String instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.measures = measures;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getMeasures() {
        return measures;
    }

    public String getInstructions() {
        return instructions;
    }
}
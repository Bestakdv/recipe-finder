public class Main {
    public static void main(String[] args) throws Exception {
        ApiClient api = new ApiClient();
        String json = api.searchIngredient("chicken_breast");
        System.out.println(json);
    }
}
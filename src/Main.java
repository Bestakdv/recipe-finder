import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        // Using Gson to see that the jar file is in classpath and works correctly
        Gson gson = new Gson();
        String json = gson.toJson(new String[] {"chicken", "rice"});
        System.out.println("Gson works: " + json);
    }
}
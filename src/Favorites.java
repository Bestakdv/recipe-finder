import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Favorites {
    private final File file;

    public Favorites(String fileName) {
        file = new File(fileName);
    }

    public List<String> load() throws IOException {
        List<String> names = new ArrayList<>();

        // The file doesn't exist until the first favorite is saved and a missing file means there are no favorites yet
        if (!file.exists()) {
            return names;
        }

        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            String line = reader.nextLine().trim();
            if (!line.isEmpty()) {
                names.add(line);
            }
        }
        reader.close();
        return names;
    }

    // Returns false when the recipe is already saved so the same favorite isn't added twice
    public boolean save(String mealName) throws IOException {
        if (load().contains(mealName)) {
            return false;
        }

        // Opening in append to make saving a new favorite not erase the old ones
        FileWriter writer = new FileWriter(file, true);
        writer.write(mealName + System.lineSeparator());
        writer.close();
        return true;
    }
}
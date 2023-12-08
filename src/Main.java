import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static final String SAVE_PATH = "Games/savegames/";

    public static void main(String[] args) {

        var gamedata = List.of(
                new GameProgress(10, 5, 1, 2.53),
                new GameProgress(50, 30, 6, 28.98),
                new GameProgress(100, 45, 9, 63.24)

        );


        List<String> filenames = new ArrayList<>();
        for (int i = 0; i < gamedata.size(); i++) {
            String name = SAVE_PATH + "game" + (i + 1) + ".dat";
            saveGame(name, gamedata.get(i));
            filenames.add(name);
        }

        zipFiles(SAVE_PATH + "save.zip", filenames);

               deleteSavedGames(filenames);
    }
    private static void deleteSavedGames(List<String> filenames) {
        for (int i = 0; i < filenames.size(); i++) {
            String name = filenames.get(i);
            File file = new File(name);
            if (file.delete()) {
                System.out.println("Файл удален");
            }
        }
    }
    public static void saveGame(String path, GameProgress game) {
        try (FileOutputStream baos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(game);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void zipFiles(String path, List<String> filenames) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (int i = 0; i < filenames.size(); i++) {
                String name = filenames.get(i);
                addFileToZipStream(zout, name);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private static void addFileToZipStream(
            ZipOutputStream zipOutputStream, String path
    ) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            File file = new File(path);
            ZipEntry entry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(entry);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            zipOutputStream.write(buffer);
            zipOutputStream.closeEntry();
        }
    }
}
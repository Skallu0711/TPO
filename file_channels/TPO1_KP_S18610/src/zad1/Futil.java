package zad1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Futil {

    public static void processDir(String inputDirectoryStr, String outputFileStr) {

        Path outputFilePath = Paths.get(outputFileStr);
        Path startingDirectory = Paths.get(inputDirectoryStr);

        try {
            OwnFileVisitor fileVisitor = new OwnFileVisitor(outputFilePath);
            Files.walkFileTree(startingDirectory, fileVisitor);

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}

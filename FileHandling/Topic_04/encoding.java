package FileHandling.Topic_04;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class encoding {
    public static void main(String[] args) throws IOException {
        // StandardCharsets gives us compile-time safe constants - always use these:

        // StandardCharsets.UTF_8  <- Use this by default
        // StandardCharsets.US_ASCII  <- 7-bit ASCII only
        // StandardCharsets.ISO_8859_1  <- Latin-1, Western European

        Path path = Path.of("testfile.txt");
        // Reading a file saved with Windows Latin-1 encoding:
        String content = Files.readString(path, StandardCharsets.ISO_8859_1);
        System.out.println(content);

        // Writing with explicit UTF-8:
        Files.writeString(path, "Hello, 你好, こんにちは! नमस्ते", StandardCharsets.UTF_8, StandardOpenOption.APPEND);

        // What happens if you use the WRONG encoding?
        // File saved as UTF-8, but you read as Latin-1:
        // "你" (3 UTF-8 bytes: E4 BD A0) -> read as 3 separate Latin-1 chars -> "ä½ " (garbled!)
    }
}

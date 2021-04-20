package zad1;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class OwnFileVisitor extends SimpleFileVisitor <Path> {

    private FileChannel outputFileChannel;
    private ByteBuffer commonBuffer;

    public Charset inCharset = Charset.forName("Cp1250");
    public Charset outCharset = Charset.forName("UTF-8");

    public OwnFileVisitor(Path outputFilePath) throws IOException {

        this.outputFileChannel = FileChannel.open(outputFilePath, EnumSet.of(CREATE, APPEND));
    }

    private void recodeForUtf(FileChannel inputFileChannel, long bufferSize){

        commonBuffer = ByteBuffer.allocate((int)bufferSize +1);
        commonBuffer.clear();

        try {

            inputFileChannel.read(commonBuffer);
            commonBuffer.flip();
            CharBuffer cbuf = inCharset.decode(commonBuffer);
            ByteBuffer buf = outCharset.encode(cbuf);

            while( buf.hasRemaining() )
                this.outputFileChannel.write(buf);

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    @Override
    public FileVisitResult visitFile(Path filePath, BasicFileAttributes attributes) {

        System.out.format("File: %s ", filePath);
        System.out.println("( " + attributes.size() + "bytes )");

        try {
            this.recodeForUtf(FileChannel.open(filePath), attributes.size());

        } catch(IOException ex){
            System.out.format("Failed to open file: %s ", filePath);
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path filePath, IOException e) {

        System.err.println("OwnFileVisitor.visitFileFailed: " + e);

        return CONTINUE;
    }

}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @create 2022-09-29 20:35
 */
public class test {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("model/file.txt");
        byte[] bytes = new byte[4];
        int readCount =0;
        while ((readCount=fis.read(bytes)) != -1) {
            System.out.print(new String(bytes,0,readCount));
        }
    }
}

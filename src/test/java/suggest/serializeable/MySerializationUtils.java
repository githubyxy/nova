package suggest.serializeable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * ���л�����
 */
public class MySerializationUtils {
    private static String FILE_NAME = "obj.bin";

    // ���л�
    public static void writeObject(Serializable s) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            oos.writeObject(s);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object readObject() {
        Object obj = null;
        // �����л���
        try {
            ObjectInput input = new ObjectInputStream(new FileInputStream(FILE_NAME));
            obj = input.readObject();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}

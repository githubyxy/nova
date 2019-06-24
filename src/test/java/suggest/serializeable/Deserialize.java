package suggest.serializeable;

/**
 * ����Ϊfinal�������Ӹ�ֵ
 */

public class Deserialize {
    public static void main(String[] args) {
        //�����л�
        Student p = (Student) MySerializationUtils.readObject();

        System.out.println(p.name);
        System.out.println(p.getName2());
        System.out.println(p.address.address);
    }
}

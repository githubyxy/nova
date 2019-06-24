package suggest.serializeable;

public class Serialize {
    public static void main(String[] args) {
        Student student = new Student();
        student.setAddress(new Address());
        MySerializationUtils.writeObject(student);
    }
}

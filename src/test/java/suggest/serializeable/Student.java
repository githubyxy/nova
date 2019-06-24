package suggest.serializeable;

import java.io.Serializable;

public class Student extends Person {
    /**
     *
     */
    private String name2 = "问问";

    public Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
}

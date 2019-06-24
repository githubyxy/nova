package designpattern.prototype;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {

        Resume a = new Resume("小宇");
        a.setPersonalInfo("男", "25");
        a.setWorkExperience("2017", "cana");
        a.disaply();

        Resume b = (Resume) a.clone();
        b.setPersonalInfo("女", "24");
        b.setWorkExperience("2017111", "cana111");
        b.disaply();
    }
}

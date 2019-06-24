package designpattern.prototype;

public class Resume implements Cloneable {

    private String name;
    private String sex;
    private String age;
    //	private String timeArea;
//	private String company;
    private WorkExperience workExperience;

    public Resume(String name) {
        this.name = name;
    }

    private Resume(WorkExperience workExperience) throws CloneNotSupportedException {
        this.workExperience = (WorkExperience) workExperience.clone();
    }

    public void setPersonalInfo(String sex, String age) {
        this.sex = sex;
        this.age = age;
    }

    public void setWorkExperience(String timeArea, String company) {
        workExperience.setTimeArea(timeArea);
        workExperience.setCompany(company);
    }

    public Object clone() throws CloneNotSupportedException {
        Resume resume = new Resume(this.workExperience);
        resume.name = this.name;
        resume.sex = this.sex;
        resume.age = this.age;

        return resume;
    }

    public void disaply() {
        System.out.println(String.format("姓名=%s，性别=%s，年纪=%s", name, sex, age));
        System.out.println(String.format("工作经历：%s %s", workExperience.getTimeArea(), workExperience.getCompany()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


}

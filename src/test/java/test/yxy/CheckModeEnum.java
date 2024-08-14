package test.yxy;

/***
 *  上传方式
 * @author yxy
 * @date 20180810
 */
public enum CheckModeEnum {

    PARTIAL_SUCCESS("部分上传成功"),
    ALL_SUCCESS("全部上传成功");

    private String desc;

    CheckModeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

package test;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyTest extends Thread {


        boolean flag = false;
        int i = 0;



        @Override
        public void run() {
            while (!flag) {
                i++;
                System.out.println("This is running..." + flag);
            }
        }

        public static void main(String[] args) throws Exception {
//            MyTest vt = new MyTest();
//            vt.start();
//            Thread.sleep(2000);
//            vt.flag = true;
            //System.out.println("stope" + vt.i);
            BlacklistUploadParamDTO dto = new BlacklistUploadParamDTO();

            List<BlacklistUploadParamDTO.BlacklistInfo> blacklistInfo = new ArrayList<>();
            BlacklistUploadParamDTO.BlacklistInfo info = new BlacklistUploadParamDTO.BlacklistInfo();
            info.setMobile("1");
            info.setExpireDate("2024-02-12");
            blacklistInfo.add(info);

            BlacklistUploadParamDTO.BlacklistInfo info2 = new BlacklistUploadParamDTO.BlacklistInfo();
            info2.setMobile("2");
            info2.setExpireDate("2024-02-13");
            blacklistInfo.add(info2);
            BlacklistUploadParamDTO.BlacklistInfo info3 = new BlacklistUploadParamDTO.BlacklistInfo();
            info3.setMobile("3");
            info3.setExpireDate("");
            blacklistInfo.add(info3);
            BlacklistUploadParamDTO.BlacklistInfo info4 = new BlacklistUploadParamDTO.BlacklistInfo();
            info4.setMobile("4");
            blacklistInfo.add(info4);

            dto.setBlacklistInfo(blacklistInfo);

            List<BlacklistUploadParamDTO.BlacklistInfo> blankExpireDateList = dto.getBlacklistInfo().stream().filter(item -> StringUtils.isBlank(item.getExpireDate())).collect(Collectors.toList());
            System.out.println(JSONObject.toJSONString(blankExpireDateList));


            Map<String, List<BlacklistUploadParamDTO.BlacklistInfo>> expireDateMap = dto.getBlacklistInfo().stream().filter(item -> StringUtils.isNotBlank(item.getExpireDate())).collect(Collectors.groupingBy(BlacklistUploadParamDTO.BlacklistInfo::getExpireDate));

            System.out.println(JSONObject.toJSONString(expireDateMap));

        }

}

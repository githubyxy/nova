package test.util;

import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.web.util.XmlToJsonUtil;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.IOException;

/**
 * @author yuxiaoyu
 * @date 2020/11/28 上午11:54
 * @Description
 */
public class XmlUtilTest {

    @Test
    public void test() {
        ////        String s= "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><statusbox><mobile>13585934620</mobile><taskid>5567768853136226355</taskid><linkid></linkid><status>20</status><errorcode>GB:0002</errorcode><receivetime>2020-11-26 08:54:45</receivetime><submittime>2020-11-26 08:54:45</submittime><ac>106901910178</ac></statusbox><statusbox><mobile>15858211094</mobile><taskid>4911103166084128890</taskid><linkid></linkid><status>20</status><errorcode>GB:0002</errorcode><receivetime>2020-11-26 08:54:45</receivetime><submittime>2020-11-26 08:54:45</submittime><ac>106901910178</ac></statusbox><statusbox><mobile>15024477177</mobile><taskid>5480885430223183034</taskid><linkid></linkid><status>20</status><errorcode>GB:0002</errorcode><receivetime>2020-11-26 08:54:45</receivetime><submittime>2020-11-26 08:54:45</submittime><ac>106901910178</ac></statusbox></returnsms>";
        String s = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><returnstatus>Success</returnstatus><message>OK</message><remainpoint>13</remainpoint><resplist><resp>5066237338107095083#@#15858211094#@#0#@#</resp><resp>4937307768906746967#@#13585934620#@#0#@#</resp></resplist><successCounts>2</successCounts></returnsms>";
//        String s = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><returnstatus>Success</returnstatus><message>OK</message><remainpoint>13</remainpoint><resplist><resp>4937307768906746967#@#13585934620#@#0#@#</resp></resplist><successCounts>2</successCounts></returnsms>";
//        String s = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
//                "<returnsms>\n" +
//                "\t<returnstatus>status</returnstatus>\n" +
//                "\t<message>message</message>\n" +
//                "\t<remainpoint> remainpoint</remainpoint>\n" +
//                "\t<taskID>taskID</taskID>\n" +
//                "\t<successCounts>successCounts</successCounts>\n" +
//                "</returnsms>";
//        String s = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
//                "<returnsms>\n" +
//                "\t<statusbox>\n" +
//                "\t\t<mobile>15023239810</mobile>\n" +
//                "\t\t<taskid>1212</taskid>\n" +
//                "\t\t<status>10</status>\n" +
//                "\t\t<receivetime>2011-12-02 22:12:11</receivetime>\n" +
//                "\t\t<errorcode>DELIVRD</errorcode>\n" +
//                "\t\t<extno>01</extno>\n" +
//                "\t</statusbox>\n" +
//                "\t<statusbox>\n" +
//                "\t\t<mobile>15023239811</mobile>\n" +
//                "\t\t<taskid>1212</taskid>\n" +
//                "\t\t<status>20</status>\n" +
//                "\t\t<receivetime>2011-12-02 22:12:11</receivetime>\n" +
//                "\t\t<errorcode>2</errorcode>\n" +
//                "\t\t<extno></extno>\n" +
//                "\t</statusbox>\n" +
//                "</returnsms>";
        JSONObject jsonObject = null;
        try {
            jsonObject = XmlToJsonUtil.xmlToJSON(s.getBytes());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        JSONObject returnsms = jsonObject.getJSONObject("returnsms");
////        System.out.println(returnsms.toJSONString());
////
//        JSONObject resplistObject = returnsms.getJSONObject("resplist");
//        System.out.println(resplistObject.toJSONString());
//        System.out.println("b:" + JSONArray.isValidArray(resplistObject.getString("resp")));
////        returnsms.getJSONArray()
//
////        String resp = resplistObject.getString("resp");
////        JSONArray resplist = JSONObject.parseArray(str);
////        JSONArray resplist = resplistObject.getJSONArray("resp");
        System.out.println(jsonObject.toJSONString());
////        System.out.println(resp);
////
////        if (resp.startsWith("[")) {
////            JSONArray resplist = JSONObject.parseArray(resp);
////            for (int i = 0; i < resplist.size(); i++) {
////                String jsonObject1 = resplist.getString(i);
////                List<String> splits = TextUtil.split(jsonObject1, "#@#");
////                System.out.println(splits.get(1) + ":"+ splits.get(0));
////            }
////        }
    }


}

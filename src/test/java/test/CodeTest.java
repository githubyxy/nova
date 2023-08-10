package test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.tools.javac.util.Convert;
import com.yxy.nova.util.DateTimeUtils;
import com.yxy.nova.util.QRCode;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * @author yuxiaoyu
 * @date 2020/1/15 上午10:25
 * @Description
 */

public class CodeTest {

    public int search(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }

        int l = 0;
        int r = nums.length - 1;
        int mid = r / 2;
        return Math.max(bs(nums, l, mid, target), bs(nums, mid + 1, r, target));

    }

    private int bs(int[] nums, int l, int r, int target) {
        if (nums[l] <= nums[r]) {
            // 连续的区间
            return ts(nums, l, r, target);
        } else {
            int mid = (l + r) >>> 2;
            return Math.max(bs(nums, l, mid, target), bs(nums, mid + 1, r, target));
        }
    }

    // 升序的 二分查找
    private int ts(int[] nums, int l, int r, int target) {
        if (target < nums[l] || target > nums[r]) {
            return -1;
        }

        while (l <= r) {
            int mid = (r + l) >>> 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return -1;
    }

    @Test
    public void test2() {
        QRCode handler = new QRCode();
        String text = "肯德基疯狂星期四，V我50";
//        handler.encoderQRCode(text, "/Users/yuxiaoyu/Downloads/a.png", "png");
        handler.encoderQRCode(text, "/Users/yuxiaoyu/Downloads/b.png", "png", 5);


    }
    @Test
    public void test3() {
        String s = "{\"type\":\"change\",\"sn\":\"TN001\",\"time\":\"2023-07-21 10:01:40\",\"data\":{\"C1.D1\":[{\"id\":\"nxseq3731\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"-0.255\"},{\"id\":\"nxseq3732\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"-0.215\"},{\"id\":\"nxseq3738\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"-0.130\"},{\"id\":\"nxseq3747\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"3.140\"},{\"id\":\"nxseq3748\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"3.096\"},{\"id\":\"nxseq3749\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.184\"},{\"id\":\"nxseq3752\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"3.053\"},{\"id\":\"nxseq3753\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"3.006\"},{\"id\":\"nxseq3754\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.190\"},{\"id\":\"nxseq3755\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.190\"},{\"id\":\"nxseq3756\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.189\"},{\"id\":\"nxseq3760\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"63.030\"},{\"id\":\"nxseq3761\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"66.565\"},{\"id\":\"nxseq3762\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"66.355\"},{\"id\":\"nxseq3763\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"63.015\"},{\"id\":\"nxseq3764\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"54.650\"},{\"id\":\"nxseq3765\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"99.985\"},{\"id\":\"nxseq3766\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"63.765\"},{\"id\":\"nxseq3771\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"75.740\"},{\"id\":\"nxseq3774\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"100.070\"},{\"id\":\"nxseq3775\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"100.450\"},{\"id\":\"nxseq3776\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"100.370\"},{\"id\":\"nxseq3777\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.260\"},{\"id\":\"nxseq3779\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"71.676\"},{\"id\":\"nxseq3780\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"71.649\"},{\"id\":\"nxseq3781\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"71.962\"},{\"id\":\"nxseq3782\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.282\"},{\"id\":\"nxseq3784\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"74.256\"},{\"id\":\"nxseq3785\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"74.146\"},{\"id\":\"nxseq3788\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.584\"},{\"id\":\"nxseq3789\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"100.537\"},{\"id\":\"nxseq3790\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"100.401\"},{\"id\":\"nxseq3791\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"101.325\"},{\"id\":\"nxseq3792\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.486\"},{\"id\":\"nxseq3793\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.466\"},{\"id\":\"nxseq3794\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"92.230\"},{\"id\":\"nxseq3796\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"95.290\"},{\"id\":\"nxseq3797\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.561\"},{\"id\":\"nxseq3798\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.550\"},{\"id\":\"nxseq3799\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"69.852\"},{\"id\":\"nxseq3800\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"91.956\"},{\"id\":\"nxseq3801\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"96.832\"},{\"id\":\"nxseq3802\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.528\"},{\"id\":\"nxseq3804\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"77.610\"},{\"id\":\"nxseq3805\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"93.960\"},{\"id\":\"nxseq3806\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"94.690\"},{\"id\":\"nxseq3808\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.101\"},{\"id\":\"nxseq3809\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.229\"},{\"id\":\"nxseq3810\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.332\"},{\"id\":\"nxseq3811\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.151\"},{\"id\":\"nxseq3812\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"0.240\"},{\"id\":\"nxseq3814\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"10737.037\"},{\"id\":\"nxseq3815\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"4339.961\"},{\"id\":\"nxseq3816\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"5636.509\"},{\"id\":\"nxseq3817\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"5343.916\"},{\"id\":\"nxseq3818\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"5257.671\"},{\"id\":\"nxseq3822\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"41.450\"},{\"id\":\"nxseq3823\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"57.395\"},{\"id\":\"nxseq3827\",\"desc\":\"\",\"quality\":\"0\",\"value\":\"53.205\"}]}}";
        JSONObject ctx = JSONObject.parseObject(s);
        JSONArray data = ctx.getJSONObject("data").getJSONArray("C1.D1");
        String time = ctx.getString("time");

        System.out.println(time);
        System.out.println(data.get(0));

    }
    @Test
    public void test4() {
        long timeDiffSecond = DateTimeUtils.diffInSecond(DateTimeUtils.parseDatetime18("2023-07-26 09:42:00"), DateTimeUtils.parseDatetime18("2023-07-26 08:32:00"));
        double v = timeDiffSecond / 3600D;
        System.out.println(new BigDecimal(v).setScale(2, BigDecimal.ROUND_UP).doubleValue());

    }

    @Test
    public void test5() {
        String phoneNumber = "+8613585934620";
        String cleanedPhoneNumber = phoneNumber.replaceFirst("^(\\+?86)?", "");

        System.out.println(cleanedPhoneNumber);
    }

}

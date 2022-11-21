package test;

import com.sun.tools.javac.util.Convert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author yuxiaoyu
 * @date 2020/1/15 上午10:25
 * @Description
 */

class CodeTest {

    public int search(int[] nums, int target) {
        if(nums.length==0){
            return -1;
        }

        int l=0;
        int r=nums.length-1;
        int mid=r/2;
        return Math.max(bs(nums,l,mid,target),bs(nums,mid+1,r,target));

    }

    private int bs(int[] nums, int l,int r,int target){
        if(nums[l]<=nums[r]){
            // 连续的区间
            return ts(nums,l,r,target);
        } else{
            int mid=(l+r)>>>2;
            return Math.max(bs(nums,l,mid,target), bs(nums,mid+1,r,target));
        }
    }

    // 升序的 二分查找
    private int ts(int[] nums, int l,int r,int target){
        if(target<nums[l] || target>nums[r]){
            return -1;
        }

        while(l<=r){
            int mid=(r+l)>>>2;
            if(nums[mid]==target){
                return mid;
            }else if(nums[mid]>target){
                r=mid-1;
            }else{
                l=mid+1;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
//        CodeTest codeTest = new CodeTest();
//        int [] nums = new int[]{4,5,6,7,0,1,2};
//        System.out.println(codeTest.search(nums,0));
        Long a = new Long(2147483648L);
        int b =2147483647;
        int i = new BigDecimal(a).compareTo(new BigDecimal(b));
        System.out.println(i);

    }



}

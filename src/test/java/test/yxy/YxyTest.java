package test.yxy;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Charsets;
import com.yxy.nova.mwh.utils.concurrent.BoundedExecutor;
import com.yxy.nova.mwh.utils.concurrent.InJvmLockUtil;
import com.yxy.nova.mwh.utils.constant.ISPEnum;
import com.yxy.nova.mwh.utils.serialization.SerializerUtil;
import com.yxy.nova.mwh.utils.text.TextUtil;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class YxyTest {

    private Person person;

    @Test
    public void test() throws Exception {
        BoundedExecutor boundedExecutor = new BoundedExecutor(2);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
//            if (i < 50) {
//            list.add("1");
//            } else {
//            list.add("2");
//            }

            if (i % 2 == 0) {
                list.add("1");
            } else {
                list.add("2");
            }
        }

        for (int i = 0; i < list.size(); i++) {
            int finalI = i;
            boundedExecutor.submitButBlockIfFull(() -> {
                InJvmLockUtil.runInLock(list.get(finalI), () -> {
                    System.out.println("处理 :" + list.get(finalI) + ":" + DateTimeUtil.datetime18());
                    try {
                        Thread.sleep(("1".equals(list.get(finalI)) ? 10 : 1) * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                return null;
            });

        }
        Thread.sleep(100 * 1000);

    }

    @Test
    public void test1() throws Exception {
        for (int i = 2; i > 0; i--) {

            System.out.println(i);
        }
    }

    @Test
    public void test11() {
        String[][] headers = new String[1][45];
        headers[0][14] = "数据信息";
        headers[0][34] = "AI数据";
        headers[0][44] = "人工数据";
        for (int i = 0; i < headers.length; i++) {
            // 创建表头
            for (int j = 0; j < headers[i].length; j++) {
                System.out.println(headers[i][j]);
            }
        }
    }


    @Test
    public void test2() throws Exception {
//        List<String> s = Arrays.asList("01", "03", "00", "10", "00", "01", "85", "CF");

        byte[] bytes = Hex.decodeHex("01030010000185CF");

        System.out.println(Hex.encodeHexString(bytes));
        for (int i = 0; i < bytes.length; i++) {
            System.out.println(bytes[i]);
        }
    }

    @Test
    public void optest() {
        Optional<Object> empty = Optional.empty();
//        Optional<String> s = Optional.of("123");
        Optional<Object> o = Optional.ofNullable(null);

//        System.out.println(s.get());
        System.out.println(o.isPresent());

        o.ifPresent(s1 -> {
            System.out.println(s1);
        });
    }

    @Test
    public void test3() {
        String c = String.format("sum(ceil(%stalkDurationSeconds / %d)) AS sumTalkDurationSeconds_%d", "", 6, 6);

        System.out.println(c);
    }

    @Test
    public void OptionalExample() {
        // 创建一个包含 Person 对象的 Optional
        Optional<Person> optionalPerson = Optional.ofNullable(new Person("Alice", 30));

        // 使用 ifPresent() 打印 Person 详细信息
        optionalPerson.ifPresent(person -> System.out.println("Name: " + person.getName() + ", Age: " + person.getAge()));

        optionalPerson.ifPresent(person -> {
            person.getAge();
        });


        // 使用 ifPresent() 调用 Person 对象的方法
        optionalPerson.ifPresent(Person::printPerson);

        // 创建一个空的 Optional
        Optional<Person> emptyOptional = Optional.ofNullable(null);

        // 使用 ifPresent() 处理空 Optional，不会执行任何操作
        emptyOptional.ifPresent(person -> System.out.println("This will not be printed"));


        String s = emptyOptional.map(Person::getName).orElse("null");

        Optional<String> emptyOptionalStr = Optional.of("123");

        System.out.println("s1=" + emptyOptionalStr.orElse(""));
        System.out.println("s2=" + emptyOptionalStr.orElseGet(() -> ""));

    }

    @Test
    public void test4() {
        String s = "测试短信${customerName}, ${customer}";

        String targetVar = "customer";
        String newVar = "antName";
        String replace = s.replaceAll("\\$\\{" + targetVar + "}", "\\${" + newVar + "}");

        System.out.println(replace);
    }

    @Test
    public void test5() {

        List<String> list = Arrays.asList("1", "2", "2", "5", "5", "5", "7", "8", "9");

        Map<String, Integer> map = new HashMap<>();

        list.forEach(id -> {
            Integer count = map.getOrDefault(id, 0);
            count = count + 1;
            map.put(id, count);
        });
        System.out.println(JSONObject.toJSONString(map));

        Map<String, Integer> map2 = new HashMap<>();
        list.forEach(id -> {
            map2.compute(id, (k, v) -> v == null ? 1 : v + 1);
        });
        System.out.println(JSONObject.toJSONString(map2));

    }

    @Test
    public void test6() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT mobileNo FROM task_item WHERE ds = '2025-06-13' AND ");

        List<String> mobiles = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String mobile = String.format("135%08d", i);
            mobiles.add(mobile);
        }
        String mobileNoSql = in("mobileNo", mobiles);
        sb.append(mobileNoSql);

        // 将 SQL 写入文件
        try (FileWriter writer = new FileWriter("demo_mobile2.sql")) {
            writer.write(sb.toString());
            System.out.println("SQL 文件已生成：demo_mobile.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String in(String columnName, Iterable<String> values) {
        if (values == null || !values.iterator().hasNext()) {
            throw new IllegalArgumentException("Values for IN statement cannot be null or empty");
        }
        long valueSize = 0L;
        StringJoiner joiner = new StringJoiner("', '", "'", "'");
        for (String value : values) {
            joiner.add(value);
            valueSize++;
        }
        if (valueSize > 5000) {
            StringBuilder inSql = new StringBuilder("(");
            List<List<String>> splitList = splitIterable(values, 5000);
            for (int i = 0; i < splitList.size(); i++) {
                List<String> splits = splitList.get(i);
                StringJoiner splitJoiner = new StringJoiner("', '", "'", "'");
                for (String value : splits) {
                    splitJoiner.add(value);
                }
                inSql.append(columnName).append(" IN (").append(splitJoiner).append(")");
                if (i != splitList.size() - 1) {
                    inSql.append(" OR ");
                }
            }
            return inSql.append(")").toString();
        } else {
            return columnName + " IN (" + joiner + ")";
        }
    }

    public static <T> List<List<T>> splitIterable(Iterable<T> iterable, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        Iterator<T> iterator = iterable.iterator();

        List<T> currentBatch = new ArrayList<>(batchSize);

        while (iterator.hasNext()) {
            currentBatch.add(iterator.next());

            if (currentBatch.size() == batchSize) {
                batches.add(new ArrayList<>(currentBatch));
                currentBatch.clear();
            }
        }

        // 加入最后一批（可能小于 batchSize）
        if (!currentBatch.isEmpty()) {
            batches.add(new ArrayList<>(currentBatch));
        }

        return batches;
    }


    @Test
    public void test7() {
        StatDimensionEnum day = StatDimensionEnum.DAY;
        System.out.println(day.convertDate("2024-06-01"));
        System.out.println(day.getMinDate10("2024-06-01"));
        System.out.println(day.getMaxDate10("2024-06-01"));

        StatDimensionEnum month = StatDimensionEnum.MONTH;
        System.out.println(month.convertDate("2024-06-01"));
        System.out.println(month.getMinDate10("2024-06"));
        System.out.println(month.getMaxDate10("2024-06"));
    }

    @Test
    public void test8() {
        SerializerFeature[] openApiFeatures = new SerializerFeature[]{
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteEnumUsingToString,
                // 输出为null的字段 为空串
                // 循环引用
        };
        Person p = new Person("1", 1);
        p.setCheckModeEnum(null);
        String jsonString = JSONObject.toJSONString(p, openApiFeatures);
        System.out.println(jsonString);

    }

    @Test
    public void test9() {
        int N = 0, t = 20, b = 10, n = 381, T = 100;
        int s = (int) Math.ceil(new BigDecimal(N).multiply(new BigDecimal(20)).multiply(new BigDecimal(10))
                .divide(new BigDecimal(n).multiply(new BigDecimal(T)), 4, BigDecimal.ROUND_HALF_UP).doubleValue());

        System.out.println(s);

        System.out.println((int) Math.ceil(new BigDecimal("0.00001").doubleValue()));
    }

    @Test
    public void test10() {
        int i = 0;
        while (true) {
            i++;
            if (i == 5) {
                break;
            } else {
                System.out.println(i);
            }
        }
    }

    @Test
    public void test12() {
        long i = 6000000L;
        long l = i / 29;

        System.out.println(l); //193548
        System.out.println(new BigDecimal(i).divide(new BigDecimal(29), 0, BigDecimal.ROUND_HALF_UP).longValue()); //193548

        System.out.println(Integer.valueOf("09") - 1);
    }

    @Test
    public void test13() {
        String s = "00.76";
        double price = Double.parseDouble(s);
        if (price < 0) {
            System.out.println(false);
        }

        // 将字符串表示的元转换为 BigDecimal
        BigDecimal yuanValue = new BigDecimal(s);

        // 定义 1 元等于 1000 厘的常量
        BigDecimal conversionFactor = new BigDecimal("1000");

        // 将元转换为厘
        BigDecimal liValue = yuanValue.multiply(conversionFactor);

        // 将结果转换为 long 类型并返回
        System.out.println(liValue.longValueExact());
    }

    @Test
    public void test14() {
        Optional<String> s = Optional.ofNullable(null);
        System.out.println(s.orElseGet(() -> "12"));
        System.out.println(s.orElse("111"));
    }

    @Test
    public void test15() {
        String s = "{\"CMCC\":[{\"startTime\":\"2024-07-30\",\"price\":\"0.76\"}],\"CUCC\":[{\"startTime\":\"2024-07-30\",\"price\":\"0.76\"}]}";
        Map<ISPEnum, List<PricePeriod>> collect = SerializerUtil.jsonMapDeserializeNullAsEmptyMap(s, ISPEnum.class, List.class)
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> SerializerUtil.jsonListDeserializeNullAsEmptyList(JSONObject.toJSONString(entry.getValue()), PricePeriod.class)
                ));
        System.out.println(JSONObject.toJSONString(collect));
        List<String> collect1 = collect.entrySet().stream().filter(ispEnumListEntry -> CollectionUtils.isNotEmpty(ispEnumListEntry.getValue())).map(ispEnumListEntry -> ispEnumListEntry.getKey().name()).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(collect1));
        List<String> list1 = new ArrayList<>(Arrays.asList("CMCC"));
        boolean b = collect1.containsAll(list1);
        System.out.println(b);

    }

    @SneakyThrows
    @Test
    public void test16() {
        String VAR_PREFIX = "${";
        String VAR_SUFFIX = "}";
        String content = "您好，${1a }，这里是测试短信模板02。  ";

        String var = content.substring(content.indexOf(VAR_PREFIX) + VAR_PREFIX.length(), content.indexOf(VAR_SUFFIX));

        System.out.println(var.length() == StringUtils.trimToEmpty(var).length());

    }

    @Test
    public void test17() {
        List<String> oldPartnerCodeList = new ArrayList<>();
        oldPartnerCodeList.add("yxy");
        List<String> newPartnerCodeList = new ArrayList<>();
        newPartnerCodeList.add("yxy");
        newPartnerCodeList.add("sjtong");

        oldPartnerCodeList.forEach(oldPartnerCode -> {
            if (org.apache.commons.collections.CollectionUtils.isEmpty(newPartnerCodeList) || !newPartnerCodeList.contains(oldPartnerCode)) {
                System.out.println(oldPartnerCode);
            }
        });
    }

    @Test
    public void test18() {
        BigDecimal bigDecimal = new BigDecimal("0.100");
        System.out.println(bigDecimal.stripTrailingZeros().toPlainString());
        System.out.println(bigDecimal.stripTrailingZeros().toString());

    }

}

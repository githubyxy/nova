package test.yxy;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Charsets;
import com.yxy.nova.mwh.utils.concurrent.BoundedExecutor;
import com.yxy.nova.mwh.utils.concurrent.CustomPrefixThreadFactory;
import com.yxy.nova.mwh.utils.concurrent.InJvmLockUtil;
import com.yxy.nova.mwh.utils.constant.ISPEnum;
import com.yxy.nova.mwh.utils.serialization.SerializerUtil;
import com.yxy.nova.mwh.utils.text.TextUtil;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import com.yxy.nova.util.CustomRejectedPolicy;
import com.yxy.nova.util.MoneyUtil;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class YxyTest {

    private Person person;

    private static final Pattern P = Pattern.compile("(\\d+)(天内免息|天周转金)");

    @Test
    public void test() throws Exception {

        String s = "48001:48001,55961,55981,54151,54171,54181,54191,54161,54201,54211,54221,53561,53581,53621,53631,51011,51021,51301,51011,51021,50941,50951,47991,47981,48011,48021&47911:47911,58141,58171,57691,57671,58151,58191,57801,57781,56541,56551,57051,57061,56501,56511,57041,57271,57281,57291,57301,56521,56531,57311,57321,57331,57341,56561,56571,57071,57351,57361,57371,57381,57441,57541,57551,57561,57571,57581,57591,57451,57601,57611,57621,57631,57641,57651,57461,57661,57681,57701,57911,57921,57941,57961,47981,47041,47971,47981&48121:48121,56851,56861,56871,56981,56991,57001,57081,57091,57101,57121,57131,56301,56261,56341,56351,56361,56121,56161,56001,56011,56021,56031,54391,54421,54431,54441,54411,54541,54551,54561,54501,54571,54581,54591,54521,54601,54611,54621,53401,53441,53451,53461,52751,52761,52771,52781,52601,52681,52611,52691,52011,52081,52091,52101,51371,51391,51401,51411,51291,51001,50991,50811,50821,50831,50881,50891,50901,50911,50681,50691,48691,48731,48741,48751,48761,48771,42511,41471,41481,45081,42091,42591,45111,41471,41481,45081,42091,42591,45111,42571,42601,42701,45021,45031,45041,45051,45061,45071,42521,42581,42591,42611,42711,45091,45101,45121,45131&48131:48131,47771,48451,48461,48471,45751,45761,45771,45801,45811,45821,45751,45761,45771,45801,45811,45821,45831,45841,45851,45861,45871,45881,45891,45901,45911,45921,45931,45941,46001,46011,46021,46501,46511,46521,46531,46541,46551,46561,46571,46581,46591,46601,46611,46621,46631,46641,46651,46661,46671,46681,46691,46701&48141:48141,52891,53021,53031,53041,47741,48271,48281,48291,45741,45791,45951,45961,45971,45981,46421,46431,46441,46451,46461,46471,46481,46491&48681:48681,48661,48671&48421:48421,47761,48391,48401,48411&48361:48361,56741,60661,60671,60681,56731,59961,59971,59981,56721,60461,60471,60481,56711,60511,60521,60531,56701,59931,59941,59951,48491,60541,60551,60561,48481,60571,60581,60591,55991,59811,59821,59831,55971,59781,59791,59801,55591,60041,60051,60061,55051,60071,60081,60091,55031,60101,60111,60121,53151,60151,60161,60171,56631,59901,59911,59921,56621,59871,59881,59891,56611,60011,60021,60031,56291,59651,59661,59671,56151,59841,59851,59861,56691,59601,59611,59621,56681,59381,59391,59401,56671,59351,59361,59371,56661,59321,59331,59341,56651,59291,59301,59311,56641,59261,59271,59281,59141,59411,59421,59431,59151,59441,59451,59461,57141,57151,57161,57171,55511,55551,55561,55571,47751,48331,48341,48351&48261:48261,57951,57971,58181,58201,57761,57791,57811,57931,55721,55781,55791,55801,56791,56801,56811,,,,,,,,53571,53591,53601,53611,48721,50201,48431,48021&48241:48241,52971,53171,52961,53141,53391,53411,53421,53431,48501,49751,48381,48011&48221:48221,57751,55771,52921,52991,53001,52931,53051,53061,52941,53081,53091,56251,56311,56241,56281,56171,56181,56881,56131,56141,56091,56101,56111,55731,55751,55761,48441,47311,48211&51281:51281,51191,51251,51261,51271&51481:51481,55491,55521,55531,55541,55501,55581,55601,54401,54451,54461,54471,54511,54631,54641,54651,54531,54661,54671,54681,53011,53071,53101,53161,53181,53191,52621,52631,52641,52651,52661,52671,48301,48311,48321,48371,51801,51811,51821,51831,51841,51851,51861,51871,51881,51891&51491:51491,57431,57471,57481,57491,57501,57511,57521,56751,56761,56771,56781,56891,56901,56951,57011,57021,57031,56321,56331,56271,56371,56381,56391,56191,56201,55381,55391,55401,55411,55421,55431,55361,55371,54981,55001,55011,55021,54991,55041,52461,52571,52581,52591,52251,52261,52291,52301,52311,52321,51691,51701,51721,52021,52031,52041,52051,52061,52071,51971,52141,52151,52161,52111,52121,52131,51981,52171,52181,52191&52211:52211,51471,51461&56581:56581,57741,57711,52951,53111,53121,56591,56601,57191,57731,53131,55501,55581,55591,56911,56921,56961,56971&57391:57391,56931,56941,56911,56921,56961,56971&60731:55741,55811,55821,55831,56821,56831,56841";

        String r ="47981,60321,60331,60341,60601,60611,60621,48211,60361,60371,60381,60421,60431,60441,51021,60231,60241,60251,60631,60641,60651,52581,60281,60291,60301,60391,60401,60411";
        for (String r1 : TextUtil.split(r)) {
            s = s.replaceAll(r1 + ",", "");
            s = s.replaceAll(r1, "");
        }

        System.out.println(s);

        Map<String, String> taskUuidMap = new HashMap<>();
        TextUtil.split(s, "&")
                .forEach(taskUUidMapping -> {
                    String[] split = taskUUidMapping.split(":");
                    TextUtil.split(split[1]).forEach(oldTaskUuid -> {
                        taskUuidMap.put(oldTaskUuid, split[0]);
                    });
                });

        for (Map.Entry<String, String> entry : taskUuidMap.entrySet()) {
            if (StringUtils.isBlank(entry.getKey())) {
                System.out.println("空："+ entry.getKey());
            }
            if (StringUtils.isBlank(entry.getValue())) {
                System.out.println("空："+ entry.getValue());
            }

        }
    }

    public static String pickMax(List<String> coupons) {
        String best = null;
        int maxDays = -1;

        for (String c : coupons) {
            Matcher m = P.matcher(c);
            if (!m.find()) continue; // 不是“*天内免息 / *天周转金”，跳过
            int days = Integer.parseInt(m.group(1));
            if (days > maxDays) {
                maxDays = days;
                best = c;
            }
        }
        return best; // 没有符合的返回 null
    }

    private void geneBatchName(AtomicInteger suffix) {
        for (int i = 0; i < 10; i++) {
            System.out.println(suffix.getAndIncrement());
        }
    }

    @Test
    public void test1() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        int j = 49365;
        for (int i = 0; i < 10001; i++) {
            stringBuilder.append("'");
            stringBuilder.append(i+j);
            stringBuilder.append("'");
            stringBuilder.append(",");
        }

        System.out.println(stringBuilder);
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

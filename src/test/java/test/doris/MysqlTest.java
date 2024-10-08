package test.doris;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.mwh.utils.text.TextUtil;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MysqlTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private JdbcTemplate jdbcTemplate;

    private DataSourceTransactionManager dataSourceTransactionManager;

    private LinkedBlockingQueue<TestJsonDorisDTO> queue = new LinkedBlockingQueue<>(10000);

    private String host = "127.0.0.1";

    private int port = 3307;

    private String username = "root";

    private String password = "Abcd12345";

    private String db = "basic_data";

    /**
     * 表名
     */
    private String tableName = "area_code";


    private static CloseableHttpClient client;

    @Before
    public void init() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://127.0.0.1:3307/basic_data?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&connectTimeout=5000&socketTimeout=5000&autoReconnect=true&maxReconnects=5&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull&useSSL=false");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("Abcd12345");
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setInitialSize(3);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(50);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenLogStatsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("SELECT 1");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setConnectionInitSqls(Arrays.asList("set session sql_mode=''"));
//        druidDataSource.setFilters("sta");

        jdbcTemplate = new JdbcTemplate(druidDataSource);
//        jdbcTemplate = new NamedParameterJdbcTemplate(druidDataSource);

        dataSourceTransactionManager = new DataSourceTransactionManager(druidDataSource);
    }


    @Test
    public void test() {
        List<Long> levels = new ArrayList<>();
        levels.add(1L);
        levels.add(2L);
        List<Long> codes = new ArrayList<>();
        codes.add(110000000000L);
        codes.add(110100000000L);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("levels", levels);

        LinkedHashMap<Long, ProvinceAreaDTO> provinceAreaDTOMap = new LinkedHashMap<>();

        String placeholders = levels.stream().map(level -> "?").collect(Collectors.joining(","));
        String placeholders2 = codes.stream().map(level -> "?").collect(Collectors.joining(","));
        String sql = String.format("SELECT code, name, level, pcode FROM area_code WHERE level IN (%s) and code IN (%s)", placeholders, placeholders2);

        levels.addAll(codes);
        Object[] params = levels.toArray();
//        String sql = "SELECT code, name, level,pcode FROM area_code WHERE level in (:levels) order by level, code";
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            long code = rs.getLong("code");
            String name = rs.getString("name");
            int level = rs.getInt("level");
            long pcode = rs.getLong("pcode");
            if (level == 1) {
                // 省份信息
                ProvinceAreaDTO provinceAreaDTO = provinceAreaDTOMap.getOrDefault(code, new ProvinceAreaDTO());
                provinceAreaDTO.setProvinceAreaCode(code);
                provinceAreaDTO.setProvince(name);
                provinceAreaDTOMap.put(code, provinceAreaDTO);
            }
            if (level == 2) {
                // 根据pcode获取省份信息
                ProvinceAreaDTO provinceAreaDTO = provinceAreaDTOMap.get(pcode);
                List<CityAreaDTO> cityAreaDTOS = provinceAreaDTO.getCities() == null ? new ArrayList<>() : provinceAreaDTO.getCities();
                // 填充城市信息
                CityAreaDTO cityAreaDTO = new CityAreaDTO();
                cityAreaDTO.setCity(name);
                cityAreaDTO.setCityAreaCode(code);
                cityAreaDTOS.add(cityAreaDTO);
                provinceAreaDTO.setCities(cityAreaDTOS);
            }
            return null;
        }, params);

        List<ProvinceAreaDTO> collect = provinceAreaDTOMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());

        AtomicInteger size = new AtomicInteger(collect.size());

        collect.forEach(provinceAreaDTO -> {
            size.addAndGet(provinceAreaDTO.getCities().size());
        });
        System.out.println(size);
        System.out.println(JSONObject.toJSONString(collect));

    }

    @Test
    public void test2() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("YourTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(def);
        try {
            String sql = "INSERT INTO `test` (`value`) VALUES (3);";
            System.out.println("execute sql:" + sql);
            jdbcTemplate.execute(sql);
            int i = 1 / 0;
            dataSourceTransactionManager.commit(transaction);
            System.out.println("execute commit");
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transaction);
            System.out.println("execute rollback");

        }
    }

    @Test
    public void test3() {
        String sql = "DELETE FROM acc WHERE created < ?";
        jdbcTemplate.update(sql, DateTimeUtil.parseDatetime18("2024-08-05 22:24:53"));
    }

}

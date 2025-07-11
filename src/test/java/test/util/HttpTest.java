package test.util;

import ch.qos.logback.classic.Level;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.mwh.utils.UUIDGenerator;
import com.yxy.nova.mwh.utils.log.LogUtil;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送短信示例
 * 依赖包: HttpClient 4.5.6, fastjson 1.2.58
 *
 */
public class HttpTest {

	private static final Logger logger = LoggerFactory.getLogger(HttpTest.class);
	private static final String client_id = "02f98ed2-3c85-43ac-b4cb-bce89cac192f";

	private  static final String client_secret = "-UspyAK-8gg8hmChm_89J_EPSdlnfI0zdahn10i0WCNqdO3NHnBA82zQfp98Dz2TWeeO7q88J4EfNZbG";


	/**
	 * 连接到某台主机的最大连接数
	 */
	private static volatile int defaultMaxPerRoute = 30;
	/**
	 * 总的最大连接数
	 */
	private static volatile int maxTotal = 300;
	/**
	 * 从连接池中获取连接的最大超时时间，单位：毫秒
	 */
	private static int connectionRequestTimeout = 30000;
	/**
	 * 连接到主机的超时时间，单位：毫秒
	 */
	private static int connectTimeout = 30000;
	/**
	 * 读超时时间
	 */
	private static int socketTimeout = 30000;

	private static CloseableHttpClient client = null;

	static  {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
		cm.setMaxTotal(maxTotal);
		RequestConfig config  = RequestConfig.custom()
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.setSocketTimeout(socketTimeout)
				.build();
		client = HttpClients.custom()
				.setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory())
				.setDefaultRequestConfig(config)
				.setConnectionManager(cm)
				.build();
	}

	public static void main(String[] args) throws Exception {
		ch.qos.logback.classic.Logger httpLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.apache.http");
		httpLogger.setLevel(Level.INFO);
//		listLineUnitCode();//调用获取可用的线路单元
//		doubleCall();//调用双呼接口
//		getCtwing();

//		ctwing();
//		String s = GetIngList();
//		System.out.println(s);
//		publish();
//		authorize();
//		es();
//		sendDingdingmarkdown();
//		testGetByteArray();
//		antUpload();
//		antSmsUpload();
		smsUpload();
	}

	private static void smsUpload() {

		long l = devsmsUpload();
//		long l = prosmsUpload();
		System.out.println("请求耗时：" + l);
	}

	@SneakyThrows
    private static long prosmsUpload() {
		JSONObject sendInfo = new JSONObject();
		sendInfo.put("mobile", getMobileList());
		sendInfo.put("content", "【闪应】测试短信，测试短信，测试短信，测试短信，测试短信，测试短信，测试短信，测试短信，测试短信，测试短信，测试短信，测试短信，测试短信，测试短信公司名称，退订回复R");
		sendInfo.put("appCode", "forTest");

		Map<String,String> headerMap = new HashMap<>();
		headerMap.put("x-access-key", "syfgs");
		headerMap.put("x-access-sign", "c3lmZ3M3YzgzNDFmZWFkZDcxYjljMWUwMmRlZmU3MDdkYWRkNg==");

		long l = System.currentTimeMillis();
		postJsonString("http://47.99.72.114:8038/openapi/smsSend", sendInfo.toJSONString(), headerMap);
		return System.currentTimeMillis() - l;
	}

	@SneakyThrows
    private static long devsmsUpload() {

		JSONObject sendInfo = new JSONObject();
		sendInfo.put("mobile", getMobileList());
		sendInfo.put("content", "【测试】测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信测试短信");
		sendInfo.put("appCode", "syceshi2");

		Map<String,String> headerMap = new HashMap<>();
		headerMap.put("x-access-key", "syfgs");
		headerMap.put("x-access-sign", "c3lmZ3MzZGI0Y2I0YjE5YTQwMDJmMTExYjExNzg4ZjY1MmZhMA==");

		long l = System.currentTimeMillis();
		postJsonString("http://114.55.2.52:8068/openapi/smsSend", sendInfo.toJSONString(), headerMap);
		return System.currentTimeMillis() - l;
	}

	private static String getMobileList() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			stringBuilder.append(generate());
			stringBuilder.append(",");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 1);
	}


	private static void antSmsUpload() {

		ExecutorService executorService = ThreadUtil.newFixedExecutor(10, "蚂蚁测试", true);
		AtomicLong rt = new AtomicLong(0L);
		int batchSize = 20;
		for (int i = 0; i < batchSize; i++) {
			Future<?> submit = executorService.submit(() -> {
				try {
//					long l = antDevSmsOpenapi();
					long l = antProSmsOpenapi();
					rt.set(rt.get() + l);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("平均耗时：" + (rt.get() / batchSize));
	}


	private static void antUpload() {

		ExecutorService executorService = ThreadUtil.newFixedExecutor(20, "蚂蚁测试", true);
		AtomicLong rt = new AtomicLong(0L);
		int batchSize = 500;
		for (int i = 0; i < batchSize; i++) {
			Future<?> submit = executorService.submit(() -> {
				try {
//					long l = antDevOpenapi();
					long l = antProOpenapi();
					rt.set(rt.get() + l);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("平均耗时：" + (rt.get() / batchSize));
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        for (int i = 0; i < 200; i++) {
//			executorService.submit(() -> {
//                try {
////					antDevOpenapi();
//					antProOpenapi();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//		}
	}

	// 生成一个随机手机号
	public static String generate() {
		String prefix = "135";
		StringBuilder sb = new StringBuilder(prefix);
		for (int i = 0; i < 8; i++) {
			sb.append(new Random().nextInt(10));
		}
		return sb.toString();
	}

	private static long antProSmsOpenapi() throws IOException {
		JSONObject sendInfo = new JSONObject();
		sendInfo.put("requestId", UUIDGenerator.generate());
		sendInfo.put("action", "SendBatchSms");
		sendInfo.put("signName", "闪应");
		sendInfo.put("templateCode", "yxy_sms_test1");

		JSONArray jsonArray = new JSONArray();

		for (int i=0; i<10;i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("phoneNumber", generate());
			jsonObject.put("smsUpExtendCode", "1111");

			JSONObject properties = new JSONObject();
			properties.put("ant1", "1234");
			jsonObject.put("templateParam", properties);
			jsonObject.put("bizProperties", "{\"name\":\"name1\",\"age\":\"18\"}");
			jsonArray.add(jsonObject);
		}

		sendInfo.put("customerInfo", jsonArray);

		Map<String,String> headerMap = new HashMap<>();
		headerMap.put("appKey", "forTest_ant");
		headerMap.put("timestamp", "1747107231174");
		headerMap.put("sign", "d5NnJ67mBrbCkObuAhsqxtMNtAM=");
		long l = System.currentTimeMillis();
		postJsonString("https://hermes.shanyingtech.com/api/v1/antopenapi/task/smsImport/v1", sendInfo.toJSONString(), headerMap);
		return System.currentTimeMillis() - l;
	}
	private static long antDevSmsOpenapi() throws IOException {
		JSONObject sendInfo = new JSONObject();
		sendInfo.put("requestId", UUIDGenerator.generate());
		sendInfo.put("action", "SendBatchSms");
		sendInfo.put("signName", "闪应");
		sendInfo.put("templateCode", "sy_test5");

		JSONArray jsonArray = new JSONArray();

		for (int i=0; i<1;i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("phoneNumber", generate());
			jsonObject.put("smsUpExtendCode", "1111");

			JSONObject properties = new JSONObject();
			properties.put("ant1", "1234");
			jsonObject.put("templateParam", properties);
			jsonObject.put("bizProperties", "{\"name\":\"name1\",\"age\":\"18\"}");
			jsonArray.add(jsonObject);
		}

		sendInfo.put("customerInfo", jsonArray);

		Map<String,String> headerMap = new HashMap<>();
		headerMap.put("appKey", "ant_test1");
		headerMap.put("timestamp", "1741312345540");
		headerMap.put("sign", "PreynKDr+4Svd4dbHMnmp1xazGo=");
		long l = System.currentTimeMillis();
		postJsonString("http://114.55.2.52:8028/api/v1/antopenapi/task/smsImport/v1", sendInfo.toJSONString(), headerMap);
		return System.currentTimeMillis() - l;
	}
	private static long antDevOpenapi() throws IOException {
		JSONObject sendInfo = new JSONObject();
		sendInfo.put("requestId", UUIDGenerator.generate());
		sendInfo.put("taskId", 621);
		sendInfo.put("importTransactionType", 0);

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", "tag");
		jsonObject.put("keyTemplate", "MOBILE");
		jsonObject.put("customerKey", generate());

		JSONObject properties = new JSONObject();
		properties.put("ant1", "1234");
		jsonObject.put("properties", properties);
		jsonArray.add(jsonObject);
		sendInfo.put("customers", jsonArray);

		Map<String,String> headerMap = new HashMap<>();
		headerMap.put("appKey", "ant_test1");
		headerMap.put("timestamp", "1741312345540");
		headerMap.put("sign", "PreynKDr+4Svd4dbHMnmp1xazGo=");
		long l = System.currentTimeMillis();
		postJsonString("http://114.55.2.52:8028/api/v1/antopenapi/task/batchImport/v1", sendInfo.toJSONString(), headerMap);
		return System.currentTimeMillis() - l;
	}
	private static long antProOpenapi() throws IOException {
		JSONObject sendInfo = new JSONObject();
		sendInfo.put("requestId", UUIDGenerator.generate());
		sendInfo.put("taskId", 26381);
		sendInfo.put("importTransactionType", 0);

		JSONArray jsonArray = new JSONArray();
		for (int i=0; i<100;i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("tag", "tag");
			jsonObject.put("keyTemplate", "MOBILE");
			jsonObject.put("customerKey", generate());

			JSONObject properties = new JSONObject();
			properties.put("ant1", "1234");
			jsonObject.put("properties", properties);
			jsonArray.add(jsonObject);
		}
		sendInfo.put("customers", jsonArray);

		Map<String,String> headerMap = new HashMap<>();
		headerMap.put("appKey", "forTest_ant");
		headerMap.put("timestamp", "1747107231174");
		headerMap.put("sign", "d5NnJ67mBrbCkObuAhsqxtMNtAM=");
		long l = System.currentTimeMillis();
		postJsonString("https://hermes.shanyingtech.com/api/v1/antopenapi/task/batchImport/v1", sendInfo.toJSONString(), headerMap);
		return System.currentTimeMillis() - l;

	}

	private static void testGetByteArray() {
		byte[] byteArray = getByteArray("https://shanying-engine5.oss-cn-hangzhou.aliyuncs.com/bssvoice/voice_file/voiking_shanying/ai/REC202502/20250210/ed52a8b539b04fffb6c40c4fe2b6187f.wav", null);
		FileUtil.writeBytes(byteArray, "/Users/yuxiaoyu/Downloads/4.wav");
	}

	@SneakyThrows
    private static void sendDingdingLink() {
		JSONObject sendInfo = new JSONObject();
		sendInfo.put("msgtype", "link");
		JSONObject link = new JSONObject();
		link.put("title", "【短信预警】");
		link.put("text", "任务id:告警内容\n 任务id2：告警内容2");
		link.put("messageUrl", "http://114.55.2.52:8028/route/aiCall/task");
		sendInfo.put("link", link);

		JSONObject atJSon = new JSONObject();
		atJSon.put("isAtAll", false); // 是否通知所有人
//		atJSon.put("atMobiles", Arrays.asList("19155137319"));
		sendInfo.put("at", atJSon);
		postJsonString("https://oapi.dingtalk.com/robot/send?access_token=62c2cb112a50c7ab72ca98d23363c294bb81acddb2b6eb662d4278bf2bf22cda", sendInfo.toJSONString(), null);
	}
	@SneakyThrows
    private static void sendDingdingmarkdown() {
		JSONObject sendInfo = new JSONObject();
		sendInfo.put("msgtype", "markdown");
		JSONObject markdown = new JSONObject();
		markdown.put("title", "【短信预警】闪应分公司-测试测试");
		markdown.put("text", "【短信预警】闪应分公司-测试测试 \\\n [91：yxy普通短信1，2025-04-17 05:50:00到2025-04-17 15:50:00，短信发送量：2010，成功率0.0000%。](http://114.55.2.52:8028/route/sms/templateStat?partnerCode=syfgs&appCode=syceshi2&beginSendTime=2025-04-17%2005:50:00&endSendTime=2025-04-17%2015:50:00) \\\n [101：yxy普通短信2，2025-04-17 05:50:00到2025-04-17 15:50:00，短信发送量：2010，成功率0.0000%。](http://114.55.2.52:8028/route/sms/templateStat?partnerCode=syfgs&appCode=syceshi2&beginSendTime=2025-04-17%2005:50:00&endSendTime=2025-04-17%2015:50:00)\n");
		sendInfo.put("markdown", markdown);

		JSONObject atJSon = new JSONObject();
		atJSon.put("isAtAll", false); // 是否通知所有人
//		atJSon.put("atMobiles", Arrays.asList("19155137319"));
		sendInfo.put("at", atJSon);
		postJsonString("https://oapi.dingtalk.com/robot/send?access_token=62c2cb112a50c7ab72ca98d23363c294bb81acddb2b6eb662d4278bf2bf22cda", sendInfo.toJSONString(), null);
	}


	@SneakyThrows
	private static void es() {
		String encoded = java.util.Base64.getEncoder().encodeToString(("elastic" + ":" + "hxkj@123").getBytes("UTF-8"));
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Basic " + encoded);
		String s = get("http://127.0.0.1:9200/_cat/indices?format=json&s=store.size:asc", headers);
		System.out.println(s);


//		String s1 = deleteJsonString("http://127.0.0.1:9200/" + "device_log_wlwmqtt_2024-3", headers);
//		System.out.println(s1);
	}


	@SneakyThrows
	public static void publish() {
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("content", "java测试接口"));
		nameValuePairs.add(new BasicNameValuePair("publicFlag", "1"));
		String post = post("https://ing.cnblogs.com/ajax/ing/Publish", nameValuePairs, null);
		JSONObject publish = JSONObject.parseObject(post);
		System.out.println(publish.toJSONString());

	}

	@SneakyThrows
	public static void authorize() {
		List<NameValuePair> request = new ArrayList();
		request.add(new BasicNameValuePair("client_id", client_id));
		request.add(new BasicNameValuePair("scope", "openid profile CnBlogsApi offline_access"));
		request.add(new BasicNameValuePair("response_type", "code id_token"));
		request.add(new BasicNameValuePair("redirect_uri", "https://oauth.cnblogs.com/auth/callback"));
		request.add(new BasicNameValuePair("state", "q12w2aj8627hd7"));
		request.add(new BasicNameValuePair("nonce", "u17j3g73hh3hg3jh3"));

		String result = get("https://oauth.cnblogs.com/connect/authorize", request);
		System.out.println(result);

	}

	@SneakyThrows
	public static String GetIngList() {
		String getStr = get("https://ing.cnblogs.com/ajax/ing/GetIngList?IngListType=my&PageIndex=1&PageSize=1&Tag=&_=" + System.currentTimeMillis(), new HashMap<>());

		// 创建正则表达式模式
		Pattern pattern = Pattern.compile("onclick='return DelIng\\((\\d+)\\)'");

		// 创建匹配器
		Matcher matcher = pattern.matcher(getStr);

		if (matcher.find()) {
			// 提取匹配的数字
			String number = matcher.group(1);
			return number + "";
		} else {
			return null;
		}
	}


	@SneakyThrows
	public static boolean del(String id) {
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("ingId", id));
		String post = post("https://ing.cnblogs.com/ajax/ing/del", nameValuePairs, null);

		return "删除成功".equals(post);
	}


	private static void getCtwing() throws IOException {

		String s = get("https://ag-api.ctwing.cn/echo", new HashMap<>());
		System.out.println(s);

//		client.get()
//				.uri("https://ag-api.ctwing.cn/echo")
//				.exchange()
//				.flatMap(response -> {
//					long end = System.currentTimeMillis();
//					long serverTime = response
//							.headers()
//							.header("x-ag-timestamp")
//							.stream()
//							.findAny()
//							.map(Long::parseLong)
//							.orElse(0L);
//					timeOffset = serverTime - (start + end) / 2L;
	}

	private static void ctwing() throws IOException {
		String productId = "15553245";
		String imei = "864901066495973";
		String MasterKey = "a22d374dd8794e24a62091bb0e3958c0";
		String appKey = "R0mH3WXlel1";
		String appSecret = "gh7DGsB694";
		Map<String, Object> body = new TreeMap<>();
		body.put("deviceName", "智咏物联水表");
		body.put("deviceSn", "");
		body.put("imei", imei);
		body.put("operator", "admin");

		{
			Map<String, Object> other = new TreeMap<>();
			other.put("autoObserver", 0);
			other.put("imsi", imei);
			other.put("pskValue", "");
			body.put("other", other);
		}
		body.put("productId", productId);

		String bodyString = JSON.toJSONString(body);

		HttpPost httpPost = new HttpPost("https://ag-api.ctwing.cn/aep_device_management/device");

		Map<String, String> header = new HashMap<>();
		header.put("application", appKey);
		header.put("timestamp", String.valueOf(System.currentTimeMillis()));
		header.put("version", "20181031202117");
		header.put("sdk", "0");

		Map<String, List<String>> parameterCopy = new HashMap<>();
		parameterCopy.put("MasterKey", Collections.singletonList(MasterKey));
		String sign = Signature.sign(appSecret,
				header.get("application"),
				header.get("timestamp"),
				parameterCopy,
				bodyString.getBytes());

		header.put("signature", sign);
		header.put("MasterKey", MasterKey);
		header.put("User-Agent", "JetLinks Iot Platform");

		String s = postJsonString("https://ag-api.ctwing.cn/aep_device_management/device", bodyString, header);
		System.out.println(s);
	}

	/**
	 * 调用获取可用的线路单元 接口示例
	 * @throws Exception
	 */
	public static void listLineUnitCode() throws Exception{
		String url = "http://120.48.8.74:8028/openapi/call/listEnabledLineUnit/v1";

		String partnerCode = "dingsheng_main";
		String openapiSecret = "11dbea52";
		Map<String,String> headerMap = new HashMap<>();
		headerMap.put("x-access-key", partnerCode);
		headerMap.put("x-access-sign", Base64.encodeBase64String((partnerCode + getMD5(openapiSecret.getBytes("UTF-8"))).getBytes("UTF-8")));

		String res = get(url, headerMap);
		System.out.println(res);
	}

	/**
	 * sendsmspkg 接口示例
	 * @throws Exception
	 */
	public static void doubleCall()throws Exception{
		String url = "http://120.48.8.74:8028/openapi/call/manualCall/v1";

		String partnerCode = "dingsheng_main";
		String openapiSecret = "11dbea52";
		Map<String,String> headerMap = new HashMap<>();
		headerMap.put("x-access-key", partnerCode);
		headerMap.put("x-access-sign", Base64.encodeBase64String((partnerCode + getMD5(openapiSecret.getBytes("UTF-8"))).getBytes("UTF-8")));

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("callType", "WEBPHONE_DOUBLE_CALL");
		jsonObject.put("lineUnitCode", "hangyun-gz");	// 线路单元结果返回的结果
		jsonObject.put("phoneNo","77700000");
		jsonObject.put("customerPhone","13811112222");

		String res = postJsonString(url, jsonObject.toJSONString() ,headerMap);
		System.out.println(res);
	}

	public static String getMD5(byte[] source) {
		String s = null;
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte[] tmp = md.digest();
			char[] str = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}


	public static String post(String url, List<NameValuePair> nameValuePairs, Map<String, String> headerMap) throws IOException {
		logger.info("请求的url:{}, nameValuePairs: {}", Arrays.asList(url, JSON.toJSONString(nameValuePairs)), "httpPost");
		String result = null;
		CloseableHttpResponse response = null;

		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			if (headerMap != null) {
				Iterator var7 = headerMap.entrySet().iterator();

				while(var7.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry)var7.next();
					httpPost.setHeader((String)entry.getKey(), (String)entry.getValue());
				}
			}

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			response = client.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (200 != statusCode) {
				logger.error("请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpPost");
			}

			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info( "服务端返回的response: {}", Arrays.asList(result), "httpPost");
			EntityUtils.consume(response.getEntity());
		} catch (IOException var16) {
			logger.error("请求URL {} 时发生错误", Arrays.asList(url, var16), "httpPost");
			throw var16;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException var15) {
					logger.error("关闭响应流出现错误: {} ", Arrays.asList(var15.getMessage(), var15), "httpPost");
				}
			}

		}

		return result;
	}

	/**
	 * 执行post请求
	 * @param url
	 * @param jsonString
	 * @return
	 */
	private static String postJsonString(String url, String jsonString, Map<String,String> headerMap) throws IOException {
		logger.info( "请求的url: {}, 报文体: {}", Arrays.asList(url, jsonString), "httpPostJson");

		String result = null;
		CloseableHttpResponse response = null;
		try {
			HttpEntity entity = new StringEntity(jsonString, Charset.forName("UTF-8"));

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			httpPost.setEntity(entity);
			response = client.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				logger.error( "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpPostJson");
			}

			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info( "服务端返回的response: {}", Arrays.asList(result), "httpPostJson");

			EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
			logger.error("请求URL{}时发生错误", url, e);
			throw e;
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error( "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpPostJson");
				}
			}
		}
		return result;
	}


	/**
	 * 执行get请求
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public static String get(String url, List<NameValuePair> nameValuePairs) throws IOException {
		return get(url, nameValuePairs, null);
	}

	/**
	 * 执行get请求
	 * @param url
	 * @param nameValuePairs
	 * @param headerMap
	 * @return
	 */
	public static String get(String url, List<NameValuePair> nameValuePairs, Map<String, String> headerMap) throws IOException {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < nameValuePairs.size(); i++) {
			buf.append(i == 0 ? "?" : "&");
			try {
				buf.append(nameValuePairs.get(i).getName()).append("=")
						.append(URLEncoder.encode(nameValuePairs.get(i).getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.error( "URL {} 编码时发生错误", Arrays.asList(url, e), "urlEncode");
				return null;
			}
		}
		return get(url + buf.toString(), headerMap);
	}

	/**
	 * 执行get请求
	 * @param url
	 * @param headerMap
	 * @return
	 */
	public static String get(String url, Map<String,String> headerMap) throws IOException {

		logger.info( "请求的url: {}", Arrays.asList(url), "httpGet");

		String result = null;
		CloseableHttpResponse response = null;

		try {
			HttpGet getMethod = new HttpGet(url);
			getMethod.setHeader("Content-Encoding", "UTF-8");

			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					getMethod.setHeader(entry.getKey(), entry.getValue());
				}
			}

			response = client.execute(getMethod);

			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				logger.error( "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpGet");
			}

			result = EntityUtils.toString(response.getEntity());
			logger.info( "服务端返回的response: {}", Arrays.asList(result), "httpGet");

			EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
			logger.error( "请求URL {} 时发生错误", Arrays.asList(url, e), "httpGet");
			throw e;
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error( "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpGet");
				}
			}
		}

		return result;
	}


	public static String deleteJsonString(String url, Map<String, String> headerMap) throws IOException {
		LogUtil.info(logger, "请求的url: {}", Arrays.asList(url), "httpPutJson");

		String result = null;
		CloseableHttpResponse response = null;
		try {
			HttpDelete httpDelete = new HttpDelete(url);
			httpDelete.setHeader("Content-Type", "application/json;charset=UTF-8");
			if (headerMap != null) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					httpDelete.setHeader(entry.getKey(), entry.getValue());
				}
			}
			response = client.execute(httpDelete);

			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				LogUtil.error(logger, "请求URL {} 时返回的statusCode为 {}", Arrays.asList(url, statusCode), "httpPostJson");
			}

			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			LogUtil.info(logger, "服务端返回的response: {}", Arrays.asList(result), "httpPutJson");

			EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
			logger.error("请求URL{}时发生错误", url, e);
			throw e;
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					LogUtil.error(logger, "关闭响应流出现错误: {} ", Arrays.asList(e.getMessage(), e), "httpPutJson");
				}
			}
		}
		return result;
	}

	public static byte[] getByteArray(String url, Map<String, String> headerMap) {
		logger.info("请求的url: {}", url);
		HttpGet getMethod = new HttpGet(url);
		if (headerMap != null) {
			for(Map.Entry<String, String> entry : headerMap.entrySet()) {
				getMethod.setHeader((String)entry.getKey(), (String)entry.getValue());
			}
		}

		try (CloseableHttpResponse response = client.execute(getMethod)) {
			int statusCode = response.getStatusLine().getStatusCode();
			if (200 != statusCode) {
				logger.error("请求URL {} 时返回的statusCode为 {}", url, statusCode);
				Object var44 = null;
				return (byte[])var44;
			} else {
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					logger.error("请求URL {} 时返回的entity为null", url);
					Object var45 = null;
					return (byte[])var45;
				} else {
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					Throwable var9 = null;

					Object var10;
					try {
						entity.writeTo(outputStream);
						var10 = outputStream.toByteArray();
					} catch (Throwable var37) {
						var10 = var37;
						var9 = var37;
						throw var37;
					} finally {
						if (outputStream != null) {
							if (var9 != null) {
								try {
									outputStream.close();
								} catch (Throwable var36) {
									var9.addSuppressed(var36);
								}
							} else {
								outputStream.close();
							}
						}

					}

					return (byte[])var10;
				}
			}
		} catch (IOException e) {
			logger.error("请求URL {} 时发生错误", url, e);
			return null;
		}
	}
}

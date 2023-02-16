package test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 发送短信示例
 * 依赖包: HttpClient 4.5.6, fastjson 1.2.58
 *
 */
public class HttpTest {

	private static final Logger logger = LoggerFactory.getLogger(HttpTest.class);

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
//		listLineUnitCode();//调用获取可用的线路单元
//		doubleCall();//调用双呼接口
//		getCtwing();

		ctwing();
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
	 * @param headerMap
	 * @return
	 */
	private static String get(String url, Map<String,String> headerMap) throws IOException {

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
}

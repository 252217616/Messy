package com.example.demo01.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

public class HttpClientUtil {
	private static Logger logger = LogManager.getLogger(HttpClientUtil.class);

	public static String post(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		logger.info("create httppost:" + url);
		HttpPost post = postForm(url, params);

		body = invoke(httpclient, post);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static String postArrayParam(String url, Map<String, String[]> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		logger.info("create httppost:" + url);
		HttpPost post = postArrayParamForm(url, params);

		body = invoke(httpclient, post);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static String postStr(String url, String params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		logger.info("create httppost:" + url);
		HttpPost post = postFormStr(url, params);
		String body = invoke(httpclient, post);
		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static String get(String url) {
		logger.info("create httppost:" + url);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		String body = invoke(httpclient, get);
		httpclient.getConnectionManager().shutdown();
		return body;
	}

	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		HttpResponse response = sendRequest(httpclient, httpost);
		if (StringUtils.isEmpty(response)) {
			return "";
		}
		String body = paseResponse(response);
		return body;
	}

	private static String paseResponse(HttpResponse response) {
		String body = "";
		try {
			HttpEntity entity = response.getEntity();
			logger.info("response status: " + response.getStatusLine());
			String charset = EntityUtils.getContentCharSet(entity) == null ? "utf-8" : EntityUtils.getContentCharSet(entity);
			logger.info(charset);
			body = new String(EntityUtils.toByteArray(entity), charset);
		} catch (ParseException e) {
			logger.debug("表示解析时出现意外错误,错误信息{}", e.getMessage());
		} catch (IOException e) {
			logger.debug("网络通道读取异常,错误信息{}", e.getMessage());
		}
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		HttpResponse response = null;

		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e); //Signals an error in the HTTP protocol.
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error("网络通道读取异常", e); //网络不通错误（请求超时错误）
			throw new RuntimeException(e);
		}

		return response;
	}

	private static HttpPost postArrayParamForm(String url, Map<String, String[]> params) {

		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			String[] values = params.get(key);
			for (String value : values) {
				nvps.add(new BasicNameValuePair(key, value));
			}
		}

		try {
			logger.info("set utf-8 form entity to httppost");
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpost;
	}

	private static HttpPost postForm(String url, Map<String, String> params) {

		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			logger.info("set utf-8 form entity to httppost");
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpost;
	}

	private static HttpPost postFormStr(String url, String params) {

		HttpPost httpost = new HttpPost(url);

		logger.info("set utf-8 form entity to httppost");
		StringEntity s = new StringEntity(params, "UTF-8");
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");
		httpost.setEntity(s);

		return httpost;
	}


	/**
	 * 下载文件
	 *
	 * @param url          http://www.xxx.com/img/333.jpg
	 * @param destFileName xxx.jpg/xxx.png/xxx.txt
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void getFile(String url, String destFileName) throws ClientProtocolException, IOException {
		// 生成一个httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		File file = new File(destFileName);
		try {
			FileOutputStream fout = new FileOutputStream(file);
			int l = -1;
			byte[] tmp = new byte[1024];
			while ((l = in.read(tmp)) != -1) {
				fout.write(tmp, 0, l);
				// 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
			}
			fout.flush();
			fout.close();
		} finally {
			// 关闭低层流。
			in.close();
		}
		httpclient.close();
	}

	/**
	 * HttpClient连接SSL
	 *
	 * @param
	 */
	public static String sslPost(String url, String params, String certUrl, String pwd, String keyStoreType) {


		logger.info("url={},params={},certUrl={},pwd={},keyStoreType={}", url, params, certUrl, pwd, keyStoreType);
		CloseableHttpClient httpclient = getCloseableHttpClient(certUrl, pwd, keyStoreType);
		HttpPost post = postFormStr(url, params);
		String body = null;
		body = getResponseStr(httpclient, post);
		try {
			if (httpclient != null) {
				httpclient.close();
			}
		} catch (IOException e) {
			logger.info("关闭httpclient失败：{}", e.getMessage());
		}
		return body;
	}

	//获取应答报文
	private static String getResponseStr(CloseableHttpClient httpClient, HttpPost post) {

		String resStr = "";
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			resStr = EntityUtils.toString(entity, "UTF-8");

		} catch (Exception e) {
			logger.info("请求服务获取应答失败：{}", e.getMessage());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.info("关闭response失败：{}", e.getMessage());
			}
		}
		return resStr;
	}

	//加载证书文件，生成httpClient对象
	@SuppressWarnings("deprecation")
	private static CloseableHttpClient getCloseableHttpClient(String certUrl, String pwd, String keyStoreType) {
		CloseableHttpClient httpclient = null;
		FileInputStream instream = null;

		try {
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			//证书文件目录
			instream = new FileInputStream(new File(certUrl));
			//密码
			keyStore.load(instream, pwd.toCharArray());

			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, pwd.toCharArray()).build();
			// 只允许TLSv1协议
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			logger.info("加载证书文件，生成httpClient对象失败：{}", e.getMessage());
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				logger.info("关闭文件输入流错误：{}", e.getMessage());
			}
		}
		return httpclient;
	}

	/**
	 * @param strvalue
	 * @return
	 */
	public static String toChinese(String strvalue) {
		try {
			if (strvalue == null) {
				return "";
			} else {
				strvalue = new String(strvalue.getBytes(), "ISO-8859-1").trim();
				return strvalue;
			}
		} catch (Exception e) {
			return "";
		}
	}

	public static String postClient(String url, String para) {
		String result = null;
		BufferedReader br = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("context", para));
			UrlEncodedFormEntity urlEndode = new UrlEncodedFormEntity(nameValuePairs);
			urlEndode.setContentType("text/json");
			urlEndode.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new TimeoutException("连接trade回调地址异常");
			}
			HttpEntity entity = response.getEntity();
			br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			result = br.readLine();
		} catch (Exception e) {
			throw new RuntimeException("连接trade回调地址异常");
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
			}
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
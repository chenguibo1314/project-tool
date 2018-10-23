package com.fcbox.oplatform.web.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.request.WebRequest;

import com.alibaba.dubbo.container.Main;

public class HttpUtil {
	
	 private static final int HTTP_STATUS_OK = 200;
	public static void main(String[] args) {
		String url="http://xt.hopebest.cc:8080/webquery.pl?callback=fillTracking&order_id=";
		Long begin=System.currentTimeMillis();
			String aa=sendGet(url,"35609953567");
			Long end=System.currentTimeMillis();
			System.out.println("时间："+(end-begin)+aa);
	}
	
	
	 /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //connection.setRequestProperty("connection", "Keep-Alive");
            //connection.setRequestProperty("user-agent",
             //       "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
	 /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
        	url=url+"?"+param;
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            //URLConnection conn = realUrl.openConnection();
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();  
            // 设置通用的请求属性
            //conn.setRequestProperty("accept", "*/*");
           /* conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");*/
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
           // conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    } 
    
  
    
    public static String PostMethod(String path, Map<String, String> params) throws IOException{  
        StringBuilder sb = new StringBuilder();  
        for(Map.Entry<String, String> entry : params.entrySet()){  
            //sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');  
            sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');  
        }  
        sb.deleteCharAt(sb.length() - 1);  
        byte[] entitydata = sb.toString().getBytes();  
        URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        //conn.setRequestMethod("POST");  
        conn.setDoOutput(true);  
        conn.setDoInput(true);
        
        // Post 请求不能使用缓存
        conn.setUseCaches(false);
        
        conn.setInstanceFollowRedirects(true);
        
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
        conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));  
       // conn.setRequestProperty("accept", "*/*");
       // conn.setRequestProperty("accept-encoding", "gzip, deflate, br");
       // conn.setRequestProperty("connection", "Keep-Alive");
       // conn.setRequestProperty("user-agent",
       //        "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36");
        OutputStream os = conn.getOutputStream();  
        os.write(entitydata);  
        os.flush();  
        os.close();  
        String result="";
        if(conn.getResponseCode() == 200){  
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        }  
        return null;  
    }  
    /**
     * 通过get方式发送请求，并返回响应结果
     * 
     * @param url
     *            请求地址
     * @param params
     *            参数列表，例如name=小明&age=8里面的中文要经过Uri.encode编码
     * @param encoding
     *            编码格式
     * @return 服务器响应结果
     * @throws Exception
     */
    public static String sendGetMethod(String url, String params,
            String encoding) throws Exception {
        String result = "";
        url += ((-1 == url.indexOf("?")) ? "?" : "&") + params;

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("charset", encoding);

        try {
            HttpResponse response = client.execute(get);
            if (HTTP_STATUS_OK == response.getStatusLine().getStatusCode()) {
                result = EntityUtils.toString(response.getEntity(), encoding);
            } else {
                throw new Exception("Invalide response from Api!"
                        + response.toString());
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    
    public static String readContentFromPost(String url,Map<String,String> param) throws IOException {
    	
    	 StringBuilder sb = new StringBuilder();  
         for(Map.Entry<String, String> entry : param.entrySet()){  
             sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');  
         }  
         String aa=sb.deleteCharAt(sb.length() - 1).toString(); 
    	 url=url+"?"+aa;
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
       
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod("POST");
        
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        
        connection.setInstanceFollowRedirects(true);
        
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
        // 进行编码
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行connect。
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection
                .getOutputStream());
        // The URL-encoded contend
        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
        //String content =aa; 
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
       // out.writeBytes(content);
        out.flush();
        out.close(); 
         
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
         String result="";
        while ((line = reader.readLine()) != null){
        	result += line;
        }
        reader.close();
        connection.disconnect();
        return result;
}
     
    
}

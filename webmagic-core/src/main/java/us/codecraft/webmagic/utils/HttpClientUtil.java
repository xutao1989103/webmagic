package us.codecraft.webmagic.utils;

import com.google.common.base.Strings;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;

/**
 * Created by 431 on 2015/4/30.
 */
public class HttpClientUtil {
    //private  static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private  static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private  static final String ENCODING_GZIP = "gzip";
    private HttpClient httpclient;

    public HttpClient getHttpclient() {
        return httpclient;
    }

    public HttpClientUtil(){
        setHttpclient(new DefaultHttpClient());
        initClient();
    }
    /**
     * 通过post提交方式获取url指定的资源和数据
     *
     * @param url
     * @return
     * @throws Exception
     */
    public  String postData(String url) throws Exception {
        return postData(url, null);
    }

    /**
     * 通过post提交方式获取url指定的资源和数据
     *
     * @param url
     * @param nameValuePairs
     *            请求参数
     * @return
     * @throws Exception
     */
    public  String postData(String url, List<NameValuePair> nameValuePairs)
            throws Exception {
        return postData(url, nameValuePairs, null);
    }

    /**
     * 通过post提交方式获取url指定的资源和数据
     *
     * @param url
     * @param nameValuePairs
     *            请求参数
     * @param headers
     *            请求header参数
     * @return
     * @throws Exception
     */
    public  String postData(String url,
                            List<NameValuePair> nameValuePairs, Map<String, String> headers)
            throws Exception {
        long start = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        try {
            if (headers != null && headers.size() > 0) {
                Set<Map.Entry<String, String>> set = headers.entrySet();
                for (Iterator<Map.Entry<String, String>> it = set.iterator(); it
                        .hasNext();) {
                    Map.Entry<String, String> header = it.next();
                    if (header != null) {
                        httpPost.setHeader(header.getKey(), header.getValue());
                    }
                }
            }
            if (nameValuePairs != null && nameValuePairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            }

            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(entity == null){
                return null;
            }
            String info = EntityUtils.toString(entity, "UTF-8");
            return info;
        } catch (Exception e) {
            throw new Exception(url
                    + "dajie postData exception：", e);
        } finally {
            httpPost.releaseConnection();
            long interval = System.currentTimeMillis() - start;
        }
    }

    /**
     * 通过ContentType 为json的格式进行http传输
     * @param url 远程url
     * @param content 传输内容
     * @return
     * @throws Exception
     */
    public  String postJSONData(String url, String content) throws Exception {
        long start = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        try {
            if (content != null && content.length() > 0) {
                httpPost.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
            }
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(entity == null){
                return null;
            }
            String info = EntityUtils.toString(entity, "UTF-8");
            return info;
        } catch (Exception e) {
            throw new Exception(url
                    + "dajie postDataByJson exception：", e);
        } finally {
            httpPost.releaseConnection();
            long interval = System.currentTimeMillis() - start;
        }
    }

    /**
     * 通过get方法获取url资源的数据
     *
     * @param url
     *            服务器地址
     * @return 返回响应的文本，如果请求发生异常，抛出Exception
     * @throws Exception
     */
    public  String getData(String url) throws Exception {
        return getData(url, null);
    }

    /**
     * 带header的get请求
     *
     * @param url
     *            服务器地址
     * @param headers
     *            添加的请求header信息
     * @return 返回服务器响应的文本，出错抛出Exception异常
     * @throws Exception
     */
    public  String getData(String url, Map<String, String> headers)
            throws Exception {
        long start = System.currentTimeMillis();
        HttpGet httpGet = new HttpGet(url);
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Iterator<Map.Entry<String, String>> it = set.iterator(); it
                    .hasNext();) {
                Map.Entry<String, String> header = it.next();
                if (header != null) {
                    httpGet.setHeader(header.getKey(), header.getValue());
                }
            }
        }
        try {
            HttpResponse response =  httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if(entity == null){
                return null;
            }
            String info = EntityUtils.toString(entity, "UTF-8");
            return info;
        } catch (Exception e) {
            throw new Exception(url
                    + "dajie getData exception：", e);
        } finally {
            httpGet.releaseConnection();
            long interval = System.currentTimeMillis() - start;
        }
    }

    /**
     * 对httpclient 做压缩处理和解压缩处理
     *
     */
    public void initClient() {
        ((DefaultHttpClient)httpclient).addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, HttpContext context) {
                if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
            }
        });

        ((DefaultHttpClient)httpclient).addResponseInterceptor(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse response, HttpContext context) {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                            response.setEntity(new GzipDecompressingEntity(
                                    response.getEntity()));
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * 关闭客户端
     */
    public void destroyClient(){
        httpclient.getConnectionManager().shutdown();
    }
    /**
     * post方式处理文件和图片上传
     *
     * @param url
     *            服务器地址
     * @param data
     *            byte数组数据
     * @param fileName
     *            文件名
     * @return 返回服务器响应信息，否则抛出Exception异常
     * @throws Exception
     */
    public  String postMultipartData(String url, byte[] data,
                                     String fileName) throws Exception {
        long start = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        try {
            if (data != null && data.length > 0) {
                MultipartEntity reqEntity = new MultipartEntity();
                ContentBody contentBody = new ByteArrayBody(data, fileName);
                reqEntity.addPart("file", contentBody);
                httpPost.setEntity(reqEntity);
            }
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String info = EntityUtils.toString(entity, "UTF-8");
            return info;
        } catch (Exception e) {
            throw new Exception(url
                    + "dajie postMultipartData exception：", e);
        } finally {
            httpPost.releaseConnection();
            long interval = System.currentTimeMillis() - start;
        }
    }

    /**
     * put 方式提交数据
     *
     * @param url
     *            ：服务器地址
     * @param nameValuePairs
     *            ：参数
     * @return 返回 服务器返回的文本信息，报错会抛出异常
     * @throws Exception
     */
    public  String putData(String url, List<NameValuePair> nameValuePairs)
            throws Exception {
        long start = System.currentTimeMillis();
        HttpPut httpPut = new HttpPut(url);

        try {
            if (nameValuePairs != null && nameValuePairs.size() > 0) {
                httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
            }
            HttpResponse response = httpclient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            if(entity == null){
                return null;
            }
            String info = EntityUtils.toString(entity, "UTF-8");
            return info;
        } catch (Exception e) {
            throw new Exception(url
                    + "dajie putData exception：", e);
        } finally {
            httpPut.releaseConnection();
            long interval = System.currentTimeMillis() - start;
        }
    }

    /**
     * delete 方式提交数据
     *
     * @param url
     *            服务器地址
     * @return 返回 服务器返回的文本信息，报错会抛出异常
     * @throws Exception
     */
    public  String deleteData(String url)
            throws Exception {
        return deleteData(url, null);
    }

    /**
     * delete 方式提交数据
     *
     * @param url
     *            服务器地址
     * @return 返回 服务器返回的文本信息，报错会抛出异常
     */
    public  String deleteData(String url, Map<String, String> headers)
            throws Exception {
        long start = System.currentTimeMillis();
        HttpDelete httpDelete = new HttpDelete(url);

        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Iterator<Map.Entry<String, String>> it = set.iterator(); it
                    .hasNext();) {
                Map.Entry<String, String> header = it.next();
                if (header != null) {
                    httpDelete.setHeader(header.getKey(), header.getValue());
                }
            }
        }
        try {
            HttpResponse response = httpclient.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            String info = EntityUtils.toString(entity, "UTF-8");
            return info;
        } catch (Exception e) {
            throw new Exception(url
                    + "dajie deleteDate exception：", e);
        } finally {
            httpDelete.releaseConnection();
            long interval = System.currentTimeMillis() - start;
        }
    }

    /**
     * 下载媒体资源
     * @param url
     * @return
     * @throws Exception
     */
    public byte[] getMultipartData(String url) throws Exception{
        long start = System.currentTimeMillis();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response =  httpclient.execute(httpGet);
            byte[] result = EntityUtils.toByteArray(response.getEntity());
            return result;
        }catch(Exception e){
            throw new Exception(url+ "dajie getMultipartData exception：", e);
        }finally{
            httpGet.releaseConnection();
            long interval = System.currentTimeMillis() - start;
        }
    }

    public byte[] getFile(String url, Map headers , String mimeType)
            throws Exception {
        HttpGet httpGet = new HttpGet(url);
        setHeader(httpGet,headers);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (ClientProtocolException e1) {
            throw new Exception(e1);
        }
        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String mimetype1 = EntityUtils.getContentMimeType(entity);
                if (!Strings.isNullOrEmpty(mimetype1)
                        && mimetype1.contains(mimeType)) {
                    return getBytes(entity);
                }
            }
        }
        return null;
    }

    private byte[] getBytes(HttpEntity entity){
        if (entity == null) return null;
        byte[] buffer = null;
        byte[] b = new byte[1000];
        int n;
        try {
            InputStream in = entity.getContent();
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            while ((n = in.read(b)) != -1){
                bos.write(b, 0, n);
            }
            in.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public File saveFile(String url, Map headers ,String filePath, String mimeType)
            throws Exception {

        File imageFile = new File(filePath);
        if (!(imageFile.exists() || imageFile.isFile())) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                throw new Exception("文件" + filePath + "无法创建.", e);
            }
        }

        HttpGet httpGet = new HttpGet(url);
        setHeader(httpGet,headers);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (ClientProtocolException e1) {
            throw new Exception(e1);
        } catch (IOException e1) {
            throw new Exception(e1);
        }
        boolean success = false;
        // 请求成功
        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
            // 取得请求内容
            HttpEntity entity = response.getEntity();
            // 显示内容
            if (entity != null) {
                // 可以判断是否是文件数据流
                String mimetype1 = EntityUtils.getContentMimeType(entity);
                if (!Strings.isNullOrEmpty(mimetype1)
                        && mimetype1.contains(mimeType)) {
                    FileOutputStream output = null;

                    try {
                        output = new FileOutputStream(imageFile);
                        entity.writeTo(output);
                        output.flush();
                        success = true;
                    } catch (IOException e) {
                        throw new Exception("I/O错误.", e);
                    } finally {
                        if (output != null) {
                            try {
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        if (success) {
            return imageFile;
        }
        return null;
    }

    private void setHeader(HttpGet httpGet, Map headers){
        Iterator it = headers.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            httpGet.setHeader(entry.getKey().toString(),entry.getValue().toString());
        }
    }

    public void setHttpclient(HttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public static List<NameValuePair> convertParams(Map params){
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            NameValuePair pair = new BasicNameValuePair(entry.getKey().toString(),entry.getValue().toString());
            pairs.add(pair);
        }
        return pairs;
    }
}

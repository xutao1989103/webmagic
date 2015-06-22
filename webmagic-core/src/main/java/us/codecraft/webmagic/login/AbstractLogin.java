package us.codecraft.webmagic.login;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.downloader.Downloader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 431 on 2015/6/22.
 */
public abstract class AbstractLogin implements ILogin{

    HttpClient httpClient;
    LoginParams loginParams;

    abstract void prepareLogin();

    @Override
    public void login() {
        prepareLogin();
        try {
            postData(httpClient,loginParams.getLoginUrl(),loginParams.getParams(),loginParams.getHeaders());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String postData(HttpClient httpClient,String url,
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

            HttpResponse response = httpClient.execute(httpPost);
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
}

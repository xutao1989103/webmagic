package us.codecraft.webmagic.login;


import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.entity.Site;
import us.codecraft.webmagic.util.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 431 on 2015/6/21.
 */

public class ZhiHuLogin {

    private Site site;
    private HttpClientUtil clientUtil = new HttpClientUtil();

    public ZhiHuLogin(Site site) {
        this.site = site;
    }

    private String getXSRF() {
        String content = null;
        try {
            content = Optional.fromNullable(clientUtil.getData(site.getLoginUrl())).or("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getParams(content, site.getData());
    }

    private void login()  {
        try {
            clientUtil.postData(site.getLoginUrl(), site.getParams(), site.getHeaders());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String look(String url){
        try {
            return clientUtil.getData(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String getParams(String  source, Map map) {
        Document doc = Jsoup.parse(source);
        String checked = "on";
        Element formContent = Optional.fromNullable(doc.select("form[method=post]").first()).get();
        Element inputContent =  Optional.fromNullable(doc.select("input[type=hidden]").first()).get();
        String actionStr = Optional.fromNullable(formContent.attr("action")).or("");
        String _xsrf = Optional.fromNullable(inputContent.attr("value")).or("");
        return _xsrf;
    }

    private static void printf(Object obj){
        System.out.println(obj);
        System.out.println("=================================================================================");
    }

    public static void main(String[] args) {
        Site site = new Site();
        site.setMainUrl("http://www.zhihu.com");
        site.setLoginUrl("http://www.zhihu.com/login");
        site.setUsername("1025430056@qq.com");
        site.setPassword("205320085");
        site.setUrls(Lists.newArrayList("http://www.zhihu.com","http://www.zhihu.com/question/following"));
        //请求Header
        Map headers = Maps.newHashMap();
        headers.put("(Request-Line)", "POST /login HTTP/1.1");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20100101 Firefox/15.0.1");
        headers.put("Referer", "http://www.zhihu.com/");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept", "*/*");
        headers.put("Connection", "keep-alive");
        headers.put("Host", "www.zhihu.com");
        headers.put("Origin", "http://www.zhihu.com");
        site.setHeaders(headers);
        ZhiHuLogin login = new ZhiHuLogin(site);
        //POST参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("_xsrf", login.getXSRF()));
        nvps.add(new BasicNameValuePair("email", site.getUsername()));
        nvps.add(new BasicNameValuePair("password", site.getPassword()));
        nvps.add(new BasicNameValuePair("rememberme", "y"));
        site.setParams(nvps);
        login.login();
        for(Object str : site.getUrls()){
            String result = login.look(str.toString());
            printf(result);
        }
        login.clientUtil.destroyClient();

    }
}

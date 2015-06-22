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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 431 on 2015/6/21.
 */

public class ZhiHuLogin extends NoIdentifyingCodeLogin {


    public ZhiHuLogin(Site site) {
        site.setMainUrl("http://www.zhihu.com");
        site.setLoginUrl("http://www.zhihu.com/login");
        //请求Header
        Map headers = Maps.newHashMap();
        headers.put("(Request-Line)", "POST /login HTTP/1.1");
        headers.put("Referer", "http://www.zhihu.com/");
        headers.put("Host", "www.zhihu.com");
        headers.put("Origin", "http://www.zhihu.com");
        site.setHeaders(headers);
        this.site = site;
    }

    @Override
    public void prepare() {
        //POST参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("_xsrf", getXSRF()));
        nvps.add(new BasicNameValuePair("email", site.getUsername()));
        nvps.add(new BasicNameValuePair("password", site.getPassword()));
        nvps.add(new BasicNameValuePair("rememberme", "y"));
        site.setParams(nvps);
        super.prepare();
    }

    private String getXSRF() {
        String content = null;
        try {
            content = Optional.fromNullable(httpClientUtil.getData(site.getLoginUrl())).or("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getParams(content, site.getData());
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

    public static void main(String[] args) {
        Site site = new Site();
        site.setUsername("1025430056@qq.com");
        site.setPassword("205320085");
        site.setUrls(Lists.newArrayList("http://www.zhihu.com", "http://www.zhihu.com/question/following"));
        ZhiHuLogin login = new ZhiHuLogin(site);
        login.login();
        for(Object str : site.getUrls()){
            String result = login.search(str.toString());
            printf(result);
        }
        login.close();
    }
}

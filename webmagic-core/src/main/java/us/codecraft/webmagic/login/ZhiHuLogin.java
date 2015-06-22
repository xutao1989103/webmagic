package us.codecraft.webmagic.login;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.utils.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 431 on 2015/6/22.
 */
public class ZhiHuLogin extends NoCodeLogin {

    HttpClientUtil httpClientUtil = new HttpClientUtil();

    public ZhiHuLogin(HttpClient httpClient,LoginParams loginParams) {
        loginParams.setMainUrl("http://www.zhihu.com");
        loginParams.setLoginUrl("http://www.zhihu.com/login");
        //请求Header
        Map headers = Maps.newHashMap();
        headers.put("(Request-Line)", "POST /login HTTP/1.1");
        headers.put("Referer", "http://www.zhihu.com/");
        headers.put("Host", "www.zhihu.com");
        headers.put("Origin", "http://www.zhihu.com");
        loginParams.setHeaders(headers);
        this.loginParams = loginParams;
        this.httpClient = httpClient;
    }

    @Override
    public void prepareLogin() {
        //POST参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("_xsrf", getXSRF()));
        nvps.add(new BasicNameValuePair("email", loginParams.getUsername()));
        nvps.add(new BasicNameValuePair("password", loginParams.getPassword()));
        nvps.add(new BasicNameValuePair("rememberme", "y"));
        loginParams.setParams(nvps);
        super.prepareLogin();
    }
    private String getXSRF() {
        String content = null;
        try {
            content = Optional.fromNullable(httpClientUtil.getData(loginParams.getLoginUrl())).or("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getParams(content, loginParams.getData());
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
}

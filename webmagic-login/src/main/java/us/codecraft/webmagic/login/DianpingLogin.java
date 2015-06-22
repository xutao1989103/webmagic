package us.codecraft.webmagic.login;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import us.codecraft.webmagic.entity.Ruo;
import us.codecraft.webmagic.entity.Site;
import us.codecraft.webmagic.util.RuoKuai;

import java.util.*;

/**
 * Created by 431 on 2015/6/21.
 */
public class DianpingLogin extends WithIdetifyingCodeLogin {

    private final String CODE_IMAGE_URL = "http://www.dianping.com/account/pweb.jpg?xx=0";
    private final String CODE_IMAGE_FORMAT = "png";

    public DianpingLogin(Site site) {
        site.setMainUrl("http://www.dianping.com");
        site.setLoginUrl("http://www.dianping.com/login");
        Map headers = Maps.newHashMap();
        headers.put("(Request-Line)", "POST /login HTTP/1.1");
        headers.put("Referer", "http://www.dianping.com/login?redir=http%3A%2F%2Fwww.dianping.com%2Fcitylist");
        headers.put("Host", "www.dianping.com");
        headers.put("Origin", "http://www.dianping.com");
        site.setHeaders(headers);
        this.site = site;
    }

    @Override
    public void prepare() {
        //请求Header
        Map headers = Maps.newHashMap();
        readCode();
        headers.put("validate", site.getCode());
        headers.put("username", site.getUsername());
        headers.put("password", site.getPassword());
        headers.put("checkCapchaCode","true");
        headers.put("redir", "http://www.dianping.com/citylist");
        headers.put("keepLogin", "on");
        site.setHeaders(headers);
        super.prepare();
    }

    private void readCode() {
        try {
            byte[] image = Optional.fromNullable(httpClientUtil.getFile(CODE_IMAGE_URL, getHeader(), CODE_IMAGE_FORMAT)).or(new byte[0]);
            String c = RuoKuai.httpPostImage(Ruo.getPostImangeUrl(), Ruo.getParams(), image);
            site.setCode(Optional.fromNullable(c.substring(c.indexOf("Result")+9,c.indexOf("Id")-3)).or(""));
            printf(site.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Site site = new Site();
        site.setUsername("15121021687");
        site.setPassword("205320085");
        site.setUrls(Lists.newArrayList("http://www.dianping.com/member/15981850/fans"));
        DianpingLogin login  = new DianpingLogin(site);
        login.login();
        printf(login.search(site.getUrls().get(0).toString()));
        login.close();
    }


    private Map getHeader() {
        Map headers = Maps.newHashMap();
        headers.put("Content-Type", "image/png;charset=utf-8");
        headers.put("Referer","http://www.dianping.com/login?redir=http%3A%2F%2Fwww.dianping.com%2F");
        headers.put("Accept-Encoding", " gzip, deflate, sdch");
        return headers;
    }
}

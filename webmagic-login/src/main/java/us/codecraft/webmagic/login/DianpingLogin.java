package us.codecraft.webmagic.login;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import us.codecraft.webmagic.entity.Site;
import us.codecraft.webmagic.util.RuoKuai;

import java.io.*;
import java.util.*;

/**
 * Created by 431 on 2015/6/21.
 */
public class DianpingLogin extends WithIdetifyingCodeLogin {

    public DianpingLogin(Site site) {
        site.setMainUrl("http://www.dianping.com");
        site.setLoginUrl("http://www.dianping.com/login");
        //请求Header
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
            File file = httpClientUtil.getFile("http://www.dianping.com/account/pweb.jpg?xx=0", setHeader(), "D:/files/pic.png", "png");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            printf("input your validate:");
//            site.setCode(Optional.fromNullable(reader.readLine()).or(""));
            String param = "username=xutao1989103&password=xutao205320085&typeid=2000&softid=40654&softkey=eef2bce102e24f8487375641d29c0483";

            String c = RuoKuai.httpPostImage("http://api.ruokuai.com/create.json", param, getBytes(file));
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


    private Map setHeader() {
        Map headers = Maps.newHashMap();
        headers.put("Content-Type", "image/png;charset=utf-8");
        headers.put("Referer","http://www.dianping.com/login?redir=http%3A%2F%2Fwww.dianping.com%2F");
        headers.put("Accept-Encoding", " gzip, deflate, sdch");
        return headers;
    }
}

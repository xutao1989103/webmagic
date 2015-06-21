package us.codecraft.webmagic.login;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.entity.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 431 on 2015/6/21.
 */
public class TongjiBBSLogin extends NoIdentifyingCodeLogin {
    public TongjiBBSLogin(Site site) {
        site.setMainUrl("http://bbs.tongji.net");
        site.setLoginUrl("http://bbs.tongji.net/member.php?mod=logging&action=login&referer=http%3A%2F%2Fbbs.tongji.net%2Fforum.php%3Fmod%3Dforumdisplay%26fid%3D111%26page%3D1");
        //请求Header
        Map headers = Maps.newHashMap();
        headers.put("(Request-Line)", "POST /login HTTP/1.1");
        headers.put("Referer", "http://bbs.tongji.net/member.php?mod=logging&action=login&referer=http%3A%2F%2Fbbs.tongji.net%2Fforum.php%3Fmod%3Dforumdisplay%26fid%3D111%26page%3D1");
        headers.put("Origin","http://bbs.tongji.net");
        site.setHeaders(headers);
        this.site = site;
    }

    @Override
    public void prepare() {
        //POST参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("formhash", "2fac8488"));
        nvps.add(new BasicNameValuePair("loginfield", "username"));
        nvps.add(new BasicNameValuePair("questionid", "0"));
        nvps.add(new BasicNameValuePair("loginsubmit", "true"));
        nvps.add(new BasicNameValuePair("answer", ""));
        nvps.add(new BasicNameValuePair("username", "xt"));
        nvps.add(new BasicNameValuePair("password", "xutao205320085"));
        nvps.add(new BasicNameValuePair("referer", "http://bbs.tongji.net/forum.phttp://bbs.tongji.net/forum-111-1.htmlhp?mod=forumdisplay&fid=111&page=1"));
        site.setParams(nvps);
        super.prepare();
    }

    public static void main(String[] args){
        Site site = new Site();
        site.setUrls(Lists.newArrayList("http://bbs.tongji.net/forum-111-1.html"));
        ZhiHuLogin login = new ZhiHuLogin(site);
        login.login();
        for(Object str : site.getUrls()){
            String result = login.search(str.toString());
            printf(result);
        }
        login.close();
    }
}

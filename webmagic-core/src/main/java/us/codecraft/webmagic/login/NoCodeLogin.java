package us.codecraft.webmagic.login;

import java.util.Map;

/**
 * Created by 431 on 2015/6/22.
 */
public class NoCodeLogin extends AbstractLogin {
    @Override
    void prepareLogin() {
        check();
        enrichSite(loginParams);
        validate();
    }

    private void check(){
        if(this.loginParams == null) throw new RuntimeException("loginParams can not be null");
    }

    private void enrichSite(LoginParams loginParams){
        Map headers = loginParams.getHeaders();
        headers.put("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept", "*/*");
        headers.put("Connection", "keep-alive");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20100101 Firefox/15.0.1");
        loginParams.setHeaders(headers);
    }

    private void validate(){

    }
}

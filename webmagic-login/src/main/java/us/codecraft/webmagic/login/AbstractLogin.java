package us.codecraft.webmagic.login;

import us.codecraft.webmagic.ILogin;
import us.codecraft.webmagic.entity.Site;
import us.codecraft.webmagic.util.HttpClientUtil;

/**
 * Created by 431 on 2015/6/21.
 */
public abstract class AbstractLogin implements ILogin{

    HttpClientUtil httpClientUtil = new HttpClientUtil();
    Site site;

    private final String EMPTY_RESULT = "";

    public abstract void prepare();

    @Override
    public void login() {
        prepare();
        try {
            httpClientUtil.postData(site.getLoginUrl(), site.getParams(), site.getHeaders());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String search(String url){
        try {
            return httpClientUtil.getData(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EMPTY_RESULT;
    }

    public void close(){
        httpClientUtil.destroyClient();
    }

    static void printf(Object obj){
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println(obj);
    }
}

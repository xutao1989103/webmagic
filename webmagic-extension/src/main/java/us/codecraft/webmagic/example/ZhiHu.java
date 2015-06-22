package us.codecraft.webmagic.example;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.login.LoginParams;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 * Created by 431 on 2015/6/22.
 */
@TargetUrl("http://www.zhihu.com/question/*")
public class ZhiHu {
    @ExtractBy("//h2[@class='zm-item-title zm-editable-content']/text()")
    private String title;

    @ExtractBy("//div[@class=' zm-editable-content clearfix']/text()")
    private List<String> content;

    public static void main(String[] args){
        OOSpider.create(Site.me(), new ConsolePageModelPipeline(), ZhiHu.class)
                .addUrl("http://www.zhihu.com/topic")
                .setLoginParams(new LoginParams("1025430056@qq.com","205320085"))
                .thread(5).start();
    }
}

package us.codecraft.webmagic.example;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 * Created by 431 on 2015/6/4.
 */

@TargetUrl("http://www.douguo.com/cookbook/\\d+")
public class DouGuo {
    @ExtractBy("//title/text()")
    private String title;
    @ExtractBy("//div[@class='recinfo']/h1/text()")
    private String name;
    @ExtractBy("//div[@class='xtip']/text()")
    private String discription;
    @ExtractBy("//td/span")
    private List<String> sources;
    @ExtractBy("//td/span[@class='right']/text()")
    private List<String> amount;
    @ExtractBy(value = "//div[@class='stepcont mll libdm pvl clearfix']/p/text()", multi = true)
    private List<String> steps;
    @ExtractBy("//div[@class='bmayi mbm']/a[@class='cboxElement']/@href")
    private String pic;
    @ExtractBy("//a[@class='btnsp2 gsp']/@href")
    private String video;

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getDiscription() {
        return discription;
    }

    public List<String>getSources() {
        return sources;
    }

    public List<String> getAmount() {
        return amount;
    }

    public List<String> getSteps() {
        return steps;
    }

    public String getPic() {
        return pic;
    }

    public String getVideo() {
        return video;
    }

    public static void main(String[] args){
        OOSpider.create(Site.me(),new ConsolePageModelPipeline(),DouGuo.class)
                .addUrl("http://www.douguo.com/allrecipes/").thread(5).start();
    }
}

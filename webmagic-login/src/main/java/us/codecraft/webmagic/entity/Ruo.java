package us.codecraft.webmagic.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Created by 431 on 2015/6/22.
 */
@Data
@Builder
public class Ruo {
    private static String username = "xutao1989103";
    private static String password = "xutao205320085";
    private static String typeid = "2000";
    private static String softid = "40654";
    private static String softkey = "eef2bce102e24f8487375641d29c0483";

    private static String postImangeUrl = "http://api.ruokuai.com/create.json";

    public static String getParams(){
        return "username="+ username +"&password=" + password +"&typeid=" + typeid + "&softid=" + softid +"&softkey=" +softkey;
    }

    public static String getPostImangeUrl() {
        return postImangeUrl;
    }
}

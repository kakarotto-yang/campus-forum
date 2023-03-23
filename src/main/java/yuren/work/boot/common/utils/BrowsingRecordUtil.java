package yuren.work.boot.common.utils;

import yuren.work.boot.pojo.BrowsingRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

//设置浏览记录工具类
public class BrowsingRecordUtil {
     public static BrowsingRecord getBrowsingRecord(HttpServletRequest request){
         BrowsingRecord browsingRecord =new BrowsingRecord();
         String reqUrl = request.getRequestURI();
         String ip = NetworkUtil.getIpAddress(request);
         browsingRecord.setIp(ip);
         browsingRecord.setReqUrl(reqUrl);
         browsingRecord.setReqDate(new Date());
         return browsingRecord;
     }
}

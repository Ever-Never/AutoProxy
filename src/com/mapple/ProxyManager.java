package com.mapple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.github.droidfu.http.BetterHttp;
import com.github.droidfu.http.BetterHttpRequest;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ProxyManager {
    
    private static final String TAG = "ProxyMapple";
    
    private static String doOcr(Bitmap bitmap) {
        TessBaseAPI baseApi = new TessBaseAPI();

        baseApi.init("/sdcard/", "chi_sim");
        baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789");//限制识别字符范围
        baseApi.setPageSegMode(TessBaseAPI.PSM_SINGLE_LINE);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        baseApi.setImage(bitmap);

        String text = baseApi.getUTF8Text();

        baseApi.clear();
        baseApi.end();

        return text;
    }
    
    public static List<ProxyAddress> requestMimvp() {
        List<ProxyAddress> list = new ArrayList<ProxyAddress>();
        
        String url = "http://proxy.mimvp.com";

        org.jsoup.nodes.Document doc;
        try {
            doc = Jsoup.connect(url).get();
            org.jsoup.select.Elements tbodyList = doc.getElementsByTag("tbody");
            for (org.jsoup.nodes.Element tbody : tbodyList) {
                org.jsoup.select.Elements trList = tbody.getElementsByTag("tr");
                for (org.jsoup.nodes.Element tr : trList) {
                    org.jsoup.select.Elements tdList = tr.getElementsByTag("td");
                    ProxyAddress model = new ProxyAddress();
                    int i = 0;
                    for (org.jsoup.nodes.Element td : tdList) {
                        switch (i) {
                            case 1:
                            {
                                model.setIp(td.text());
                            }
                                break;
                            case 2:
                            {
                                org.jsoup.nodes.Element img = td.getElementsByTag("img").first();
                                if (img != null) {
                                    String src = url + "/" + img.attr("src");
                                    BetterHttpRequest conn = BetterHttp.get(src, "192.168.9.4");
                                    byte[] imgBytes = conn.send().getResponseBodyAsBytes();
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0,
                                            imgBytes.length);
                                    String port = doOcr(bitmap);
                                    model.setPort(Integer.parseInt(port));
                                }
                            }
                                break;
                            case 3:
                            {
                                model.setType(td.text());
                            }
                                break;
                            case 4:
                            {
                                model.setLevel(td.text());
                            }
                                break;
                            case 5:
                            {
                                model.setLocation(td.text());
                            }
                                break;
                            case 8:
                            {
                                model.setTime(td.text());
                            }
                                break;
                            default:
                                break;
                        }
                        i++;
                    }
                    Log.d(TAG, model.toString());
                    if(model.isValid()) {
                        list.add(model);
                    }
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return list;
    }
}

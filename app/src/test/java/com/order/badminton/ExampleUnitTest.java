package com.order.badminton;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {


        BufferedReader reader = new BufferedReader(new FileReader("d://1.txt"));


        HashMap<String, String> hashMap = new HashMap<>();
        String line = reader.readLine();
        while (line != null) {

            line = line.trim();
            String arrs[] = line.split(":");
            hashMap.put(arrs[0].trim(), arrs[1].trim());
            line = reader.readLine();
        }

        String timeSamp="" + System.currentTimeMillis();
        hashMap.put("timestamp", timeSamp);

        String url = "https://accpbet.sporttery.cn/?m=bet&c=jjcBet&d=offline&vId=1700000&cmsWhere=400000&g_token=620118381&useCookie=1";
        String params = "total_money=200&float=-1%2C-1%2C-1&hash=&cms_where=400000&province=44&codes=0%7C%7C%7C%7C%2C1%7C%7C%7C%7C%2C0%7C%7C%7C%7C&qihao=1&loty=jczq&project_desc=%5Bjczq%5D+%E6%9C%9F%E5%8F%B7%EF%BC%9A1&pre_awardmoney=49.80&qihao_id=1&process_act=0&project_title=%5Bjczq%5D+Android%E4%B8%8B%E5%8D%95&beishu=1&channel=ad_ticai&longitude=114.232608&sp=2.39%7C%7C%7C%7C%2C3.45%7C%7C%7C%7C%2C3.02%7C%7C%7C%7C&ids=109028%2C109029%2C109030&danma=&zhushu=1&select=1&platform=1&key_val=" +
                "521230278" +
                timeSamp+"&balance=0&play_id=f_hhgg_f&opentype=0&string=3_1&latitude=22.683367&payPsw=&selType=&v_id=1700000";
                //1532350873436
        String data = httpPost(url, params, hashMap);

        System.out.println(data);


    }


    /**
     * 发送httppost请求
     *
     * @param url
     * @param data 提交的参数为key=value&key1=value1的形式
     * @return
     */
    public static String httpPost(String url, String data, HashMap<String, String> headers) throws Exception {
        String result = null;
        OkHttpClient httpClient = new OkHttpClient();
        //

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        Request request = new Request.Builder().url(url).headers(Headers.of(headers)).post(requestBody).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
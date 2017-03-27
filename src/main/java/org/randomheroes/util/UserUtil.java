package org.randomheroes.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jixu_m on 2017/3/24.
 */
public class UserUtil {

    public static String sendPost(String user_id){
        HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        HttpPost httpPost = new HttpPost("http://202.113.244.44:9012/loginAction.do");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("zjh", "02240130209"));
        params.add(new BasicNameValuePair("mm", "13174452"));
        UrlEncodedFormEntity uefEntity;
        String username = "";
        try {
            uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);
            httpClient.execute(httpPost);
            username = post(httpClient,user_id);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
            return username;
        }
    }

    public static String post(HttpClient httpClient,String user_id) throws IOException {
        HttpPost httppost = new HttpPost("http://202.113.244.44:9012/setReportParams");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("LS_XH", user_id));
        params.add(new BasicNameValuePair("resultPage", "http://202.113.244.44:9012/reportFiles/cj/cj_zwcjd.jsp?"));
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
        httppost.setEntity(uefEntity);
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();
        return get(httpClient, EntityUtils.toString(entity, "UTF-8").split("\"")[1]);
    }

    public static String get(HttpClient httpClient,String url) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
        httpPost.setEntity(uefEntity);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, "UTF-8").split("<td colSpan=2 class=\"report1_2_1\">")[1].split("<")[0];
    }

    public static boolean login(String user_id,String password,String username){
        HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        HttpPost httpPost = new HttpPost("http://202.113.244.44:9012/loginAction.do");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("zjh", user_id));
        params.add(new BasicNameValuePair("mm", password));
        UrlEncodedFormEntity uefEntity;
        boolean flag = false;
        try {
            uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            HttpPost httpPost1 = new HttpPost("http://202.113.244.44:9012"+EntityUtils.toString(entity, "UTF-8").split("src=\"")[1].split("\"")[0]);
            HttpResponse response1 = httpClient.execute(httpPost1);
            HttpEntity entity1 = response1.getEntity();
            if(EntityUtils.toString(entity1, "UTF-8").split("欢迎光临&nbsp;")[1].split("&nbsp;")[0].equals(username)){
                flag = true;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
            return flag;
        }
    }
}

package kz.gcs.rest;

import com.vaadin.server.VaadinSession;
import kz.gcs.domain.User;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CommandRestService {

    private static HttpContext httpContext;
    private static String BASE_URL = "http://104.199.242.112:8082/";

    public static boolean login(String userName, String password) {
        try {
            CookieStore cookieStore = new BasicCookieStore();
            httpContext = new BasicHttpContext();
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost(BASE_URL+"api/session");

            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<>(3);
            params.add(new BasicNameValuePair("email", userName));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("undefined", "false"));
            postRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpClient.execute(postRequest, httpContext);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    //System.out.println(IOUtils.toString(instream));
                } finally {
                    instream.close();
                }
            }

            httpClient.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean sendCommand(String type) {
        try {

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost(BASE_URL+"api/commands");

            User user = VaadinSession.getCurrent().getAttribute(User.class);

            StringEntity params = new StringEntity("{\"deviceId\": "+user.getDeviceId()+",\"type\": \""+type+"\"}");
            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setEntity(params);

            //Execute and get the response.
            HttpResponse response = httpClient.execute(postRequest, httpContext);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    /*System.out.println(entity.getContent());
                    System.out.println(IOUtils.toString(instream));*/

                } finally {
                    instream.close();
                }
            }

            httpClient.close();

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

        return true;
    }



}

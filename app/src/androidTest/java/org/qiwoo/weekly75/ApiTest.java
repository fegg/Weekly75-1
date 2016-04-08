package org.qiwoo.weekly75;

import org.json.JSONException;
import org.json.JSONObject;
import org.qiwoo.weekly75.Weekly75;
import org.qiwoo.weekly75.moduel.Issue;

import java.io.IOException;

/**
 * Created by hujun-iri on 16/3/26.
 *
 */
public class ApiTest {
    public static void main1(String args[]) {
        try {
            Issue issue = Weekly75.getLatestIssue();
            JSONObject js = new JSONObject();
            js.put("test", issue);
            System.out.print(js.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

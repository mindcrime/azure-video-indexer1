package com.metlife.example.ai.azure.video_indexer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import apisamples.TokensStore;
import apisamples.Video;
import apisamples.Account.Account;
import apisamples.HttpUtils.Utils;
import apisamples.authentication.ArmAccessTokenPermission;
import apisamples.authentication.ArmAccessTokenScope;

public class AzureVideoIndexerMain1 
{

    public static final String AzureResourceManager = "https://management.azure.com";
    public static final String SubscriptionId = "2fce2e18-4cc5-48b0-bf65-3154d259b477";
    public static final String ResourceGroup = "AI-Experiments";
    public static final String AccountName = "fgbm-video-indexer1";
    public static final String ApiVersion = "2022-08-01";
    
    public static final String ApiUrl = "https://api.videoindexer.ai";	
    
    //If you want to be notified with POST events to your website
    //The callback URL can contain additional query parameters for example adding the externalId field
    //Or any Custom Field.
    //Example Callback with custom Parameters : https://webhook.site/#!/0000/?externalId=1234&customField=MyCustomField
    // private static final String CallbackUrl = ""; 
    private final Gson gson;
    private Account account = null;
    private TokensStore tokensStore;    
    
    private String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJWZXJzaW9uIjoiMi4wLjAuMCIsIktleVZlcnNpb24iOiI1MTBhNTI4NTRiZjg0MDIwOTZhNDZjN2I0ZWY1NTE4OCIsIkFjY291bnRJZCI6ImNhOGVjMGExLTg5MzgtNDZhNy05MjAxLWY2ZGQ1M2VmNWQ0MSIsIkFjY291bnRUeXBlIjoiQXJtIiwiUGVybWlzc2lvbiI6IkNvbnRyaWJ1dG9yIiwiRXh0ZXJuYWxVc2VySWQiOiJGQTlERjI5NUJGMUI0NUU4QUQ5Q0M5MjBFRkFDRjA3QiIsIlVzZXJUeXBlIjoiTWljcm9zb2Z0Q29ycEFhZCIsIklzc3VlckxvY2F0aW9uIjoiZWFzdHVzIiwibmJmIjoxNzE5NTM5NDM3LCJleHAiOjE3MTk1NDMzMzcsImlzcyI6Imh0dHBzOi8vYXBpLnZpZGVvaW5kZXhlci5haS8iLCJhdWQiOiJodHRwczovL2FwaS52aWRlb2luZGV4ZXIuYWkvIn0.DF1D1kgUsT4z7CIMZWIEuNHfJxlUK_edho8mRa2_E5832hEkv2IraDu-hNxeD8f-H79s5K2XTo8uCKuWSfgT11W6_2mopITCltvNGNnhPPk12oCMJ6DVKTIwhribqpMSVvS2gw47Qej3ADYi-VrfnfHXAmpTHsARfWBgDZ6fv-FWq6cEyAhYXQGjZJuAgaUHjVmVLGOOqFywOzsak7Epa8ZH2n1wIt2WNCOftOko79hOcAdNYl7KuRNcFpXRy6EuNV9L25Iywd8zw76Sji8tVR8p-vcuRcjE6KN7HV0x_9ibcyXqzZfCVw5Jog-hIhCdKXG7X-O8kykkjCN8YoPQhg";
	
	public static void main(String[] args) 
	{
		
		System.out.println("Azure Video Indexer Main1");
	
		AzureVideoIndexerMain1 main = new AzureVideoIndexerMain1(ArmAccessTokenPermission.Contributor, ArmAccessTokenScope.Account);
		
		main.run();
		
		System.out.println( "done" );
	}
	
	
	public void run()
	{
		
		this.account = getAccountInfo();
		
		uploadVideo("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", "video-345-sample");
		
	}

	public AzureVideoIndexerMain1(ArmAccessTokenPermission permission, ArmAccessTokenScope scope)
	{
		gson = new GsonBuilder().setPrettyPrinting().create();
		tokensStore = new TokensStore(permission,scope);
	}
	

	   public Account getAccountInfo() {
	        System.out.println("Getting Account Info ( Location/AccountId)");

	        try {
	            var requestUri = MessageFormat.format("{0}/subscriptions/{1}/resourcegroups/{2}/providers/Microsoft.VideoIndexer/accounts/{3}?api-version={4}",
	                    AzureResourceManager, SubscriptionId, ResourceGroup, AccountName, ApiVersion);
	            
	            
	            System.out.println( "requestUri: " + requestUri);
	            
	            try {
	                HttpRequest request = Utils.httpGetRequestWithBearer(requestUri,  "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik1HTHFqOThWTkxvWGFGZnBKQ0JwZ0I0SmFLcyIsImtpZCI6Ik1HTHFqOThWTkxvWGFGZnBKQ0JwZ0I0SmFLcyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldCIsImlzcyI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0L2QxMWJkMDRlLTA5YzAtNGEwOC1iZmMxLWU5MjI4NjA0YTE0ZC8iLCJpYXQiOjE3MTk1NDM2MzEsIm5iZiI6MTcxOTU0MzYzMSwiZXhwIjoxNzE5NTQ4MTUxLCJhY3IiOiIxIiwiYWlvIjoiQVlRQWUvOFhBQUFBb2NZOTh5UEdhZnhyK2JFbmFkT1dWMUU3OEdZd1dwZEY2RWFqM0R0V1ZHb0xJOWNOSVFWOVpOam40L0crbTh1Nlc3dG9pNkxidVIvOHhkMkFDWU9Vc2hqVWI3NEtudmRHTXNVdVFLL2MyME9BRm1yWXh1QldMbjVmZ2hpRWhyclY3ZXZVRWVCQy9pd1hQb0ZlUmM0UHVZaGhCRmc2Mi9pYmtkU0V1d29tT1NRPSIsImFsdHNlY2lkIjoiMTpsaXZlLmNvbTowMDAzNDAwMUQyQTkyMDYwIiwiYW1yIjpbInB3ZCIsIm1mYSJdLCJhcHBpZCI6IjE4ZmJjYTE2LTIyMjQtNDVmNi04NWIwLWY3YmYyYjM5YjNmMyIsImFwcGlkYWNyIjoiMCIsImVtYWlsIjoicHJob2Rlc0Bmb2diZWFtLmNvbSIsImZhbWlseV9uYW1lIjoicHJob2RlcyIsImdpdmVuX25hbWUiOiJwcmhvZGVzIiwiZ3JvdXBzIjpbIjc4YzM0MTcyLTAzYTAtNDM1ZS04N2NjLTdhYTA2NzgxMmE0MSIsIjk1MGU1YzU1LTdlNGEtNDZmZi1iYzg1LWRlN2YwN2UyZTkyNSJdLCJpZHAiOiJsaXZlLmNvbSIsImlkdHlwIjoidXNlciIsImlwYWRkciI6IjI2MDM6NjA4MDo1OTA1OjhkNDg6ZGQyZDo3MmU4OjQwODY6ZTRlYyIsIm5hbWUiOiJwcmhvZGVzIiwib2lkIjoiZmE5ZGYyOTUtYmYxYi00NWU4LWFkOWMtYzkyMGVmYWNmMDdiIiwicHVpZCI6IjEwMDM3RkZFOTRGREIxNkQiLCJyaCI6IjAuQVJnQVR0QWIwY0FKQ0VxX3dla2loZ1NoVFVaSWYza0F1dGRQdWtQYXdmajJNQk1ZQU9zLiIsInNjcCI6InVzZXJfaW1wZXJzb25hdGlvbiIsInN1YiI6InZvUW9Yd0QtUkExX3djeG8teFZfSjNkRXdhN0dkVE03cnBzZkpnRV91dm8iLCJ0aWQiOiJkMTFiZDA0ZS0wOWMwLTRhMDgtYmZjMS1lOTIyODYwNGExNGQiLCJ1bmlxdWVfbmFtZSI6ImxpdmUuY29tI3ByaG9kZXNAZm9nYmVhbS5jb20iLCJ1dGkiOiJIelBTaF95dUpFcUljUmJSY01ldUFBIiwidmVyIjoiMS4wIiwid2lkcyI6WyI2MmU5MDM5NC02OWY1LTQyMzctOTE5MC0wMTIxNzcxNDVlMTAiLCJiNzlmYmY0ZC0zZWY5LTQ2ODktODE0My03NmIxOTRlODU1MDkiXSwieG1zX2Vkb3YiOnRydWUsInhtc19pZHJlbCI6IjEgMTgiLCJ4bXNfdGNkdCI6MTQ0ODM5NjYzM30.jrcky2SdOMdFVGoJXIUdFdZKOb1fB1M5msWk05Ik6fSjLc_vWFvEt4NpdVHN3fc0Rz3Voi18D10jHILhZn49z9aFlGYbwpTPSiI-JPzvQd_FAfxw1-4ay0I3bdHh8JeyK5xkcyYy0B2sDN72DL-JQUJXMSr-jFymrhNQHBoZ1N_fzGkSwytgxIPJngbwr8kUg0SSgmxhq6XgPvt18-sFkWMPWQ9MTJeS9Z3jp-UKU2adW0AahBEcVq2wrP_BcPmqYJhEWMj3_XGKHo4KxWYLWJkFuJCJlvftil0V-lTsCfqD86V1EGn8vdwMjf38SPvc4luYjDAFRHcyte0cgl7FbQ" /* tokensStore.getArmAccessToken()*/ );
	                var responseBodyJson = Utils.httpStringResponse(request).body();
	                this.account = gson.fromJson(responseBodyJson, Account.class);
	            } catch (URISyntaxException | IOException | InterruptedException ex) {
	                throw new RuntimeException(ex);
	            }
	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }
	        return this.account;
	    }	
	
	
	
	
	/**
     * Uploads a video and starts the video index. Calls the uploadVideo API (<a href="https://api-portal.videoindexer.ai/api-details#api=Operations&operation=Upload-Video">...</a>)
     * 
     * @param videoUrl : the video Url to upload
     * @return Video ID of the video being indexed, otherwise throws exception
     */
    public String uploadVideo(String videoUrl, String videoName) {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJWZXJzaW9uIjoiMi4wLjAuMCIsIktleVZlcnNpb24iOiI1MTBhNTI4NTRiZjg0MDIwOTZhNDZjN2I0ZWY1NTE4OCIsIkFjY291bnRJZCI6ImNhOGVjMGExLTg5MzgtNDZhNy05MjAxLWY2ZGQ1M2VmNWQ0MSIsIkFjY291bnRUeXBlIjoiQXJtIiwiUGVybWlzc2lvbiI6IkNvbnRyaWJ1dG9yIiwiRXh0ZXJuYWxVc2VySWQiOiJGQTlERjI5NUJGMUI0NUU4QUQ5Q0M5MjBFRkFDRjA3QiIsIlVzZXJUeXBlIjoiTWljcm9zb2Z0Q29ycEFhZCIsIklzc3VlckxvY2F0aW9uIjoiZWFzdHVzIiwibmJmIjoxNzE5NTQxMDk3LCJleHAiOjE3MTk1NDQ5OTcsImlzcyI6Imh0dHBzOi8vYXBpLnZpZGVvaW5kZXhlci5haS8iLCJhdWQiOiJodHRwczovL2FwaS52aWRlb2luZGV4ZXIuYWkvIn0.vmdlvWu_ZqbPg41FLyQsmTEqHZbmciC4xBUOTGHqFVCvMZ6XfEVayjd_toT5tkuk-fBOC3VZEk-EtddE5xN3jO13y3gPwccYOvh-QMEliYaMONXVeCkZQurmG5SPCBCM-Ifhz25lnYgYuGEj60smFSV_mRzesD6m5NUV8jzvwXmbD9B2F6zKEqONer4RiBYqJeL4Uj6EXGwtHIxatPd27mCkqJ94VPLcjRLKYc_8wm8pEU6qV1JjCINoBRQEkjcfwkGo1mqusQmOnAW5iSNk8fMf1Kz5dLDG86fPxOCKpTix-lrDZr_QrAuYy_55hkfYLc9RxeZcVfNQBAHA2jy5fA" );
        map.put("name", videoName);
        map.put("description", "video_description");
        map.put("privacy", "private");
        map.put("partition", "partition");
        map.put("videoUrl", videoUrl);
        // For API Based Scenarios it is advised to set "NoStream" for faster indexing. 
        map.put("streamingPreset","NoStreaming");
        //Retention Period of Video in days. Default is No retention. Max Allowed value is 7.
        map.put("retentionPeriod","1");
        //Add externalId field in order to eventually  this is useful for external correlation Ids.
        //the field will then be present on the event hub processor.
        map.put("externalId", UUID.randomUUID().toString());
        
        
        // Use Callback URL to get notified on Video Indexing Events ( Start/ End Processing)
        /* 
        if (!CallbackUrl.isBlank()) {
            map.put("callbackUrl", URLEncoder.encode(CallbackUrl, StandardCharsets.UTF_8));
        }
	    */
        
        var queryParam = Utils.toQueryParamString(map);

        var requestUri = MessageFormat.format("{0}/{1}/Accounts/{2}/Videos?{3}", ApiUrl, account.location, account.properties.accountId, queryParam);

        System.out.println( "requestUri: " + requestUri);
        
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(requestUri))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();

            var response = Utils.httpStringResponse(request);
            Video upoloadedVideo = gson.fromJson(response.body(), Video.class);

            String videoId = upoloadedVideo.id;
            System.out.println(MessageFormat.format("Video ID {0} was uploaded successfully", videoId));
            return videoId;
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }	
	
	
	
	
	
	
	
	

}

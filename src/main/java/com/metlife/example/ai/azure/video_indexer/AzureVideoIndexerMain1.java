package com.metlife.example.ai.azure.video_indexer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;


import apisamples.Account.Account;
import apisamples.HttpUtils.Utils;
import apisamples.authentication.ArmAccessTokenPermission;
import apisamples.authentication.ArmAccessTokenScope;


public class AzureVideoIndexerMain1 
{

    public static final String AzureResourceManager = "https://management.azure.com";
    public static final String SubscriptionId = "<Your_Subscription_Id_here>";
    public static final String ResourceGroup = "<Your_Resource_Group_here>";
    public static final String AccountName = "<Your_Account_Name_Here>";
    public static final String ApiVersion = "2022-08-01";
    public static final String ApiUrl = "https://api.videoindexer.ai";	
	
    
    //If you want to be notified with POST events to your website
    //The callback URL can contain additional query parameters for example adding the externalId field
    //Or any Custom Field.
    //Example Callback with custom Parameters : https://webhook.site/#!/0000/?externalId=1234&customField=MyCustomField
    private static final String CallbackUrl = ""; 
    private final Gson gson;
    private Account account = null;
    private TokensStore tokensStore ;    
    
	
	public static void main(String[] args) 
	{
		
		System.out.println("Azure Video Indexer Main1");
	}
	
	/**
     * Uploads a video and starts the video index. Calls the uploadVideo API (<a href="https://api-portal.videoindexer.ai/api-details#api=Operations&operation=Upload-Video">...</a>)
     * 
     * @param videoUrl : the video Url to upload
     * @return Video ID of the video being indexed, otherwise throws exception
     */
    public String uploadVideo(String videoUrl, String videoName) {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", tokensStore.getVIAccessToken());
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
        map.put("externalId", randomUUID().toString());
        // Use Callback URL to get notified on Video Indexing Events ( Start/ End Processing)
        if (!CallbackUrl.isBlank()) {
            map.put("callbackUrl", URLEncoder.encode(CallbackUrl, StandardCharsets.UTF_8));
        }

        var queryParam = Utils.toQueryParamString(map);

        var requestUri = MessageFormat.format("{0}/{1}/Accounts/{2}/Videos?{3}", ApiUrl, account.location, account.properties.accountId, queryParam);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(requestUri))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();

            var response = httpStringResponse(request);
            Video upoloadedVideo = gson.fromJson(response.body(), Video.class);

            String videoId = upoloadedVideo.id;
            System.out.println(MessageFormat.format("Video ID {0} was uploaded successfully", videoId));
            return videoId;
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }	
	
	
	
	
	
	
	
	

}

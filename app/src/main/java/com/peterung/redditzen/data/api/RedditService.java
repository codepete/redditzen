package com.peterung.redditzen.data.api;

import com.peterung.redditzen.data.api.model.Account;
import com.peterung.redditzen.data.api.model.AuthTokenResponse;
import com.peterung.redditzen.data.api.model.Listing;
import com.peterung.redditzen.data.api.model.MoreChildrenResponse;
import com.peterung.redditzen.data.api.model.RedditResponse;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RedditService {
    String CLIENT_ID = "vjuvvYyO2qrYMA";
    String DEFAULT_SCOPE = "identity edit flair history mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread";
    String DEFAULT_RESPONSE_TYPE = "code";
    String DEFAULT_REDIRECT_URI = "redditzen://authRedirect";
    String DEFAULT_DURATION = "permanent";
    String DEFAULT_GRANT_TYPE = "authorization_code";

    @GET("/")
    Observable<RedditResponse<Listing>> getFrontpage(@Query("sort") String sort,
                                                     @Query("limit") int limit,
                                                     @Query("after") String after);

    @GET("/r/{subreddit}")
    Observable<RedditResponse<Listing>> getPosts(@Path("subreddit") String subreddit,
                                                 @Query("sort") String sort,
                                                 @Query("limit") int limit,
                                                 @Query("after") String after);

    @GET("/r/{subreddit}/comments/{postId}")
    Observable<List<RedditResponse<Listing>>> getComments(@Path("subreddit") String subreddit,
                                                          @Path("postId") String postId,
                                                          @Query("sort") String sort);

    @GET("/api/morechildren")
    Observable<MoreChildrenResponse> getMoreChildren(@Query("link_id") String linkId,
                                                     @Query("children") String children,
                                                     @Query("api_type") String apiType,
                                                     @Query("id") String id,
                                                     @Query("sort") String sort);

    @GET("/subreddits/search")
    Observable<RedditResponse<Listing>> searchSubreddits(@Query("q") String query,
                                                         @Query("sort") String sort,
                                                         @Query("after") String after);


    @FormUrlEncoded
    @POST("/api/v1/access_token")
    Observable<AuthTokenResponse> getToken(@Field("code") String code,
                                           @Field("grant_type") String grantType,
                                           @Field("redirect_uri") String redirectUri);

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    Observable<AuthTokenResponse> refreshToken(@Field("refresh_token") String refreshToken,
                                               @Field("grant_type") String grantType);

    @GET("/api/v1/me")
    Observable<Account> getAccount();

    @GET("/subreddits/mine/subscriber?limit=100")
    Observable<RedditResponse<Listing>> getSubcribedSubreddits(
            @Query("limit") int limit,
            @Query("after") String after);

    @FormUrlEncoded
    @POST("/api/vote")
    Observable<Void> vote(@Field("id") String id, @Field("dir") int dir);

    @FormUrlEncoded
    @POST("/api/save")
    Observable<Void> save(@Field("id") String id, @Field("category") String category);

    @FormUrlEncoded
    @POST("/api/unsave")
    Observable<Void> unsave(@Field("id") String id, @Field("category") String category);

    @GET("/message/inbox")
    Observable<RedditResponse<Listing>> getMessages(@Query("limit") int limit,
                                                    @Query("after") String after);

    @FormUrlEncoded
    @POST("/api/read_message")
    Observable<Void> readMessage(@Field("id") String messageId);
}


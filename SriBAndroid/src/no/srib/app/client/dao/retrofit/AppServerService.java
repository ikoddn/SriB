package no.srib.app.client.dao.retrofit;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface AppServerService {

	@GET("/articles")
	Response getArticles(@Query("q") String query);

	@GET("/podcast/programs")
	Response getPodcastPrograms();
}

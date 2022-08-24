import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.json.JSONObject;
//HttpClient client = HttpClient.newHttpClient();
import main.App;

public class rslok {

	public static void main(String[] args) throws Exception {
		HttpResponse<String> msg = new rslok().getSlok();
		System.out.println(msg.toString());
	}

	public HttpResponse<String> getSlok() throws Exception {
		HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS)
				.connectTimeout(Duration.ofSeconds(10)) // follow redirects
				.build();

		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("https://bhagavadgitaapi.in/slok/")).build();

		HttpResponse<String> msg = client.send(request, HttpResponse.BodyHandlers.ofString());
//		String msg = parse(req);
		return msg;
	}

	public static String parse(String responseBody) {
		JSONObject obj = new JSONObject(responseBody);
//		String id = obj.getString("_id");
		int chapter = obj.getInt("chapter");
		int verse = obj.getInt("verse");
		String slok = obj.getString("slok");
		String hindi = obj.getJSONObject("chinmay").getString("hc");
		String english = obj.getJSONObject("san").getString("et");
		String Final = "Here is Bhagwat Geeta Slok you Requested: " + "\n\nChapter: " + chapter + "  Verse: " + verse
				+ "\n\nSlok: " + slok + "\n\nHindi Translation: " + hindi + "\n\nEnglish Translation: " + english
				+ "\n\n                 Hare Krishna!!";
		return Final;

	}

}

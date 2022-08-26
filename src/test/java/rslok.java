import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class rslok {

	public static void main(String[] args) throws Exception {
		String msg = new rslok().getSlok();
		System.out.println(msg);
	}

//	function gitaslokid() {
//        const slokcount = [47, 72, 43, 42, 29, 47, 30, 28, 34, 42, 55, 20, 35, 27, 20, 24, 28, 78]
//        const chapter = Math.floor(Math.random() * 17) + 1
//        const slok = Math.floor(Math.random() * slokcount[chapter - 1]) + 1
//        return `https://bhagavadgitaapi.in/slok/${chapter}/${slok}/`
//    }

	public String getSlok() {
		HttpClient client = HttpClient.newHttpClient();
		int[] slokcount = { 47, 72, 43, 42, 29, 47, 30, 28, 34, 42, 55, 20, 35, 27, 20, 24, 28, 78 };
		int ch = (int) Math.floor(Math.random() * 17) + 1;
		int sl = (int) Math.floor(Math.random() * slokcount[(int) (ch - 1)]) + 1;
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create("https://bhagavadgitaapi.in/slok/" + ch + "/" + sl + "/")).build();

		String msg = client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.thenApply(rslok::parse).join();
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

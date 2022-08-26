package vidurBot;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class App extends TelegramLongPollingBot {

	@Override
	public void onUpdateReceived(Update update) {
		String command = update.getMessage().getText();
		SendMessage msg = new SendMessage();
		msg.setChatId(setChat(update));
		System.out.println("runLog: | user: " + update.getMessage().getFrom().getFirstName() + " | command: "
				+ update.getMessage().getText());
		if (command.equalsIgnoreCase("/start")) {
			String messageStr = "Kaise ho " + update.getMessage().getFrom().getFirstName() + " "
					+ update.getMessage().getFrom().getLastName() + "!!";
			msg.setText(messageStr);
			try {
				execute(msg);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e);
			}
		} else if (command.equalsIgnoreCase("dog")) {
			try {
				sendDogImage(update);
			} catch (Exception e) {
				System.out.println("Exception occurred");
			}
		} else if (command.equalsIgnoreCase("joke")) {
			try {
				msg.setText(sendJoke(update));
				execute(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (command.toLowerCase().contains("gslok")) {
			try {
				String ch = command.split(" ")[1];
				String sl = command.split(" ")[2];
				msg.setText(getSlok(ch, sl));
				execute(msg);
			} catch (Exception e) {
				e.printStackTrace();

				try {
					msg.setText("Your Request Couldn't be FullFilled");
					execute(msg);
				} catch (TelegramApiException e1) {

				}

			}
		} else if (command.toLowerCase().contains("rslok")) {
			try {
				msg.setText(getSlok());
				execute(msg);
			} catch (TelegramApiException e) {
				e.printStackTrace();
				try {
					msg.setText("Your Request Couldn't be FullFilled");
					execute(msg);
				} catch (TelegramApiException e1) {

				}
			}
		} else {
//			System.out.println(update.getMessage().getText());
			String msg2 = "Invalid Command!!";
			msg.setText(msg2);
			try {
				execute(msg);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	String setChat(Update update) {
		String chatid = update.getMessage().getChatId().toString();
		return chatid;
	}

	public void sendDogImage(Update update) {
		URL req;
		try {
			req = new URL("https://random.dog/woof.json");
			URLConnection con = req.openConnection();
			Scanner scn = new Scanner(new InputStreamReader(con.getInputStream()));
			String inputLine = scn.nextLine();
			String arr[] = inputLine.split("\"");
			SendPhoto sendPhotoRequest = new SendPhoto();
			sendPhotoRequest.setChatId(setChat(update));
			// Set the photo url as a simple photo
			sendPhotoRequest.setPhoto(new InputFile(arr[5]));
			execute(sendPhotoRequest);
			scn.close();
		} catch (TelegramApiException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String sendJoke(Update update) throws IOException {
		URL req = new URL("https://v2.jokeapi.dev/joke/Any?format=txt&type=single");
		URLConnection con = req.openConnection();
		Scanner scn = new Scanner(new InputStreamReader(con.getInputStream()));
		String inputLine = scn.nextLine();
//		System.out.println(inputLine);
		scn.close();
		return inputLine;
	}

	public String getSlok() {
		HttpClient client = HttpClient.newHttpClient();
		int[] slokcount = { 47, 72, 43, 42, 29, 47, 30, 28, 34, 42, 55, 20, 35, 27, 20, 24, 28, 78 };
		int ch = (int) Math.floor(Math.random() * 17) + 1;
		int sl = (int) Math.floor(Math.random() * slokcount[(int) (ch - 1)]) + 1;
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create("https://bhagavadgitaapi.in/slok/" + ch + "/" + sl + "/")).build();

		String msg = client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.thenApply(App::parse).join();
		return msg;
	}

	public String getSlok(String ch, String sl) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create("https://bhagavadgitaapi.in/slok/" + ch + "/" + sl + "/")).build();

		String msg = client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.thenApply(App::parse).join();
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

	@Override
	public String getBotUsername() {
		// TODO something
		return "Akroor_Bot";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "1635655061:AAFThYmSJPNzhk21N1vGeMagw_noapjwr9E";
	}

}

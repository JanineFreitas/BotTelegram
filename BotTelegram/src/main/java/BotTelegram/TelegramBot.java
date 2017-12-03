package BotTelegram;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

public class TelegramBot {
	private final String endpoint = "https://api.telegram.org/";
	private final String token;
	private String menu;

	public TelegramBot(String token) {
		this.token = token;
	}

	public HttpResponse<JsonNode> sendMessage(Integer chatId, String text) throws UnirestException {
		return Unirest.post(endpoint + "bot" + token + "/sendMessage").field("chat_id", chatId).field("text", text)
				.asJson();
	}

	public HttpResponse<JsonNode> getUpdates(Integer offset) throws UnirestException {
		return Unirest.post(endpoint + "bot" + token + "/getUpdates").field("offset", offset).asJson();
	}

	public void run() throws UnirestException {
		int last_update_id = 0; // controle das mensagens processadas
		HttpResponse<JsonNode> response;
		while (true) {
			response = getUpdates(last_update_id++);
			if (response.getStatus() == 200) {
				JSONArray responses = response.getBody().getObject().getJSONArray("result");
				if (responses.isNull(0)) {
					continue;
				} else {
					last_update_id = responses.getJSONObject(responses.length() - 1).getInt("update_id") + 1;
				}

				for (int i = 0; i < responses.length(); i++) {
					JSONObject message = responses.getJSONObject(i).getJSONObject("message");
					int chat_id = message.getJSONObject("chat").getInt("id");
					String usuario = message.getJSONObject("chat").getString("first_name");
					menu = message.getString("text");
					
					if(menu.equals("/inverter")) {
						sendMessage(chat_id, "Sr(a)"+usuario+" digite o texto que deseja inverter.");
						
						response = getUpdates(last_update_id++);
						JSONArray responses2 = response.getBody().getObject().getJSONArray("result");
						
						tempoResposta();
						
						sendMessage(chat_id, inverterTexto(responses2.getJSONObject(0).getJSONObject("message")));
					} else if(menu.equals("/help")) {
						sendMessage(chat_id, "Escolha uma opção: /inverter");
					} else {
						sendMessage(chat_id, "Sr(a)"+usuario+" opção inválida.");
					}
				}
			}
		}
	}
	
	private void tempoResposta() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String inverterTexto(JSONObject message) {
		String texto = message.getString("text");
		String textoInvertido = "";

		for (int j = texto.length() - 1; j >= 0; j--) {
			textoInvertido += texto.charAt(j);
		}
		return textoInvertido;
	}
}

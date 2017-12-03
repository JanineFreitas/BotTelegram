package BotTelegram;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	public static void main(String[] args) {
		//Chave de segurança
		TelegramBot tb = new TelegramBot("450525790:AAHPGfellUtL9Nj49XOhaRUqPk7sbsezm4o");
        try {
            tb.run();
        } catch (UnirestException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}

package discord.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.Random;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getAuthor().isBot()) return;
        
        String denchickId = "469452560529358850";
        String myId = "648774699656019969";
        Random rnd = new Random();
        ArrayList<String> denchiks = new ArrayList<>();
        denchiks.add("images/denchik.png");
        denchiks.add("images/denchik1.png");
        denchiks.add("images/denchik2.png");
        denchiks.add("images/denchik3.png");

        String content = event.getMessage().getContentRaw().toLowerCase(); //получаем текст сообщения
        if(content.equals("!ping")){
            event.getChannel().sendMessage("Заебись бро, пошел ты нахуй").queue(); //команда для отклика бота
        }
        //еще команда 
        if(content.contains("привет") || content.contains("здарова")){
            event.getChannel().sendMessage("Привет долбаеб по имени " + event.getAuthor().getName()).queue(null, Throwable::printStackTrace);
        }
        if(content.contains("ден") || content.contains("den") || content.contains("без д")){
            event.getMessage().addReaction(Emoji.fromUnicode("🐖")).queue();
            event.getChannel().sendMessage("Алёу шеф, вас вызывают " + "<@" + "469452560529358850" + ">" ).queue();
        }
        if(content.contains("иди нахуй")){
            event.getChannel().sendMessage("Ты ко мне в машину не садишься чмо").queue();
        }
        if(content.contains("охрана труда") || content.contains("rock n roll") || content.contains("фулл")){
            String picked = denchiks.get(rnd.nextInt(denchiks.size()));
            File file = new File(picked);
            if(file.exists()){
                event.getMessage().replyFiles(FileUpload.fromData(file)).queue();
            }
            else{
                event.getMessage().reply("Кто бы что не говорил, но денчик реально красавчик").queue();
            }
        }

        //генериррование ответа с поомощью модели я пьяный если чо
        if(event.getAuthor().getId().equals(myId)){
            try{
                String response = sendPromptToModel(content);
                event.getMessage().reply(response).queue();
            }
            catch (Exception e){
                event.getChannel().sendMessage("Ошибка боб").queue();
            }
        }

    }

    private String sendPromptToModel(String prompt) throws IOException {
        URL url = new URL("http://localhost:5000/generate");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
    
        int words = Math.min(20, prompt.split("\\s+").length);
            JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("prompt", prompt);
        jsonRequest.put("words", words);
    
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonRequest.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getString("response");
    }
    
}

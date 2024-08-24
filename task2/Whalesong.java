import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Whalesong {
    private static Map<String, String> whalesongs = new HashMap<String, String>();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()){
            String Userinput = sc.nextLine().trim();
            String[] temp = Userinput.split(" ", 3);
            String userinput0 = temp[0].toLowerCase();
            String userinput1 = temp.length > 1 ? temp[1] : null;
            String userinput2 = temp.length > 2 ? temp[2] : null;
            switch(userinput0){
                case "add":
                    if(userinput1 != null && userinput2 != null){
                        add(userinput1, userinput2);
                    }else {
                        System.out.println("Invalid user input");
                    }
                    break;
                case "play":
                    if(userinput1 != null){
                        play(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "hear":
                    if(userinput1 != null){
                        hear(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "augment":
                    if(userinput1 != null){
                        augment(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "clarify":
                    if(userinput1 != null){
                        clarify(userinput1);
                    }
                    break;
                case "normalise":
                    if(userinput1 != null){
                        normalise(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "reverse":
                    if(userinput1 != null){
                        reverse(userinput1);
                    }else {
                        System.out.println("Invalid user input");
                    }
                    break;
                case "mirror":
                    if(userinput1 != null){
                        mirror(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "uppercase":
                    if(userinput1 != null){
                        uppercase(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "pattern":
                    if(userinput1 != null){
                        pattern(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "echo":
                    if(userinput1 != null){
                        echo(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "compress":
                    if(userinput1 != null){
                        compress(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "expand":
                    if(userinput1 != null){
                        expand(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "invert":
                    if(userinput1 != null){
                        invert(userinput1);
                    }else{
                        System.out.println("Invalid user input");
                    }
                    break;
                case "exit":
                    exit();
                    sc.close();
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;

            }



        }

    }

    public static void add(String whale, String song){
        if(whalesongs.containsKey(whale)){
            System.out.println("Error: Whale already has a song.");
        }else{
            whalesongs.put(whale,song);
            System.out.println("Song added for " + whale);
        }

    }
    public static void play(String whale){
        if(!whalesongs.containsKey(whale)){
            System.out.println("Error: No song found for this whale.");
        }else{
            System.out.println(whalesongs.get(whale));
        }
    }
    public static void hear(String whale){
        if(!whalesongs.containsKey(whale)){
            System.out.println("Error: No song found for this whale.");
        }else{
            System.out.printf("Song for %s: %s%n",whale,whalesongs.get(whale));
        }

    }
    public static void augment(String whale){
        if(!whalesongs.containsKey(whale)){
            System.out.println("Error: No song to augment.");
        }else{
            String songs = whalesongs.get(whale);
            whalesongs.put(whale, songs+songs);

            System.out.printf("Augmented song: %s%n", whalesongs.get(whale));
        }
    }
    public static String vowelsTransfer(String str){
        String vowels = "aeiouAEIOU";
        StringBuilder finalresult = new StringBuilder();
        for(int i = 0; i<str.length(); i++){
            char c = str.charAt(i);
            if(vowels.indexOf(c) == -1){
                finalresult.append(c);
                continue;
            }else{
                finalresult.append(Character.toUpperCase(c));
            }
        }
        return finalresult.toString();
    }
    public static void clarify(String whale){
        if(!whalesongs.containsKey(whale)){
            System.out.println("Error: No song to clarify.");
        }else{
            String songs = whalesongs.get(whale);
            String clarifiedsongs = vowelsTransfer(songs);
            whalesongs.put(whale,clarifiedsongs);
            System.out.printf("Clarified song: %s%n", clarifiedsongs);

        }
    }

    public static void normalise(String whale){
        if(!whalesongs.containsKey(whale)){
            System.out.println("Error: No song to normalise.");
        }else{
            String song = whalesongs.get(whale);
            song = song.toLowerCase();
            whalesongs.put(whale, song);
            System.out.printf("Normalised song: %s%n", song);
        }
    }

    public static void reverse(String whale){
        if(!whalesongs.containsKey(whale)){
            System.out.println("Error: No song to reverse.");
        }else {
            String song = whalesongs.get(whale);

            StringBuilder finalresult = new StringBuilder();
            for(int i = song.length()-1; i >= 0; i--){
                finalresult.append(song.charAt(i));
            }
            whalesongs.put(whale,finalresult.toString());
            System.out.printf("Reversed song: %s%n", finalresult);
        }
    }

    public static void mirror(String whale){
        if(!whalesongs.containsKey(whale)){
            System.out.println("Error: No song to mirror.");
        }else{
            StringBuilder songs = new StringBuilder();
            String song = whalesongs.get(whale);
            songs.append(song);

            for(int i = song.length()-1; i >= 0; i--){
                songs.append(song.charAt(i));
            }
            whalesongs.put(whale,songs.toString());
            System.out.printf("Mirrored song: %s%n", songs);
        }
    }

    public static void uppercase(String whale){
        if(!whalesongs.containsKey(whale)) {
            System.out.println("Error: No song to uppercase.");
        }else{
            String song = whalesongs.get(whale);
            String finalresult = song.toUpperCase();
            whalesongs.put(whale,finalresult);
            System.out.printf("Uppercase song: %s%n", finalresult);
        }
    }

    public static void pattern(String whale){
        if(!whalesongs.containsKey(whale)){
            System.out.println("Error: No song to pattern.");
        }else{
            String song = whalesongs.get(whale);
            StringBuilder finalresult = new StringBuilder();
            for(int i = 0 ; i < song.length(); i++){
                if(i%2!=0) {
                    finalresult.append("*");
                }else{
                    finalresult.append(song.charAt(i));
                }
            }
            whalesongs.put(whale,finalresult.toString());
            System.out.printf("Patterned song: %s%n", finalresult);
        }
    }

    public static void echo(String whale){
        if(!whalesongs.containsKey(whale)) {
            System.out.println("Error: No song to echo.");
            return;
        }else{
            String song = whalesongs.get(whale);
            StringBuilder finalresult = new StringBuilder();
            finalresult.append(song);
            String temp = "";
            if(song.length()<3){
                System.out.println("Error: No song to echo.");
                return;
            }else{
                for(int i = song.length()-3 ; i < song.length(); i++){
                    temp += song.charAt(i);
                }
                for(int i = 1 ; i <= 5; i++){
                    finalresult.append(temp);
                }
                whalesongs.put(whale,finalresult.toString());
                System.out.printf("Echoed song: %s%n", finalresult);
            }

        }
    }

    public static void compress(String whale){
        if(!whalesongs.containsKey(whale)) {
            System.out.println("Error: No song to compress.");
            return;
        }else{
            String song = whalesongs.get(whale);
            StringBuilder finalresult = new StringBuilder();
            String vowels = "aeiouAEIOU";
            for(int i = 0; i < song.length(); i++){
                char c = song.charAt(i);
                if(vowels.indexOf(c) == -1){
                    finalresult.append(c);
                }else{
                    continue;
                }
            }
            whalesongs.put(whale,finalresult.toString());
            System.out.printf("Compressed song: %s%n", finalresult);
        }
    }

    public static void expand(String whale){
        if(!whalesongs.containsKey(whale)) {
            System.out.println("Error: No song to expand.");
        }else{
            String song = whalesongs.get(whale);
            StringBuilder finalresult = new StringBuilder();
            String vowels = "aeiouAEIOU";
            for(int i = 0; i < song.length(); i++){
                char c = song.charAt(i);
                if(vowels.indexOf(c) == -1){
                    finalresult.append(c).append(c);
                }else{
                    finalresult.append(c);
                }
            }
            whalesongs.put(whale,finalresult.toString());
            System.out.printf("Expanded song: %s%n", finalresult);
        }
    }

    public static void invert(String whale){
        if(!whalesongs.containsKey(whale)) {
            System.out.println("Error: No song to invert.");
        }else {
            String song = whalesongs.get(whale);
            StringBuilder finalresult = new StringBuilder();
            for(int i = 0; i < song.length(); i++){
                char c = song.charAt(i);
                if(Character.isUpperCase(c)){
                    finalresult.append(Character.toLowerCase(c));
                }else {
                    finalresult.append(Character.toUpperCase(c));
                }
            }
            whalesongs.put(whale,finalresult.toString());
            System.out.printf("Inverted song: %s%n", finalresult);

        }

    }

    public static void exit(){
        System.exit(0);
    }









}

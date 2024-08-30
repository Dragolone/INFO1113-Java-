
import java.io.*;
import java.util.*;


public class Skibidi {

    public static void main(String[] args) {
        String File1 = args[0];
        String File2 = args[1];
        File test1 = new File(File1);
        File test2 = new File(File2);
        Map<String, Integer> characters1 = new HashMap<>();
        Map<String, Integer> characters2 = new HashMap<>();
        Map<String, Integer> abilities1 = new HashMap<>();
        Map<String, Integer> abilities2 = new HashMap<>();
        boolean flag = true;
        ArrayList<String > ability = new ArrayList<String>(Arrays.asList("powersurge", "bopshield", "energyboost", "zaptrap", "titanboost"));

        if(test1.exists() && !test2.exists()){
            System.out.printf("Error: File '%s' not found. Please check the path and try again.%n", File2);
            return;
        }
        if (!test1.exists() && test2.exists()) {
            System.out.printf("Error: File '%s' not found. Please check the path and try again.%n", File1);
            return;
        }

        try{BufferedReader alliance = new BufferedReader(new FileReader(File1));
            BufferedReader skibidi = new BufferedReader(new FileReader(File2));
            String line;
            while ((line = alliance.readLine()) != null) {
                String[] words = line.split(" ");
                if(words.length == 2 && !ability.contains(words[0].toLowerCase()) && flag){
                    try{
                        int percentage = Integer.parseInt(words[1]);
                        characters1.put(words[0], percentage);
                    }catch (NumberFormatException e){
                        System.out.printf("Error: Invalid parameter for '%s' in line '%s'. Line skipped.%n", words[0], line);
                    }
                } else if (words.length == 2 && ability.contains(words[0].toLowerCase())) {
                    try{
                        int percentage = Integer.parseInt(words[1]);
                        abilities1.put(words[0], percentage);
                        flag = false;
                    }catch (NumberFormatException e){
                        System.out.printf("Error: Invalid parameter for '%s' in line '%s'. Line skipped.%n", words[0], line);
                    }

                } else if(words.length == 2){
                    System.out.printf("Warning: Unknown ability '%s' detected in '%s'. Ignored.%n", words[0], File1);
                }
                else{
                    System.out.printf("Error: No valid data found in '%s'. Battle cannot proceed.%n", File1);
                    return;
                }
            }
            flag = true;
            while ((line = skibidi.readLine()) != null) {
                String[] words = line.split(" ");
                if(words.length == 2 && !ability.contains(words[0].toLowerCase()) && flag){
                    try{
                        int percentage = Integer.parseInt(words[1]);
                        characters2.put(words[0], percentage);
                    }catch (NumberFormatException e){
                        System.out.printf("Error: Invalid parameter for '%s' in line '%s'. Line skipped.%n", words[0], line);
                    }
                } else if (words.length == 2 && ability.contains(words[0].toLowerCase())) {
                    try{
                        int percentage = Integer.parseInt(words[1]);
                        abilities2.put(words[0], percentage);
                        flag = false;
                    }catch (NumberFormatException e){
                        System.out.printf("Error: Invalid parameter for '%s' in line '%s'. Line skipped.%n", words[0], line);
                    }

                }else if(words.length == 2){
                    System.out.printf("Warning: Unknown ability '%s' detected in '%s'. Ignored.%n", words[0], File2);
                }
                else{
                    System.out.printf("Error: No valid data found in '%s'. Battle cannot proceed.%n", File2);
                    return;
                }
            }
        }catch(IOException e){
            System.out.println("Error reading files.");
        }

        int skibidiScore = calculateTotalPower(characters2, abilities2);
        int allianceScore = calculateTotalPower(characters1, abilities1);

        if(abilities1.containsKey("zaptrap")){
            skibidiScore -= skibidiScore * abilities1.get("zaptrap")/100;
        }
        if(abilities2.containsKey("zaptrap")){
            allianceScore -= allianceScore * abilities2.get("zaptrap")/100;
        }
        if (allianceScore > skibidiScore){
            System.out.println("Battle result: Alliance wins");
        }else if (allianceScore < skibidiScore){
            System.out.println("Battle result: Skibidi wins");
        }else {
            System.out.println("Battle result: Draw");
        }
    }

    private static int calculateTotalPower(Map<String, Integer> characters, Map<String, Integer> abilities){
        for(String ability : abilities.keySet()){
            int value = abilities.get(ability);
            switch(ability.toLowerCase()){
                case "powersurge":{
                    String firstCharacter = characters.keySet().iterator().next();
                    int updatedPower = characters.get(firstCharacter)*(100+value)/100;
                    characters.put(firstCharacter, updatedPower);
                    break;
                }
                case "bopshield":{
                    for(String Character : characters.keySet()){
                        int updatedPower = characters.get(Character)*(100+value)/100;
                        characters.put(Character, updatedPower);
                    }
                    break;
                }
                case "energyboost":{
                    for(String Character : characters.keySet()){
                        int updatedPower = (characters.get(Character) + value);
                        characters.put(Character, updatedPower);
                    }
                    break;
                }
                case "titanboost":{
                    List<String> characterNames = new ArrayList<>(characters.keySet());
                    String lastCharacter = characterNames.get(characterNames.size()-1);
                    int updatedPower = characters.get(lastCharacter)*value;
                    characters.put(lastCharacter, updatedPower);
                    break;
                }
            }
        }

        int totalscore = 0;
        for(String character : characters.keySet()){
            totalscore += characters.get(character);
        }
        return totalscore;

    }

}
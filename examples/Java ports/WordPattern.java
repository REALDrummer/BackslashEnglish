import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordPattern {
	public static boolean matches(String pattern, String[] words) {
		Map<Character, String> patterns_matched = new HashMap<>();
		
		if (pattern.length() != words.length)
			return false;
		
		for (int i = 0; i < words.length; i++) {
			char pattern_char = pattern.charAt(i);
			String word = words[i];
			
			if (!patterns_matched.containsKey(pattern_char))
				patterns_matched.put(pattern_char, word);
			else {
				if (!patterns_matched.get(pattern_char).equals(word))
					return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		String pattern = in.nextLine();
		
		String to_match = in.nextLine();
		
		String[] words_to_match = to_match.split(" ");
		
		System.out.println(matches(pattern, words_to_match));
		
		in.close();
	}
}


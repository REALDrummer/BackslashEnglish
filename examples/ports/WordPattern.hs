import Data.Map (Map)

main = do {
	pattern <- getLine
	words <- splitOn " " getLine
	
	patternMatches pattern words
}

patternMatches pattern words = patternMatches' pattern words Data.Map.empty
patternMatches' [] [] _ = True
patternMatches' pattern [] _ = False
patternMatches' [] words _ = False

patternMatches' pattern_char:pattern word:words pattern_map =
if member pattern_char pattern_map
	patternMatches' pattern_char:pattern word:words (pattern_map ++ (pattern_char, word))
else if pattern_map ! pattern_char == word
	False
else
	patternMatches' pattern words pattern_map

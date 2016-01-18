{-# LANGUAGE CPP, TemplateHaskell #-}
-----------------------------------------------------------------------------
--
-- Module      :  Main
-- Copyright   :
-- License     :  AllRightsReserved
--
-- Maintainer  :
-- Stability   :
-- Portability :
--
-- |
--
-----------------------------------------------------------------------------

module Main (
    main
) where

import Data.Char
import Data.Word
import Data.Maybe
import System.IO
import Text.Read

data Parser = Parser { fileContents :: IO String {- the contents of the file, parsed lazily -},
    filePath :: FilePath {- the path of the input file -},
    lineNumber :: Int {- the current line number -},
    charNumber :: Int {- the current char number -} }
newParser :: String -> Parser
newParser filePath' = Parser { fileContents = readFile filePath', filePath = filePath', lineNumber = 0, charNumber = 0 }

data Value = Label { identifier :: String {- the identifier for the label, including optional local label dots, but not including the colon -} } |
    Literal { value :: Word } |
    Register

data Register = RAX | RBX | RCX | RDX | 		        {- 0-3; 64-bit -}
	EAX | EBX | ECX | EDX |			                {- 4-7; 32-bit -}
	AX | BX | CX | DX |				        {- 8-11; 16-bit -}
	AH | BH | CH | DH |				        {- 12-15; high 8-bit -}
	AL | BL | CL | DL |				        {- 16-19; low 8-bit -}
	CS | DS | ES | FS | GS | SS |		                {- 20-25; 16-bit; segment -}
	RDI | RSI | RSP | RBP | RIP |	                        {- 26-30; 64-bit; special -}
	EDI | ESI | ESP | EBP | EIP | 	                        {- 31-35; 32-bit; special -}
	DI | SI | SP | BP | IP |			        {- 36-40; 16-bit; special -}
	R8 | R9 | R10 | R11 | R12 | R13 | R14 | R15 |		{- 41-48; 64-bit; x86_64 only -}
	R8D | R9D | R10D | R11D | R12D | R13D | R14D | R15D |	{- 49-56; 32-bit; x86_64 only -}
	R8W | R9W | R10W | R11W | R12W | R13W | R14W | R15W |	{- 57-64; 16-bit; x86_64 only -}
	R8B | R9B | R10B | R11B | R12B | R13B | R14B | R15B     {- 65-72; 8-bit; x86_64 only -}


isRestOfValidIdentifier [] = True
isRestOfValidIdentifier (firstChar : restOfIdentifier) =
    (isAlphaNum firstChar || firstChar == '_') && isRestOfValidIdentifier restOfIdentifier

isValidIdentifier [] = False
isValidIdentifier (firstChar : restOfIdentifier) =
    isLetter firstChar &&
    isRestOfValidIdentifier restOfIdentifier

isValidLabelIdentifier [] = False
isValidLabelIdentifier (firstChar : restOfIdentifier) =
    firstChar == '.' && isValidIdentifier restOfIdentifier ||
    isValidIdentifier (firstChar : restOfIdentifier)

newLabel :: String -> Maybe Value
newLabel identifier =
    if not (isValidLabelIdentifier identifier) then do
        error ("\"" ++ identifier ++ "\" is not a valid Assembly label identifier!\nAssembly label identifiers have to optionally start with a dot (for local labels); start with a letter; and contain only letters, numbers, and underscores.")
        Nothing
    else
        Just $ Label identifier

newLiteral :: Literal
newLiteral [] = error ("I got an empty string for a new literal!")
newLiteral string = do {
    value' <- readMaybe string :: Int;
    if (isNothing value') then do
        error ("\"" ++ string ++ "\" is not a valid Assembly literal!")
        Nothing
    else
        Just $ Literal { value = fromJust value' }
}

data OpSyntax =
    OpQuantifiedSyntax Parser OpSyntax |
    OpMetaQuoteSyntax Parser |
    OpLiteralSyntax Parser |
    OpGroupSyntax Parser |
    OpCharClassSyntax Parser |
    OpOrSyntax Parser OpSyntax |
    OpConcatSyntax Parser


main = putStrLn "This main method isn't doing anything yet!!"

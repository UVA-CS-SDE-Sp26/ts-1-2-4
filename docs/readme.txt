TopSecret HW 2 Readme

Goal
- Build a CLI tool to list mission files or display one file by number.
- Support optional deciphering with a default or alternate key.

Team Roles
- Team A: CLI parsing and user interaction.
- Team B: File access and data listing.
- Team C: Program control and coordination.
- Team D: Cipher decoding and key validation (teams of four).

Architecture Overview
- TopSecret parses args and delegates to ProgramController.
- ProgramController requests file lists or contents from File Handler.
- CipherService deciphers content when ciphering is enabled.
- UI prints lists, file contents, or errors and exits.

Core Classes And Members(Instance Variables and Methods)
- TopSecret
  - run(String[] args)
  - main(String[] args)
  - ProgramController controller
- ProgramController
  - listFiles(): String
  - showFile(int index, Optional<String> keyFilename): String
  - FileCatalog catalog
  - FileReaderService reader
  - CipherService cipher
- FileCatalog
  - listFiles(): List<String>
  - getByIndex(int index): Optional<String>
  - getByName(String filename): Optional<String>
  - FileReaderService fileReader
  - String dataDirectoryPath
  - List<String> cachedFileList
- FileReaderService
  - readFile(String path): String throws IOException
- CipherService
  - loadKey(): String
  - loadKey(String keyFilename): String
  - decrypt(String input, String keyContent): String
- CipherKey
  - Map<Character, Character> mapping
  - validate(): boolean

Program Flow
- No args: list files -> print numbered list -> exit.
- With index: read file -> decipher using default key (or provided key filename) -> print content -> exit.

Command Usage
- java TopSecret
- java TopSecret <fileNumber> [keyFilename]

File/Folder Layout
- data/ mission files. Resolved in this order: ./data, ./build/../data, data.
- ciphers/key.txt default key file. Resolved in this order: ./ciphers/key.txt, ./build/../ciphers/key.txt, ciphers/key.txt.
- Optional alternate key filename: second command argument, e.g. custom_key.txt.
- docs/ (role documentation)

Error Behavior
- Missing or invalid mission file index: "File not found".
- Invalid file number format: "Invalid file number".
- Missing or invalid alternate key filename (second argument): "Invalid key".
- Missing data directory: warning on stderr, file list may be empty.
- Decrypt runtime failure: "Decryption failed".

Testing Approach (TDD)
- Unit tests cover non-getter/setter methods in TopSecret, ProgramController, FileCatalog,
  FileReaderService, CipherService, and CipherKey.
- Includes normal, edge, and failure path tests.
- Includes Mockito-based tests in ProgramController and CipherService tests.

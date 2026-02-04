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
- TopSecretApp parses args and delegates to ProgramController.
- ProgramController requests file lists or contents from File Handler.
- CipherService deciphers content when ciphering is enabled.
- UI prints lists, file contents, or errors and exits.

Core Classes And Members(Instance Variables and Methods)
- TopSecretApp
  - main(String[] args)
  - ProgramController controller
- ProgramController
  - listFiles(): String
  - showFile(int index, Optional<String> keyPath): String
  - FileCatalog catalog
  - FileReaderService reader
  - CipherService cipher
- FileCatalog
  - listFiles(): List<String>
  - getByIndex(int index): Optional<String>
  - String dataDir
- FileReaderService
  - readFile(String path): String
- CipherService
  - loadKey(String keyPath): CipherKey
  - decipher(String input, CipherKey key): String
- CipherKey
  - Map<Character, Character> mapping
  - validate(): boolean

Program Flow
- No args: list files -> print numbered list -> exit.
- With index: read file -> optional decipher -> print content -> exit.

Command Usage
- java topsecret
- java topsecret <fileNumber> [keyPath]

File/Folder Layout
- data/ (mission files)
- ciphers/key.txt (cipher key)
- docs/ (role documentation)

Testing Approach (TDD)
- Unit tests for file listing and file reading.
- Unit tests for cipher validation and decoding.
- Integration test for CLI end‑to‑end flow.

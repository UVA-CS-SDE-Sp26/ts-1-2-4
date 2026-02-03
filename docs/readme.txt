TopSecret documentation

Goal
Build a command-line program (java topsecret) that lists available data files or displays a selected file, optionally deciphering with a key.

Workflow / Separation of concerns
1) User Interface (Member A)
- Parses command line arguments.
- Accepts: no args (list files), one arg (file number), optional second arg (alternate key).
- Prints errors and exits.

2) Program Control (Member C)
- Orchestrates flow based on arguments.
- Uses File Handler to list files or fetch file contents.
- If ciphering is enabled, passes raw content to Cipher module and prints deciphered text.

3) File Handler (Member B)
- Owns all direct file system access.
- Locates data/ directory and lists files in a stable order.
- Returns file contents as raw strings.

4) Cipher (Member D)
- Loads and validates key from ciphers/key.txt or alternate key.
- Deciphers raw content and returns readable text.

Proposed classes
- TopSecret (main entry)
  - Parses args, delegates to ProgramController.
- ProgramController
  - listFiles(): String
  - showFile(int index, Optional<Path> keyPath): String
- FileCatalog
  - listFiles(): List<FileEntry>
  - getByIndex(int index): Optional<FileEntry>
- FileReaderService
  - readFile(Path path): String
- CipherService
  - loadKey(Path keyPath): CipherKey
  - decipher(String input, CipherKey key): String
- CipherKey
  - Map<Character, Character> mapping
  - validate(): boolean

Flow (no args)
- UI -> ProgramController.listFiles()
- ProgramController -> FileCatalog.listFiles()
- UI prints numbered list

Flow (one or two args)
- UI parses index (and optional key path)
- ProgramController -> FileCatalog.getByIndex(index)
- ProgramController -> FileReaderService.readFile(path)
- If ciphering enabled:
  - ProgramController -> CipherService.loadKey(keyPath or default)
  - ProgramController -> CipherService.decipher(raw)
- UI prints final output

Notes
- Keep file ordering stable (sorted) so indices stay consistent.
- All file system errors should be surfaced as clear user messages.
- Ciphering is optional based on team size; architecture keeps it pluggable.

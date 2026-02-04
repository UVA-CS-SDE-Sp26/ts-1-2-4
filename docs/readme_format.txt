Readme Format Guide (HW‑Level)

Purpose
- Standardize the homework‑level readme for each HW.
- Summarize the full assignment across all roles in one place.

General rules
- Plain text only.
- Use short headings with Title Case.
- Use dash bullets for lists.
- Keep lines under ~90 characters when possible.
- Avoid long paragraphs; prefer bullets.
- No code blocks unless needed for commands/examples.

Required sections (in this order)
1) Title
- One line: "TopSecret HW <#> Readme".

2) Goal
- 1–2 bullets describing the assignment objective.

3) Team Roles
- List each role with a one‑line responsibility summary.

4) Architecture Overview
- 4–8 bullets describing major components and how they interact.

5) Program Flow
- Two short flows: no‑args and args present.

6) Command Usage
- Show the primary run commands and argument meanings.

7) File/Folder Layout
- Bullet list of required folders and key files.

8) Testing Approach (TDD)
- 3–6 bullets for how testing is organized.

Optional sections (use only if needed)
- Assumptions
- Open Questions
- Future Extensions

Example (for LLMs to follow)

Title
TopSecret HW 2 Readme

Goal
- Build a CLI tool to list mission files or display one file by number.

Team Roles
- Team A: CLI parsing and user prompts.
- Team B: File access and data listing.
- Team C: Program control and coordination.
- Team D: Cipher decoding (if required).

Architecture Overview
- TopSecretApp parses args and delegates to ProgramController.
- ProgramController requests file lists or contents from File Handler.
- CipherService deciphers content when ciphering is enabled.
- UI layer prints lists, file contents, or errors.

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

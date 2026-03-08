# Contributing to Snapshot

Thank you for considering contributing to Snapshot! All contributions are welcome — bug fixes, new features, documentation improvements, and more.
By participating you agree to abide by the [Code of Conduct](CODE_OF_CONDUCT.md).

## Development Setup

1. **Fork and clone** the repository.
2. **Build** with the included script:
   ```bash
   ./build.sh
   ```
3. **Test manually** by running commands against a test directory:
   ```bash
   mkdir /tmp/test-repo && cd /tmp/test-repo
   java -jar /path/to/snapshot.jar init
   java -jar /path/to/snapshot.jar status
   ```

## Project Layout

| File | Responsibility |
|------|---------------|
| `Utils.java` | Path constants, SHA-1 hashing, UTF-8 file I/O, index read/write |
| `Index.java` | Staging area — `add`, `rm` |
| `Commit.java` | Commit creation and commit-data parsing |
| `Repository.java` | `init` and `status` |
| `Log.java` | `log` history display |
| `Main.java` | CLI entry point |

## How to Contribute

1. **Create a branch** for your change:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. **Make your changes.** Keep commits focused and atomic.
3. **Verify the build passes:**
   ```bash
   ./build.sh
   ```
4. **Write a clear commit message** that explains *why*, not just *what*.
5. **Open a pull request** against `main` with a description of your changes.

## Coding Standards

- All methods that belong to a utility class should be `static` — do not add instance state to `Utils`.
- Use `LinkedHashMap` or `ArrayList` (not `List.of()`) for mutable collections.
- Always use `Utils.ROOT`, `Utils.SNAPSHOT`, etc. for file paths — never hardcode strings like `".snapshot/index"`.
- Handle binary files gracefully in any code that reads file content.
- Keep each class focused on a single responsibility.

## Reporting Issues

Open an issue with:
- The command you ran
- The expected behaviour
- The actual output (including any stack trace)

## Contact

Open an issue or start a discussion on the repository.
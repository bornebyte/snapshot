# Snapshot

A lightweight, Git-inspired version control system written in Java.
Snapshot lets you track changes to text files, stage them, commit snapshots, and review history — all from the command line.

## Features

- `init` — initialize a repository in the current directory
- `add` — stage individual files, multiple files, or entire directory trees
- `rm` — unstage a file without deleting it
- `commit` — save a snapshot of all staged files
- `status` — show staged changes, unstaged modifications, deletions, and untracked files
- `log` — display commit history (newest first) with timestamps in local time
- Binary files are automatically detected and skipped

## Project Structure

```
snapshot/
├── build.sh            Build script (compiles + packages as snapshot.jar)
├── addToPath.sh        Helper to install the 'snapshot' command system-wide
├── manifest.txt        JAR manifest
├── src/
│   └── snapshot/
│       ├── Main.java         CLI entry point and command dispatcher
│       ├── Utils.java        Shared path constants, SHA-1 hashing, file I/O
│       ├── Repository.java   init and status commands
│       ├── Index.java        Staging area management (add / rm)
│       ├── Commit.java       Commit creation and commit-data reading
│       └── Log.java          Commit history display
└── .snapshot/          (created by 'snapshot init')
    ├── HEAD            Hash of the latest commit
    ├── index           Staging index  (hash filepath per line)
    └── objects/
        ├── staging/    Staged file content, keyed by SHA-1 hash
        ├── commits/    Per-commit object directories
        └── refs/       Commit metadata files
```

## Getting Started

### Prerequisites

- Java 17 or later

### Build

```bash
chmod +x build.sh
./build.sh
```

This compiles the source and produces `snapshot.jar`.

### Install system-wide (optional)

```bash
chmod +x addToPath.sh
./addToPath.sh
```

This creates an executable wrapper and copies it to `/usr/local/bin/snapshot`.

### Run without installing

```bash
java -jar /path/to/snapshot.jar <command>
```

## Usage

```
Usage: snapshot <command> [args]

Commands:
  init                Initialize a new repository
  add <file> [...]    Stage files for commit (use '.' for all)
  rm  <file>          Remove a file from the staging area
  commit <message>    Commit staged changes
  status              Show working directory and staging status
  log                 Show commit history
```

### Example workflow

```bash
# 1. Set up a repository
cd my-project
snapshot init

# 2. Stage files
snapshot add .                  # stage everything
snapshot add src/Main.java      # or stage a single file
snapshot add a.txt b.txt c.txt  # or multiple files

# 3. Review what will be committed
snapshot status

# 4. Commit
snapshot commit "Initial commit"

# 5. Make changes, then check what changed
snapshot status

# 6. Stage and commit the changes
snapshot add .
snapshot commit "Fix bug in parser"

# 7. View history
snapshot log
```

### snapshot status output

```
Changes to be committed:
  new file:   src/Main.java
  modified:   README.md

Changes not staged for commit:
  modified:   config.json
  deleted:    old-file.txt

Untracked files:
  notes.md
```

## Repository format

### Staging index (`.snapshot/index`)

One entry per staged file:
```
<sha1-hash> <absolute-filepath>
```

### Commit ref file (`.snapshot/objects/refs/<commit-hash>`)

```
time=<epoch-millis>
message=<commit message>
<sha1-hash> <absolute-filepath>
...
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## Code of Conduct

See [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).

## License

MIT — see [LICENSE](LICENSE).


### Key Files and Directories
- **build.sh**: Script to build the project.
- **data.txt**: Data file used by the project.
- **manifest.txt**: Contains metadata for the project.
- **object.txt**: Stores object-related information.
- **build/**: Directory for compiled files.
- **src/**: Contains the source code for the project.
  - **Commit.java**: Handles commit-related operations.
  - **HashObject.java**: Manages hash object creation and retrieval.
  - **Index.java**: Manages the index for tracking changes.
  - **Log.java**: Handles logging of commit history.
  - **Main.java**: Entry point for the application.
  - **Repository.java**: Manages repository operations.
  - **Utils.java**: Utility functions used across the project.

## How to Build
1. Ensure you have Java installed on your system.
2. Run the `build.sh` script to compile the project:
   ```bash
   ./build.sh
   ```

## How to Run
1. Navigate to the `build/snapshot` directory.
2. Run the `Main` class using the Java command:
   ```bash
   java snapshot.Main
   ```

## Detailed Usage Instructions

### Prerequisites
Before using this project, ensure the following:
- Java Development Kit (JDK) is installed on your system.
- The `JAVA_HOME` environment variable is set correctly.
- You have a terminal or command prompt to execute commands.

### Setting Up the Project
1. Clone or download the project to your local machine.
2. Navigate to the project directory:
   ```bash
   cd /path/to/snapshot
   ```

### Building the Project
1. Make the `build.sh` script executable (if not already):
   ```bash
   chmod +x build.sh
   ```
2. Run the build script to compile the project:
   ```bash
   ./build.sh
   ```
   This will compile the Java source files and place the compiled classes in the `build/snapshot` directory.

### Running the Project
1. After building the project, navigate to the `build/snapshot` directory:
   ```bash
   cd build/snapshot
   ```
2. Run the `Main` class to start the application:
   ```bash
   java snapshot.Main
   ```

### Using the Snapshot Application
The Snapshot application provides several commands to manage and track changes in your project. Below are the detailed steps for using the application:

#### 1. Initialize a Repository
To start using Snapshot, initialize a repository:
```bash
snapshot init
```
This command sets up the necessary files and directories for version control.

#### 2. Add Files to the Index
To track specific files, add them to the index:
```bash
snapshot add <file1> <file2>
```
Replace `<file1>` and `<file2>` with the names of the files you want to track.

#### 3. Commit Changes
To save the current state of tracked files, create a commit:
```bash
snapshot commit "Commit message"
```
Replace `"Commit message"` with a descriptive message about the changes.

#### 4. View Commit Log
To see the history of commits, use the log command:
```bash
snapshot log
```
This will display a list of all commits, including their hash, author, date, and message.

#### 5. Check Repository Status
To view the current status of the repository, including staged and unstaged changes, run:
```bash
snapshot status
```

#### 6. Restore Files
To restore a file to a previous state, use the checkout command:
```bash
snapshot checkout <commit-hash> <file>
```
Replace `<commit-hash>` with the hash of the commit you want to restore from, and `<file>` with the name of the file.

#### 7. Remove Files from the Index
To stop tracking a file, remove it from the index:
```bash
snapshot rm <file>
```
Replace `<file>` with the name of the file to untrack.

### Example Workflow
Here is an example workflow to demonstrate how to use Snapshot:
1. Initialize a repository:
   ```bash
   snapshot init
   ```
2. Add files to the index:
   ```bash
   snapshot add file1.txt file2.txt
   ```
3. Commit the changes:
   ```bash
   snapshot commit -m "Initial commit"
   ```
4. Make changes to `file1.txt` and check the status:
   ```bash
   snapshot status
   ```
5. Commit the changes:
   ```bash
   snapshot commit -m "Updated file1.txt"
   ```
6. View the commit log:
   ```bash
   snapshot log
   ```

### Troubleshooting
- **Build Errors**: Ensure that the `build.sh` script is executable and that you have the correct version of Java installed.
- **Command Not Found**: Ensure the `snapshot` command is in your system's PATH. You can add it by creating an alias or adding the project directory to your PATH.
- **Permission Denied**: Use `chmod +x` to make scripts executable.

### Additional Notes
- The `data.txt`, `manifest.txt`, and `object.txt` files are used internally by the application to store metadata and object information. Do not modify these files manually.
- Always provide meaningful commit messages to make it easier to track changes.

For further assistance, refer to the source code in the `src/snapshot` directory or contact the project maintainers.

## Features
- Commit tracking
- File indexing
- Repository management
- Logging of changes

## Contributing
Contributions are welcome! Feel free to fork the repository and submit pull requests.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.
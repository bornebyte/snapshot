# Snapshot Project

## Overview
The Snapshot project is a version control system implementation. It provides functionality to manage and track changes to files and directories, similar to Git. The project is implemented in Java and includes various components to handle commits, logs, indexing, and repository management.

## Project Structure
```
/home/shubham/dev/snapshot
├── build.sh
├── data.txt
├── manifest.txt
├── object.txt
├── build/
│   └── snapshot/
├── src/
    └── snapshot/
        ├── Commit.java
        ├── HashObject.java
        ├── Index.java
        ├── Log.java
        ├── Main.java
        ├── Repository.java
        └── Utils.java
```

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
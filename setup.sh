#!/bin/bash

# Get the directory of the current script
SCRIPT_DIR=$(dirname $(realpath $0))

# Create the snapshot file with the required content
echo "#!/bin/bash" > snapshot
echo "java -jar \"$SCRIPT_DIR/snapshot.jar\" \"\$@\"" >> snapshot

# Make the snapshot file executable
chmod +x snapshot

# Copy the snapshot file to /usr/local/bin
sudo cp snapshot /usr/local/bin/
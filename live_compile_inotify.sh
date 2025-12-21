#!/bin/bash
touch timestamp

echo "Watching for changes"
while true; do
    # Wait for modify/create/move events on .java files
    FILE=$(inotifywait -e modify,create,move --format "%w%f" -r "./src" --include ".*\.java$" 2>/dev/null)

    if [ $? -eq 0 ]; then
        echo "Change detected in: $FILE"
        
        # Compile the changed Java file
        javac -d out -cp out "$FILE"
        
        if [ $? -eq 0 ]; then
            echo "Compiled successfully: $FILE"
        else
            echo "Compilation failed: $FILE"
        fi
    fi
done

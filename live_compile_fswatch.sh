#!/bin/bash
touch timestamp

echo "Watching for changes..."
fswatch -o --event Updated --event Created --event Renamed src | while read f; do
  # Get changed files
  changed_files=$(find src -name "*.java" -newer timestamp 2>/dev/null)

  for file in $changed_files; do
    echo "Recompiling: $file"
    javac -d out -cp out "$file"
    echo "finished."
  done

  touch timestamp
done

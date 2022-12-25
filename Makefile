FILE_SRC = src/file/*.java
EDITOR_SRC = src/editor/*.java
MENU_SRC = src/menu/*.java
GAME_SRC = src/game/*.java

GAME_TARGET = src/game/Main.class
EDITOR_TARGET = src/editor/EditorMain.class

all: $(GAME_TARGET) $(EDITOR_TARGET)

$(GAME_TARGET) : $(FILE_SRC) $(MENU_SRC) $(GAME_SRC)
	javac src/game/*.java

$(EDITOR_TARGET) : $(FILE_SRC) $(EDITOR_SRC)
	javac src/editor/*.java

.PHONY: clean
clean:
	rm src/file/*.class 
	rm src/editor/*.class
	rm src/menu/*.class
	rm src/game/*.class

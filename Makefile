FILE_SRC = src/file/*.java
EDITOR_SRC = src/editor/*.java
MENU_SRC = src/menu/*.java
GAME_SRC = src/game/*.java

GAME_CLASSES = src/game/*.class
EDITOR_CLASSES = src/editor/*.class
MENU_CLASSES = src/menu/*.class
FILE_CLASSES = src/file/*.class

all: $(GAME_CLASSES) $(EDITOR_CLASSES)

$(GAME_CLASSES) : $(FILE_CLASSES) $(MENU_CLASSES) $(GAME_SRC)
	javac src/game/*.java

$(EDITOR_CLASSES) : $(FILE_CLASSES) $(EDITOR_SRC)
	javac src/editor/*.java 

$(MENU_CLASSES) : $(MENU_SRC)
	javac src/menu/*.java

$(FILE_CLASSES) : $(FILE_SRC)
	javac src/file/*.java

.PHONY: clean
clean:
	rm -f src/file/*.class 
	rm -f src/editor/*.class
	rm -f src/menu/*.class
	rm -f src/game/*.class

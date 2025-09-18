CFLAGS = `pkg-config --cflags gtk4` -g
LIBS = `pkg-config --libs gtk4`
OBJ = simple_button_app.o os_custom.o

simple_button_app: $(OBJ)
	$(CC) $(CFLAGS) -o simple_button_app $(OBJ) $(LIBS) $(LDFLAGS)

clean:
	$(RM) $(OBJ) simple_button_app

all: simple_button_app

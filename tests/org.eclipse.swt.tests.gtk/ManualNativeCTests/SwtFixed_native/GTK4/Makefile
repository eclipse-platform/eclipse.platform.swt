CFLAGS = `pkg-config --cflags gtk+-4.0` -g
LIBS = `pkg-config --libs gtk+-4.0`
OBJ = simple_button_app.o os_custom.o

simple_button_app: $(OBJ)
	$(CC) $(CFLAGS) -o simple_button_app $(OBJ) $(LIBS) $(LDFLAGS)

clean:
	$(RM) $(OBJ) simple_button_app

all: simple_button_app

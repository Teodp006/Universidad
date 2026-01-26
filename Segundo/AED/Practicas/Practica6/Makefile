
CC = gcc
CFLAGS = -Wall
SRC = main.c lista.c backtraking.c funciones.c ramipoda.c
OBJ = $(SRC:.c=.o)

programa: matriz3x3 matrix6x6 launcher
	./launcher

# -----------------------------------------------------------
#  Ejecutable para N = 3
# -----------------------------------------------------------
matriz3x3: $(SRC)
	$(CC) $(CFLAGS) -DN=3 $(SRC) -o matriz3x3

# -----------------------------------------------------------
#  Ejecutable para N = 6
# -----------------------------------------------------------
matrix6x6: $(SRC)
	$(CC) $(CFLAGS) -DN=6 $(SRC) -o matrix6x6

# -----------------------------------------------------------
#  Launcher (programa que decide cual ejecutar)
# -----------------------------------------------------------
launcher: launcher.c
	$(CC) $(CFLAGS) launcher.c -o launcher

# -----------------------------------------------------------
#  Limpieza
# -----------------------------------------------------------
cleanall:
	rm -f *.o matriz3x3 matrix6x6 launcher


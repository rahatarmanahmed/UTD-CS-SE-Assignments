CC=g++
CFLAGS=-c -Wall
LDFLAGS=
SOURCES=Product.cpp store.cpp
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=project1

all: $(SOURCES) $(EXECUTABLE)
	
$(EXECUTABLE): $(OBJECTS) 
	$(CC) $(LDFLAGS) $(OBJECTS) -o $@

.cpp.o:
	$(CC) $(CFLAGS) $< -o $@

clean: 
	/bin/rm *.o $(EXECUTABLE)

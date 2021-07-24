#Filename:	Makefile
#Date:		07/15/2021
#Author:	James Anyabine
#Email: 	joa170000@utdallas.edu
#Version:	1.0
#Copyright:	2021, All Rights Reserved
#
#Description:
#	This is the general use Makefile for Project 1 of SE 4348. It will be adjusted to accomodate for whatever
#	functions that are necessary for the project
#                                                                                                                                                            
sourcefiles = \
Client.java

classfiles  = $(sourcefiles:.java=.class)
#classfiles = Simple3.class Simple2.class Simple1.class

all: $(classfiles)

%.class: %.java
	javac -d . -classpath . $<

clean:
	rm -f *.class *.java~ *~ *#




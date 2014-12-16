Rahat Ahmed (rxa121930)
Assignment 5

To view Task 1:
	Use squareloops.dat with any of the viewer programs from the book.

To view Task 2:
	In this directory run javac *.java && java PainterRotateAnim

Notes on Task 2:
Because I used a lot of classes from the book, here is a list of what I changed and wrote myself:

	cube.dat:
		I wrote this myself
	Fr3D.java:
		I removed the menu bar.
	PainterRotateAnim.java:
		I forced it to open only cube.dat.
	Obj3D.java:
		Added rotate method at the end, which rotates the original points with Rota3D.
	CvPainter.java:
		I created a thread to call obj.rotate() and repaint() repeatedly.
		I also added double buffering with an Image.
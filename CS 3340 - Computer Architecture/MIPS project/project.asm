.data

filename: .space 256
FILENAME: .asciiz "02mid0.mid"	# DEBUG: this is so i don't have to enter filename each time, remove this before final testing
fileDescriptor: .word -1

promptFilename: .asciiz "Filename of midi song: "
loadingStr: .asciiz "Parsing MIDI File (this may take a while)...\n"
startStr: .asciiz "Now playing... (playback controls space = pause, ESC = stop, '=' = increase tempo, '-' = decrease tempo)\n"
pauseStr: .asciiz "The song is paused. Press any key to continue playback.\n"
resumeStr: .asciiz "Resuming the song..."
stopStr: .asciiz "The song has ended.\n"


newLine: .ascii "\n"
nullTerminator: .ascii "\0"
garbageBuffer: .space 1024
byteBuffer: .space 1
.align 2
halfByteBuffer: .space 2
.align 2
tempoBuffer: .word 0

.align 2
ticksPerQuarterNote: .half -1
tempo: .word 500000			# in microseconds


trackChunkBytes:	.word -1

# Room to store data for a single event
# Delta simply stores the number of ticks between this command and the one before it
# If command byte is meta event, it will only be 0x51 (tempo), and commandData should have the tempo in microseconds
# If command byte is regular event, first half of byte will be event, second half will be channel
#	If note on or off, first half of commandData is note number, second half is velocity/volume
#	If program change, the whole thing is the new program number (instrument?)
.align 2
deltaTime: .word 0
commandByte: .byte 0x00
commandData: .word 0

totalDelta: .word 0

# Keeps track of if a channel has a note currently pressed.
# Every word keeps the delta time of the note on event
# Use the channel number as the index to get that channel's status
# If the value is -1, means there is no note pressed
trackerDelta: .word -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
trackerPointer: .word -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1

# listHead is a pointer to the head node of the linkedlist
# listNode is a pointer to any node of the linkedlist, use in the code to traverse through a linked list if you want
# 	Each node consists of 4 words
#	Word 0: deltaTime
#	Word 1: commandByte (takes up the whole word even though it's just one byte... whatever yolo)
#	Word 2: commandData
#	Word 3: address of next node (0 if end of list)
listHead: .word 0
listNode: .word 0

errorGetFilename: .asciiz "Unable to get user input for filename."
errorOpenFile: .asciiz "Unable to open file."
.text

j	main

# Prints a single space.
printSpace:
	li	$v0, 11
	li	$a0, ' '
	syscall
	jr	$ra

# Prints a single space.
printNewLine:
	li	$v0, 11
	li	$a0, '\n'
	syscall
	jr	$ra

# $a0: address of string to output
errorMessage:
	li	$v0, 55
	li	$a1, 0
	syscall
	jr	$ra


# $a1: address of value
# $a2: length of value in bytes
# These arguments should match what you use for the read file syscall,
# So you can just call it right after reading.
bigEndianToLittle:
	move	$t0, $a1	# $t0 is first byte
	add	$t1, $a1, $a2	# $t1 is last byte
	add	$t1, $t1, -1
	bigEndianToLittleEndian.loop:
	ble	$t1, $t0, bigEndianToLittleEndian.endLoop
	lbu	$t2, ($t0)
	lbu	$t3, ($t1)
	sb	$t3, ($t0)
	sb	$t2, ($t1)
	addi	$t0, $t0, 1
	addi	$t1, $t1, -1
	j 	bigEndianToLittleEndian.loop
	bigEndianToLittleEndian.endLoop:
	jr	$ra

# Prompts user for filename, stored in label filename
getFilename:
	# Run syscall to prompt for filename
	li	$v0, 54
	la	$a0, promptFilename
	la	$a1, filename
	li	$a2, 260
	syscall
	# If successful (status value 0), return 
	beqz	$a1, getFilename.success
	# If unsuccessful, output error message, and terminate
	la	$a0, errorGetFilename
	jal	errorMessage
	li	$v0, 17
	move	$a0, $a1
	syscall
	getFilename.success:
		addi	$sp, $sp, -4
		sw	$ra, ($sp)
		la	$a0, filename
		jal	replaceNewLineWithNull
		lw	$ra, ($sp)
		addi	$sp, $sp, 4
	jr	$ra

# $a0: address of string 
replaceNewLineWithNull:
	# Compare first byte at $a0 with 
	lbu	$t0, ($a0)
	lbu	$t1, newLine
	# If matches newline, replace it
	beq	$t0, $t1, replaceNewLineWithNull.replace
	# Else, go to next byte
	addiu	$a0, $a0, 1
	j	replaceNewLineWithNull
	
	replaceNewLineWithNull.replace:
		lbu	$t1, nullTerminator
		sb	$t1, ($a0)
	jr	$ra

# filename: path to file
openMidiFile:
	# Open file syscall
	li	$v0, 13
	la	$a0, filename	# DEBUG: change to FILENAME for easy testing
	li	$a1, 0		# Read-only flag
	li	$a2, 0		# doesn't work if not 0???
	syscall
	
	bgez	$v0, readMidiFile.success	# non-zero file descriptor means sucess
	# If unsuccessful, output error message, and terminate
	la	$a0, errorOpenFile
	jal	errorMessage
	move	$a0, $v0
	li	$v0, 17
	syscall
	readMidiFile.success:
	la	$t0, fileDescriptor
	sw	$a1, ($t0)
	jr	$ra


# fileDescriptor: the file descriptor with which to read the file
readHeader:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	# First 8 bytes of a header are useless to us
	# 4 bytes after that are the midi type and number of tracks
	# But we're assuming type 0 and 1 track so throw that away too
	li	$v0, 14
	la	$t0, fileDescriptor
	lw	$a0, ($t0)
	la	$a1, garbageBuffer
	li	$a2, 12
	syscall

	# Read and store the delta-time ticks per quarter note
	li	$v0, 14
	la	$a1, ticksPerQuarterNote
	li	$a2, 2
	syscall
	jal	bigEndianToLittle

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

# fileDescriptor: the file descriptor with which to read the file
# $a2: the number of bytes to read and do nothing with
readGarbage:
	li	$v0, 14
	la	$t0, fileDescriptor
	lw	$a0, ($t0)
	la	$a1, garbageBuffer
	syscall
	jr	$ra


# Increments totalDelta by deltaTime
incrementTotalDelta:
	lw	$t0, deltaTime
	lw	$t1, totalDelta
	add	$t1, $t1, $t0
	sw	$t1, totalDelta
	jr	$ra

# fileDescriptor: the file descriptor with which to read the file
# returns value to $v1
readVariableLengthValue:
	# Initialize value to 0
	move	$v1, $zero
	# Begin read loop (No need for converting to big endian, it's only one byte.)
	readVariableLengthValue.loop:
		li	$v0, 14
		la	$t0, fileDescriptor
		lw	$a0, ($t0)
		la	$a1, byteBuffer
		li	$a2, 1
		syscall
		# If the bytes read is less than 1, means either EOF or error. just exit
		blez	$v0, readVariableLengthValue.end

		# Shift value to the left 7 times to make room for next byte read
		sll	$v1, $v1, 7

		# Load in the byte that was just read
		la	$t7, byteBuffer
		lbu	$t0, ($t7)
		# Make a copy and then mask out the first bit that signals if there are more bytes to read
		move	$t1, $t0
		andi	$t1, $t1, 0x7F
		# Put the masked byte into the value
		or	$v1, $v1, $t1
		# If the first bit that we masked out was set, that means we loop back and read another byte
		# Easy way to check this, if $t0 and $t1 don't match, that means we really masked out the first bit
		bne	$t0, $t1, readVariableLengthValue.loop
	readVariableLengthValue.end:
	jr	$ra
		

# fileDescriptor: the file descriptor with which to read the file
readTrack:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	# First 4 bytes of track header are useless
	li	$v0, 14
	la	$t0, fileDescriptor
	lw	$a0, ($t0)
	la	$a1, garbageBuffer
	li	$a2, 4
	syscall

	# Read and store the byte length of track chunk
	li	$v0, 14
	la	$a1, trackChunkBytes
	li	$a2, 4
	syscall
	jal	bigEndianToLittle

		# Read first delta value
		jal	readVariableLengthValue
		# Store into deltaTime
		sw	$v1, deltaTime
		jal	incrementTotalDelta
		# Read the first command byte
		li	$v0, 14
		la	$t0, fileDescriptor
		lw	$a0, ($t0)
		la	$a1, commandByte
		li	$a2, 1
		syscall
		# Save the command byte into $t7
		la	$t0, commandByte
		lbu	$t7, ($t0)
		# Mask the command byte to just get the command part and hide the channel part
		andi	$t6, $t7, 0xF0

		# Read the first data for the command
		li	$v0, 14
		la	$t0, fileDescriptor
		lw	$a0, ($t0)
		la	$a1, commandData
		li	$a2, 1
		syscall
	# Starting here means there is an actual commandByte in commandByte, so handle data accordingly
	readTrack.eventLoop:
		# Switch for handling each type of event (note, by now one byte of data from each event has already been read
		readTrack.beginEventSwitch:
		beq	$t7, 0xFF, readTrack.readMetaEvent
		beq	$t6, 0x80, readTrack.readNoteOff
		beq	$t6, 0x90, readTrack.readNoteOn
		beq	$t6, 0xA0, readTrack.readOneGarbageByte
		beq	$t6, 0xB0, readTrack.readOneGarbageByte
		beq	$t6, 0xC0, readTrack.readProgramChange
		beq	$t6, 0xD0, readTrack.endEventSwitch
		beq	$t6, 0xE0, readTrack.readOneGarbageByte
#		j	readTrack.eventLoop
			readTrack.readNoteOff:
			readTrack.readNoteOn:
				# Save the velocity, too
				li	$v0, 14
				la	$t0, fileDescriptor
				lw	$a0, ($t0)
				la	$a1, commandData
				addi	$a1, $a1, 1
				li	$a2, 1
				syscall
				# Save the event with note number and velocity
				jal	saveEvent
				j readTrack.endEventSwitch
			readTrack.readProgramChange:
				jal	saveEvent
				# Since program change only has one data byte just move on to next step, no garbage to throw away
				j readTrack.endEventSwitch
			readTrack.readMetaEvent:
				# Read the length of the meta event
				li	$v0, 14
				la	$t0, fileDescriptor
				lw	$a0, ($t0)
				la	$a1, byteBuffer
				li	$a2, 1
				syscall
				# Load the meta command byte that we read already
				lbu	$t0, commandData
				beq	$t0, 0x51, readTrack.saveTempoEvent
				# Just throw it all away if it's not a tempo event
				lbu	$a2, byteBuffer
				jal	readGarbage
				j readTrack.endEventSwitch

				readTrack.saveTempoEvent:
				# Move the commandByte to where it belongs
				sb	$t0, commandByte
				# Clear the command data so that byte we don't write doesn't have garbage data
				sw	$zero, commandData
				# Read the tempo into the commandData
				li	$v0, 14
				la	$t0, fileDescriptor
				lw	$a0, ($t0)
				la	$a1, commandData
				li	$a2, 3
				syscall
				jal	bigEndianToLittle
				jal	saveEvent
				j readTrack.endEventSwitch
			readTrack.readOneGarbageByte:
				# This is for commands we don't care about but also have one extra data byte to get rid of
				li	$a2, 1
				jal	readGarbage
				j readTrack.endEventSwitch
		readTrack.endEventSwitch:
		# Read next delta value
		jal	readVariableLengthValue
		# If <=0 means we didn't read anything so assume EOF
		blez	$v0, readTrack.eventLoopEnd
		# Store into deltaTime
		sw	$v1, deltaTime
		jal	incrementTotalDelta
		
		# Read the next byte (which may or may not be a command byte)
		li	$v0, 14
		la	$t0, fileDescriptor
		lw	$a0, ($t0)
		la	$a1, byteBuffer
		li	$a2, 1
		syscall
		# Check if the byte's MSB is 1, that means it IS a command byte
		lbu	$t0, byteBuffer
		bgeu	$t0, 0x80, readTrack.checkByteIf
		j	readTrack.checkByteElse
		readTrack.checkByteIf:		# byte IS a command, change the command	
			sb	$t0, commandByte
			# Save the command byte into $t7 and the masked byte into $t0 for the event switch
			la	$t0, commandByte
			lbu	$t7, ($t0)
			andi	$t6, $t7, 0xF0
			# Clear the command data so that byte we don't write doesn't have garbage data from the old data
			sw	$zero, commandData
			# Read the first data byte for the new command
			li	$v0, 14
			la	$t0, fileDescriptor
			lw	$a0, ($t0)
			la	$a1, commandData
			li	$a2, 1
			syscall
			j	readTrack.checkByteEnd
		readTrack.checkByteElse:	# byte is data for running status, leave command the same
			sb	$t0, commandData
			j	readTrack.checkByteEnd
		readTrack.checkByteEnd:

		j	readTrack.eventLoop
	readTrack.eventLoopEnd:

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

# deltaTime, commandByte, commandData should all contain what you want to save
saveEvent:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	# Load all the variables
#	lw	$t0, deltaTime
#	lbu	$t1, commandByte
#	lbu	$t2, commandData
#	lbu	$t3, commandData+1 	# velocity if command is note on/off

	# Save this event (possible events: tempo, program change)
	jal	addListNode

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

# Calls newListNode to make a new listnode with data in labels, and sets it as the next node of listNode
# Then it sets listNode to the new node
# If listHead points to 0, then it will make a new listnode
addListNode:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)
	# Make dat new node
	jal	newListNode
	# Check if the head is 0
	lw	$t0, listHead
	beqz	$t0, addListNode.firstNode
	j	addListNode.nextNode
	addListNode.firstNode:
	# If listHead is 0, put the listnode in both head and node
	sw	$v0, listHead
	sw	$v0, listNode
	j	addListNode.end
	addListNode.nextNode:
	# Else make it the next node of listNode
	# Load the address of the node into $t0
	lw	$t0, listNode
	# Store in the 4th word the pointer to the new node
	sw	$v0, 12($t0)
	# Make the new node the current node
	sw	$v0, listNode
	addListNode.end:
	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra
# Allocates and makes a list node with data in deltaTime, commandByte, commandData
# Shouldn't have to call this manually, addListNode will do it for you
# returns $v0: address to node
newListNode:
	li	$v0, 9
	li	$a0, 16
	syscall
	# $v0 now has the pointer to the 16 bytes of space we can make a node in now!
	# Lets get all our data
	lw	$t1, deltaTime
	lbu	$t2, commandByte
	lw	$t3, commandData
	li	$t4, 0
	# Store all the data in the allocated space
	sw	$t1, 0($v0)
	sw	$t2, 4($v0)
	sw	$t3, 8($v0)
	sw	$t4, 12($v0)
	jr	$ra

# DEBUG: this just prints the linked list, for testing purposes
printList:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	# Set the listNode to the first node
	lw	$t0, listHead

	printList.loop:
		# Load data from node
		lw	$t1, 0($t0)	# deltaTime
		lw	$t2, 4($t0)	# commandByte
		lw	$t3, 8($t0)	# commandData
		
		# Print delta
		li	$v0, 1
		move	$a0, $t1
		syscall
		jal	printSpace
		# Print commandByte
		li	$v0, 34
		move	$a0, $t2
		syscall
		jal	printSpace
		# Go to the right sections depending on what type of event
		beq	$t2, 0x51, printList.printTempo
		bgeu	$t2, 0xA0, printList.printProgramChange
		j	printList.printNote

		printList.printTempo:
		printList.printProgramChange:
			# Print the tempo/program change
			li	$v0, 1
			move	$a0, $t3
			syscall
			j	printList.endSwitch
		printList.printNote:
			# Print the note number
			li	$v0, 1
			lbu	$a0, 8($t0)
			syscall
			jal	printSpace
			# Print the velocity
			li	$v0, 1
			lbu	$a0, 9($t0)
			syscall
			jal	printSpace
			# Print the duration
			li	$v0, 1
			lhu	$a0, 10($t0)
			syscall

			j	printList.endSwitch
		printList.endSwitch:
		jal	printNewLine
		lw	$t0, 12($t0)
		bnez	$t0, printList.loop

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

# Plays the song after being loaded into memory
playSong:
	addi	$sp, $sp, -4
	sw	$ra, ($sp)

	# Set the listNode to the first node
	lw	$s0, listHead

	# Print startStr
	li	$v0, 4
	la	$a0, startStr
	syscall

	playSong.loop:
		# Check keyboard for input
		# Check control word to see if there is any data waiting for us
		lw	$t0, 0xffff0000
		beqz	$t0, playSong.exitIO
		lw	$t0, 0xffff0004
		beq	$t0, 0x20, playSong.enterPauseLoop
		beq	$t0, 0x1B, playSong.stop
		beq	$t0, 0x3d, playSong.tempoUp
		beq	$t0, 0x2d, playSong.tempoDown
		j	playSong.exitIO

			playSong.stop:
			# stop playing everything
			li	$v0, 62
			syscall
			j	playSong.endSong

			playSong.enterPauseLoop:
			# stop playing everything
			li	$v0, 62
			syscall
			li	$v0, 4
			la	$a0, pauseStr
			syscall

			playSong.pauseLoop:
			lw	$t0, 0xffff0000
			bnez	$t0, playSong.exitPauseLoop			
			j	playSong.pauseLoop
			playSong.exitPauseLoop:
			lw	$t0, 0xffff0004
			# Print resumeStr
			li	$v0, 4
			la	$a0, resumeStr
			j	playSong.exitIO

			playSong.tempoUp:
			li	$t0, -50000
			j	playSong.adjustTempo
			playSong.tempoDown:
			li	$t0, 50000
			playSong.adjustTempo:
			lw	$t1, tempo
			add	$t1, $t1, $t0
			sw	$t1, tempo
			j	playSong.exitIO

			
		playSong.exitIO:


		# Load data from node
		lw	$t1, 0($s0)	# deltaTime
		lw	$t2, 4($s0)	# commandByte
		lw	$t3, 8($s0)	# commandData
		
		# Sleep delta ( ms = ticks * tempo / 1000 / ticksPerQuarterNote )
		li	$v0, 32
		lw	$t4, tempo
		lw	$t5, ticksPerQuarterNote
		mult 	$t1, $t4	# ticks * tempo
		mflo	$a0
#		srl	$a0, $a0, 1 	# multiply by 3.5 to make it work (idk why)
#		li	$t6, 7
#		mult 	$a0, $t6	
#		mflo	$a0
		div	$a0, $a0, 273	# / 1000
		div	$a0, $a0, $t5	# / ticksPerQuarterNote
		syscall

		# Go to the right sections depending on what type of event
		beq	$t2, 0x51, playSong.changeTempo
		bgeu	$t2, 0xA0, playSong.programChange
		j	playSong.playNote

		playSong.changeTempo:
			sw	$t3, tempo
			j	playSong.endSwitch	
		playSong.programChange:
			li	$v0, 38
			andi	$a0, $t2, 0x0F
			move	$a1, $t3
			syscall
			j	playSong.endSwitch
		playSong.playNote:
			# Check if command is on or off, then load appropriate syscall
			bgeu	$t2, 0x90, playSong.playOnNote
			j	playSong.playOffNote
			playSong.playOnNote:
			li	$v0, 60
			j	playSong.endNoteCheck
			playSong.playOffNote:
			li	$v0, 61
			j	playSong.endNoteCheck			
			playSong.endNoteCheck:
			# load the rest of the syscall
			andi	$a0, $t2, 0x0F
			lbu	$a1, 8($s0)
			lbu	$a2, 9($s0)
			syscall

			j	playSong.endSwitch
		playSong.endSwitch:
		lw	$s0, 12($s0)
		bnez	$s0, playSong.loop
		playSong.endSong:
	# print stopStr
	li	$v0, 4
	la	$a0, stopStr
	syscall

	lw	$ra, ($sp)
	addi	$sp, $sp, 4
	jr	$ra

main:
	jal	getFilename

	li	$v0, 4
	la	$a0, loadingStr
	syscall

	jal	openMidiFile
	jal	readHeader
	jal	readTrack
	jal	playSong

	j	end





end:

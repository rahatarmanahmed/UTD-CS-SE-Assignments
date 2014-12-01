## part 1: get notes and store in array
.text
main:
         la $a0,str1 #Load and print string asking for notes
         li $v0,4
         syscall


## part 2 load refernces for asciiz a-g and A-G in registers

## load the low octave
li $t2, 0x00000061  ## store a note char for reference
li $t3, 0x00000062  ## store b note char for reference
li $t4, 0x00000063  ##   "   c  "     "   "     "
li $t5, 0x00000064  ##   "   d  "     "   "     "
li $t6, 0x00000065  ##   "   e  "     "   "     "
li $t7, 0x00000066  ##   "   f  "     "   "     "
li $s0, 0x00000067  ##   "   g  "     "   "     "

## load the high octave 
li $s1, 0x00000041  ##   "   A  "     "   "     "
li $s2, 0x00000042  ##   "   B  "     "   "     "
li $s3, 0x00000043  ##   "   C  "     "   "     "
li $s4, 0x00000044  ##   "   D  "     "   "     "
li $s5, 0x00000045  ##   "   E  "     "   "     "
li $s6, 0x00000046  ##   "   F  "     "   "     "
li $s7, 0x00000047  ##   "   G  "     "   "     "


## part 3: loop through array to read input, compare to reference and branch to play notes
         
         play_loop:
	# IO check
	lw	$t0, 0xffff0000
	beqz	$t0, play_loop
	lw	$t1, 0xffff0004

         ## low octave branch
         beq $t1,$t2,anote  # play note a if t1 = t0
         beq $t1,$t3,bnote  
         beq $t1,$t4,cnote
         beq $t1,$t5,dnote
         beq $t1,$t6,enote
         beq $t1,$t7,fnote
         beq $t1,$s0,gnote

         ## high octave branch
         beq $t1,$s1,Anote  # play note A if t1 = s1
         beq $t1,$s2,Bnote   
         beq $t1,$s3,Cnote
         beq $t1,$s4,Dnote
         beq $t1,$s5,Enote
         beq $t1,$s6,Fnote
         beq $t1,$s7,Gnote

	 ## esc to exit
	 beq $t1, 0x1B, end_play_loop

	 ## default: loop
	 j play_loop

         end_play_loop:
         li $v0,10 #end program
         syscall

## part 4: notes to branch to in loop

anote: ## plays an low a note 
 li $a0, 57     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
    syscall
 add $t0, $t0, 1 ##advance the array to next note
 b play_loop

bnote:           ## plays a low b note
 li $a0, 59     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1 ##advance array to next note
 syscall
 b play_loop

cnote:           ## plays a low c note
 li $a0, 61     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop


dnote:          ## plays a low d note
 li $a0, 63     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

enote:          ## plays a low e note
 li $a0, 65     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

fnote:         ## plays a low f note
 li $a0, 67     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

gnote:       ## plays a low g note
 li $a0, 69     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

Anote:       ## plays a high A note
 li $a0, 70     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

Bnote:       ## plays a High B note
 li $a0, 72     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

Cnote:       ## plays a High C note
 li $a0, 74     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

Dnote:       ## plays a High D note
 li $a0, 76     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

Enote:       ## plays a high E note
 li $a0, 78     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

Fnote:       ## plays a high F note
 li $a0, 80     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

Gnote:       ## plays a high G note
 li $a0, 82     
 li $a1, 800
 li $a2, 0
 li $a3, 60
 li $v0, 31
  add $t0, $t0, 1
    syscall
 b play_loop

 .data
             buffer: .space 100
             str1:  .asciiz "Piano started, type the notes (a-gA-G) into the keyboard simulator. To exit press ESC."

      


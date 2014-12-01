.data
promptStr: .asciiz "Please input two integer numbers: "
sumStr: .asciiz "\nSum of your two numbers is: "
productStr: .asciiz "\nProduct of your two numbers is: "
divisionStr: .asciiz "\nQuotient of your two numbers is: "
remainderStr: .asciiz "\nWith a remainder of: "
.text

# Display prompt string for the user to enter numbers
li $v0, 4
la $a0, promptStr
syscall

# Read the two integers
li $v0, 5
syscall
move $s0, $v0 		# Save the first integer into $s0

li $v0, 5
syscall
move $s1, $v0 		# Save the second integer into $s1

# Calculate arithmetic results and output
# Sum
add $t0, $s0, $s1 	# Put sum of two ints into $t0

li $v0, 4
la $a0, sumStr
syscall 		# Output string for displaying sum

li $v0, 1
move $a0, $t0
syscall 		# Output sum

# Product
mul $t1, $s0, $s1 	# Put product of two ints into $t1

li $v0, 4
la $a0, productStr
syscall 		# Output string for displaying product

li $v0, 1
move $a0, $t1
syscall 		# Output product

# Division (Quotient/Remainder)
div $s0, $s1  		# Results are in HI and LO
mflo $t2		# Move quotient from LO to $t2
mfhi $t3		# Move remainder from HI to $t3

li $v0, 4
la $a0, divisionStr
syscall 		# Output string for displaying quotient

li $v0, 1
move $a0, $t2
syscall 		# Output quotient

li $v0, 4
la $a0, remainderStr
syscall 		# Output string for displaying remainder

li $v0, 1
move $a0, $t3
syscall 		# Output remainder


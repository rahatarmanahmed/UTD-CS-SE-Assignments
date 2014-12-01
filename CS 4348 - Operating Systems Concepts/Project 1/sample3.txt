.0
1    // Load 10
10
14   // CopyToX
1    // Load A
65
9    // Output A
2
1    // Load newline 
10
9    // Output newline 
2
29   // Syscall
26   // DecX
15   // CopyFromX
22   // Jump NE Load A  
3
50


.1000
27   // Push
15   // CopyFromX
27   // Push
17   // CopyFromY
27   // Push

2    // load data 
1700
14   // CopyToX
25   // IncX
15   // CopyFromX
7    // Store data 
1700

28   // Pop
16   // CopyToY
28   // Pop
14   // CopyToX
28   // Pop
30   // IRet 



.1500
27   // Push
15   // CopyFromX
27   // Push
17   // CopyFromY
27   // Push

2    // load data 
1700
9    // write value
1
1    // load newline
10
9    // write newline
2

28   // Pop
16   // CopyToY
28   // Pop
14   // CopyToX
28   // Pop
30   // IRet 

.1700
0    // data 


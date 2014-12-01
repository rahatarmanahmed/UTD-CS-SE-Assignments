23   // line one
15
23   // line two 
30
23   // line three 
51
23   // line four 
86
23   // line five 
103
23   // line six 
142
23   // line seven 
163
50

      // line one
1    ld 4
4         
27   push
23   call spaces 
206
28   remove parm 
1    ld 6
6         
27   push
23   call line
178
28   remove parm 
23   call newline
220
24   return

      // line two
1
32
9
2
1    ld /
47
9
2
1    ld 9
9         
27   push
23   call spaces 
206
28   remove parm 
1    load \ 
92    
9
2
23   call newline
220
24   return

      // line three
1    ld /
47
9    output
2
1    three spaces
32
9
2
9
2
9
2
23   print eye
225
1    two spaces
32
9
2
9
2
23   print eye
225
1    two spaces
32
9
2
9
2
1    load \ 
92    
9
2
23   call newline
220
24   return

      // line four 
1    load | 
124
9    put
2
1    ld 11
11         
27   push
23   call spaces 
206
28   remove parm 
1    load | 
124
9    put
2
23   call newline
220
24

      // line five
1    load \ 
92    
9
2
1    three spaces
32
9
2
9
2
9
2
1    load \ 
92    
9
2
1    ld 4 
4
27   push
23   call underscore 
192
28   remove parm 
1    load / 
47    
9
2
1    two spaces
32
9
2
9
2
1    load / 
47    
9
2
23   call newline
220
24   return

      // line six 
1
32
9
2
1    ld \
92
9
2
1    ld 9
9         
27   push
23   call spaces 
206
28   remove parm 
1    load / 
47 
9
2
23   call newline
220
24   return

      // line seven 
1    ld 4
4         
27   push
23   call spaces 
206
28   remove parm 
1    ld 6
6         
27   push
23   call line
178
28   remove parm 
23   call newline
220
24   return

      // print dash 
1    ld 1
1 
14   CopyToX
6    LoadSpX  - get parm
14   CopyToX
1    ld -
45
9    output
2
26   decX
15   CopyFromX
22   JNE 183
183
24

      // print underscore 
1    ld 1
1 
14   CopyToX
6    LoadSpX  - get parm
14   CopyToX
1    ld _
95
9    output
2
26   decX
15   CopyFromX
22   JNE 197
197
24

      // print space 
1    ld 1
1 
14   CopyToX
6    LoadSpX  - get parm
14   CopyToX
1    ld ' ' 
32
9    output
2
26   decX
15   CopyFromX
22   JNE 211
211
24

      // print newline 
1
10
9
2
24

      // print -* 
1    ld dash 
45       
9    output
2
1    ld asterisk
42    
9    output
2
24   return


.1000
30   interrupt handler - just return

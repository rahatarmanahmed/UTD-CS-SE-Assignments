#lang racket

; Aux function square to test with
(define (square x) (* x x)) 

; Reverses a list
(define (my-reverse lst)
  (if (null? lst) ; Checks if lst is empty
      lst ; if so, return the empty list
      (append ; create a list consisting of
       (my-reverse (cdr lst)) ; the reverse of the rest of the list
       (list (car lst))))) ; and the first element of the list (put in a list because that's what append wants)

; Testing my-reverse
(printf "(my-reverse '(1 2 3 4 5)): ~v~n" (my-reverse '(1 2 3 4 5)))
(printf "(my-reverse '(1)): ~v~n" (my-reverse '(1)))
(printf "(my-reverse '()): ~v~n~n" (my-reverse '()))

; Applies a function to every element in a list
(define (my-map func lst) 
  (if (null? lst) ; Checks if lst is empty
      lst ; if so, returns an empty list
      (cons ; create a list consisting of
       (func (car lst)) ; the output of func with first element as input
       (my-map func (cdr lst))))) ; and the my-map of the rest of the list

; Testing my-map
(printf "(my-map sqrt '(1 2 3 4 5)): ~v~n" (my-map sqrt '(1 2 3 4 5)))
(printf "(my-map square '(1 2 3 4 5)): ~v~n" (my-map square '(1 2 3 4 5)))
(printf "(my-map add1 '(1 2 3 4 5)): ~v~n~n" (my-map add1 '(1 2 3 4 5)))

; Applies a function to 3
(define (do-sth-to-3 func)
  (func 3)) ; return func when given the argument 3

; Testing do-sth-to-3
(printf "(do-sth-to-3 sqrt): ~v~n" (do-sth-to-3 sqrt))
(printf "(do-sth-to-3 square): ~v~n" (do-sth-to-3 square))
(printf "(do-sth-to-3 add1): ~v~n~n" (do-sth-to-3 add1))

; Returns a list of pairs
(define (zip list1 list2)
  (if (or (null? list1) (null? list2)) ; Check if either list is null
      null ; if so, return an empty list
      (cons ; Create a list consisting of
       (cons (car list1) (cons (car list2) null)) ; The pair of first elements from both lists
       (zip (cdr list1) (cdr list2))))) ; And the zip of the rest of the list

; Testing zip
(printf "(zip '(1 2 3) '(4 9 5)): ~v~n" (zip '(1 2 3) '(4 9 5)))
(printf "(zip '(1 2 3) '(4 5 6 7 8)): ~v~n" (zip '(1 2 3) '(4 5 6 7 8)))
(printf "(zip '(3 5 6) '(\"one\" \"three\" \"hello\" \"two\")): ~v~n~n" (zip '(3 5 6) '("one" "three" "hello" "two")))

; Separates even and odd members of the list into two lists
(define (segregate lst)
  (if (null? lst) ; If the list is null
      '(null null) ; Returns two null lists
      (list (get-evens lst) (get-odds lst)))) ; else returns a list containing a list of evens and a list of odds

; Returns list of even integers from input list
; Auxilliary function for segregate
(define (get-evens lst)
  (if (null? lst) ; If the list is null
      null ; Return a null list
      (if (even? (car lst)) ; Check if first element is even
          (cons (car lst) (get-evens (cdr lst))) ; If so, append to the evens of the rest of the list
          (get-evens (cdr lst))))) ; Else return the evens of the rest of the list

; Returns list of odd integers fom input list
; Auxilliary function for segregate
(define (get-odds lst)
  (if (null? lst) ; If the list is null
      null ; Return a null list
      (if (odd? (car lst)) ; Check if first element is odd
          (cons (car lst) (get-odds (cdr lst)))
          (get-odds (cdr lst)))))
  
; Testing segregate
(printf "(segregate '(1 2 3 4 9 5)): ~v~n" (segregate '(1 2 3 4 9 5)))
(printf "(segregate '(2 4 6 8)): ~v~n" (segregate '(2 4 6 8)))
(printf "(segregate '(1698 4354 68 4635 435 48 798 432 19 87)): ~v~n~n" (segregate '(1698 4354 68 4635 435 48 798 432 19 87)))

; Checks if a value is in a list
(define (in-list? lst val)
  (if (null? lst) ; Checks if the list is empty
      #f ; If so, value can't be in list; return false
      (if (equal? val (car lst)) ; Checks if the first element is equal to val
          #t ; If so, return true
          (in-list? (cdr lst) val)))) ; Else check if val is in the rest of the list

; Testing in-list?
(printf "(in-list? '(1 2 3 4 9 5) 4): ~v~n" (in-list? '(1 2 3 4 9 5) 4))
(printf "(in-list? '(1 2 3 4 9 5) 99): ~v~n" (in-list? '(1 2 3 4 9 5) 99))
(printf "(in-list? '(\"a\" \"b\" \"c\" \"d\") \"b\"): ~v~n~n" (in-list? '("a" "b" "c" "d") "b"))

; Checks if a list is sorted in ascending order
(define (sorted? lst)
  (if (<= (length lst) 1) ; Checks if list is empty or only length 1
      #t ; If so, return true;
      (if (< (first lst) (second lst)) ; Else compare first two elements to see if they are ascending
          (sorted? (cdr lst)) ; If so, check if the rest of the list is sorted
          #f))) ; Else return false

; Testing sorted?
(printf "(sorted? '(1 2 3 4 5)): ~v~n" (sorted? '(1 2 3 4 5)))
(printf "(sorted? '(5 4 3 2 1)): ~v~n" (sorted? '(5 4 3 2 1)))
(printf "(sorted? '(1 2 100 4 5)): ~v~n~n" (sorted? '(1 2 100 4 5)))

; Flattens a list with sublists so it contains no sublists
(define (flatten lst)
  (if (null? lst) ; Check if the list is null
      null ; If so return the empty list
      (if (list? (car lst)) ; Check if first element is a list
          (append (flatten (car lst)) (flatten (cdr lst))) ; If so, append the flattened first element to the flattened rest of the list
          (append (list (car lst)) (flatten (cdr lst)))))) ; Else, append the first element to the flattened rest of the list

; Testing flatten
(printf "(flatten '((1 2 3) 4 5)): ~v~n" (flatten '((1 2 3) 4 5)))
(printf "(flatten '(((5 4) 3) 2 (1)): ~v~n" (flatten '(((5 4) 3) 2 (1))))
(printf "(flatten '(1 2 100 (((((4))))) 5)): ~v~n~n" (flatten '(1 2 100((((( )))))4 5)))

; Merges two lists in order based on their first elements
; Auxilliary function for rev-sort
(define (merge list1 list2)
  (if (null? list1) ; If the first list is empty
      list2 ; Then the merge is just the second list
      (if (null? list2) ; If the second list is null
          list1 ; Then the merge is just the first list
          (if (> (car list1) (car list2)) ; If the first element of list1 > first element of list2
              (cons (car list1) (merge (cdr list1) list2)) ; combine the first element of list1 with the merging of the rest of list1 and list2
              (cons (car list2) (merge (cdr list2) list1)))))) ; else combine the first element of list2 wit the merging of the rest of list2 and list1

(define (rev-sort lst)
  (if (<= (length lst) 1) ; If the length is 0 or 1
      lst ; List is already sorted, return it
      (merge ; Else split the list into two, sort them, and merge them back together.
       (rev-sort (take lst (quotient (length lst) 2)))
       (rev-sort (drop lst (quotient (length lst) 2))))))

; Testing rev-sort
(printf "(rev-sort '(-10 2 -4 -8 18 15 11 11 20 -19 19 4 -1 -10 10)): ~v~n" (rev-sort '(-10 2 -4 -8 18 15 11 11 20 -19 19 4 -1 -10 10)))
(printf "(rev-sort '(11 9 10 9 11 5 8 11 17 7)): ~v~n" (rev-sort '(11 9 10 9 11 5 8 11 17 7)))
(printf "(rev-sort '(14 5 18 4 20 0 11 0 5 3)): ~v~n~n" (rev-sort '(14 5 18 4 20 0 11 0 5 3)))
% 1. Even
is_even(X) :- X mod 2 =:= 0.


% 2. Factorial
fact(N, X) :-
	N > 0,
	M is N - 1,
	fact(M, Y),
	X is N * Y.
fact(1, 1).


% 3. Prime
is_prime(X) :-
	X > 0,
	not(is_even(X)),
	check_prime(X, 3).
is_prime(2).

% Helper function to check if X is prime
check_prime(X, N) :-
	not(divides(N, X)),
	M is N+2,
	check_prime(X, M),
	!.
check_prime(X, N) :-
	N > sqrt(X).

% Helper function divides, true if A divides B
divides(A, B) :-
	A =\= 0,
	R is B mod A,
	R =:= 0.


% 4. Segregate
segregate([H | T], E, O) :- 
	is_even(H),
	segregate(T, E1, O),
	append(E1, [H], E),
	!.
segregate([H | T], E, O) :- 
	not(is_even(H)),
	segregate(T, E, O1),
	append(O1, [H], O),
	!.

	
segregate([], [], []).


% 5. Product List
prod_list([H | T], P) :-
	prod_list(T, P1),
	P is H * P1,
	!.

prod_list([H], H).


% 6. Bookends
bookends(P, S, L) :-
	prefix(P, L),
	suffix(S, L).

prefix([], _).
prefix([H | T], [H | A]) :-
	prefix(T, A).

suffix(L, L).
suffix(S, [_ | T]) :-
	suffix(S, T),
	!.


% 7. Subslice

subslice([], _).

subslice([H | T], [A | B]) :-
	H = A,
	subslice(T, B),
	!.

subslice([H | T], [A | B]) :-
	not(H = A),
	subslice([H | T], B),
	!.


% 8. Clue

affair(mrBoddy, msGreen).
affair(msGreen, mrBoddy).
married(profPlum, msGreen).
married(msGreen, profPlum).
rich(mrBoddy).
greedy(colMustard).
affair(mrBoddy, missScarlett).
affair(missScarlett, mrBoddy).

% THIS FACT IS ADDED TO MAKE THE KILLER UNIQUE
rich(colMustard).

suspect(K, V) :-
	married(K, S),
	affair(S, V);
	greedy(K),
	not(rich(K)),
	rich(V).

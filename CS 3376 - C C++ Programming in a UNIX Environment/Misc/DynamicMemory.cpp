int main()
{
	// Creating memory configuration
	int *xp[100];
	for(int k = 0; k < 100 ; k++)
	{
		xp[k] = new int[100-k];
	}

	// Do stuff
	// ...

	// Make sure we delete when we're done
	for(int k = 0; k < 100 ; k++)
	{
		delete [] xp[k];
	}



	return 0;
}
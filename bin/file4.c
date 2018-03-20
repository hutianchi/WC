void getString()
{
	cout << "please input string end with Enter. \n" << endl;
	cin.get(str, 100);
	cout << "\n" << endl;
}

int main()
{
	getString();
	getsym();
	statement();
	system("pause");
	return 0;
}
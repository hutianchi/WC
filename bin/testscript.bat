wc.exe -c file.c -o output1.txt
wc.exe -c file1.c -o output2.txt
wc.exe -w file2.c -o output3.txt
wc.exe -w file3.c -e stoplist.txt -o output4.txt
wc.exe -w file4.c -e stoplist1.txt -o output5.txt
wc.exe -w file5.c -e stoplist2.txt -o output6.txt
wc.exe -l -a file6.c -o output7.txt
wc.exe -l -a file7.c -o output8.txt
wc.exe -l -a file8.c -o output9.txt
wc.exe -l -a file9.c -o output10.txt
wc.exe -c -w -l -a -s -o output11.txt
wc.exe -c -w -l -a -s -e stoplist.txt


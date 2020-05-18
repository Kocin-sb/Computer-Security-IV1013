clear

echo "Encrypting file4.data:"
echo " "
sleep 1

rm Hidenc.class file4.data
javac Hidenc.java
java Hidenc --key=0a40f2e6da4a1dee3a73453dbc77f51c --ctr=84bbd4ab12b4c654be4fac080a3e3f63 --input=task4.data --output=file4.data --offset=1312 --template=template.data

echo " "
echo "Decrypting file4.data:"
echo " "
sleep 1

rm Hiddec.class file4.txt
javac Hiddec.java
java Hiddec --key=0a40f2e6da4a1dee3a73453dbc77f51c --ctr=84bbd4ab12b4c654be4fac080a3e3f63 --input=file4.data --output=file4.txt 

echo " "
cat file4.txt
echo " "
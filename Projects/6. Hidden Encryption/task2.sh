clear

echo "Encrypting task2.data:"
echo " "
sleep 1

rm Hidenc.class file2.data
javac Hidenc.java
java Hidenc --key=83d9174ba6ea9d2763ce045bd40521d6 --input=task2.data --output=file2.data --offset=80 --size=2048

echo " "
echo "Decrypting file2.data:"
echo " "
sleep 1

rm Hiddec.class file2.txt
javac Hiddec.java
java Hiddec --key=83d9174ba6ea9d2763ce045bd40521d6 --input=file2.data --output=file2.txt 

echo " "
cat file2.txt
echo " "
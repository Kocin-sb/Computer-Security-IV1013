clear
rm key 
rm cipher
rm plain
echo "Generating key:"
javac -cp ".:./jscience.jar" HillKeys.java
java -cp ".:./jscience.jar" HillKeys 26 3 key

echo "Key generated"
cat key 
echo " "

echo "Encrypting plain-alpha"

javac HillCipher.java
java HillCipher 26 3 key plain-alpha cipher

echo "Cipher created" ; echo " "

cat cipher ; echo " "

echo "Decrypting cipher"

javac -cp ".:./jscience.jar" HillDecipher.java
java -cp ".:./jscience.jar" HillDecipher 26 3 key plain cipher

echo "Cipher decrypted" ; echo " "

echo "Plain: " ; echo " "
cat plain
clear
rm key 
rm cipher
rm plain
cat plain-alpha
echo "Generating key:"
javac -cp ".:./jscience.jar" HillKeys.java
java -cp ".:./jscience.jar" HillKeys 26 8 key

echo "Key generated"
cat key 
echo " "
echo " "
echo "Encrypting plain-alpha"

echo "Cipher created" ; echo " "
javac HillCipher.java
java HillCipher 26 8 key plain-alpha cipher

cat cipher

echo " "

echo "Decrypting cipher"

javac -cp ".:./jscience.jar" HillDecipher.java
java -cp ".:./jscience.jar" HillDecipher 26 8 key plain cipher

echo "Cipher decrypted" ; echo " "

echo "Plain: " ; cat plain

echo " "
echo " "
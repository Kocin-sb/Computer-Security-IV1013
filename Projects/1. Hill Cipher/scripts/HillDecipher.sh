clear
echo "Decrypting cipher"
rm plain
javac -cp ".:./jscience.jar" HillDecipher.java
java -cp ".:./jscience.jar" HillDecipher 26 3 key plain cipher
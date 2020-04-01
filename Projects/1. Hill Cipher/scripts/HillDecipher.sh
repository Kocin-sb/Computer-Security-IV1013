clear
echo "Decrypting cipher"
rm plain
javac -cp ".:./jscience.jar" HillDecipher.java
java -cp ".:./jscience.jar" HillDecipher 26 7 key plain cipher
clear
echo "Generating key:"
javac -cp ".:./jscience.jar" HillKeys.java
java -cp ".:./jscience.jar" HillKeys 256 8 key
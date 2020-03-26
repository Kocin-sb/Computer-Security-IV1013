Purpose
The purpose of this assignment is that you will design a realistic encryption application around a symmetric encryption algorithm. So you will write programs for key generation, data encryption, and data decryption that use the encryption algorithm.

The encryption algorithm for this assignment is Hill Cipher. Hill Cipher is a block cipher where encryption and decryption are based on matrix multiplication. It is described in Section 8.1.5 in Goodrich-Tamassia.

Material 
A zip archive with the files you need for this assignment is here: hill-task.zip.

Background
Hill Cipher is defined in terms of matrix operations, where the keys are matrixes, and the plaintext and the ciphertext are vectors. This means that Hill Cipher operates on data that consists of sequences of integer numbers. In other words, the plaintext and the ciphertext are number sequences, and the encryption/decryption keys are number sequences (matrixes).

Since we want to encrypt text, not numbers, we first need to translate a message to a sequence of numbers. Say that we represent "A" with number 0, "B" with number 1, etc. 

For example,  consider the key K and its inverse K-1: 

     0 23 24            25  2 17
K =  5 13 21      K-1 =  9  4  6
     1  9  2            12  7 17
To encrypt the message "MYHILLCIPHER", it is first convered to an integer sequence: 

12 24 7 8 11 11 2 8 15 7 4 17
After matrix multiplication with K, we get another integer sequence:

18 25 8 23 24 25 24 13 0 6 2 25
This corresponds to the string "SZIXYZYNAGCZ", which is our ciphertext.

Hill Cipher is a block cipher, where the block size corresponds to the dimension of the key matrix. For messages larger than the block size, we use the ECB mode of operation.

Implementation
In this assignment, a Hill Cipher implementation consists of three programs:

HillKeys is a program that generates a key matrix K. 
HillCipher is a program that takes as input a plaintext message as a sequence of integers, and an encryption key matrix K. The output is a ciphertext consisting of an integer sequence.
HillDecipher is a program that takes as input a ciphertext message as a sequence of integers, and an encryption key matrix K.  The output consists of a plaintext as a sequence of integers. 
HillKeys takes three arguments, and should be executed from the command line as follows:

$ javac HillKeys.java 
$ java HillKeys <radix> <blocksize> <keyfile>
HillKeys will generate a random encryption key and write it to the output file named <keyfile>. The key is written with one matrix row per line, and with spaces between the elements in a row.

The first argument <radix> is an integer that gives the valid range of data values. For instance, if <radix> is 26, then encryption operations are made with modulo 26 arithmetics, and all data (plaintext, ciphertext and keys) consists of values in the range 0–25.  The second argument <blocksize> is the dimension for the key matrix. For example, if dimension is 3, then the key is a 3 x 3 matrix. This corresponds to the block size.

A complication with HillKeys is that not all matrices are suitable as encryption keys. Therefore, HillKeys cannot just generate any random matrix. It also has to check that the matrix is suitable as an encryption key. For this, HillKeys checks that the matrix is invertible (or nonsingular)– that it has an inverse.

HillCipher takes five arguments, and should be executed as follows:

$ javac HillCipher.java 
$ java HillCipher <radix> <blocksize> <keyfile> <plainfile> <cipherfile>
The first two arguments are the same as for HillKeys. HillCipher will read the key from <keyfile>, the file with the name given as the third argument, read the plaintext from the file <plainfile>, and write the resulting ciphertext to the file <cipherfile>. If the length of the input data in the plaintext file is not a multiple of the blocksize, the input should be truncated to the nearest number that is a multiple of the blocksize. So if blocksize is 3, and the length of the input is 14 bytes, the input will be truncated to 12 bytes.

All data files have the same format: they are text files consisting of sequences of integers with whitespaces in between. (The whitespaces can be spaces, newlines, tabs, etc.) See the Encoding section below for an explanation of the data format.

HillDecipher takes five arguments, and should be executed as follows:

$ javac HillDecipher.java 
$ java HillDecipher <radix> <blocksize> <keyfile> <plainfile> <cipherfile>
The arguments are the same as for HillCipher. HillDecipher and HillCipher are really very similar – they use the same encryption operation. The difference is that HillDecipher computes the decryption key by inverting the key matrix before encryption. This is a hint, really: the programs can be based on the same code. 

Task 1. Hill Cipher
The first task is to implement the HillCipher program, as described above. For this assignment, you will get a pre-made encryption key of size 3 x 3. Thus, for Task 1, you do not need to implement the HillKeys program. Moreover, your program only needs to support block size 3 and radix 26. If other values are given, your program may exit with a suitable error message.

With Hill Cipher, the encryption and decryption operations are really the same (matrix multiplication). The difference is that the decryption operation uses the inverse of the encryption matrix. To help with your testing, you will get the inverse of the encryption key as well. That means that you can use your HillCipher program both for encryption and decryption.

For this task, you will get the following files to work with:

"key3-26.txt" is a key created with HillKeys for radix 26 and blocksize 3
"invkey3-26.txt"  is the corresponding inverse key
"plain-alpha.txt" is a plaintext written in an alphabet consisting only of English capital letters
"cipher-alpha.txt" is the decoded ciphertext obtained by running HillCipher with "key3-26.txt" and "plain-alpha.txt", which is first encoded. 
To summarise:
First "plain-alpha.txt" is encoded.
The result is encrypted with HillCipher and "key3-26.txt".
The output is decoded, and the result is stored in "cipher-alpha.txt".
You can find all files in the zip archive with material for this assignment: hill-task.zip. Use these files to check and debug your solutions.

Submit your solution as a zip archive called "hill1.java". To help you prepare "hill1.zip", you get a "make" configuration file called "Makefile" (also in the zip archive with material).  So to create the submission file, simply type:

$ make hill1.zip
If you want to use your own files with additional Java classes, that is OK. To do this, you need to edit "Makefile" and insert the names of the files with your classes. Follow the instructions in "Makefile"!

Task 2. HillKeys
This task consists of implementing HillKeys for any values of radix and block size. (Actually, in the grading we will use radix 256 and block size 8 at the most, so you are not expected to support larger values than that.) The format of the key files is straight-forward: a text file with one line for each row in the matrix. The numbers in a row are separated with spaces. See the key files from Task 1, for example.  

In addition, you should implement HillDecipher. HillDecipher is very similar to HillCipher; the difference is that HillDecipher inverts the encryption key.

Your submission should contain the files for both Task 1 and 2. This is handled automatically by the "make" configuration file. Thus, to submit your solution, use make in the following way:

$ make hill2.zip
Task 3. Padding
For a block cipher to be practically useful, it needs to support padding. The input data may not be aligned on an integer number of blocks. Your block size could be eight, for instance, and the binary input consist of only two bytes. Then you need to add six bytes of padding, which should be removed at decryption. Your solution should deal with this in a correct way, so that the decrypted output matches exactly with the input to the encryption. There are different ways of solving this; design one that works, and implement it. You may find that in some cases, your padding scheme will add an extra block to the ciphertext. This is completely acceptable. See for example the slides on Symmetric Key EncryptionFörhandsgranska dokumentet and PKCS #7: Cryptographic Message Syntax Version 1.5 (Länkar till en externa sida.). 

For Task 3, you should implement padding, both for encryption and decryption.  

Your submission should contain the files for Task 1, 2 and 3. To submit your solution, use make in the following way:

$ make hill3.zip
Data enoding

The internal data format that Hill Cipher uses is based on integers, since Hill Cipher operates on integer numbers. But the data we want to encrypt can be text, binary files, etc. This means that there needs to be a way to translate between data and integers – an encoding scheme. To help with this, you get two programs (Python scripts) to encode and decode data. The programs support three kinds of encoding schemes:

coding scheme	valid input	radix	description
alpha	Capital letters "A" to "Z"	26	"A" is 0, ..., "Z" is 25
ascii	ASCII-encoded letters	128	ASCII-encoding: "A" is 65, "a" is 97, etc.
binary	Any data	256	Each byte is treated as an 8-bit integer number
The programs are  hillencode.py and hilldecode.py, which are provided together with your files. Running them is straight-forward: to encode a file "plain.txt" with capital letters using "alpha" encoding, and store the result in "encoded.txt", run the following command:

$ python hillencode.py --coding=alpha plain.txt encoded.txt
Note that your programs only need to support Hill Cipher's internal data format. In other words, your programs should read and write files that consist of space-separated integer numbers. So if you want to encrypt a text file, for instance, you first need to encode the text file to the internal format, by running "hillencode.py" using the "ascii" encoding. The output of the encoding will be your plaintext. Encrypt the plaintext will "HillCipher" to get a ciphertext (in the internal data format).

To decrypt, take the file with the ciphertext in the internal data format and feed it into "HillDecipher". The result of this will be the plaintext (also in the internal data format). To verify that everything works as it should, decode the plaintext into ASCII text using "hilldecode.py" with "ascii" encoding.

Environment 

This assignment is a programming exercise, and you should use Java to solve it. You can use any Java development environment of your choice; however, it is a requirement that the code you finally submit runs on the the course virtual machine. Furthermore, your submitted code should be possible to execute directly in a Linux shell, as described above in section "Implementation", so it should not depend on any IDE tool such NetBeans or Eclipse.

Hints and Tips 
Reading Input Data
A first choice you need to make is about the general operation of the program. Should it read the entire plaintext into memory, and then do the encryption? Or should it read the plaintext block by block, and encrypt it one block at a time? If you chose the first option, and read the entire plaintext into memory, you probably need to use a dynamic data structure such as ArrayList (Länkar till en externa sida.) for storing the input data. You don't know how large the data is in advance! 

Reading block by block would be the preferred way for a production-quality application that should be able to process large data files. It is not really more complicated to implement, if you first spend some time considering how to deal with padding.  

In both cases, you will probably find the Scanner (Länkar till en externa sida.) class useful for reading integers from a file.

Matrix Operations
Hill Cipher relies on matrix operations, which are not directly supported in Java. For Task 1, you need to do matrix multiplication. You could decide to implement this yourself, it is not that difficult.  

However, for Task 2 and 3, you also need to invert matrices.  We recommend that you use JScience  (Länkar till en externa sida.)to extend Java with support for matrix operations and modulo arithmetics. In particular, consider the vector package (Länkar till en externa sida.) and the ModuloInteger (Länkar till en externa sida.) class. You may also be able to find Java programs for matrix inversion online. Use them at your own risk, and make sure to specify where the code comes from!

You will notice that the matrix operations require some work in Java. For testing, you could use Mathematica to check your program output.

Installing JScience
The JScience library is already included in the course virtual machine and is set up automatically.

If you are developing the application on a different machine, you will need to add the library. Download the jscience-4.3.1-bin.zip (Länkar till en externa sida.) archive, and extract it to an appropriate location.

If you are using NetBeans, right-click "Libraries" under your project in the "Projects" window on the left, and select Add JAR/Folder... Then navigate to where you extracted the archive and select the .jar file.

Organization 
This assignment is done individually.

Collaboration 
We encourage you to collaborate and discuss with other students, but each student must produce and submit his/her own solution. We will check solutions for originality. If two students submit solutions that are very similar, it will be treated as a case of plagiarism.

Requirements and Points

For this assignment, you can get a maximum of 100 points. The grading scale is as follows:

10 points: your submission compiles correctly, without compilation errors, and your submission meet the requirement above.
60 points: Task 1 correctly implemented: 
Takes a plaintext, consisting of a text file with a sequence of space-separated integers (in other words, the output from hillencode), and encrypts/decrypts it correctly.
The length of the plaintext is a multiple of the blocksize (3). So no padding is needed.
85 points: Task 2 correctly implemented:
For any combination of radix and block size, HillKeys finds an invertible matrix, and write the matrix to the output files.
 The maximum value for radix is 256, and for block size 8.  
Extend HillCipher from Task 1 to deal with any radix up and including 256, and any block size up to and including 8.
Use HillDecipher to decrypt any data encrypted with HillCipher.
100 points: Task 3 correctly implemented:
The solution should work for any radix up to and including 256 and block size up to and including 8.  
The solution should work for files of arbitrary size.
Proper padding so that a message with a length that is not a multiple of the block size is encrypted/decrypted correctly.   
Note that there can be deduction in points depending on the quality of your solution. If you aim for 100 points, for instance, but we assess that you solution only solves half of the challenges for Task 3 in a satisfactory manner, you might get 90 points.

Submission
Submit your solutions here by uploading your zip file. Upload one file, which is called either hill1.zip, hill2.zip or hill3.zip, depending on what tasks you have done.

You can upload as many times you like before the due date.

Testing

The grading of your submission will be made with the assistance of an automated grading tool. There will be a number of pre-runs of the tests before the final due date. See Assignments with automated grading for more information about the grading process.

Your implementation will be tested for basic input validation and error handling. If the user gives incorrect parameters, or if the input data does not match the parameters, this should be detected and an informative error message should be generated. (Java exceptions do not count as informative error messages.)

Here are some examples of tests:

Input validation
Do the input files exist and are they readable?
Are input values in the valid range? For example, if the radix is 26, check that all input values are less than 26.
Is the key a square matrix? 3 x 3, 4 x 4, 5 x 5, etc.
For HillDecipher, is the key invertible?
Can HillCipher/HillDecipher process large files (several kbytes)?  
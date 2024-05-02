package common.utilities;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


import org.apache.log4j.*;
import org.glassfish.tyrus.core.Base64Utils;
import org.junit.Test;

public class CryptoUtils {
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
	private static final Logger LOGGER =  LogManager.getLogger(CryptoUtils.class);
	 private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
	    private static final int TAG_LENGTH_BIT = 128;
	    private static final int IV_LENGTH_BYTE = 12;
	    private static final int AES_KEY_BIT = 256;
	    private static final Charset UTF_8 = StandardCharsets.UTF_8;
	    // Reference: https://mkyong.com/java/java-aes-encryption-and-decryption/
	    
	private String encrypt(String encodekey, String inputStr) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			byte[] key = encodekey.getBytes(StandardCharsets.UTF_8);
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] inputBytes = inputStr.getBytes();
			byte[] outputBytes = cipher.doFinal(inputBytes);
			return Base64Utils.encodeToString(outputBytes, false);
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return "";
	}

	
	
	public static byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    // AES secret key
    public static SecretKey getAESKey(int keysize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keysize, SecureRandom.getInstanceStrong());
        return keyGen.generateKey();
    }

    // Password derived AES 256 bits secret key
    public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        // iterationCount = 65536
        // keyLength = 256
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;

    }

    // hex representation
    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    // print hex with block size split
    public static String hexWithBlockSize(byte[] bytes, int blockSize) {

        String hex = hex(bytes);

        // one hex = 2 chars
        blockSize = blockSize * 2;

        // better idea how to print this?
        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < hex.length()) {
            result.add(hex.substring(index, Math.min(index + blockSize, hex.length())));
            index += blockSize;
        }

        return result.toString();

    }



	// AES-GCM needs GCMParameterSpec
    public static byte[] encrypt(byte[] pText, SecretKey secret, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] encryptedText = cipher.doFinal(pText);
        return encryptedText;
    }
    

    // prefix IV length + IV bytes to cipher text
    public static byte[] encryptWithPrefixIV(byte[] pText, SecretKey secret, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException  {
        byte[] cipherText = encrypt(pText, secret, iv);
        byte[] cipherTextWithIv = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();
        return cipherTextWithIv;
    }

    public static String decrypt(byte[] cText, SecretKey secret, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException  {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] plainText = cipher.doFinal(cText);
        return new String(plainText, UTF_8);
   }

    public static String decryptWithPrefixIV(byte[] cText, SecretKey secret) throws Exception {
        ByteBuffer bb = ByteBuffer.wrap(cText);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);        
        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);
        String plainText = decrypt(cipherText, secret, iv);
        return plainText;
    }
	
    public static void main(String[] args) throws Exception {
      
        String pText = "ABCDxyz1234";
        // encrypt and decrypt need the same key.
        // get AES 256 bits (32 bytes) key
        SecretKey secretKey = CryptoUtils.getAESKey(AES_KEY_BIT);

        // encrypt and decrypt need the same IV.
        // AES-GCM needs IV 96-bit (12 bytes)
        byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);

        byte[] encryptedText = encryptWithPrefixIV(pText.getBytes(UTF_8), secretKey, iv);        
        String decryptedText = decryptWithPrefixIV(encryptedText, secretKey);
        
        LOGGER.info("Secret key:"+secretKey.getEncoded());
        LOGGER.info("Encrypted text:"+encryptedText);
        LOGGER.info("Decrypted (plain text):"+decryptedText);
        
        

    }

}
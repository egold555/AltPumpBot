package org.golde.discord.pumpbot;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/*
 * Basic encrypion for our text document containing all the alts. 
 * We don't want even the admin to have access to the accounts!
 */
public class AES
{
    private byte[] key;

    private static final String ALGORITHM = "AES";

    public AES(String key)
    {
        this.key = key.getBytes(StandardCharsets.UTF_8);
    }

    public String encrypt(String plainText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return new String(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String cipherText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return new String(cipher.doFinal(cipherText.getBytes(StandardCharsets.UTF_8)));
    }
}

/* 
 * encryption BC implementation
 *
 * This file is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1, or (at your
 * option) any later version.
 *
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 *
 */

/**
 * @author ramiel <>
 * @version  v1.0.0
 *
 */
package java_encryption_lib;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

import sun.applet.Main;

public class DataVault {

    PaddedBufferedBlockCipher encryptCipher;
    PaddedBufferedBlockCipher decryptCipher;

    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[16];                 //input buffer
    byte[] obuf = new byte[512];            //output buffer

    byte[] key = null;


    public DataVault(){
        key = "SECRET_1SECRET_2SECRET_3".getBytes();
        InitCiphers();
    }
    public DataVault(byte[] keyBytes){
        key = new byte[keyBytes.length];
        System.arraycopy(keyBytes, 0 , key, 0, keyBytes.length);
        InitCiphers();
    }

    private void InitCiphers(){
        encryptCipher = new PaddedBufferedBlockCipher(new AESEngine());
        encryptCipher.init(true, new KeyParameter(key));
        decryptCipher =  new PaddedBufferedBlockCipher(new AESEngine());
        decryptCipher.init(false, new KeyParameter(key));
    }

    public void ResetCiphers() {
        if(encryptCipher!=null)
            encryptCipher.reset();
        if(decryptCipher!=null)
            decryptCipher.reset();
    }

    public void encrypt(InputStream in, OutputStream out)
    throws ShortBufferException, IllegalBlockSizeException,  BadPaddingException,
            DataLengthException, IllegalStateException, InvalidCipherTextException
    {
        try {
            // Bytes written to out will be encrypted
            // Read in the cleartext bytes from in InputStream and
            //      write them encrypted to out OutputStream

            int noBytesRead = 0;              //number of bytes read from input
            int noBytesProcessed = 0;   //number of bytes processed

            while ((noBytesRead = in.read(buf)) >= 0) {
                    //System.out.println(noBytesRead +" bytes read");

                noBytesProcessed = encryptCipher.processBytes(buf, 0, noBytesRead, obuf, 0);
                    //System.out.println(noBytesProcessed +" bytes processed");
                out.write(obuf, 0, noBytesProcessed);
            }

             //System.out.println(noBytesRead +" bytes read");
             noBytesProcessed = encryptCipher.doFinal(obuf, 0);

             //System.out.println(noBytesProcessed +" bytes processed");
             out.write(obuf, 0, noBytesProcessed);

            out.flush();
        }
        catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void decrypt(InputStream in, OutputStream out)
    throws ShortBufferException, IllegalBlockSizeException,  BadPaddingException,
            DataLengthException, IllegalStateException, InvalidCipherTextException
    {
        try {
            // Bytes read from in will be decrypted
            // Read in the decrypted bytes from in InputStream and and
            //      write them in cleartext to out OutputStream

            int noBytesRead = 0;        //number of bytes read from input
            int noBytesProcessed = 0;   //number of bytes processed

            while ((noBytesRead = in.read(buf)) >= 0) {
                    //System.out.println(noBytesRead +" bytes read");
                    noBytesProcessed = decryptCipher.processBytes(buf, 0, noBytesRead, obuf, 0);
                    //System.out.println(noBytesProcessed +" bytes processed");
                    out.write(obuf, 0, noBytesProcessed);
            }
            //System.out.println(noBytesRead +" bytes read");
            noBytesProcessed = decryptCipher.doFinal(obuf, 0);
            //System.out.println(noBytesProcessed +" bytes processed");
            out.write(obuf, 0, noBytesProcessed);

            out.flush();
        }
        catch (java.io.IOException e) {
             System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
        	 DataVault dv = new DataVault();
        	 String env = System.getenv("ATA_Home");
            FileInputStream fis =
                    new FileInputStream(new File(env + "/bouncy_clear.txt"));
            FileOutputStream fos =
                    new FileOutputStream(new File(env + "/bouncy_enc.txt"));
      
            
       
            //solution 1
            //BouncyCastleAPI_AES_CBC bc = new BouncyCastleAPI_AES_CBC();
            //solution 2
           BouncyCastleProvider_AES_CBC bc=new BouncyCastleProvider_AES_CBC();
           /* bc.InitCiphers();
 
            //encryption
            bc.CBCEncrypt(fis, fos);*/
           dv.encrypt(fis, fos);
           BufferedReader br2 = new BufferedReader(new FileReader(env + "/bouncy_enc.txt"));
           String line1 = null;
           while ((line1 = br2.readLine()) != null) {
             System.out.println(line1);
           }
            fis = new FileInputStream(new File(env + "/bouncy_enc.txt"));
            fos = new FileOutputStream(new File(env + "/bouncy_clear_dec.txt"));
            
            
            dv.decrypt(fis, fos);
            
           
            BufferedReader br = new BufferedReader(new FileReader(env + "/bouncy_clear_dec.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
              System.out.println(line);
            }
            
            
            //decryption
            //bc.CBCDecrypt(fis, fos);
 
        } catch (ShortBufferException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataLengthException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        System.out.println("Test done !");
    	}
}
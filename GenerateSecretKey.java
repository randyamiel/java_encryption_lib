/**
 *  This file is part of the ATA project from Avaap.
  Copyright (C) 2017 Randy Amiel <randy@alumni.ufl.edu>
  Copyright (C) 2017 Avaap USA, LLC <info@avaap.com>
  
  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
  ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
 
package java_encryption_lib;


import java.security.Key;
import java.util.Formatter;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * @author r
 *
 */
public class GenerateSecretKey {

	/**
	 * 
	 */
	public GenerateSecretKey() {
		// TODO Auto-generated constructor stub
	}
	
	// Byte array tp hex conversion for md5 checksum
		@SuppressWarnings({ "resource", "unused" })
		private static String byteArray2Hex(byte[] hash) {
	        Formatter formatter = new Formatter();
	        for (byte b : hash) {
	            formatter.format("%02x", b);
	        }
	        return formatter.toString();
		}
	 private static String formatKey(Key key){
		    StringBuffer sb = new StringBuffer();
		    String algo = key.getAlgorithm();
		    String fmt = key.getFormat();
		    byte[] encoded = key.getEncoded();
		    sb.append("Key[algorithm=" + algo + ", format=" + fmt +
		        ", bytes=" + encoded.length + "]\n");
		    if (fmt.equalsIgnoreCase("RAW")){
		      sb.append("Key Material (in hex):: ");
		      sb.append(byteArray2Hex(key.getEncoded()));
		    }
		    return sb.toString();
		  }
	 
	 
		  public static void main(String[] unused) throws Exception {
		    KeyGenerator kg = KeyGenerator.getInstance("AES");
		    kg.init(256); // 256
		    SecretKey key = kg.generateKey();
		    System.out.println("Generated Key:: " + formatKey(key));
		  }

}

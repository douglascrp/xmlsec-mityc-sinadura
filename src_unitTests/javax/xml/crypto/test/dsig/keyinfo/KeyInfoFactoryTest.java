/*
 * Copyright 2006 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 */
package javax.xml.crypto.test.dsig.keyinfo;

import java.math.BigInteger;
import java.util.*;
import java.security.KeyException;
import java.security.PublicKey;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dom.*;

import junit.framework.*;

/**
 * Unit test for javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
 *
 * @version $Id$
 * @author Valerie Peng
 */
public class KeyInfoFactoryTest extends TestCase {

    KeyInfoFactory factory;

    public KeyInfoFactoryTest(String name) {
	super(name);
    }

    public void setUp() throws Exception { 
	factory = KeyInfoFactory.getInstance
	    ("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());
    }

    public void tearDown() { }

    public void testgetInstance() {
	try {
	    KeyInfoFactory fac = KeyInfoFactory.getInstance("non-existent");
	    fail("Should throw NoSuchMechanismException if no impl found");
	} catch (NoSuchMechanismException ex) {}

	try {
	    KeyInfoFactory fac = KeyInfoFactory.getInstance(null);
	    fail("Should raise a NPE for null xmltype"); 
	} catch (NullPointerException npe) {}
    }

    public void testgetMechanismType() {
	assertNotNull(factory);
	assertEquals("DOM", factory.getMechanismType());
    }

    public void testisFeatureSupported() {
	try {
	    factory.isFeatureSupported(null); 
	    fail("Should raise a NPE for null feature"); 
	} catch (NullPointerException npe) {}

	assertTrue(!factory.isFeatureSupported("not supported"));
    }

    public void testnewKeyInfo() {
	String id = "keyId";
	// test newKeyInfo(List, String)
	KeyInfo ki = factory.newKeyInfo
	    (Collections.singletonList(factory.newKeyName("foo")), id);
	assertEquals(id, ki.getId());
	try {
	    ki = factory.newKeyInfo(null, id); 
	    fail("Should raise a NPE for null key info types"); 
	} catch (NullPointerException npe) {}
    }

    public void testnewKeyName() {
	final String name = "keyName";
	KeyName kn = factory.newKeyName(name);
	assertEquals(name, kn.getName());
	try {
	    kn = factory.newKeyName(null); 
	    fail("Should raise a NPE for null key name"); 
	} catch (NullPointerException npe) {}
    }

    public void testnewKeyValue() {
	// test newKeyValue(PublicKey pk)
	PublicKey myPubKey = new PublicKey() {
		public byte[] getEncoded() {
		    return new byte[20];
		}
		public String getFormat() {
		    return "none";
		}
		public String getAlgorithm() {
		    return "test";
		}
	    };
	try {
	    KeyValue kv = factory.newKeyValue(myPubKey);
	    assertEquals(myPubKey, kv.getPublicKey());
	    fail("Should throw a KeyException");
	} catch (KeyException ke) { }
  
	try {
	    KeyValue kv = factory.newKeyValue((PublicKey) null); 
	    fail("Should raise a NPE for null key"); 
	} catch (KeyException ke) {
	    fail("Should raise a NPE for null key"); 
	} catch (NullPointerException npe) {}
    }

    public void testnewPGPKeyId() {
	byte[] valid_id = { 
	    0x01, 0x02, 0x03, 0x04,
	    0x05, 0x06, 0x07, 0x08 
	};
	byte[] invalid_id = {
	    0x01, 0x02, 0x03, 0x04,
	    0x05, 0x06, 0x07, 0x08,
	    0x09
	};
	byte[] valid_packet = { (byte)0xc6, (byte)0x01, (byte)0x00 };
	byte[] invalid_packet = { (byte)0xc8, (byte)0x01, (byte)0x00 };

	// test newPGPData(byte[])
	PGPData pd = factory.newPGPData(valid_id);
	assertTrue(Arrays.equals(valid_id, pd.getKeyId()));
	try {
	    pd = factory.newPGPData(invalid_id);
	    fail("Should throw IAE for invalid key id values");
	} catch (IllegalArgumentException ex) {}

	// test newPGPData(byte[], byte[], List)
	pd = factory.newPGPData(valid_id, valid_packet, null);
	assertTrue(Arrays.equals(valid_id, pd.getKeyId()));
	assertTrue(Arrays.equals(valid_packet, pd.getKeyPacket()));
	try {
	    pd = factory.newPGPData(invalid_id, valid_packet, null);
	    fail("Should throw IAE for invalid key id values");
	} catch (IllegalArgumentException ex) {}
	try {
	    pd = factory.newPGPData(valid_id, invalid_packet, null);
	    fail("Should throw IAE for invalid key packet values");
	} catch (IllegalArgumentException ex) {}
	try {
	    pd = factory.newPGPData(invalid_id, invalid_packet, null);
	    fail("Should throw IAE for invalid key id and packet values");
	} catch (IllegalArgumentException ex) {}

	// test newPGPData(byte[], List)
	pd = factory.newPGPData(valid_packet, null);
	assertTrue(Arrays.equals(valid_packet, pd.getKeyPacket()));
	try {
	    pd = factory.newPGPData(invalid_packet, null);
	    fail("Should throw IAE for invalid key packet values");
	} catch (IllegalArgumentException ex) {}
    }

    public void testnewRetrievalMethod() throws Exception {
        final String uri = "#X509CertChain";
        // test RetrievalMethod(String)
        RetrievalMethod rm = factory.newRetrievalMethod(uri);
	assertEquals(uri, rm.getURI());

	try {
	    rm = factory.newRetrievalMethod(null); 
	    fail("Should raise a NPE for null URI"); 
        } catch (NullPointerException npe) {}

	// test RetrievalMethod(String, String, List)	
	try {
	    rm = factory.newRetrievalMethod(null, null, null); 
	    fail("Should raise a NPE for null URI"); 
        } catch (NullPointerException npe) {}
	
	String type = "http://www.w3.org/2000/09/xmldsig#X509Data";
	try {
	    rm = factory.newRetrievalMethod(null, type, null); 
	    fail("Should raise a NPE for null URI"); 
        } catch (NullPointerException npe) {}

        rm = factory.newRetrievalMethod(uri, type, null);
        assertEquals(uri, rm.getURI());
        assertEquals(type, rm.getType());
    }

    public void testnewX509Data() {
	// test newX509Data(List)
	X509Data x509 = 
	    factory.newX509Data(Collections.singletonList("cn=foo"));
	assertNotNull(x509);
    }

    public void testnewX509IssuerSerial() {
	String name = "CN=valeriep";
	// test newX509IssuerSerial(String, BigInteger)
	X509IssuerSerial x509is = factory.newX509IssuerSerial(name, 
							      BigInteger.ONE);
	assertEquals(name, x509is.getIssuerName());
	assertEquals(BigInteger.ONE, x509is.getSerialNumber());
	try {
	    x509is = factory.newX509IssuerSerial(null, BigInteger.ZERO);
	    fail("Should raise an NPE for null issuer names"); 
	} catch (NullPointerException ex) {
	} catch (IllegalArgumentException ex2) {
	    fail("Should throw NPE instead of IAE for null issuer names");
	}
	try {
	    x509is = factory.newX509IssuerSerial(name, null);
	    fail("Should raise an NPE for null serial numbers"); 
	} catch (NullPointerException ex) {
	} catch (IllegalArgumentException ex2) {
	    fail("Should throw NPE instead of IAE for null serial numbers");
	}
	try {
	    x509is = factory.newX509IssuerSerial(null, null);
	    fail("Should raise an NPE for null issuer names/serial numbers"); 
	} catch (NullPointerException ex) {
	} catch (IllegalArgumentException ex2) {
	    fail("Should throw NPE instead of IAE for null issuer " + 
		 "names/serial numbers");
	}
	try {
	    x509is = factory.newX509IssuerSerial("valeriep", BigInteger.ZERO);
	    fail("Should throw IAE for invalid issuer names"); 
	} catch (IllegalArgumentException ex) {}
    }

    // for debugging purposes
    public static void main(String[] args) throws Exception {
	KeyInfoFactoryTest kifTest = 
	    new KeyInfoFactoryTest("KeyInfoFactoryTest");
	kifTest.setUp();
    }
}

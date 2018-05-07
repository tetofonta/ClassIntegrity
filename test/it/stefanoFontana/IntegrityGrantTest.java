/*
 * Copyright (C) 2018 - stefano
 *
 * This file is part of ClassIntegrity
 *
 * ClassIntegrity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClassIntegrity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClassIntegrity.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.stefanoFontana;

import it.stefanoFontana.annotations.IntegrityCheck;
import it.stefanoFontana.exceptions.HashingException;
import it.stefanoFontana.templates.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class IntegrityGrantTest {

    IntegrityGrant tst;

    @TestFactory
    public Collection<DynamicTest> classObtain() {

        class std1 extends IntegrityGrant implements Serializable {
            @IntegrityCheck
            String a = "a";
            @IntegrityCheck
            int b = 6;
            @IntegrityCheck
            Object c = new Serializable() {};

            @Override
            protected Methods setMethods() {
                return new Methods_SHA256_OBJ_ENCODED_CONCAT();
            }
        }
        class std2 extends IntegrityGrant implements Serializable {
            @IntegrityCheck
            String a = "Ste";
            @IntegrityCheck
            String b = "fa";
            @IntegrityCheck
            String c = "no F";
            @IntegrityCheck
            String d = "on";
            @IntegrityCheck
            String e = "t";
            @IntegrityCheck
            String f = "an";
            @IntegrityCheck
            String g = "a";

            @Override
            protected Methods setMethods() {
                return new Methods_SHA256_STR_HASH_CONCAT();
            }
        }
        class std3 extends IntegrityGrant {
        }
        class std4_1 extends IntegrityGrant implements Serializable {
            @IntegrityCheck
            String s = "aaaa";

            std4_1() throws HashingException {
                updateHash();
            }

            @Override
            protected Methods setMethods() {
                return new Methods_SHA256_STR_CONCAT();
            }
        }
        class std4 extends IntegrityGrant implements Serializable {
            @IntegrityCheck
            std4_1 s = new std4_1();

            std4() throws HashingException {
            }

            @Override
            protected Methods setMethods() {
                return new Methods_SHA256_STR_ENCODED_CONCAT();
            }
        }
        class std5 extends IntegrityGrant implements Serializable {
            @IntegrityCheck
            int[] vector = {3, 4, 5, 6, 7, 8};
            @Override
            protected Methods setMethods() {
                return new Methods_SHA256_OBJ_ENCODED_CONCAT();
            }
        }

        return Arrays.asList(
                dynamicTest("Standard class", () -> retCorrect("be9105e88ade49ed34b3b896d2a28f1128e80a80136cb8e06522d68fbc9ff1c2", new std1())),
                dynamicTest("Only Strings", () -> retCorrect("a22d826d5a9c92fdd86feda3826c8b390c258762a985583d1e099e5208f18637", new std2())),
                dynamicTest("NoAttributes", () -> retCorrect("0", new std3())),
                dynamicTest("inside", () -> retCorrect("7d0fc826ccf2a71d3131d54053392e09196ba46c9d54a63261af08e526a0bd26", new std4())),
                dynamicTest("Vectors", () -> retCorrect("6c8bde7832124aebc3ec0af996da45423457893454fb56d510fe0564902af987", new std5()))
        );
    }

    public void retCorrect(String expected, IntegrityGrant aa) throws HashingException {
        aa.updateHash();
        aa.checkHash();
        assertEquals(expected, aa.showHash());
    }

    @Test
    public void testCheck() throws HashingException, NoSuchFieldException, IllegalAccessException {
        class std1 extends IntegrityGrant {
            @IntegrityCheck
            String a = "a";
            @IntegrityCheck
            int b = 6;
            @IntegrityCheck
            Object c = new Object();

            @Override
            protected Methods setMethods() {
                return new Methods_SHA256_STR_HASH_CONCAT();
            }
        }
        std1 obj = new std1();
        obj.updateHash();
        obj.checkHash();

        Field f = IntegrityGrant.class.getDeclaredField("hash");
        f.setAccessible(true);

        HashMap<String, String> h = (HashMap<String, String>) f.get(obj);
        h.put("@it.stefanoFontana.annotations.IntegrityCheck()", "cisaooo");
        assertThrows(HashingException.class, obj::checkHash);
    }

    static <T extends Serializable> String getObj(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static <T> T readObj(String in) throws IOException, ClassNotFoundException {
        return (T) new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(in))).readObject();
    }

    @Test
    public void transmission() throws HashingException, IOException, ClassNotFoundException {
        Message s = new Message();
        s.setH();
        s.check();

        Message n = readObj(getObj(s));
        assertEquals(s.showHash(), n.showHash());
    }

}
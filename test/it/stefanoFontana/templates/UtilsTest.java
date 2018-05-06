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

package it.stefanoFontana.templates;

import it.stefanoFontana.IntegrityGrant;
import it.stefanoFontana.annotations.IntegrityCheck;
import it.stefanoFontana.exceptions.HashingException;
import jdk.jshell.execution.Util;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class UtilsTest {

    @TestFactory
    public Collection<DynamicTest> SHAtest() {
        return Arrays.asList(
                dynamicTest("SHA256 Empty string", () -> assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", Utils.bytesToHex(Utils.hash("")))),
                dynamicTest("SHA256 ciao", () -> assertEquals("b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2", Utils.bytesToHex(Utils.hash("ciao")))),
                dynamicTest("SHA256 0000", () -> assertEquals("9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0", Utils.bytesToHex(Utils.hash("0000")))),
                dynamicTest("SHA256 Long string", () -> assertEquals("576f3d916911de56636ad82983561b675384e97f7f52e1c58a53d633722199cd", Utils.bytesToHex(Utils.hash("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"))))
        );
    }


    private static <T> T readObj(String in) throws IOException, ClassNotFoundException {
        return (T) new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(in))).readObject();
    }

    @Test
    public void getObj() throws HashingException, IOException, ClassNotFoundException {
        Message msg = new Message();
        msg.setH();

        int prevHash = msg.hashCode();
        msg = readObj(Utils.getObj(msg));
        assertEquals(prevHash, msg.hashCode());
        msg.check();
    }
}
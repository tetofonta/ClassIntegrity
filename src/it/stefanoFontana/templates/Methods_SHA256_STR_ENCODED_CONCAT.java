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

import it.stefanoFontana.Methods;
import it.stefanoFontana.SuperField;
import it.stefanoFontana.exceptions.HashingException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Base64;

public final class Methods_SHA256_STR_ENCODED_CONCAT implements Methods {
    @Override
    public String getHash(String data) throws HashingException {
        return Utils.bytesToHex(Utils.hash(data));
    }

    @Override
    public String getObject(SuperField o) {
        try {
            StringBuilder ret = new StringBuilder("{");

            if (o.getF().getType().isArray()) {
                Object array = o.getF().get(o.getRef());
                int len = Array.getLength(array);
                for (int q = 0; q < len; q++) ret.append((Array.get(array, q)).toString()).append(", ");
                return ret.append("}").toString();
            } else return Base64.getEncoder().encodeToString(o.getF().get(o.getRef()).toString().getBytes("UTF-8"));
        } catch (IllegalAccessException | UnsupportedEncodingException e) {
            return null;
        }
    }
}

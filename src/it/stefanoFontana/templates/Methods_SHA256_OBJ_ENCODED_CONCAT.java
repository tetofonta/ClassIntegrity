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
import it.stefanoFontana.exceptions.HashingException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * This is a model for the hashing process.
 * Given a set of fields to be hashed, the final hash will be
 * the SHA256 hash of the concatened base64 representation serialized object of each single field
 * Returns null in case of unserializable object.
 */
public final class Methods_SHA256_OBJ_ENCODED_CONCAT implements Methods {

    @Override
    public String getHash(String data) throws HashingException {
        return Utils.bytesToHex(Utils.hash(data));
    }

    @SuppressWarnings("Duplicates")
    @Override
    public String getObject(Field f, Object o) {
        try {
            if (f.getType().isArray()) {
                Object array = f.get(o);
                int len = Array.getLength(array);
                Object[] ar = new Object[len];
                for (int q = 0; q < len; q++) ar[q] = Array.get(array, q);
                return Utils.getObj(ar);
            } else return Utils.getObj(f.get(o));
        } catch (IllegalAccessException | IOException e) {
            return null;
        }
    }
}

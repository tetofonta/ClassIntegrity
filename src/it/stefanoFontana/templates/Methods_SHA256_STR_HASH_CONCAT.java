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

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * This is a model for the hashing process.
 * Given a set of fields to be hashed, the final hash will be
 * the SHA256 of the concatened hashes of each single field
 */
public final class Methods_SHA256_STR_HASH_CONCAT implements Methods {
    @Override
    public String getHash(String data) throws HashingException {
        return Utils.bytesToHex(Utils.hash(data));
    }


    @Override
    public String getObject(Field f, Object o) {
        try {
            StringBuilder ret = new StringBuilder("{");

            if (f.getType().isArray()) {
                Object array = f.get(o);
                int len = Array.getLength(array);
                for (int q = 0; q < len; q++) ret.append((Array.get(array, q)).toString()).append(", ");
                return getHash(ret.append("}").toString());
            } else return getHash(f.get(o).toString());
        } catch (IllegalAccessException | HashingException e) {
            return null;
        }
    }
}

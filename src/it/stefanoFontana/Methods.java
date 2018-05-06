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

import it.stefanoFontana.exceptions.HashingException;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public interface Methods extends Serializable {

    /**
     * Defines the method used to retrive the hash code of a {@code String}
     * Only {@code String} are used for hashing, {@see getObject}.
     * @param data {@code String} passed containing the data to be hashed.
     * @return a {@code String} containing the hash of the data passed.
     * @throws HashingException Not thrown by default, can be thrown in user-default code.
     */
    default String getHash(String data) throws HashingException {
        return String.valueOf(data.hashCode());
    }

    /**
     * Defines the method used to retrive an object representation as {@code String}.
     * @param f The {@code Field} containing the info, {@see Reflection}
     * @param o The {@code Object} who references the Field {@see f}.
     * @return The {@code String} representation of the object passed.
     */
    default String getObject(Field f, Object o) {
        try {
            StringBuilder ret = new StringBuilder("{");

            if (f.getType().isArray()) {
                Object array = f.get(o);
                int len = Array.getLength(array);
                for (int q = 0; q < len; q++) ret.append((Array.get(array, q)).toString()).append(", ");
                return ret.append("}").toString();
            } else return f.get(o).toString();
        } catch (IllegalAccessException e) {
            return null;
        }
    }


}

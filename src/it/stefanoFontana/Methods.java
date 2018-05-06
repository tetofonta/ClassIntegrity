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

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public interface Methods {

    default String getHash(String data) throws HashingException {
        return String.valueOf(data.hashCode());
    }

    default String getObject(SuperField o){
        try {
            StringBuilder ret = new StringBuilder("{");

            if (o.getF().getType().isArray()) {
                Object array = o.getF().get(o.getRef());
                int len = Array.getLength(array);
                for (int q = 0; q < len; q++) ret.append((Array.get(array, q)).toString()).append(", ");
                return ret.append("}").toString();
            } else return o.getF().get(o.getRef()).toString();
        } catch (IllegalAccessException e){
            return null;
        }
    }

}

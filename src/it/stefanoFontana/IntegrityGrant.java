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

package it.stefanoFontana;/*
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

import it.stefanoFontana.exceptions.HashingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

public class IntegrityGrant {

    private Methods utils = new Methods() {};
    private HashMap<String, String> hash = new HashMap<>();

    protected Methods setMethods(){return new Methods(){};}

    private String getHashingData(String inter) {

        utils = setMethods();

        StringBuilder toHash = new StringBuilder();
        Field[] getMyAttributes = this.getClass().getDeclaredFields();
        for (Field i : getMyAttributes) {
            Annotation[] ans = i.getDeclaredAnnotations();
            for (Annotation a : ans) {
                if (a.toString().equals(inter)) {
                    i.setAccessible(true);
                    toHash.append(utils.getObject(new SuperField(i, this)));
                    break;
                }
            }
        }
        return toHash.toString();
    }

    private String getFieldsHash(String iface) throws HashingException {
        return utils.getHash(getHashingData(iface));
    }

    protected final void updateHash() throws HashingException {
        hash.put("@it.stefanoFontana.annotations.IntegrityCheck()", getFieldsHash("@it.stefanoFontana.annotations.IntegrityCheck()"));
    }

    protected void updateHash(String interfaceName) throws HashingException {
        hash.put(interfaceName, getFieldsHash(interfaceName));
    }

    protected final void checkHash() throws HashingException {
        if(! hash.get("@it.stefanoFontana.annotations.IntegrityCheck()").equals(getFieldsHash("@it.stefanoFontana.annotations.IntegrityCheck()"))) throw new HashingException("Wrong hashes");
    }

    protected void checkHash(String iface) throws HashingException {
        if(! hash.get(iface).equals(getFieldsHash(iface))) throw new HashingException("Wrong hashes");
    }

}

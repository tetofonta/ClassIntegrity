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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

public class IntegrityGrant implements Serializable {

    @IntegrityCheck
    private final String Version = "0.1.2";

    private Methods utils = new Methods() {};
    private final HashMap<String, String> hash = new HashMap<>();

    /**
     * @return Class hashCode of its components
     */
    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + hash.hashCode();
        return result;
    }

    /**
     * This fuction is executed every time before the start of an new hash calculation.
     * The method is designed to set the default Methods class {@see Methods} defining
     * how to get hash and object.
     * @return A new class implementing Methods.
     */
    protected Methods setMethods() {
        return new Methods() {
        };

    }

    private String getHashingData(String inter) {

        utils = setMethods();

        StringBuilder toHash = new StringBuilder();
        Field[] getMyAttributes = this.getClass().getDeclaredFields();
        for (Field i : getMyAttributes) {
            Annotation[] ans = i.getDeclaredAnnotations();
            for (Annotation a : ans) {
                if (a.toString().equals(inter)) {
                    i.setAccessible(true);
                    toHash.append(utils.getObject(i, this));
                    break;
                }
            }
        }
        return toHash.toString();
    }

    private String getFieldsHash(String iface) throws HashingException {
        utils = setMethods();
        return utils.getHash(getHashingData(iface));
    }

    /**
     * Updates class hash for Fields with the standard annotation {@see IntegrityCheck}
     * in it.stefanoFontana.annotations
     * @throws HashingException In case of exception during the hashing function execution
     */
    protected final void updateHash() throws HashingException {
        hash.put("@it.stefanoFontana.annotations.IntegrityCheck()", getFieldsHash("@it.stefanoFontana.annotations.IntegrityCheck()"));
    }

    /**
     * Updates class hash for Fields with the annotation passed as parameter.
     * Generally the format used is {@code @<package>.annotationInterface}
     * @param interfaceName The interface representation of the interface wanted.
     * @throws HashingException In case of exception during the hashing function execution
     */
    protected void updateHash(String interfaceName) throws HashingException {
        hash.put(interfaceName, getFieldsHash(interfaceName));
    }

    /**
     * Checkes the hash for Fields with the standard annotation {@see IntegrityCheck}
     * in it.stefanoFontana.annotations
     * @throws HashingException In case the hash saved is different to the one generated.
     */
    protected final void checkHash() throws HashingException {
        if (!hash.get("@it.stefanoFontana.annotations.IntegrityCheck()").equals(getFieldsHash("@it.stefanoFontana.annotations.IntegrityCheck()")))
            throw new HashingException("Wrong hashes");
    }

    /**
     * Checkes the hash for Fields with the annotation passed as parameter.
     * Generally the format used is {@code @<package>.annotationInterface}
     * @param iface The interface representation of the interface wanted.
     * @throws HashingException In case the hash saved is different to the one generated.
     */
    protected void checkHash(String iface) throws HashingException {
        if (!hash.get(iface).equals(getFieldsHash(iface))) throw new HashingException("Wrong hashes");
    }

    /**
     * @return The hash saved for the standard annotation {@see IntegrityCheck}
     * in it.stefanoFontana.annotations
     */
    public final String showHash(){
        return hash.get("@it.stefanoFontana.annotations.IntegrityCheck()");
    }

    /**
     *
     * @param iface The interface representation of the interface wanted.
     * @return The hash saved for the interface passed
     */
    public String showHash(String iface){
        return hash.get(iface);
    }

}

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
import it.stefanoFontana.Methods;
import it.stefanoFontana.annotations.IntegrityCheck;
import it.stefanoFontana.exceptions.HashingException;

import java.io.Serializable;

public class Message extends IntegrityGrant implements Serializable {
    @IntegrityCheck
    public String a = "Stefano";

    public void setH() throws HashingException {
        updateHash();
    }

    public void check() throws HashingException {
        checkHash();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (a != null ? a.hashCode() : 0);
        return result;
    }

    @Override
    protected Methods setMethods() {
        return new Methods_SHA256_STR_HASH_CONCAT();
    }
}
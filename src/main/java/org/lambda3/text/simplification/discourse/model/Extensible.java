/*
 * ==========================License-Start=============================
 * DiscourseSimplification : Extensible
 *
 * Copyright © 2018 Lambda³
 *
 * GNU General Public License 3
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * ==========================License-End==============================
 */

package org.lambda3.text.simplification.discourse.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lambda3.text.simplification.discourse.model.serializer.ExtensionsDeserializer;
import org.lambda3.text.simplification.discourse.model.serializer.ExtensionsSerializer;

import java.util.*;

public abstract class Extensible {
    public static final String LIST_KEY = "_list_";

    @JsonSerialize(using = ExtensionsSerializer.class)
    @JsonDeserialize(using = ExtensionsDeserializer.class)
    private Map<Object, Object> extensions = new HashMap<>();

    public <O> void addListExtension(O o) {
        List<O> list = (List<O>) this.extensions.computeIfAbsent(new Key(o.getClass(), LIST_KEY), l -> new LinkedList<O>());
        list.add(o);
    }

    public <O> List<O> getListExtension(Class<O> clazz) {
        return (List<O>) this.extensions.getOrDefault(new Key(clazz, LIST_KEY), Collections.EMPTY_LIST);
    }

    public <O> void addExtension(O o) {
        this.extensions.put(o.getClass(), o);
    }

    public void addExtension(String key, Object o) {
        this.extensions.put(new Key(o.getClass(), key), o);
    }

    public <O> O getExtension(Class<O> clazz) {
        return (O) this.extensions.get(clazz);
    }

    public <O> O getExtension(Class<O> clazz, String key) {
        return (O) this.extensions.get(new Key(clazz, key));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extensible that = (Extensible) o;
        return Objects.equals(extensions, that.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extensions);
    }

    public static class Key {
        public final Class<?> clazz;
        public final String desc;

        public Key(Class<?> clazz, String desc) {
            this.clazz = clazz;
            this.desc = desc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(clazz, key.clazz) &&
                    Objects.equals(desc, key.desc);
        }

        @Override
        public int hashCode() {

            return Objects.hash(clazz, desc);
        }

        @Override
        public String toString() {
            return "Key{" +
                    "clazz=" + clazz +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
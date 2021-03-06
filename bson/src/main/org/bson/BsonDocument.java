/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bson;

import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.json.JsonReader;
import org.bson.json.JsonWriter;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

/**
 * A type-safe container for a BSON document.
 *
 * @since 3.0
 */
public class BsonDocument extends BsonValue implements Map<String, BsonValue>, Serializable, Bson {
    private static final long serialVersionUID = -8366220692735186027L;

    private final Map<String, BsonValue> map = new LinkedHashMap<String, BsonValue>();

    /**
     *
     * Create a BsonDocument from a JSON String representation.
     *
     * @param json a JSON string
     * @return a BSON document
     */
    public static BsonDocument parse(final String json) {
        return new BsonDocumentCodec().decode(new JsonReader(json), DecoderContext.builder().build());
    }

    /**
     * Construct a new instance with the given list {@code BsonElement}, none of which may be null.
     *
     * @param bsonElements a list of {@code BsonElement}
     */
    public BsonDocument(final List<BsonElement> bsonElements) {
        for (BsonElement cur : bsonElements) {
            put(cur.getName(), cur.getValue());
        }
    }

    /**
     * Construct a new instance with a single key value pair
     *
     * @param key   the key
     * @param value the value
     */
    public BsonDocument(final String key, final BsonValue value) {
        put(key, value);
    }

    /**
     * Construct an empty document.
     */
    public BsonDocument() {
    }

    @Override
    public <C> BsonDocument toBsonDocument(final Class<C> documentClass, final CodecRegistry codecRegistry) {
        return this;
    }

    @Override
    public BsonType getBsonType() {
        return BsonType.DOCUMENT;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    @Override
    public BsonValue get(final Object key) {
        return map.get(key);
    }

    /**
     * Gets the value of the key if it is a BsonDocument, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonDocument
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not a BsonDocument
     */
    public BsonDocument getDocument(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asDocument();
    }

    /**
     * Gets the value of the key if it is a BsonArray, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonArray
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonArray getArray(final Object key) {
        throwIfKeyAbsent(key);

        return get(key).asArray();
    }

    /**
     * Gets the value of the key if it is a BsonNumber, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonNumber
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonNumber getNumber(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asNumber();
    }

    /**
     * Gets the value of the key if it is a BsonInt32, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonInt32
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonInt32 getInt32(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asInt32();
    }

    /**
     * Gets the value of the key if it is a BsonInt64, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonInt64
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonInt64 getInt64(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asInt64();
    }

    /**
     * Gets the value of the key if it is a BsonDouble, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonDouble
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonDouble getDouble(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asDouble();
    }

    /**
     * Gets the value of the key if it is a BsonBoolean, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonBoolean
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonBoolean getBoolean(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asBoolean();
    }

    /**
     * Gets the value of the key if it is a BsonString, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonString
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonString getString(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asString();
    }

    /**
     * Gets the value of the key if it is a BsonDateTime, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonDateTime
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonDateTime getDateTime(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asDateTime();
    }

    /**
     * Gets the value of the key if it is a Timestamp, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a Timestamp
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonTimestamp getTimestamp(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asTimestamp();
    }

    /**
     * Gets the value of the key if it is a BsonObjectId, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a BsonObjectId
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonObjectId getObjectId(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asObjectId();
    }

    /**
     * Gets the value of the key if it is a RegularExpression, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a RegularExpression
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonRegularExpression getRegularExpression(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asRegularExpression();
    }

    /**
     * Gets the value of the key if it is a Binary, or throws if not.
     *
     * @param key the key
     * @return the value of the key as a Binary
     * @throws org.bson.BsonInvalidOperationException if the document does not contain the key or the value is not of the expected type
     */
    public BsonBinary getBinary(final Object key) {
        throwIfKeyAbsent(key);
        return get(key).asBinary();
    }

    /**
     * Returns true if the value of the key is a BsonNull, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonNull, returns false if the document does not contain the key.
     */
    public boolean isNull(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isNull();
    }

    /**
     * Returns true if the value of the key is a BsonDocument, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonDocument, returns false if the document does not contain the key.
     */
    public boolean isDocument(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isDocument();
    }

    /**
     * Returns true if the value of the key is a BsonArray, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonArray, returns false if the document does not contain the key.
     */
    public boolean isArray(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isArray();
    }

    /**
     * Returns true if the value of the key is a BsonNumber, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonNumber, returns false if the document does not contain the key.
     */
    public boolean isNumber(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isNumber();
    }

    /**
     * Returns true if the value of the key is a BsonInt32, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonInt32, returns false if the document does not contain the key.
     */
    public boolean isInt32(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isInt32();
    }

    /**
     * Returns true if the value of the key is a BsonInt64, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonInt64, returns false if the document does not contain the key.
     */
    public boolean isInt64(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isInt64();
    }

    /**
     * Returns true if the value of the key is a BsonDouble, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonDouble, returns false if the document does not contain the key.
     */
    public boolean isDouble(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isDouble();
    }

    /**
     * Returns true if the value of the key is a BsonBoolean, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonBoolean, returns false if the document does not contain the key.
     */
    public boolean isBoolean(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isBoolean();
    }

    /**
     * Returns true if the value of the key is a BsonString, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonString, returns false if the document does not contain the key.
     */
    public boolean isString(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isString();
    }

    /**
     * Returns true if the value of the key is a BsonDateTime, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a BsonDateTime, returns false if the document does not contain the key.
     */
    public boolean isDateTime(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isDateTime();
    }

    /**
     * Returns true if the value of the key is a Timestamp, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a Timestamp, returns false if the document does not contain the key.
     */
    public boolean isTimestamp(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isTimestamp();
    }

    /**
     * Returns true if the value of the key is a ObjectId, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a ObjectId, returns false if the document does not contain the key.
     */
    public boolean isObjectId(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isObjectId();
    }

    /**
     * Returns true if the value of the key is a Binary, returns false if the document does not contain the key.
     *
     * @param key the key
     * @return true if the value of the key is a Binary, returns false if the document does not contain the key.
     */
    public boolean isBinary(final Object key) {
        if (!containsKey(key)) {
            return false;
        }
        return get(key).isBinary();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonValue
     */
    public BsonValue get(final Object key, final BsonValue defaultValue) {
        BsonValue value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * BsonDocument.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonDocument
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonDocument getDocument(final Object key, final BsonDocument defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asDocument();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * BsonArray.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonArray
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonArray getArray(final Object key, final BsonArray defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asArray();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * BsonNumber.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonNumber
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonNumber getNumber(final Object key, final BsonNumber defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asNumber();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * BsonInt32.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonInt32
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonInt32 getInt32(final Object key, final BsonInt32 defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asInt32();
    }

    /**
     * Gets the value of the key if it is a BsonInt64, or throws if not.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonInt64
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonInt64 getInt64(final Object key, final BsonInt64 defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asInt64();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * BsonDouble.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonDouble
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonDouble getDouble(final Object key, final BsonDouble defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asDouble();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * BsonBoolean.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonBoolean
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonBoolean getBoolean(final Object key, final BsonBoolean defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asBoolean();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * BsonString.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonString
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonString getString(final Object key, final BsonString defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asString();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * BsonDateTime.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a BsonDateTime
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonDateTime getDateTime(final Object key, final BsonDateTime defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asDateTime();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * Timestamp.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a Timestamp
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonTimestamp getTimestamp(final Object key, final BsonTimestamp defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asTimestamp();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * ObjectId.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a ObjectId
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonObjectId getObjectId(final Object key, final BsonObjectId defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asObjectId();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * Binary.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a ObjectId
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonBinary getBinary(final Object key, final BsonBinary defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asBinary();
    }

    /**
     * If the document does not contain the given key, return the given default value.  Otherwise, gets the value of the key as a
     * ObjectId.
     *
     * @param key the key
     * @param defaultValue the default value
     * @return the value of the key as a ObjectId
     * @throws org.bson.BsonInvalidOperationException if the document contains the key but the value is not of the expected type
     */
    public BsonRegularExpression getRegularExpression(final Object key, final BsonRegularExpression defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return get(key).asRegularExpression();
    }

    @Override
    public BsonValue put(final String key, final BsonValue value) {
        if (value == null) {
            throw new IllegalArgumentException(format("The value for key %s can not be null", key));
        }
        if (key.contains("\0")) {
            throw new BSONException(format("BSON cstring '%s' is not valid because it contains a null character at index %d", key,
                                           key.indexOf('\0')));
        }
        return map.put(key, value);
    }

    @Override
    public BsonValue remove(final Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends BsonValue> m) {
        for (Map.Entry<? extends String, ? extends BsonValue> cur : m.entrySet()) {
            put(cur.getKey(), cur.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<BsonValue> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, BsonValue>> entrySet() {
        return map.entrySet();
    }

    /**
     * Put the given key and value into this document, and return the document.
     *
     * @param key   the key
     * @param value the value
     * @return this
     */
    public BsonDocument append(final String key, final BsonValue value) {
        put(key, value);
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BsonDocument)) {
            return false;
        }

        BsonDocument that = (BsonDocument) o;

        return entrySet().equals(that.entrySet());
    }

    @Override
    public int hashCode() {
        return entrySet().hashCode();
    }

    /**
     * Gets a JSON representation of this document
     * @return a JSON representation of this document
     */
    public String toJson() {
        StringWriter writer = new StringWriter();
        new BsonDocumentCodec().encode(new JsonWriter(writer), this, EncoderContext.builder().build());
        return writer.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }

    private void throwIfKeyAbsent(final Object key) {
        if (!containsKey(key)) {
            throw new BsonInvalidOperationException("Document does not contain key " + key);
        }
    }
}

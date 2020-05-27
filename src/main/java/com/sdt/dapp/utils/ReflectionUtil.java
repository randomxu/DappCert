package com.sdt.dapp.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Component
public class ReflectionUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ObjectMapper objectMapper;


    public Object priseObject(Field field, Object value) throws IOException {
        Class fieldClazz = field.getType();
        if (isBaseType(field)) {
            return value;
        }
        String jsonString = objectMapper.writeValueAsString(value);
        //假如实体类属性是集合类型，则
        if (Collection.class.isAssignableFrom(fieldClazz)) {
            Type genericType = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) genericType;
            Class genericClazz = (Class) pt.getActualTypeArguments()[0];
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(fieldClazz, genericClazz);
            return objectMapper.readValue(jsonString, javaType);
        }
        //假如传入的类型为集合且集合范型是实体类属性类型，则直接返回该对象
        if (value instanceof Collection) {
            Collection collection = (Collection) value;
            Object object = collection.iterator().hasNext() ? collection.iterator().next() : null;
            if (object != null) {
                logger.info("priseObject:" + object.getClass() + "fieldClazz:" + fieldClazz.getName());
                if (object.getClass().isAssignableFrom(fieldClazz)) {
                    return value;
                }
            }
        }
        try {
            return objectMapper.readValue(jsonString, fieldClazz);
        }catch (Exception e){
            return value;
        }
    }


    private List generateCollection(Collection collection, Class fieldClazz) {
        Iterator iterator = collection.iterator();
        List array = new ArrayList();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            Constructor constructor;
            try {
                constructor = fieldClazz.getDeclaredConstructor(object.getClass());
                array.add(constructor.newInstance(object));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage());
            }
        }
        return array;
    }


    public boolean isBaseType(Field field) {
        boolean isBaseType = false;
        Class fieldClazz = field.getType();
        if (fieldClazz.isPrimitive() || fieldClazz.getName().startsWith("java.lang")) {
            isBaseType = true;
        }
        return isBaseType;
    }
}

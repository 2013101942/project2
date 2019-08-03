package com.example.yq.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * Created by YQ on 2019/4/20.
 */

public class JsonUtil {
    private static ObjectMapper objectMapper =  null;
    public static ObjectMapper getObjectMapper(){
        if (objectMapper == null){
             objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
    //获取集合类的类型
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}

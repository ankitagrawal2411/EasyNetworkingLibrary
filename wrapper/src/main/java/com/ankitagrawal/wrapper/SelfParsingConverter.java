package com.ankitagrawal.wrapper;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by ankitagrawal on 26/7/16.
 */
public class SelfParsingConverter implements Converter {
    @Override
    public <J> J convert(Object value, Class<J> aClass) throws IOException {
        Type[] inter = aClass.getGenericInterfaces();
        for (int i=0;i<inter.length;i++){
            if(inter[i] instanceof  ParseListener){
              // return  ((ParseListener)inter[i]).onParse(value);
            }
        }

        return null;
    }

    @Override
    public boolean canConvert(Object value) {
        return false;
    }
}

package com.ankitagrawal.example;

/**
 * Created by ankitagrawal on 14/7/16.
 */


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by t_kumarga on 13-07-2016.
 */

public class CSVSerializer<T> {

    private T classType;
    private Map<String, Object> mappedObject = new HashMap<>();

    public CSVSerializer(T classType){
        this.classType = classType;

    }

    public void createFields(){
        Field[] fields = classType.getClass().getFields();

        for(Field field: fields){
            try {
                if(field.getClass().getFields() != null){
                    mappedObject = createFields(field.getClass(), mappedObject);
                }
                mappedObject.put(classType.getClass().getName()+"."+field.getName(), field.get(classType));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Object> createFields(Class<?> classType, Map<String, Object> mappedObject){

        Field[] fields = classType.getClass().getFields();

        for(Field field: fields){
            try {
                if(field.getClass().getFields() != null){
                    createFields( field.getClass(), mappedObject);
                }
                mappedObject.put(classType.getClass().getName()+"."+field.getName(), field.get(classType));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return mappedObject;
    }

    public Map<String, Object> createFields(Class<?> classType, List<?> listData, Map<String, Object> mappedObject){

        Field[] fields = classType.getClass().getFields();

        for(Field field: fields){
            try {
                if(field.getClass().getFields() != null){
                    createFields( field.getClass(), mappedObject);
                }
                mappedObject.put(classType.getClass().getName()+"."+field.getName(), field.get(classType));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return mappedObject;
    }




    public String getCSVData(){
        if (mappedObject == null || mappedObject.size() == 0) {
            return "";
        }

        StringBuilder headerBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();

        for (Map.Entry<String, Object> entry : mappedObject.entrySet()) {

            headerBuilder.append(entry.getKey());
            headerBuilder.append(",");
            valuesBuilder.append(entry.getValue());
            valuesBuilder.append(",");
        }

        headerBuilder.append("\n" + valuesBuilder);
        return headerBuilder.toString();
    }

}
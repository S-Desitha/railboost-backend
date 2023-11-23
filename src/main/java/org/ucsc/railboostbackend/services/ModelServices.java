//package org.ucsc.railboostbackend.services;
//
////public class ModelServices<T> {
////
////    public T diff(Class<T> classType, T original, T updated) throws IntrospectionException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
////        boolean isChanged = false;
////       T newObj = classType.getConstructor().newInstance();
////
//////        System.out.println("--Generic Types--");
//////        for (Field field : classType.getDeclaredFields()) {
//////            System.out.println(field.getGenericType());
//////        }
////
////        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(classType, Object.class).getPropertyDescriptors()) {
////            Class<?> returnType = descriptor.getReadMethod().getReturnType();
////            Method getterMethod = descriptor.getReadMethod();
////            Method setterMethod = descriptor.getWriteMethod();
////
////            if (returnType == List.class) {
////                Field field = classType.getDeclaredField(descriptor.getName());
////                System.out.println("List: "+ field.getGenericType());
////                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
////                for (Type type : parameterizedType.getActualTypeArguments()) {
////                    System.out.println("Actual Type: " + type.getTypeName());
////                }
////
////
////                String innerType = ((ParameterizedType) classType.getDeclaredField(descriptor.getName()).getGenericType()).getActualTypeArguments()[0].getTypeName();
////                List<?> original_val = (ArrayList<?>) getterMethod.invoke(original);
////                List<?> updated_val = (ArrayList<?>) getterMethod.invoke(updated);
////
////                Class<?> name = Class.forName(innerType);
////
////
//////                for (int i=0; i < original_val.size(); i++) {
//////                    ModelServices<?> modelServices = new ModelServices<>();
//////                    modelServices.getClass().getDeclaredConstructor().newInstance();
////////                    ModelServices<name> modelServices = new ModelServices<>();
////////                    modelServices.diff(name, original_val.get(i), updated_val.get(i));
////////                    Object updatedElmnt = ModelServices.diff(innerClassType, original_val.get(i), updated_val.get(i));
////////                    ScheduleStation updatedElmnt = new ModelServices<>().diff(innerClassType, original_val.get(i), updated_val.get(i));
////////                    setterMethod.invoke(newObj, ((ArrayList<Object>) getterMethod.invoke(newObj)).add(i, arrElmnt));
//////                }
////            }
////
////            else {
////                Object original_val = getterMethod.invoke(original);
////                Object updated_val = getterMethod.invoke(updated);
////
////                if (original_val != updated_val){
////                    isChanged = true;
////                    System.out.println("Value change: " +original_val +" -> "+ updated_val);
////                    setterMethod.invoke(newObj, updated_val);
////                }
////            }
////
////
////        }
////
////        return isChanged? newObj : null;
////    }
////
////
////}
//
//import java.beans.IntrospectionException;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ModelServices {
//
//    public BaseModel diff(Class<BaseModel> classType,  BaseModel original, BaseModel updated) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IntrospectionException, NoSuchFieldException, ClassNotFoundException {
//        boolean isChanged = false;
//        BaseModel newObj = classType.getConstructor().newInstance();
//
//        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(classType, Object.class).getPropertyDescriptors()) {
//            Class<?> returnType = descriptor.getReadMethod().getReturnType();
//            Method getterMethod = descriptor.getReadMethod();
//            Method setterMethod = descriptor.getWriteMethod();
//
//            if (returnType == List.class) {
//                Field field = classType.getDeclaredField(descriptor.getName());
//                System.out.println("List: "+ field.getGenericType());
//                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
//                for (Type type : parameterizedType.getActualTypeArguments()) {
//                    System.out.println("Actual Type: " + type.getTypeName());
//                }
//
//
//                String innerType = ((ParameterizedType) classType.getDeclaredField(descriptor.getName()).getGenericType()).getActualTypeArguments()[0].getTypeName();
//                List<BaseModel> original_val = (Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName()).getClass()) getterMethod.invoke(original);
//
//                List<?> updated_val = (ArrayList<?>) getterMethod.invoke(updated);
//
//                Class<BaseModel> name = Class.forName(innerType);
//
//
//                for (int i=0; i < original_val.size(); i++) {
//                    ModelServices modelServices = ModelServices.createInstance(name);
//                    modelServices.diff(BaseModel, original_val.get (i), updated_val.get(i));
////                    ModelServices<?> modelServices = new ModelServices<>();
////                    modelServices.getClass().getDeclaredConstructor().newInstance();
//////                    ModelServices<name> modelServices = new ModelServices<>();
//////                    modelServices.diff(name, original_val.get(i), updated_val.get(i));
//////                    Object updatedElmnt = ModelServices.diff(innerClassType, original_val.get(i), updated_val.get(i));
//////                    ScheduleStation updatedElmnt = new ModelServices<>().diff(innerClassType, original_val.get(i), updated_val.get(i));
//////                    setterMethod.invoke(newObj, ((ArrayList<Object>) getterMethod.invoke(newObj)).add(i, arrElmnt));
//                }
//            }
//
//
//
//            else {
//                Object original_val = getterMethod.invoke(original);
//                Object updated_val = getterMethod.invoke(updated);
//
//                if (original_val != updated_val){
//                    isChanged = true;
//                    System.out.println("Value change: " +original_val +" -> "+ updated_val);
//                    setterMethod.invoke(newObj, updated_val);
//                }
//            }
//
//        }
//
//        return newObj;
//    }
//
//    public static <T> ModelServices<T> createInstance(Class<T> clazz) {
//        ModelServices<T> modelServices = new ModelServices<>();
//        modelServices.setClassType(clazz); // You can initialize the value as needed
//        return modelServices;
//    }
//}

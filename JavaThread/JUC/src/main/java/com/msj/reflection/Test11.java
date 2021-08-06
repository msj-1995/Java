package com.msj.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

//通过反射获取泛型
public class Test11 {
    //泛型作为参数
    public void test01(Map<String,User> map, List<User> list){
        System.out.println("test01");
    }

    //泛型作为返回值类型
    public Map<String,User> test02(){
        System.out.println("test02");
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method method = Test11.class.getMethod("test01", Map.class, List.class);
        
        //后的泛型的参数类型
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type parameterType : genericParameterTypes) {
            System.out.println("泛型类型:" + parameterType);
            //只获取泛型的类型信息:如果泛型的类型是参数化类型，则就取出里面的类型
            if(parameterType instanceof ParameterizedType){
                //获得真实类型
                Type[] actualTypeArguments = ((ParameterizedType) parameterType).getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    System.out.println("结构化类型: " + actualTypeArgument);
                }
            }
        }
        
        //获取返回值类型的泛型
        Method test02 = Test11.class.getMethod("test02", null);
        Type genericReturnType = test02.getGenericReturnType();
        System.out.println("返回值泛型类型：" + genericReturnType);
        //真实类型
        if(genericReturnType instanceof ParameterizedType){
            Type[] types = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            for (Type type : types) {
                System.out.println("结构化类型：" + type);
            }
        }
    }
}

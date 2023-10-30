package org.ucsc.railboostbackend.models;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseModel {

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj==null || this.getClass() != obj.getClass())
            return false;

        try {
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(this.getClass()).getPropertyDescriptors()){
                Method getterMethod = descriptor.getReadMethod();
                if (getterMethod.invoke(this)!=null && getterMethod.invoke(obj)!=null && !getterMethod.invoke(this).equals(getterMethod.invoke(obj)))
                    return false;
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

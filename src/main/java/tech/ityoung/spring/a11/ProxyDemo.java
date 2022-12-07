package tech.ityoung.spring.a11;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class ProxyDemo {
    public static void main(String[] args) {
//        proxyTest();
        Dog dog = new Dog();
        Dog cgLibProxy = (Dog) Enhancer.create(Dog.class, (MethodInterceptor) (proxy, method, args1, methodProxy) -> {
            log.info("cgLibProxy was born");
            // Object invoke = method.invoke(dog, args1);
            // methodProxy避免反射, spring采用这个方案
//            Object invoke = methodProxy.invoke(dog, args1);
            // proxy ： 代理对象自身
            Object invoke = methodProxy.invokeSuper(proxy, args);
            log.info("cgLibProxy was dead");
            return invoke;
        });
        cgLibProxy.live();
    }

    private static void proxyTest() {
        Dog dog = new Dog();
        Animal proxyInstance = (Animal) Proxy.newProxyInstance(ProxyDemo.class.getClassLoader(), new Class[]{Animal.class}, (proxy, method, args1) -> {
            log.info("animal was born");
            Object invoke = method.invoke(dog, args1);
            log.info("animal was dead");
            return invoke;
        });
        proxyInstance.live();
    }
}

@Slf4j
class Dog implements Animal {
    @Override
    public void live() {
        log.info("dog live for 17 years");
    }
}

interface Animal {
    void live();
}

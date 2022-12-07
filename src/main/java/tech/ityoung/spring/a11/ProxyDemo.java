package tech.ityoung.spring.a11;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class ProxyDemo {
    public static void main(String[] args) {
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
